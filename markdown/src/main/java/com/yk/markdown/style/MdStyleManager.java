package com.yk.markdown.style;

import com.yk.markdown.style.style.BaseMdStyle;
import com.yk.markdown.style.style.StandardMdStyle;
import com.yk.markdown.style.style.TyporaMdStyle;

public class MdStyleManager {
    public enum Style {
        STANDARD,
        TYPORA,
    }

    private BaseMdStyle mdStyle;

    private static volatile MdStyleManager instance;

    private MdStyleManager() {
        choose(Style.STANDARD);
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
            case TYPORA:
                mdStyle = new TyporaMdStyle();
                break;
        }
    }

    public BaseMdStyle getMdStyle() {
        return mdStyle;
    }
}
