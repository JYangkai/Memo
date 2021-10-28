package com.yk.markdown2.core.render.section;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.yk.markdown2.bean.MdSection;
import com.yk.markdown2.span.MdSeparatorSpan;
import com.yk.markdown2.style.style.BaseMdStyle;

public class MdSeparatorRender implements IMdSectionRender {
    @Override
    public SpannableStringBuilder render(Context context, MdSection section, BaseMdStyle style) {
        String src = " ";

        SpannableStringBuilder spanStr = new SpannableStringBuilder(src);
        spanStr.setSpan(new MdSeparatorSpan(style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }
}
