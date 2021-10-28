package com.yk.imageloader2.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import com.yk.imageloader2.lru.ImageDiskLruCache;
import com.yk.imageloader2.lru.ImageLruCache;
import com.yk.imageloader2.request.BaseRequester;
import com.yk.imageloader2.utils.KeyUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestThreadManager {
    private static volatile RequestThreadManager instance;

    private final ExecutorService ioService;

    private final Handler uiHandler;

    private RequestThreadManager() {
        ioService = Executors.newFixedThreadPool(5);
        uiHandler = new Handler(Looper.getMainLooper());
    }

    public static RequestThreadManager getInstance() {
        if (instance == null) {
            synchronized (RequestThreadManager.class) {
                if (instance == null) {
                    instance = new RequestThreadManager();
                }
            }
        }
        return instance;
    }

    public void executeIoTask(BaseRequester requester) {
        ioService.execute(new RequestIoRunnable(requester));
    }

    public void executeUiTask(BaseRequester requester) {
        uiHandler.post(new RequestUiRunnable(requester));
    }

    public static class RequestIoRunnable implements Runnable {
        private final BaseRequester requester;

        public RequestIoRunnable(BaseRequester requester) {
            this.requester = requester;
        }

        @Override
        public void run() {
            requester.setRequestState(BaseRequester.RequestState.START_REQUEST);

            String key = KeyUtils.hashKey(requester.getPath());

            Bitmap bitmap = null;

            if (requester.isOpenLruCache()) {
                bitmap = ImageLruCache.getInstance().getBitmap(key);
            }
            if (bitmap != null) {
                requester.setBitmap(bitmap);
                RequestThreadManager.getInstance().executeUiTask(requester);
                return;
            }

            if (requester.isOpenDiskLruCache()) {
                bitmap = ImageDiskLruCache.getInstance().getBitmap(key);
            }
            if (bitmap != null) {
                if (requester.isOpenLruCache()) {
                    ImageLruCache.getInstance().addBitmap(key, bitmap);
                }
                requester.setBitmap(bitmap);
                RequestThreadManager.getInstance().executeUiTask(requester);
                return;
            }

            bitmap = loadTheBestBitmap(requester.getPath(), requester.getIv().getWidth(), requester.getIv().getHeight());
            if (bitmap == null) {
                requester.setRequestState(BaseRequester.RequestState.STOP_REQUEST);
                return;
            }

            if (requester.isOpenLruCache()) {
                ImageLruCache.getInstance().addBitmap(key, bitmap);
            }

            if (requester.isOpenDiskLruCache()) {
                ImageDiskLruCache.getInstance().addBitmap(key, bitmap);
            }

            requester.setBitmap(bitmap);
            RequestThreadManager.getInstance().executeUiTask(requester);
        }

        private static Bitmap loadTheBestBitmap(String path, int requestW, int requestH) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            options.inSampleSize = calculateInSampleSize(options, requestW, requestH);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(path, options);
        }

        public static int calculateInSampleSize(BitmapFactory.Options options,
                                                int reqWidth, int reqHeight) {
            // 源图片的高度和宽度
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;
            if (height > reqHeight || width > reqWidth) {
                // 计算出实际宽高和目标宽高的比率
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
                // 一定都会大于等于目标的宽和高。
                inSampleSize = Math.min(heightRatio, widthRatio);
            }
            return inSampleSize;
        }
    }

    public static class RequestUiRunnable implements Runnable {
        private final BaseRequester requester;

        public RequestUiRunnable(BaseRequester requester) {
            this.requester = requester;
        }

        @Override
        public void run() {
            BaseRequester.RequestState requestState = requester.getRequestState();
            if (requestState == BaseRequester.RequestState.STOP_REQUEST) {
                RequestManager.getInstance().removeRequester(requester);
                return;
            }

            requester.getIv().setImageBitmap(requester.getBitmap());

            requester.setRequestState(BaseRequester.RequestState.DONE_REQUEST);
            RequestManager.getInstance().removeRequester(requester);
        }
    }
}
