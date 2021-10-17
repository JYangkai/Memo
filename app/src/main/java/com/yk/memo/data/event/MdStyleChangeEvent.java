package com.yk.memo.data.event;

import com.yk.markdown.style.MdStyleManager;

public class MdStyleChangeEvent {
    private MdStyleManager.Style style;

    public MdStyleChangeEvent(MdStyleManager.Style style) {
        this.style = style;
    }

    public MdStyleManager.Style getStyle() {
        return style;
    }

    public void setStyle(MdStyleManager.Style style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return "MdStyleChangeEvent{" +
                "style=" + style +
                '}';
    }
}
