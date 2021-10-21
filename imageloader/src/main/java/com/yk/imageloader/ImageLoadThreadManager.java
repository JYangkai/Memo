package com.yk.imageloader;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoadThreadManager {

    private static volatile ImageLoadThreadManager instance;

    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    private final ExecutorService ioThread;

    private ImageLoadThreadManager() {
        ioThread = Executors.newSingleThreadExecutor();
    }

    public static ImageLoadThreadManager getInstance() {
        if (instance == null) {
            synchronized (ImageLoadThreadManager.class) {
                if (instance == null) {
                    instance = new ImageLoadThreadManager();
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
