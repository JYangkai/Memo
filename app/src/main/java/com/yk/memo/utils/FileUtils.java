package com.yk.memo.utils;

import android.content.Context;

import com.yk.memo.data.bean.Note;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    private static final String FOLDER_MARKDOWN = "folder_markdown";

    public static String getMarkdownFolder(Context context) {
        return getFolderPath(context, FOLDER_MARKDOWN);
    }

    public static String getFolderPath(Context context, String folder) {
        return context.getExternalFilesDir(folder).getPath() + File.separator;
    }

    public static boolean outputNoteToMarkdownFolder(Context context, Note note) {
        String path = getMarkdownFolder(context) + TimeUtils.getTime(note.getCreateTime(), "yyyy-MM-dd_HH-mm-ss") + ".md";

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        boolean success;

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(note.getSrc().getBytes());
            fos.close();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

}
