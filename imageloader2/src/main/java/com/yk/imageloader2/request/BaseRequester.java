package com.yk.imageloader2.request;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.yk.imageloader2.manager.RequestManager;
import com.yk.imageloader2.utils.KeyUtils;

public abstract class BaseRequester implements IRequester {
    public enum RequestState {
        READY_REQUEST, // 准备
        START_REQUEST, // 开始
        STOP_REQUEST, // 停止
        DONE_REQUEST, // 完成
    }

    private RequestState requestState = RequestState.READY_REQUEST;

    private String path;
    private Bitmap bitmap;

    private ImageView iv;

    private boolean openLruCache = true;
    private boolean openDiskLruCache = true;

    public abstract Context getContext();

    @Override
    public IRequester load(String path) {
        this.path = path;
        return this;
    }

    @Override
    public IRequester openLruCache(boolean open) {
        openLruCache = open;
        return this;
    }

    @Override
    public IRequester openDisLruCache(boolean open) {
        openDiskLruCache = open;
        return this;
    }

    @Override
    public void into(ImageView iv) {
        this.iv = iv;
        iv.setTag(KeyUtils.hashKey(path));
        RequestManager.getInstance().addRequester(this);
    }

    public RequestState getRequestState() {
        return requestState;
    }

    public void setRequestState(RequestState requestState) {
        this.requestState = requestState;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getPath() {
        return path;
    }

    public boolean isOpenLruCache() {
        return openLruCache;
    }

    public boolean isOpenDiskLruCache() {
        return openDiskLruCache;
    }

    public ImageView getIv() {
        return iv;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof BaseRequester) {
            BaseRequester requester = (BaseRequester) obj;
            return requester.getIv().getTag().equals(this.getIv().getTag());
        }
        return super.equals(obj);
    }
}
