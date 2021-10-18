package com.yk.markdown.style;

import android.content.Context;
import android.text.TextUtils;

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
        mdStyle.init();
    }

    public void choose(Context context, String style) {
        mdStyle = getStyle(context, style);
        mdStyle.init();
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

    public BaseMdStyle getStyle(Context context, String style) {
        if (TextUtils.isEmpty(style)) {
            return mdStyle;
        }
        BaseMdStyle baseMdStyle = new StandardMdStyle();
        switch (style) {
            case "Standard":
                baseMdStyle = new StandardMdStyle();
                break;
            case "Typora":
                baseMdStyle = new TyporaMdStyle();
                break;
            case "Custom":
                baseMdStyle = new CustomMdStyle(context);
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
