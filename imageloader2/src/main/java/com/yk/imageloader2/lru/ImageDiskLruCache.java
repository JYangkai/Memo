package com.yk.imageloader2.lru;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;

public class ImageDiskLruCache {
    private static final String TAG = "ImageDiskLruCache";

    private static volatile ImageDiskLruCache instance;

    private DiskLruCache cache;

    private ImageDiskLruCache() {
    }

    public static ImageDiskLruCache getInstance() {
        if (instance == null) {
            synchronized (ImageDiskLruCache.class) {
                if (instance == null) {
                    instance = new ImageDiskLruCache();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        File file = getDiskLruCacheDir(context, "bitmap");
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            cache = DiskLruCache.open(file, getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBitmap(String key, Bitmap bitmap) {
        Log.d(TAG, "addBitmap: " + key);
        if (cache == null) {
            return;
        }
        try {
            DiskLruCache.Editor editor = cache.edit(key);
            boolean success = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, editor.newOutputStream(0));
            if (success) {
                editor.commit();
            } else {
                editor.abort();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmap(String key) {
        Log.d(TAG, "getBitmap: " + key);
        if (cache == null) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            DiskLruCache.Snapshot snapshot = cache.get(key);
            if (snapshot == null) {
                return bitmap;
            }
            bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void removeBitmap(String key) {
        if (cache == null) {
            return;
        }
        try {
            cache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush() {
        Log.d(TAG, "flush: ");
        if (cache == null) {
            return;
        }
        try {
            cache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDiskLruCacheDir(Context context, String uniqueName) {
        String path = context.getExternalCacheDir().getPath() + File.separator + uniqueName;
        return new File(path);
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

}
