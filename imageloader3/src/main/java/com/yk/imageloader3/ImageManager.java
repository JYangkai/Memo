package com.yk.imageloader3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageManager {

    private static volatile ImageManager instance;

    private final ExecutorService ioService;

    private final Handler uiHandler;

    private final List<Requester> requesterList;

    private final LruCache<String, Bitmap> cache;

    private ImageManager() {
        ioService = Executors.newFixedThreadPool(5);
        uiHandler = new Handler(Looper.getMainLooper());

        requesterList = new ArrayList<>();

        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheMemory = maxMemory / 8;
        cache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    public static ImageManager getInstance() {
        if (instance == null) {
            synchronized (ImageManager.class) {
                if (instance == null) {
                    instance = new ImageManager();
                }
            }
        }
        return instance;
    }

    public void deal(Requester requester) {
        if (requesterList.contains(requester)) {
            Bitmap bitmap = requester.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                requester.getIv().setImageBitmap(bitmap);
                return;
            }
        }

        Bitmap bitmap = getBitmap(requester.getPath());
        if (bitmap != null && !bitmap.isRecycled()) {
            requester.setBitmap(bitmap);
            requesterList.add(requester);
            requester.getIv().setImageBitmap(bitmap);
            return;
        }

        requesterList.add(requester);
        ioService.execute(new Runnable() {
            @Override
            public void run() {
                String path = requester.getPath();

                Bitmap bitmap = loadTheBestBitmap(path, requester.getIv().getWidth(), requester.getIv().getHeight());
                if (bitmap != null) {
                    addBitmap(path, bitmap);
                    requester.setBitmap(bitmap);
                    refreshUi(requester);
                }
            }
        });
    }

    private void refreshUi(Requester requester) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                ImageView iv = requester.getIv();

                String tag = (String) iv.getTag();
                String path = requester.getPath();

                if (!path.equals(tag)) {
                    return;
                }

                iv.setImageBitmap(requester.getBitmap());
            }
        });
    }

    private void addBitmap(String key, Bitmap bitmap) {
        if (getBitmap(key) == null) {
            cache.put(key, bitmap);
        }
    }

    private Bitmap getBitmap(String key) {
        return cache.get(key);
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
