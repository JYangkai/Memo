package com.yk.memo;

import android.app.Application;

import com.yk.db.manager.NoteDbManager;
import com.yk.markdown.Markdown;
import com.yk.memo.utils.SpManager;

public class MemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initSp();
        initLitePal();
        initMarkdown();
    }

    private void initSp() {
        SpManager.getInstance().init(this);
    }

    private void initLitePal() {
        NoteDbManager.getInstance().init(this);
    }

    private void initMarkdown() {
        Markdown.configStyle(SpManager.getInstance().getMarkdownStyle());
    }
}
