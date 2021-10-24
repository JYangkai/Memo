package com.yk.memo.markdown.core.render.section;

import android.content.Context;
import android.text.SpannableStringBuilder;

import com.yk.memo.markdown.bean.MdSection;
import com.yk.memo.markdown.style.style.BaseMdStyle;

public interface IMdSectionRender {
    SpannableStringBuilder render(Context context, MdSection section, BaseMdStyle style);
}
