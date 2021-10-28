package com.yk.markdown2.core.render.word;

import android.content.Context;
import android.text.SpannableStringBuilder;

import com.yk.markdown2.bean.MdWord;
import com.yk.markdown2.style.style.BaseMdStyle;

/**
 * word渲染接口
 */
public interface IMdWordRender {
    /**
     * 渲染方法
     *
     * @param context 上下文
     * @param word    word
     * @param style   风格
     * @return SpannableStringBuilder
     */
    SpannableStringBuilder render(Context context, MdWord word, BaseMdStyle style);
}
