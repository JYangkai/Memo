package com.yk.memo.data.event;

import com.yk.memo.data.bean.Image;

public class MdImageAddEvent {
    private Image image;

    public MdImageAddEvent(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "MdImageAddEvent{" +
                "image=" + image +
                '}';
    }
}
