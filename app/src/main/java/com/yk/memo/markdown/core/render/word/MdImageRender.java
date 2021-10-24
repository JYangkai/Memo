package com.yk.memo.markdown.core.render.word;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.yk.memo.markdown.bean.MdWord;
import com.yk.memo.markdown.span.MdImageSpan;
import com.yk.memo.markdown.style.style.BaseMdStyle;

public class MdImageRender implements IMdWordRender {
    @Override
    public SpannableStringBuilder render(Context context, MdWord word, BaseMdStyle style) {
        String src = word.getSrc();
        String path = src.substring(src.indexOf("(") + 1, src.lastIndexOf(")"));

        SpannableStringBuilder spanStr = new SpannableStringBuilder(src);
        spanStr.setSpan(new MdImageSpan(context, path), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }
}
