package com.yk.memo.markdown.style;

import android.content.Context;

import com.yk.memo.markdown.style.style.BaseMdStyle;
import com.yk.memo.markdown.style.style.CustomMdStyle;
import com.yk.memo.markdown.style.style.StandardMdStyle;
import com.yk.memo.markdown.style.style.TyporaMdStyle;

public class MdStyleManager {
    public enum Style {
        STANDARD,
        TYPORA,
        CUSTOM,
    }

    private static volatile MdStyleManager instance;

    private BaseMdStyle style;

    private MdStyleManager() {
        choose(null, Style.STANDARD);
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

    public void choose(Context context, Style style) {
        this.style = getStyle(context, style);
    }

    public BaseMdStyle getStyle(Context context, String style) {
        if (Style.STANDARD.name().equals(style)) {
            return getStyle(null, Style.STANDARD);
        } else if (Style.TYPORA.name().equals(style)) {
            return getStyle(null, Style.TYPORA);
        } else if (Style.CUSTOM.name().equals(style)) {
            return getStyle(context, Style.CUSTOM);
        }
        return getStyle();
    }

    public BaseMdStyle getStyle(Context context, Style style) {
        if (style == null) {
            return getStyle();
        }
        BaseMdStyle baseMdStyle = new StandardMdStyle();
        switch (style) {
            case STANDARD:
                baseMdStyle = new StandardMdStyle();
                break;
            case TYPORA:
                baseMdStyle = new TyporaMdStyle();
                break;
            case CUSTOM:
                baseMdStyle = new CustomMdStyle(context);
                break;
            default:
                break;
        }

        baseMdStyle.init();

        return baseMdStyle;
    }

    public BaseMdStyle getStyle() {
        return style;
    }
}
