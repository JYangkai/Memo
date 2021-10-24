package com.yk.memo.markdown.core.render;

import android.content.Context;
import android.text.SpannableStringBuilder;

import com.yk.memo.markdown.bean.MdSection;
import com.yk.memo.markdown.bean.MdType;
import com.yk.memo.markdown.core.render.section.IMdSectionRender;
import com.yk.memo.markdown.core.render.section.MdCodeBlockRender;
import com.yk.memo.markdown.core.render.section.MdOrderedListRender;
import com.yk.memo.markdown.core.render.section.MdQuoteRender;
import com.yk.memo.markdown.core.render.section.MdSeparatorRender;
import com.yk.memo.markdown.core.render.section.MdTitleRender;
import com.yk.memo.markdown.core.render.section.MdUnorderedListRender;
import com.yk.memo.markdown.style.style.BaseMdStyle;

public class MdSectionRender {
    private final IMdSectionRender quoteRender = new MdQuoteRender();
    private final IMdSectionRender codeBlockRender = new MdCodeBlockRender();
    private final IMdSectionRender orderedListRender = new MdOrderedListRender();
    private final IMdSectionRender unorderedListRender = new MdUnorderedListRender();
    private final IMdSectionRender titleRender = new MdTitleRender();
    private final IMdSectionRender separatorRender = new MdSeparatorRender();

    public SpannableStringBuilder dealSection(Context context, MdSection section, BaseMdStyle style) {
        MdType type = section.getType();

        switch (type) {
            case QUOTE:
                return quoteRender.render(context, section, style);
            case CODE_BLOCK:
                return codeBlockRender.render(context, section, style);
            case ORDERED_LIST:
                return orderedListRender.render(context, section, style);
            case UNORDERED_LIST:
                return unorderedListRender.render(context, section, style);
            case TITLE:
                return titleRender.render(context, section, style);
            case SEPARATOR:
                return separatorRender.render(context, section, style);
            default:
                return null;
        }
    }
}
