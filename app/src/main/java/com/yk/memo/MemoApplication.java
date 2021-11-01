package com.yk.memo;

import android.app.Application;

import com.yk.markdown.Markdown;
import com.yk.memo.utils.SpManager;

import org.litepal.LitePal;

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
        LitePal.initialize(this);
    }

    private void initMarkdown() {
        Markdown.configStyle(SpManager.getInstance().getMarkdownStyle());
    }
}
