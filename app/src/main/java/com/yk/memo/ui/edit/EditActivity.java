package com.yk.memo.ui.edit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import com.yk.db.bean.Note;
import com.yk.eventposter.EventPoster;
import com.yk.eventposter.Subscribe;
import com.yk.markdown.bean.MdType;
import com.yk.markdown.edit.EditManager;
import com.yk.memo.R;
import com.yk.memo.data.bean.Image;
import com.yk.memo.data.event.MdImageAddEvent;
import com.yk.memo.ui.album.AlbumActivity;
import com.yk.memo.ui.preview.PreviewActivity;
import com.yk.memo.ui.view.edit.MarkdownEditAdapter;
import com.yk.memo.ui.view.edit.MarkdownEditBean;
import com.yk.memo.ui.view.edit.MarkdownEditView;
import com.yk.memo.utils.SnackBarUtils;
import com.yk.mvp.BaseMvpActivity;
import com.yk.permissionrequester.PermissionFragment;
import com.yk.permissionrequester.PermissionRequester;

import java.util.List;

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
                if (bean.getType() == MdType.IMAGE) {
                    startAlbumActivity();
                } else {
                    EditManager.getInstance().insert(bean.getType(), etContent);
                }
            }
        });
    }

    private void startAlbumActivity() {
        PermissionRequester.build(this)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .request(new PermissionFragment.OnPermissionRequestListener() {
                    @Override
                    public void onRequestSuccess(boolean success) {
                        if (success) {
                            AlbumActivity.start(EditActivity.this);
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
        } else if (item.getItemId() == R.id.menu_edit_copy) {
            presenter.copyToClipBoard(getSrc());
        } else if (item.getItemId() == R.id.menu_edit_replay) {
            if (note != null) {
                etContent.setText(note.getSrc());
                etContent.setSelection(getSrc().length());
            }
        } else if (item.getItemId() == R.id.menu_edit_clear) {
            etContent.setText(null);
        } else if (item.getItemId() == R.id.menu_edit_preview) {
            PreviewActivity.start(this, note, getSrc());
        } else if (item.getItemId() == R.id.menu_edit_save) {
            if (note != null) {
                presenter.updateNote(note, getSrc());
            } else {
                presenter.saveNote(getSrc());
            }
        }
        return true;
    }

    @Subscribe(threadMode = Subscribe.Thread.UI)
    public void onMdImageAddEvent(MdImageAddEvent event) {
        if (event == null) {
            return;
        }

        Image image = event.getImage();
        if (image == null) {
            return;
        }

        EditManager.getInstance().insertImage(etContent, image.getName(), image.getPath());
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
