package com.yk.memo.ui.edit;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.yk.base.mvp.BaseMvpActivity;
import com.yk.memo.R;
import com.yk.memo.data.bean.Note;
import com.yk.memo.ui.edit.fragment.EditFragment;
import com.yk.memo.ui.edit.fragment.PreviewFragment;

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

        editFragment.setOnEditListener(new EditFragment.OnEditListener() {
            @Override
            public void onTextChange(String src) {
                EditActivity.this.onTextChange(src);
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

    private void onTextChange(String src) {
        if (replayMenuItem != null) {
            replayMenuItem.setVisible(isCanReplay(src));
        }

        if (saveMenuItem != null) {
            saveMenuItem.setVisible(isCanSave(src));
        }

        if (copyMenuItem != null) {
            copyMenuItem.setVisible(isCanCopy(src));
        }

        if (clearMenuItem != null) {
            clearMenuItem.setVisible(isCanClear(src));
        }

        if (deleteMenuItem != null) {
            deleteMenuItem.setVisible(isCanDelete());
        }
    }

    private boolean isCanReplay(String src) {
        if (note == null) {
            // note为空，直接返回false，表示不能撤销修改
            return false;
        }
        // 如果对note的src做出了修改，则可以撤销
        return !note.getSrc().equals(src);
    }

    private boolean isCanSave(String src) {
        if (TextUtils.isEmpty(src)) {
            // 如果src为空，则直接返回false，表示不能保存
            return false;
        }
        // 如果note为空，或者note的src做出了修改，则可以保存
        return note == null || !note.getSrc().equals(src);
    }

    private boolean isCanCopy(String src) {
        // 不为空，才可以复制
        return !TextUtils.isEmpty(src);
    }

    private boolean isCanClear(String src) {
        // 不为空，才可以清理
        return !TextUtils.isEmpty(src);
    }

    private boolean isCanDelete() {
        // note不为空，才可以删除
        return note != null;
    }

    private ClipboardManager clipboardManager;

    private void copyToClipBroad(String src) {
        if (clipboardManager == null) {
            clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        }
        if (clipboardManager == null) {
            return;
        }
        ClipData clipData = ClipData.newPlainText("markdown_src", src);
        clipboardManager.setPrimaryClip(clipData);

        Snackbar.make(getWindow().getDecorView(), "已复制文本到剪切板", Snackbar.LENGTH_SHORT).show();
    }

    private String getCurSrc() {
        String src = editFragment.getSrc();
        if (!TextUtils.isEmpty(src)) {
            return src;
        }
        if (note != null) {
            src = note.getSrc();
        }
        return src;
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
        if (editMenuItem != null) {
            editMenuItem.setVisible(false);
        }
        if (previewMenuItem != null) {
            previewMenuItem.setVisible(true);
        }
    }

    private void onMenuPreviewMode() {
        if (editMenuItem != null) {
            editMenuItem.setVisible(true);
        }
        if (previewMenuItem != null) {
            previewMenuItem.setVisible(false);
        }
    }

    private MenuItem copyMenuItem;
    private MenuItem replayMenuItem;
    private MenuItem clearMenuItem;
    private MenuItem saveMenuItem;
    private MenuItem deleteMenuItem;
    private MenuItem editMenuItem;
    private MenuItem previewMenuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        copyMenuItem = menu.findItem(R.id.menu_edit_copy);
        replayMenuItem = menu.findItem(R.id.menu_edit_replay);
        clearMenuItem = menu.findItem(R.id.menu_edit_clear);
        saveMenuItem = menu.findItem(R.id.menu_edit_save);
        deleteMenuItem = menu.findItem(R.id.menu_edit_delete);
        editMenuItem = menu.findItem(R.id.menu_edit_edit);
        previewMenuItem = menu.findItem(R.id.menu_edit_preview);
        chooseMode(note != null ? Mode.PREVIEW : Mode.EDIT);
        onTextChange(note != null ? note.getSrc() : null);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_edit_copy) {
            copyToClipBroad(getCurSrc());
        } else if (item.getItemId() == R.id.menu_edit_replay) {
            editFragment.setSrc(note.getSrc());
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
        finish();
    }

    @Override
    public void onUpdateNote(Note note) {
        finish();
    }

    @Override
    public void onDeleteNote(Note note) {
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
