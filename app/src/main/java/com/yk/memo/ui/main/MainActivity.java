package com.yk.memo.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yk.base.eventbus.EventBus;
import com.yk.base.eventbus.Subscribe;
import com.yk.base.mvp.BaseMvpActivity;
import com.yk.memo.R;
import com.yk.memo.data.adapter.NoteAdapter;
import com.yk.memo.data.bean.Note;
import com.yk.memo.data.event.NoteAddEvent;
import com.yk.memo.data.event.NoteUpdateEvent;
import com.yk.memo.ui.edit.EditActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseMvpActivity<IMainView, MainPresenter> implements IMainView {
    private static final String TAG = "MainActivity";

    private Toolbar toolbar;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvNote;

    private final List<Note> noteList = new ArrayList<>();
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getInstance().register(this);
        findView();
        initData();
        bindEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getInstance().unregister(this);
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

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    private void initRvNote() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        noteAdapter = new NoteAdapter(noteList);
        rvNote.setLayoutManager(linearLayoutManager);
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

    private void bindEvent() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: " + newText);
                noteAdapter.getFilter().filter(newText);
                return true;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadAllNote();
            }
        });

        noteAdapter.setOnItemListener(new NoteAdapter.OnItemListener() {
            @Override
            public void onItemClick(View view, Note note) {
                if (noteAdapter.isMoreSelectMode()) {
                    noteAdapter.selectNote(note);
                } else {
                    startEditActivity(note);
                }
            }

            @Override
            public void onItemLongClick(View view, Note note) {
                if (noteAdapter.isMoreSelectMode()) {
                    return;
                }
                noteAdapter.setMoreSelectMode(true);
                noteAdapter.selectNote(note);
            }

            @Override
            public void onItemMoreClick(View view, Note note) {
                showItemNotePopupMenu(view, note);
            }
        });
    }

    private void startEditActivity(Note note) {
        EditActivity.startEditActivity(this, note != null ? note.getId() : -1);
    }

    private void showItemNotePopupMenu(View view, Note note) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_item_note, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_note_delete:
                        presenter.deleteNote(note);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Subscribe(threadMode = Subscribe.Thread.UI)
    public void onNoteUpdate(NoteUpdateEvent event) {
        if (event == null) {
            return;
        }

        Note note = noteAdapter.findNoteForId(event.getNoteId());

        if (note == null) {
            return;
        }

        note.setSrc(event.getSrc());
        note.setUpdateTime(event.getUpdateTime());
        note.setSpanStrBuilder(event.getSpanStrBuilder());

        noteAdapter.topNote(note);
    }

    @Subscribe(threadMode = Subscribe.Thread.UI)
    public void onNoteAdd(NoteAddEvent event) {
        if (event == null) {
            return;
        }

        noteAdapter.refreshDataAdd(event.getNote(), true);

        noteAdapter.getFilter().filter(searchView.getQuery());
    }

    @Override
    public void onLoadNoteList(List<Note> noteList) {
        noteAdapter.refreshDataAdd(noteList);
        swipeRefreshLayout.setRefreshing(false);

        noteAdapter.getFilter().filter(searchView.getQuery());
    }

    @Override
    public void onDeleteNote(boolean success, Note note) {
        Toast.makeText(this, success ? "delete success" : "delete fail", Toast.LENGTH_SHORT).show();
        if (!success) {
            return;
        }
        noteAdapter.refreshDataRemove(note);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_edit:
                startEditActivity(null);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (noteAdapter.isMoreSelectMode()) {
            noteAdapter.setMoreSelectMode(false);
            return;
        }
        super.onBackPressed();
    }
}