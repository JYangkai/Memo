package com.yk.memo.data.event;

public class MdStyleChangeEvent {
    private String style;

    public MdStyleChangeEvent(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return "MdStyleChangeEvent{" +
                "style=" + style +
                '}';
    }
}
