package com.yk.memo.markdown.core.render;

import android.content.Context;
import android.text.SpannableStringBuilder;

import com.yk.memo.markdown.bean.MdType;
import com.yk.memo.markdown.bean.MdWord;
import com.yk.memo.markdown.core.render.word.IMdWordRender;
import com.yk.memo.markdown.core.render.word.MdCodeRender;
import com.yk.memo.markdown.style.style.BaseMdStyle;

public class MdWordRender {
    private final IMdWordRender normalRender = new MdCodeRender();
    private final IMdWordRender codeRender = new MdCodeRender();
    private final IMdWordRender boldItalicsRender = new MdCodeRender();
    private final IMdWordRender boldRender = new MdCodeRender();
    private final IMdWordRender italicsRender = new MdCodeRender();
    private final IMdWordRender imageRender = new MdCodeRender();

    public SpannableStringBuilder dealWord(Context context, MdWord word, BaseMdStyle style) {
        MdType type = word.getType();

        switch (type) {
            case NORMAL:
                return normalRender.render(context, word, style);
            case CODE:
                return codeRender.render(context, word, style);
            case BOLD_ITALICS:
                return boldItalicsRender.render(context, word, style);
            case BOLD:
                return boldRender.render(context, word, style);
            case ITALICS:
                return italicsRender.render(context, word, style);
            case IMAGE:
                return imageRender.render(context, word, style);
            default:
                return null;
        }
    }
}
