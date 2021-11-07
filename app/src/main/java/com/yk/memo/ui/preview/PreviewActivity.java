package com.yk.memo.ui.preview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.yk.db.bean.Note;
import com.yk.markdown.Markdown;
import com.yk.memo.R;
import com.yk.memo.ui.edit.EditActivity;
import com.yk.memo.utils.SnackBarUtils;
import com.yk.mvp.BaseMvpActivity;
import com.yk.share.ShareUtils;

public class PreviewActivity extends BaseMvpActivity<IPreviewView, PreviewPresenter> implements IPreviewView {

    private static final String EXTRA_NOTE = "extra_note";
    private static final String EXTRA_SRC = "extra_src";

    private Toolbar toolbar;
    private AppCompatTextView tvPreview;

    private Note note;

    private String src;

    public static void start(Context context, Note note, String src) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra(EXTRA_NOTE, note);
        intent.putExtra(EXTRA_SRC, src);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        toolbar = findViewById(R.id.toolbar);
        tvPreview = findViewById(R.id.tvPreview);
    }

    private void initData() {
        initExtra();
        initToolbar();
        initTvPreview();
    }

    private void bindEvent() {

    }

    private void initExtra() {
        Object o = getIntent().getSerializableExtra(EXTRA_NOTE);
        if (o != null) {
            note = (Note) o;
        }
        src = getIntent().getStringExtra(EXTRA_SRC);
    }

    private void initToolbar() {
        toolbar.setTitle("预览");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initTvPreview() {
        if (!TextUtils.isEmpty(src)) {
            Markdown.with(this).load(src).into(tvPreview);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_preview_edit) {
            if (note != null) {
                EditActivity.start(this, note);
            }
            finish();
        } else if (item.getItemId() == R.id.menu_preview_share_text) {
            ShareUtils.shareText(this, note != null ? note.getSrc() : src);
        } else if (item.getItemId() == R.id.menu_preview_share_file) {
            if (note != null) {
                presenter.shareFile(note);
            }
        } else if (item.getItemId() == R.id.menu_preview_share_image) {
            presenter.shareImage(tvPreview);
        }
        return true;
    }

    @Override
    public PreviewPresenter createPresenter() {
        return new PreviewPresenter(this);
    }

    @Override
    public void onShareFile(Uri uri) {
        ShareUtils.shareFile(this, uri);
    }

    @Override
    public void onShareImage(Uri uri) {
        ShareUtils.shareJpg(this, uri);
    }

    @Override
    public void onShareFileError(Exception e) {
        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "分享文件失败");
    }

    @Override
    public void onShareImageError(Exception e) {
        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "分享图片失败");
    }
}
