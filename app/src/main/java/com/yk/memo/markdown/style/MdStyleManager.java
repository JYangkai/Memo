package com.yk.memo.markdown.style;

import android.content.Context;

import com.yk.memo.markdown.style.style.BaseMdStyle;
import com.yk.memo.markdown.style.style.CustomMdStyle;
import com.yk.memo.markdown.style.style.StandardMdStyle;
import com.yk.memo.markdown.style.style.TyporaMdStyle;

public class MdStyleManager {
    public enum Style {
        STANDARD,
        TYPORA
    }

    public enum CustomStyle {
        CUSTOM
    }

    private static volatile MdStyleManager instance;

    private BaseMdStyle style;

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
        this.style = getStyle(style);
    }

    public void chooseCustom(Context context, CustomStyle customStyle) {
        this.style = getCustomStyle(context, customStyle);
    }

    public static BaseMdStyle getStyle(String style) {
        if (Style.STANDARD.name().equals(style)) {
            return getStyle(Style.STANDARD);
        } else if (Style.TYPORA.name().equals(style)) {
            return getStyle(Style.TYPORA);
        }
        return getStyle(Style.STANDARD);
    }

    public static BaseMdStyle getStyle(Style style) {
        BaseMdStyle baseMdStyle;
        switch (style) {
            case STANDARD:
                baseMdStyle = new StandardMdStyle();
                break;
            case TYPORA:
                baseMdStyle = new TyporaMdStyle();
                break;
            default:
                baseMdStyle = new StandardMdStyle();
                break;
        }

        baseMdStyle.init();

        return baseMdStyle;
    }

    public static BaseMdStyle getCustomStyle(Context context, String customStyle) {
        if (CustomStyle.CUSTOM.name().equals(customStyle)) {
            return getCustomStyle(context, CustomStyle.CUSTOM);
        }
        return getStyle(Style.STANDARD);
    }

    public static BaseMdStyle getCustomStyle(Context context, CustomStyle customStyle) {
        BaseMdStyle baseMdStyle;
        switch (customStyle) {
            case CUSTOM:
                baseMdStyle = new CustomMdStyle(context);
                break;
            default:
                baseMdStyle = new StandardMdStyle();
                break;
        }

        baseMdStyle.init();

        return baseMdStyle;
    }

    public BaseMdStyle getStyle() {
        return style;
    }
}
