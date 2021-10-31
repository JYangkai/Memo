package com.yk.memo.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    public static File zipAllMarkdown(Context context) throws IOException {
        File markdownFolder = new File(FileUtils.getMarkdownFolder(context));
        String zipPath = markdownFolder.getParent() + File.separator + TimeUtils.getTime(System.currentTimeMillis(), "yyyy-MM-dd_HH-mm-ss") + ".zip";
        zipFile(markdownFolder, zipPath);
        return new File(zipPath);
    }

    public static void zipFile(File file, String zipPath) throws IOException {
        zipFile(file, new ZipOutputStream(new FileOutputStream(zipPath)), true);
    }

    public static void zipFile(File file, ZipOutputStream zos, boolean needClose) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                ZipEntry entry = new ZipEntry(file.getName());
                zos.putNextEntry(entry);
                zos.closeEntry();
            } else {
                for (File subFile : files) {
                    zipFile(subFile, zos, false);
                }
            }
        } else {
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[fis.available()];
            int len = fis.read(data);

            ZipEntry entry = new ZipEntry(file.getName());
            zos.putNextEntry(entry);
            zos.write(data, 0, len);
            zos.closeEntry();
            fis.close();
        }
        if (needClose) {
            zos.close();
        }
    }

    public static void unzipFile(String zipPath, String outputPath) throws IOException {
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath));
        String name;
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            name = entry.getName();
            if (entry.isDirectory()) {
                name = name.substring(0, name.length() - 1);
                File folder = new File(outputPath + File.separator + name);
                folder.mkdirs();
            } else {
                File file = new File(outputPath + File.separator + name);

                FileOutputStream fos = new FileOutputStream(file);
                int len;
                byte[] data = new byte[1024];
                while ((len = zis.read(data)) != -1) {
                    fos.write(data, 0, len);
                }
                fos.close();
            }
        }
        zis.close();
    }

}
