package com.yk.memo.ui.preview;

import android.net.Uri;

import com.yk.mvp.BaseMvpView;

public interface IPreviewView extends BaseMvpView {
    void onShareFile(Uri uri);

    void onShareImage(Uri uri);

    void onShareFileError(Exception e);

    void onShareImageError(Exception e);
}
