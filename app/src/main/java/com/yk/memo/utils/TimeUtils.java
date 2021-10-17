package com.yk.memo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;
    private static final long MONTH = 31 * DAY;
    private static final long YEAR = 12 * MONTH;

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

    public static String getSmartTime(long time) {
        long curTime = System.currentTimeMillis();

        long timeDiff = curTime - time;

        if (timeDiff < MINUTE) {
            return "刚刚";
        } else if (timeDiff < HOUR) {
            return timeDiff / MINUTE + " 分前";
        } else if (timeDiff < DAY) {
            return timeDiff / HOUR + " 小时前";
        } else if (time < MONTH) {
            return timeDiff / DAY + " 天前";
        } else if (time < YEAR) {
            return getTime(time, "MM-dd HH:mm:ss");
        } else {
            return getTime(time, "yyyy-MM-dd HH:mm:ss");
        }
    }
}
