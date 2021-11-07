package com.yk.memo.ui.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import com.yk.db.bean.Note;
import com.yk.markdown.edit.EditManager;
import com.yk.memo.R;
import com.yk.memo.ui.preview.PreviewActivity;
import com.yk.memo.ui.view.edit.MarkdownEditAdapter;
import com.yk.memo.ui.view.edit.MarkdownEditBean;
import com.yk.memo.ui.view.edit.MarkdownEditView;
import com.yk.memo.utils.SnackBarUtils;
import com.yk.mvp.BaseMvpActivity;

public class EditActivity extends BaseMvpActivity<IEditView, EditPresenter> implements IEditView {
    private static final String EXTRA_NOTE = "extra_note";

    private Toolbar toolbar;
    private AppCompatEditText etContent;
    private MarkdownEditView markdownEditView;

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
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        toolbar = findViewById(R.id.toolbar);
        etContent = findViewById(R.id.etContent);
        markdownEditView = findViewById(R.id.markdownEditView);
    }

    private void initData() {
        initExtra();
        initToolbar();
        initEt();
    }

    private void bindEvent() {
        markdownEditView.setOnItemClickListener(new MarkdownEditAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MarkdownEditBean bean) {
                EditManager.getInstance().insert(bean.getType(), etContent);
            }
        });
    }

    private void initExtra() {
        Object o = getIntent().getSerializableExtra(EXTRA_NOTE);
        if (o != null) {
            note = (Note) o;
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

    private void initEt() {
        etContent.requestFocus();

        if (note != null) {
            etContent.setText(note.getSrc());
            etContent.setSelection(getSrc().length());
        }
    }

    private String getSrc() {
        Editable editable = etContent.getText();
        if (editable == null) {
            return "";
        }
        return editable.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_edit_2_copy) {
            presenter.copyToClipBoard(getSrc());
        } else if (item.getItemId() == R.id.menu_edit_2_replay) {
            if (note != null) {
                etContent.setText(note.getSrc());
            }
        } else if (item.getItemId() == R.id.menu_edit_2_clear) {
            etContent.setText(null);
        } else if (item.getItemId() == R.id.menu_edit_2_preview) {
            if (note != null) {
                PreviewActivity.start(this, note);
            } else {
                PreviewActivity.start(this, getSrc());
            }
        } else if (item.getItemId() == R.id.menu_edit_2_save) {
            if (note != null) {
                presenter.updateNote(note, getSrc());
            } else {
                presenter.saveNote(getSrc());
            }
        }
        return true;
    }

    @Override
    public EditPresenter createPresenter() {
        return new EditPresenter(this);
    }

    @Override
    public void onCopyToClipBoard(boolean success) {
        SnackBarUtils.showMsgShort(getWindow().getDecorView(), success ? "已复制文本到剪切板" : "复制失败");
    }

    @Override
    public void onSaveNote(boolean success) {
        if (success) {
            finish();
        } else {
            SnackBarUtils.showMsgShort(getWindow().getDecorView(), "保存失败");
        }
    }

    @Override
    public void onUpdateNote(boolean success) {
        if (success) {
            finish();
        } else {
            SnackBarUtils.showMsgShort(getWindow().getDecorView(), "更新失败");
        }
    }
}
