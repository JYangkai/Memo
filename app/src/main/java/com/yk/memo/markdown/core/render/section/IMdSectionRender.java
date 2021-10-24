package com.yk.memo.markdown.core.render.section;

import android.content.Context;
import android.text.SpannableStringBuilder;

import com.yk.memo.markdown.bean.MdSection;
import com.yk.memo.markdown.style.style.BaseMdStyle;

/**
 * section渲染接口
 */
public interface IMdSectionRender {
    /**
     * 渲染方法
     *
     * @param context 上下文
     * @param section section
     * @param style   风格
     * @return SpannableStringBuilder
     */
    SpannableStringBuilder render(Context context, MdSection section, BaseMdStyle style);
}
