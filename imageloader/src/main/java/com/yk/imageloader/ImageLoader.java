package com.yk.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

public class ImageLoader {

    private String path;

    private boolean openLruCache = true;

    private boolean openDiskLruCache = true;

    private ImageView iv;

    private ImageLoader(Context context) {
        ImageDiskLruCache.getInstance().init(context);
    }

    public static ImageLoader with(Context context) {
        return new ImageLoader(context);
    }

    public ImageLoader load(String path) {
        this.path = path;
        return this;
    }

    public ImageLoader openLruCache(boolean openLruCache) {
        this.openLruCache = openLruCache;
        return this;
    }

    public ImageLoader openDisLruCache(boolean openDiskLruCache) {
        this.openDiskLruCache = openDiskLruCache;
        return this;
    }

    public void into(ImageView iv) {
        if (iv == null) {
            return;
        }

        if (TextUtils.isEmpty(path)) {
            return;
        }

        this.iv = iv;

        executeTask();
    }

    private void executeTask() {
        ImageLoadThreadManager.getInstance().postIo(new Runnable() {
            @Override
            public void run() {
                String key = KeyUtils.hashKeyForDisk(path);

                Bitmap bitmap = null;

                // LruCache
                if (openLruCache) {
                    bitmap = ImageLruCache.getInstance().getBitmap(key);
                }
                if (bitmap != null) {
                    refreshImage(bitmap);
                    return;
                }

                // DiskLruCache
                if (openDiskLruCache) {
                    bitmap = ImageDiskLruCache.getInstance().getBitmap(key);
                }
                if (bitmap != null) {
                    // 存入LruCache
                    if (openLruCache) {
                        ImageLruCache.getInstance().addBitmap(key, bitmap);
                    }
                    refreshImage(bitmap);
                    return;
                }

                // BitmapFactory
                bitmap = BitmapFactory.decodeFile(path);
                if (bitmap == null) {
                    return;
                }

                // 存入LruCache
                if (openLruCache) {
                    ImageLruCache.getInstance().addBitmap(key, bitmap);
                }

                // 存入DiskLruCache
                if (openDiskLruCache) {
                    ImageDiskLruCache.getInstance().addBitmap(key, bitmap);
                }

                refreshImage(bitmap);
            }
        });
    }

    private void refreshImage(Bitmap bitmap) {
        ImageLoadThreadManager.getInstance().postUi(new Runnable() {
            @Override
            public void run() {
                iv.setImageBitmap(bitmap);
            }
        });
    }
}
