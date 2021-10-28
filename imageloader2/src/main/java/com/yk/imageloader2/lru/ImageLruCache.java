package com.yk.imageloader2.lru;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageLruCache {

    private static volatile ImageLruCache instance;

    private final LruCache<String, Bitmap> cache;

    private ImageLruCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheMemory = maxMemory / 8;
        cache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    public static ImageLruCache getInstance() {
        if (instance == null) {
            synchronized (ImageLruCache.class) {
                if (instance == null) {
                    instance = new ImageLruCache();
                }
            }
        }
        return instance;
    }

    public void addBitmap(String key, Bitmap bitmap) {
        if (getBitmap(key) == null) {
            cache.put(key, bitmap);
        }
    }

    public Bitmap getBitmap(String key) {
        return cache.get(key);
    }
}
