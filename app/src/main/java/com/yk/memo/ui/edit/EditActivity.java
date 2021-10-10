package com.yk.memo.ui.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import com.yk.base.mvp.BaseMvpActivity;
import com.yk.memo.R;
import com.yk.memo.data.bean.Note;
import com.yk.memo.utils.TimeUtils;

public class EditActivity extends BaseMvpActivity<IEditView, EditPresenter> implements IEditView {
    private static final String TAG = "EditActivity";

    private static final String EXTRA_NOTE_ID = "extra_note_id";

    public static void startEditActivity(Context context, long noteId) {
        Log.d(TAG, "startEditActivity: " + noteId);
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        context.startActivity(intent);
    }

    private Toolbar toolbar;
    private AppCompatEditText etNoteContent;

    private AppCompatImageView ivQuote;
    private AppCompatImageView ivCode;
    private AppCompatImageView ivOrderedList;
    private AppCompatImageView ivUnorderedList;
    private AppCompatImageView ivTitle;
    private AppCompatImageView ivSeparator;

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
        toolbar = findViewById(R.id.toolbar);
        etNoteContent = findViewById(R.id.etNoteContent);

        ivQuote = findViewById(R.id.ivQuote);
        ivCode = findViewById(R.id.ivCode);
        ivOrderedList = findViewById(R.id.ivOrderedList);
        ivUnorderedList = findViewById(R.id.ivUnorderedList);
        ivTitle = findViewById(R.id.ivTitle);
        ivSeparator = findViewById(R.id.ivSeparator);
    }

    private void initData() {
        initExtra();
        initToolbar();
    }

    private void initToolbar() {
        toolbar.setTitle("Edit");
        setSupportActionBar(toolbar);
    }

    private void initExtra() {
        noteId = getIntent().getLongExtra(EXTRA_NOTE_ID, -1);
        if (noteId != -1) {
            presenter.loadNote(noteId);
        }
    }

    private void bindEvent() {
        ivQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContentLastChar() == '\n') {
                    appContent("> ");
                } else {
                    appContent("\n> ");
                }
                selectContentLast();
            }
        });

        ivCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContentLastChar() == '\n') {
                    appContent("```\n\n```");
                } else {
                    appContent("\n```\n\n```");
                }
                selectContent(getContent().lastIndexOf("\n"));
            }
        });

        ivOrderedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContentLastChar() == '\n') {
                    appContent("1. ");
                } else {
                    appContent("\n1. ");
                }
                selectContentLast();
            }
        });

        ivUnorderedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContentLastChar() == '\n') {
                    appContent("- ");
                } else {
                    appContent("\n- ");
                }
                selectContentLast();
            }
        });

        ivTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(EditActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_iv_title, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_iv_title_1) {
                            insertTitle("# ");
                        } else if (item.getItemId() == R.id.menu_iv_title_2) {
                            insertTitle("## ");
                        } else if (item.getItemId() == R.id.menu_iv_title_3) {
                            insertTitle("### ");
                        } else if (item.getItemId() == R.id.menu_iv_title_4) {
                            insertTitle("#### ");
                        } else if (item.getItemId() == R.id.menu_iv_title_5) {
                            insertTitle("##### ");
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        ivSeparator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContentLastChar() == '\n') {
                    appContent("---\n");
                } else {
                    appContent("\n---\n");
                }
                selectContentLast();
            }
        });
    }

    private void insertTitle(String content) {
        if (getContentLastChar() == '\n') {
            appContent(content);
        } else {
            appContent("\n" + content);
        }
        selectContentLast();
    }

    private void appContent(String content) {
        etNoteContent.append(content);
    }

    private void selectContentLast() {
        selectContent(getContent().length());
    }

    private void selectContent(int index) {
        etNoteContent.setSelection(index);
    }

    private String getContent() {
        return etNoteContent.getText().toString();
    }

    private char getContentLastChar() {
        String content = getContent();
        if (TextUtils.isEmpty(content)) {
            return '\n';
        }
        return content.charAt(content.length() - 1);
    }

    private void save() {
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

    private void clear() {
        etNoteContent.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_edit_save) {
            save();
        } else if (item.getItemId() == R.id.menu_edit_clear) {
            clear();
        }
        return true;
    }

    @Override
    public EditPresenter createPresenter() {
        return new EditPresenter();
    }

    @Override
    public void onLoadNote(Note note) {
        etNoteContent.setText(note.getSrc());
        toolbar.setSubtitle(TimeUtils.getTime(note.getUpdateTime()));
    }

    @Override
    public void onSaveNote(boolean success) {
        Toast.makeText(this, success ? "Save Success" : "Save Fail", Toast.LENGTH_SHORT).show();
        finish();
    }
}
