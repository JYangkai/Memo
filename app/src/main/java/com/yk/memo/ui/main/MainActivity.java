package com.yk.memo.ui.main;

import android.content.Intent;
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

import com.yk.base.mvp.BaseMvpActivity;
import com.yk.memo.R;
import com.yk.memo.data.adapter.NoteAdapter;
import com.yk.memo.data.bean.Note;
import com.yk.memo.ui.edit.EditActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseMvpActivity<IMainView, MainPresenter> implements IMainView {
    private static final String TAG = "MainActivity";

    private Toolbar toolbar;
    private SearchView searchView;
    private RecyclerView rvNote;

    private final List<Note> noteList = new ArrayList<>();
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchView);
        rvNote = findViewById(R.id.rvNote);
    }

    private void initData() {
        setSupportActionBar(toolbar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        noteAdapter = new NoteAdapter(noteList);
        rvNote.setLayoutManager(linearLayoutManager);
        rvNote.setAdapter(noteAdapter);
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

        noteAdapter.setOnItemListener(new NoteAdapter.OnItemListener() {
            @Override
            public void onItemClick(View view, Note note) {
                startEditActivity(note);
            }

            @Override
            public void onItemLongClick(View view, Note note) {
                showItemNotePopupMenu(view, note);
            }
        });
    }

    private void startEditActivity(Note note) {
        EditActivity.startEditActivity(this, note.getId());
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
    protected void onResume() {
        super.onResume();
        presenter.loadAllNote();
    }

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void onLoadNoteList(List<Note> noteList) {
        if (noteList == null || noteList.isEmpty()) {
            return;
        }

        noteAdapter.addAll(noteList);
    }

    @Override
    public void onDeleteNote(boolean success, Note note) {
        Toast.makeText(this, success ? "delete success" : "delete fail", Toast.LENGTH_SHORT).show();
        if (!success) {
            return;
        }
        noteList.remove(note);
        noteAdapter.notifyDataSetChanged();
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
                startActivity(new Intent(this, EditActivity.class));
                break;
            default:
                break;
        }
        return true;
    }
}