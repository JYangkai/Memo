package com.yk.memo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SpManager {

    private static volatile SpManager instance;

    private SharedPreferences sp;

    private SpManager() {
    }

    public static SpManager getInstance() {
        if (instance == null) {
            synchronized (SpManager.class) {
                if (instance == null) {
                    instance = new SpManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getMarkdownStyle() {
        return getValue(Sp.MarkdownStyle.KEY, Sp.MarkdownStyle.DEFAULT_VALUE);
    }

    public String getValue(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public interface Sp {
        interface MarkdownStyle {
            String KEY = "key_markdown_style";
            String DEFAULT_VALUE = "Standard";
        }
    }

}
