package com.yk.imageloader2.request;

import android.widget.ImageView;

public interface IRequester {
    IRequester load(String path);

    IRequester openLruCache(boolean open);

    IRequester openDisLruCache(boolean open);

    void into(ImageView iv);
}
