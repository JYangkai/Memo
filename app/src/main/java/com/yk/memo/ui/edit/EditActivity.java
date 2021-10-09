package com.yk.memo.ui.edit;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.yk.base.mvp.BaseMvpActivity;
import com.yk.memo.R;

public class EditActivity extends BaseMvpActivity<IEditView, EditPresenter> implements IEditView {

    private AppCompatEditText etNoteContent;
    private AppCompatButton btnSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        etNoteContent = findViewById(R.id.etNoteContent);
        btnSave = findViewById(R.id.btnSave);
    }

    private void initData() {

    }

    private void bindEvent() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etNoteContent.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                presenter.saveNote(content);
            }
        });
    }

    @Override
    public EditPresenter createPresenter() {
        return new EditPresenter();
    }

    @Override
    public void onSaveNote(boolean success) {
        Toast.makeText(this, success ? "Save Success" : "Save Fail", Toast.LENGTH_SHORT).show();
        finish();
    }
}
