package com.yk.memo.markdown.core.render.word;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.yk.memo.markdown.bean.MdWord;
import com.yk.memo.markdown.span.MdCodeSpan;
import com.yk.memo.markdown.style.style.BaseMdStyle;

public class MdCodeRender implements IMdWordRender{
    @Override
    public SpannableStringBuilder render(Context context, MdWord word, BaseMdStyle style) {
        String src = word.getSrc();
        src = src.replace("`", " ");

        SpannableStringBuilder spanStr = new SpannableStringBuilder(src);
        spanStr.setSpan(new MdCodeSpan(style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }
}
