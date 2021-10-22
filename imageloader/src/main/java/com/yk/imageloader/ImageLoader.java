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
                bitmap = loadTheBestBitmap(path, iv.getWidth(), iv.getHeight());
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

    private Bitmap loadTheBestBitmap(String path, int requestW, int requestH) {
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
