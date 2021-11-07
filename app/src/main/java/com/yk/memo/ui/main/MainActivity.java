package com.yk.memo.ui.main;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yk.db.bean.Note;
import com.yk.eventposter.EventPoster;
import com.yk.eventposter.Subscribe;
import com.yk.markdown.Markdown;
import com.yk.memo.R;
import com.yk.memo.data.adapter.NoteAdapter;
import com.yk.memo.data.event.MdStyleChangeEvent;
import com.yk.memo.data.event.NoteAddEvent;
import com.yk.memo.data.event.NoteRemoveEvent;
import com.yk.memo.data.event.NoteUpdateEvent;
import com.yk.memo.ui.edit.EditActivity;
import com.yk.memo.ui.preview.PreviewActivity;
import com.yk.memo.ui.setting.SettingActivity;
import com.yk.memo.utils.SnackBarUtils;
import com.yk.mvp.BaseMvpActivity;
import com.yk.permissionrequester.PermissionFragment;
import com.yk.permissionrequester.PermissionRequester;
import com.yk.share.ShareUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseMvpActivity<IMainView, MainPresenter> implements IMainView {

    private Toolbar toolbar;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvNote;

    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventPoster.getInstance().register(this);
        findView();
        initData();
        bindEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventPoster.getInstance().unregister(this);
    }

    private void findView() {
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        rvNote = findViewById(R.id.rvNote);
    }

    private void initData() {
        initToolbar();
        initRvNote();

        loadData();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    private void initRvNote() {
        swipeRefreshLayout.setColorSchemeResources(
                R.color.design_default_color_primary,
                R.color.design_default_color_secondary);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        noteAdapter = new NoteAdapter(new ArrayList<>());
        rvNote.setLayoutManager(layoutManager);
        rvNote.setAdapter(noteAdapter);
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(true);
        presenter.loadData();
    }

    private void bindEvent() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                noteAdapter.getFilter().filter(newText);
                return true;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        noteAdapter.setOnItemListener(new NoteAdapter.OnItemListener() {
            @Override
            public void onItemClick(View view, Note note) {
                onNoteItemClick(note);
            }

            @Override
            public void onItemLongClick(View view, Note note) {
                onNoteItemLongClick(note);
            }

            @Override
            public void onItemMoreClick(View view, Note note) {
                onNoteItemMoreClick(view, note);
            }
        });
    }

    private void onNoteItemClick(Note note) {
        if (noteAdapter.isMoreSelectMode()) {
            noteAdapter.selectNote(note);
            if (noteAdapter.getSelectNoteList().size() == 0) {
                noteAdapter.setMoreSelectMode(false);
            }
        } else {
            PreviewActivity.start(this, note, note.getSrc());
        }
    }

    private void onNoteItemLongClick(Note note) {
        if (noteAdapter.isMoreSelectMode()) {
            return;
        }
        noteAdapter.setMoreSelectMode(true);
        noteAdapter.selectNote(note);
    }

    private void onNoteItemMoreClick(View view, Note note) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_note_more, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_note_more_share_text) {
                    ShareUtils.shareText(MainActivity.this, note.getSrc());
                } else if (item.getItemId() == R.id.menu_note_more_share_file) {
                    shareFile(note);
                } else if (item.getItemId() == R.id.menu_note_more_delete) {
                    presenter.deleteNote(note);
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void shareFile(Note note) {
        PermissionRequester.build(this)
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(new PermissionFragment.OnPermissionRequestListener() {
                    @Override
                    public void onRequestSuccess(boolean success) {
                        if (success) {
                            presenter.shareFile(note);
                        } else {
                            SnackBarUtils.showMsgShort(getWindow().getDecorView(), "操作需要授权");
                        }
                    }

                    @Override
                    public void onGrantedList(List<String> grantedList) {

                    }

                    @Override
                    public void onDeniedList(List<String> deniedList) {

                    }
                });
    }

    private void updateToolbarSubTitle() {
        toolbar.setSubtitle(noteAdapter.getItemCount() + " 条数据");
    }

    @Override
    public void onBackPressed() {
        if (noteAdapter.isMoreSelectMode()) {
            noteAdapter.setMoreSelectMode(false);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_main_delete) {
            List<Note> noteList = noteAdapter.getSelectNoteList();
            if (noteList.isEmpty()) {
                SnackBarUtils.showMsgShort(getWindow().getDecorView(), "所选数据为空");
            } else {
                presenter.deleteNoteList(noteList);
            }
        } else if (item.getItemId() == R.id.menu_main_share) {
            shareZip();
        } else if (item.getItemId() == R.id.menu_main_setting) {
            SettingActivity.start(this);
        } else if (item.getItemId() == R.id.menu_main_edit) {
            EditActivity.start(this, null);
        }
        return true;
    }

    private void shareZip() {
        PermissionRequester.build(this)
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(new PermissionFragment.OnPermissionRequestListener() {
                    @Override
                    public void onRequestSuccess(boolean success) {
                        if (success) {
                            presenter.shareZip();
                        } else {
                            SnackBarUtils.showMsgShort(getWindow().getDecorView(), "操作需要授权");
                        }
                    }

                    @Override
                    public void onGrantedList(List<String> grantedList) {

                    }

                    @Override
                    public void onDeniedList(List<String> deniedList) {

                    }
                });
    }

    @Subscribe(threadMode = Subscribe.Thread.UI)
    public void onNoteAddEvent(NoteAddEvent event) {
        if (event == null) {
            return;
        }

        noteAdapter.refreshDataAdd(event.getNote(), event.isNeedTop());

        if (event.isNeedTop()) {
            rvNote.scrollToPosition(0);
        }

        updateToolbarSubTitle();

        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "新增一条数据");
    }

    @Subscribe(threadMode = Subscribe.Thread.UI)
    public void onNoteUpdateEvent(NoteUpdateEvent event) {
        if (event == null) {
            return;
        }

        Note eventNote = event.getNote();

        Note note = noteAdapter.findNoteForId(eventNote.getId());
        note.setSrc(eventNote.getSrc());
        note.setUpdateTime(eventNote.getUpdateTime());

        if (event.isNeedTop()) {
            noteAdapter.topNote(note);
            rvNote.scrollToPosition(0);
        } else {
            noteAdapter.refreshDataChange(note);
        }

        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "更新一条数据");
    }

    @Subscribe(threadMode = Subscribe.Thread.UI)
    public void onNoteRemoveEvent(NoteRemoveEvent event) {
        if (event == null) {
            return;
        }

        Note eventNote = event.getNote();

        Note note = noteAdapter.findNoteForId(eventNote.getId());
        noteAdapter.refreshDataRemove(note);

        updateToolbarSubTitle();

        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "删除一条数据");
    }

    @Subscribe(threadMode = Subscribe.Thread.UI)
    public void onMdStyleChangeEvent(MdStyleChangeEvent event) {
        if (event == null) {
            return;
        }

        Markdown.configStyle(event.getStyle());

        noteAdapter.notifyDataSetChanged();

        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "已切换为 " + event.getStyle() + " 风格");
    }

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void onLoadData(List<Note> noteList) {
        noteAdapter.refreshDataAdd(noteList);

        swipeRefreshLayout.setRefreshing(false);
        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "加载 " + noteList.size() + " 条数据");

        updateToolbarSubTitle();
    }

    @Override
    public void onShareFile(Uri uri) {
        ShareUtils.shareFile(this, uri);
    }

    @Override
    public void onDeleteNote(Note note) {
        noteAdapter.refreshDataRemove(note);

        updateToolbarSubTitle();

        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "删除成功");
    }

    @Override
    public void onDeleteNoteList(List<Note> noteList) {
        noteAdapter.refreshDataRemove(noteList);

        updateToolbarSubTitle();

        noteAdapter.setMoreSelectMode(false);

        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "删除所选数据成功");
    }

    @Override
    public void onShareZip(Uri uri) {
        ShareUtils.shareFile(this, uri);
    }

    @Override
    public void onLoadDataError(Exception e) {
        swipeRefreshLayout.setRefreshing(false);

        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "加载失败，或数据为空");
    }

    @Override
    public void onShareFileError(Exception e) {
        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "分享文件失败");
    }

    @Override
    public void onDeleteNoteError(Exception e) {
        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "删除失败");
    }

    @Override
    public void onDeleteNoteListError(Exception e) {
        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "删除所选数据失败");
    }

    @Override
    public void onShareZipError(Exception e) {
        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "分享文件失败");
    }
}
