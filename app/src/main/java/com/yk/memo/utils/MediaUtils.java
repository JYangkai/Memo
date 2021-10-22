package com.yk.memo.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.yk.memo.data.bean.Image;

import java.util.ArrayList;
import java.util.List;

public class MediaUtils {
    public static List<Image> getAllImage(Context context) {
        List<Image> list = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, null, null,
                MediaStore.Images.Media.DATE_ADDED + " desc"
        );

        if (cursor == null) {
            return list;
        }

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
            Image image = new Image(path, name);
            list.add(image);
        }

        cursor.close();

        return list;
    }
}
