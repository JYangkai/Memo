package com.yk.memo.ui.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.yk.base.mvp.BaseMvpActivity;
import com.yk.memo.R;
import com.yk.memo.data.bean.Note;

public class EditActivity extends BaseMvpActivity<IEditView, EditPresenter> implements IEditView {
    private static final String TAG = "EditActivity";

    private static final String EXTRA_NOTE_ID = "extra_note_id";

    public static void startEditActivity(Context context, long noteId) {
        Log.d(TAG, "startEditActivity: " + noteId);
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        context.startActivity(intent);
    }

    private AppCompatEditText etNoteContent;
    private AppCompatButton btnSave;

    private long noteId;

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
        noteId = getIntent().getLongExtra(EXTRA_NOTE_ID, -1);
        if (noteId != -1) {
            presenter.loadNote(noteId);
        }
    }

    private void bindEvent() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etNoteContent.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                if (noteId != -1) {
                    presenter.updateNote(noteId, content);
                } else {
                    presenter.saveNote(content);
                }
            }
        });
    }

    @Override
    public EditPresenter createPresenter() {
        return new EditPresenter();
    }

    @Override
    public void onLoadNote(Note note) {
        etNoteContent.setText(note.getSrc());
    }

    @Override
    public void onSaveNote(boolean success) {
        Toast.makeText(this, success ? "Save Success" : "Save Fail", Toast.LENGTH_SHORT).show();
        finish();
    }
}
