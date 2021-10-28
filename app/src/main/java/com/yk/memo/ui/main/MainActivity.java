package com.yk.memo.ui.main;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.yk.eventposter.EventPoster;
import com.yk.eventposter.Subscribe;
import com.yk.markdown2.Markdown;
import com.yk.memo.R;
import com.yk.memo.data.adapter.NoteAdapter;
import com.yk.memo.data.bean.Note;
import com.yk.memo.data.event.MdStyleChangeEvent;
import com.yk.memo.data.event.NoteAddEvent;
import com.yk.memo.data.event.NoteRemoveEvent;
import com.yk.memo.data.event.NoteUpdateEvent;
import com.yk.memo.ui.edit.EditActivity;
import com.yk.memo.ui.setting.SettingActivity;
import com.yk.memo.utils.SnackBarUtils;
import com.yk.mvp.BaseMvpActivity;
import com.yk.permissionrequester.PermissionFragment;
import com.yk.permissionrequester.PermissionRequester;

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
        EventPoster.getInstance().register(this);
        setContentView(R.layout.activity_main);
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
        initSwipeRefreshLayout();

        loadAllNote();
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
                loadAllNote();
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

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    private void initRvNote() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        noteAdapter = new NoteAdapter(new ArrayList<>());
        rvNote.setLayoutManager(layoutManager);
        rvNote.setAdapter(noteAdapter);
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(
                R.color.design_default_color_primary,
                R.color.design_default_color_secondary);
    }

    private void loadAllNote() {
        swipeRefreshLayout.setRefreshing(true);
        presenter.loadAllNote();
    }

    private void onNoteItemClick(Note note) {
        if (noteAdapter.isMoreSelectMode()) {
            noteAdapter.selectNote(note);
            if (noteAdapter.getSelectNoteList().isEmpty()) {
                // 如果没有选中的note，则退出多选模式
                noteAdapter.setMoreSelectMode(false);
                hideDeleteMenuItem();
            }
        } else {
            startEditActivity(note);
        }
    }

    private void onNoteItemLongClick(Note note) {
        if (noteAdapter.isMoreSelectMode()) {
            return;
        }
        noteAdapter.setMoreSelectMode(true);
        noteAdapter.selectNote(note);
        showDeleteMenuItem();
    }

    private void onNoteItemMoreClick(View view, Note note) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_item_note, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_item_note_delete) {
                    presenter.deleteNote(note);
                } else if (item.getItemId() == R.id.menu_item_note_output) {
                    outputNote(note);
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void outputNote(Note note) {
        PermissionRequester.build(this)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .request(new PermissionFragment.OnPermissionRequestListener() {
                    @Override
                    public void onRequestSuccess(boolean success) {
                        if (success) {
                            presenter.outputNote(note);
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

    private void startEditActivity(Note note) {
        EditActivity.start(this, note);
    }

    private void startSettingActivity() {
        SettingActivity.start(this);
    }

    private void deleteNoteList() {
        presenter.deleteNoteList(noteAdapter.getSelectNoteList());
    }

    private MenuItem deleteMenuItem;

    private void showDeleteMenuItem() {
        if (deleteMenuItem == null) {
            return;
        }
        deleteMenuItem.setVisible(true);
    }

    private void hideDeleteMenuItem() {
        if (deleteMenuItem == null) {
            return;
        }
        deleteMenuItem.setVisible(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        deleteMenuItem = menu.findItem(R.id.menu_main_delete);
        hideDeleteMenuItem();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_main_delete) {
            deleteNoteList();
        } else if (item.getItemId() == R.id.menu_main_edit) {
            startEditActivity(null);
        } else if (item.getItemId() == R.id.menu_main_setting) {
            startSettingActivity();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (noteAdapter.isMoreSelectMode()) {
            noteAdapter.setMoreSelectMode(false);
            hideDeleteMenuItem();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter(this);
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

    @Subscribe(threadMode = Subscribe.Thread.UI)
    public void onNoteAddEvent(NoteAddEvent event) {
        if (event == null) {
            return;
        }
        noteAdapter.refreshDataAdd(event.getNote(), event.isNeedTop());

        if (event.isNeedTop()) {
            rvNote.scrollToPosition(0);
        }

        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "新增一条数据 成功");
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
        }

        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "更新一条数据 成功");
    }

    @Subscribe(threadMode = Subscribe.Thread.UI)
    public void onNoteRemoveEvent(NoteRemoveEvent event) {
        if (event == null) {
            return;
        }
        Note eventNote = event.getNote();

        Note note = noteAdapter.findNoteForId(eventNote.getId());
        noteAdapter.refreshDataRemove(note);

        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "移除一条数据 成功");
    }

    @Override
    public void onLoadNoteList(List<Note> noteList) {
        noteAdapter.refreshDataAdd(noteList);
        swipeRefreshLayout.setRefreshing(false);

        String query = searchView.getQuery().toString();
        if (!TextUtils.isEmpty(query)) {
            noteAdapter.getFilter().filter(query);
        }

        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "加载 " + noteAdapter.getItemCount() + " 条数据");
    }

    @Override
    public void onDeleteNote(Note note) {
        noteAdapter.refreshDataRemove(note);

        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "删除 成功");
    }

    @Override
    public void onDeleteNoteList(List<Note> noteList) {
        noteAdapter.refreshDataRemove(noteList);
        noteAdapter.setMoreSelectMode(false);
        hideDeleteMenuItem();

        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "删除所选数据 成功");
    }

    @Override
    public void onOutputNote(Note note) {
        noteAdapter.refreshDataChange(note);

        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "导出数据 成功");
    }

    @Override
    public void onLoadNoteListError(Exception e) {

    }

    @Override
    public void onDeleteNoteError(Exception e) {

    }

    @Override
    public void onDeleteNoteListError(Exception e) {

    }

    @Override
    public void onOutputNoteError(Exception e) {

    }
}
