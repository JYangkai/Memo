package com.yk.markdown.style;

import android.content.Context;

import com.yk.markdown.style.style.BaseMdStyle;
import com.yk.markdown.style.style.CustomMdStyle;
import com.yk.markdown.style.style.StandardMdStyle;
import com.yk.markdown.style.style.TyporaMdStyle;

public class MdStyleManager {
    public enum Style {
        STANDARD,
        TYPORA,
        CUSTOM
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
        mdStyle = getStyle(style);
    }

    public BaseMdStyle getStyle(Style style) {
        if (style == null) {
            return mdStyle;
        }
        BaseMdStyle baseMdStyle = new StandardMdStyle();
        switch (style) {
            case STANDARD:
                baseMdStyle = new StandardMdStyle();
                break;
            case TYPORA:
                baseMdStyle = new TyporaMdStyle();
                break;
        }
        return baseMdStyle;
    }

    public void chooseCustom(Context context) {
        mdStyle = new CustomMdStyle(context);
    }

    public BaseMdStyle getMdStyle() {
        return mdStyle;
    }
}
