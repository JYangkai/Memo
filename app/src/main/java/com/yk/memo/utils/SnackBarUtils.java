package com.yk.memo.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class SnackBarUtils {

    public static void showMsgShort(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    public static void showMsgLong(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

}
