package com.yk.memo.ui.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.yk.base.eventposter.EventPoster;
import com.yk.base.mvp.BaseMvpActivity;
import com.yk.memo.R;
import com.yk.memo.data.bean.Note;
import com.yk.memo.data.event.NoteAddEvent;
import com.yk.memo.data.event.NoteRemoveEvent;
import com.yk.memo.data.event.NoteUpdateEvent;
import com.yk.memo.ui.edit.fragment.EditFragment;
import com.yk.memo.ui.edit.fragment.PreviewFragment;
import com.yk.memo.utils.TimeUtils;

public class EditActivity extends BaseMvpActivity<IEditView, EditPresenter> implements IEditView {
    private static final String TAG = "EditActivity2";

    private static final String EXTRA_NOTE = "extra_note";

    private enum Mode {
        PREVIEW,
        EDIT
    }

    private Toolbar toolbar;

    private PreviewFragment previewFragment;
    private EditFragment editFragment;

    private Note note;

    public static void start(Context context, Note note) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(EXTRA_NOTE, note);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Log.d(TAG, "onCreate: ");
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        toolbar = findViewById(R.id.toolbar);
    }

    private void initData() {
        initExtra();
        initToolbar();
        initFragment();
    }

    private void bindEvent() {
        previewFragment.setOnPreviewListener(new PreviewFragment.OnPreviewListener() {
            @Override
            public void onClickPreview() {
                chooseMode(Mode.EDIT);
            }
        });
    }

    private void initExtra() {
        Object o = getIntent().getSerializableExtra(EXTRA_NOTE);
        if (o != null) {
            note = (Note) o;
            Log.d(TAG, "initExtra: " + note);
        }
    }

    private void initToolbar() {
        toolbar.setTitle("编辑");
        if (note != null) {
            toolbar.setSubtitle(TimeUtils.getTime(note.getUpdateTime()));
        }
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initFragment() {
        previewFragment = PreviewFragment.newInstance(note != null ? note.getSrc() : null);
        editFragment = EditFragment.newInstance(note != null ? note.getSrc() : null);
    }

    private void chooseMode(Mode mode) {
        Fragment fragment = null;
        switch (mode) {
            case PREVIEW:
                fragment = previewFragment;
                onMenuPreviewMode();
                break;
            case EDIT:
                fragment = editFragment;
                onMenuEditMode();
                break;
        }

        if (fragment == null) {
            throw new RuntimeException("mode params is error:" + mode);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_content, fragment)
                .commit();
    }

    private void onMenuEditMode() {
        if (clearMenuItem != null) {
            clearMenuItem.setVisible(true);
        }
        if (saveMenuItem != null) {
            saveMenuItem.setVisible(true);
        }
        if (deleteMenuItem != null) {
            deleteMenuItem.setVisible(false);
        }
        if (editMenuItem != null) {
            editMenuItem.setVisible(false);
        }
        if (previewMenuItem != null) {
            previewMenuItem.setVisible(true);
        }
    }

    private void onMenuPreviewMode() {
        if (clearMenuItem != null) {
            clearMenuItem.setVisible(false);
        }
        if (saveMenuItem != null) {
            saveMenuItem.setVisible(false);
        }
        if (deleteMenuItem != null && note != null) {
            deleteMenuItem.setVisible(true);
        }
        if (editMenuItem != null) {
            editMenuItem.setVisible(true);
        }
        if (previewMenuItem != null) {
            previewMenuItem.setVisible(false);
        }
    }

    private MenuItem clearMenuItem;
    private MenuItem saveMenuItem;
    private MenuItem deleteMenuItem;
    private MenuItem editMenuItem;
    private MenuItem previewMenuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        clearMenuItem = menu.findItem(R.id.menu_edit_clear);
        saveMenuItem = menu.findItem(R.id.menu_edit_save);
        deleteMenuItem = menu.findItem(R.id.menu_edit_delete);
        editMenuItem = menu.findItem(R.id.menu_edit_edit);
        previewMenuItem = menu.findItem(R.id.menu_edit_preview);
        chooseMode(note != null ? Mode.PREVIEW : Mode.EDIT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_edit_clear) {
            editFragment.clear();
        } else if (item.getItemId() == R.id.menu_edit_save) {
            if (note != null) {
                presenter.updateNote(note, editFragment.getSrc());
            } else {
                presenter.saveNote(editFragment.getSrc());
            }
        } else if (item.getItemId() == R.id.menu_edit_delete) {
            presenter.deleteNote(note);
        } else if (item.getItemId() == R.id.menu_edit_edit) {
            chooseMode(Mode.EDIT);
        } else if (item.getItemId() == R.id.menu_edit_preview) {
            PreviewFragment.loadData(previewFragment, editFragment.getSrc());
            chooseMode(Mode.PREVIEW);
        }
        return true;
    }

    @Override
    public EditPresenter createPresenter() {
        return new EditPresenter();
    }

    @Override
    public void onSaveNote(Note note) {
        EventPoster.getInstance().post(new NoteAddEvent(true, note));
        finish();
    }

    @Override
    public void onUpdateNote(Note note) {
        EventPoster.getInstance().post(new NoteUpdateEvent(true, note));
        finish();
    }

    @Override
    public void onDeleteNote(Note note) {
        EventPoster.getInstance().post(new NoteRemoveEvent(note));
        finish();
    }

    @Override
    public void onSaveNoteError(Exception e) {

    }

    @Override
    public void onUpdateNoteError(Exception e) {

    }

    @Override
    public void onDeleteNoteError(Exception e) {

    }
}
