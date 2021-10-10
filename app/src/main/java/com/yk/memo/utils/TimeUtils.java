package com.yk.memo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    /**
     * 获取系统当前时间
     */
    public static String getCurrentTime() {
        return getTime(System.currentTimeMillis());
    }

    /**
     * 通过时间戳获取时间
     */
    public static String getTime(long time) {
        return getTime(time, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 通过时间戳和format获取时间
     */
    public static String getTime(long time, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = new Date(time);
        return format.format(date);
    }
}
