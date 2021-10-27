package com.yk.markdown2.core.render.word;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.yk.markdown2.bean.MdWord;
import com.yk.markdown2.span.MdBoldSpan;
import com.yk.markdown2.style.style.BaseMdStyle;

public class MdBoldRender implements IMdWordRender {
    @Override
    public SpannableStringBuilder render(Context context, MdWord word, BaseMdStyle style) {
        String src = word.getSrc();
        src = src.substring(src.indexOf("**") + 2, src.lastIndexOf("**"));

        SpannableStringBuilder spanStr = new SpannableStringBuilder(src);
        spanStr.setSpan(new MdBoldSpan(style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }
}
