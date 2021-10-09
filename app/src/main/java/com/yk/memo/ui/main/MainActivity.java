package com.yk.memo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
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

        for (Note note : noteList) {
            if (this.noteList.contains(note)) {
                continue;
            }
            this.noteList.add(note);
            Log.d(TAG, "onLoadNoteList: " + note);
        }

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