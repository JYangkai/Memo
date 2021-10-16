package com.yk.memo;

import android.app.Application;

import org.litepal.LitePal;

public class MemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
