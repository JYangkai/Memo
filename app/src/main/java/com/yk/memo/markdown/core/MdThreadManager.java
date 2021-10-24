package com.yk.memo.markdown.core;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MdThreadManager {

    private static volatile MdThreadManager instance;

    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    private final ExecutorService ioThread;

    private MdThreadManager() {
        ioThread = Executors.newSingleThreadExecutor();
    }

    public static MdThreadManager getInstance() {
        if (instance == null) {
            synchronized (MdThreadManager.class) {
                if (instance == null) {
                    instance = new MdThreadManager();
                }
            }
        }
        return instance;
    }

    public void postUi(Runnable runnable) {
        uiHandler.post(runnable);
    }

    public void postIo(Runnable runnable) {
        ioThread.execute(runnable);
    }
}
