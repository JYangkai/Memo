package com.yk.markdown.core;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.yk.markdown.bean.MdSection;
import com.yk.markdown.bean.MdType;
import com.yk.markdown.span.MdCodeBlockSpan;
import com.yk.markdown.span.MdNormalSpan;
import com.yk.markdown.span.MdOrderedListSpan;
import com.yk.markdown.span.MdQuoteSpan;
import com.yk.markdown.span.MdSeparatorSpan;
import com.yk.markdown.span.MdTitleSpan;
import com.yk.markdown.span.MdUnorderedListSpan;

import java.util.List;

/**
 * Markdown渲染器
 */
public class MdRender {

    public static SpannableStringBuilder getSpanStrBuilder(List<MdSection> sectionList) {
        if (sectionList == null) {
            return null;
        }

        if (sectionList.isEmpty()) {
            return null;
        }

        SpannableStringBuilder spanStrBuilder = new SpannableStringBuilder();

        for (MdSection section : sectionList) {
            spanStrBuilder.append(getSpanStr(section)).append("\n");
        }

        return spanStrBuilder;
    }

    private static SpannableString getSpanStr(MdSection section) {
        MdType type = section.getType();

        SpannableString spanStr;

        switch (type) {
            case NORMAL:
                spanStr = dealNormal(section);
                break;
            case QUOTE:
                spanStr = dealQuote(section);
                break;
            case CODE_BLOCK:
                spanStr = dealCodeBlock(section);
                break;
            case ORDERED_LIST:
                spanStr = dealOrderedList(section);
                break;
            case UNORDERED_LIST:
                spanStr = dealUnorderedList(section);
                break;
            case TITLE:
                spanStr = dealTitle(section);
                break;
            case SEPARATOR:
                spanStr = dealSeparator(section);
                break;
            default:
                spanStr = new SpannableString(section.getSrc());
                break;
        }

        return spanStr;
    }

    private static SpannableString dealNormal(MdSection section) {
        String src = section.getSrc();

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdNormalSpan(), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    private static SpannableString dealQuote(MdSection section) {
        String src = section.getSrc();
        src = src.substring(src.indexOf(" ") + 1);

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdQuoteSpan(), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    private static SpannableString dealCodeBlock(MdSection section) {
        String src = section.getSrc();
        src = "\n" + src.substring(src.indexOf("```") + 3, src.lastIndexOf("```")) + "\n";

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdCodeBlockSpan(), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    private static SpannableString dealOrderedList(MdSection section) {
        String src = section.getSrc();
        int index = Integer.parseInt(src.substring(0, 1));
        src = src.substring(src.indexOf(" ") + 1);

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdOrderedListSpan(index), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    private static SpannableString dealUnorderedList(MdSection section) {
        String src = section.getSrc();
        src = src.substring(src.indexOf(" ") + 1);

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdUnorderedListSpan(), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    private static SpannableString dealTitle(MdSection section) {
        String src = section.getSrc();
        int level = src.indexOf(" ");
        src = src.substring(src.indexOf(" ") + 1);

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdTitleSpan(level), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    private static SpannableString dealSeparator(MdSection section) {
        String src = " ";

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdSeparatorSpan(), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

}
