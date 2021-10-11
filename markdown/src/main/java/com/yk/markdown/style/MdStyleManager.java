package com.yk.markdown.style;

import com.yk.markdown.style.style.BaseMdStyle;
import com.yk.markdown.style.style.StandardMdStyle;

public class MdStyleManager {
    public enum Style {
        STANDARD
    }

    private BaseMdStyle mdStyle;

    private static volatile MdStyleManager instance;

    private MdStyleManager() {
        mdStyle = new StandardMdStyle();
    }

    public static MdStyleManager getInstance() {
        if (instance == null) {
            synchronized (MdStyleManager.class) {
                if (instance == null) {
                    instance = new MdStyleManager();
                }
            }
        }
        return instance;
    }

    public void choose(Style style) {
        switch (style) {
            case STANDARD:
                mdStyle = new StandardMdStyle();
                break;
        }
    }

    public BaseMdStyle getMdStyle() {
        return mdStyle;
    }
}
