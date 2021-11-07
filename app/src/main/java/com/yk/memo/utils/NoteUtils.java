package com.yk.memo.utils;

import android.content.Context;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.yk.db.bean.Note;
import com.yk.io.FileUtils;
import com.yk.io.ZipUtils;

import java.io.File;

public class NoteUtils {
    private static final String FOLDER_MARKDOWN = "folder_markdown";
    private static final String FOLDER_MARKDOWN_SHOT = "folder_markdown_shot";

    public static String getMarkdownFolder(Context context) {
        return getFolderPath(context, FOLDER_MARKDOWN);
    }

    public static String getMarkdownShotFolder(Context context) {
        return getFolderPath(context, FOLDER_MARKDOWN_SHOT);
    }

    public static String getFolderPath(Context context, String folder) {
        return context.getExternalFilesDir(folder).getPath() + File.separator;
    }

    public static boolean outputNote(Context context, Note note) {
        if (context == null) {
            return false;
        }

        if (note == null) {
            return false;
        }

        return FileUtils.outputTextToFile(note.getSrc(), generateNotePath(context, note));
    }

    public static boolean isOutputNote(Context context, Note note) {
        if (context == null) {
            return false;
        }

        if (note == null) {
            return false;
        }

        return FileUtils.isFileExists(generateNotePath(context, note));
    }

    public static String zipAllMarkdown(Context context) {
        File markdownFolder = new File(getMarkdownFolder(context));
        String zipPath = generateZipPath(markdownFolder);
        ZipUtils.zipFile(markdownFolder, zipPath);
        return zipPath;
    }

    public static String generateNotePath(Context context, Note note) {
        return getMarkdownFolder(context) + TimeUtils.getTime(note.getCreateTime(), getTimeFormat()) + ".md";
    }

    public static String generateShotPath(Context context) {
        return getMarkdownShotFolder(context) + TimeUtils.getTime(System.currentTimeMillis(), getTimeFormat()) + ".jpg";
    }

    public static String generateZipPath(File folder) {
        return folder.getParent() + File.separator + TimeUtils.getTime(System.currentTimeMillis(), getTimeFormat()) + ".zip";
    }

    public static Uri getFileUri(Context context, File file) {
        return FileProvider.getUriForFile(context, "com.yk.memo.fileprovider", file);
    }

    public static String getTimeFormat() {
        return "yyyy-MM-dd_HH-mm-ss";
    }

}
