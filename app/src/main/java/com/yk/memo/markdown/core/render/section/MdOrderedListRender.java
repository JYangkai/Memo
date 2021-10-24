package com.yk.memo.markdown.core.render.section;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;

import com.yk.memo.markdown.bean.MdSection;
import com.yk.memo.markdown.span.MdOrderedListSpan;
import com.yk.memo.markdown.style.style.BaseMdStyle;

public class MdOrderedListRender implements IMdSectionRender {
    @Override
    public SpannableStringBuilder render(Context context, MdSection section, BaseMdStyle style) {
        String src = section.getSrc();
        int index = Integer.parseInt(src.substring(0, 1));
        src = src.substring(src.indexOf(" ") + 1);

        if (TextUtils.isEmpty(src)) {
            src = " ";
        }

        SpannableStringBuilder spanStr = new SpannableStringBuilder(src);
        spanStr.setSpan(new MdOrderedListSpan(index, style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }
}
