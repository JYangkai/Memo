package com.yk.imageloader3;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class Requester {
    private String path;
    private ImageView iv;

    private Bitmap bitmap;

    public Requester(String path) {
        this.path = path;
    }

    public static Requester get(String path) {
        return new Requester(path);
    }

    public void into(ImageView iv) {
        this.iv = iv;
        iv.setTag(path);
        iv.setImageResource(R.color.color_white);
        ImageManager.getInstance().deal(this);
    }

    public String getPath() {
        return path;
    }

    public ImageView getIv() {
        return iv;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Requester) {
            Requester requester = (Requester) obj;
            return requester.getPath().equals(this.getPath());
        }
        return super.equals(obj);
    }
}
