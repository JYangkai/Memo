package com.yk.memo.markdown.core.render.word;

import android.content.Context;
import android.text.SpannableStringBuilder;

import com.yk.memo.markdown.bean.MdWord;
import com.yk.memo.markdown.style.style.BaseMdStyle;

public interface IMdWordRender {
    SpannableStringBuilder render(Context context, MdWord word, BaseMdStyle style);
}
