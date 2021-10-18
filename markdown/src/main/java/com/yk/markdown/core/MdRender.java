package com.yk.markdown.core;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;

import com.yk.markdown.bean.MdSection;
import com.yk.markdown.bean.MdType;
import com.yk.markdown.bean.MdWord;
import com.yk.markdown.span.MdBoldItalicsSpan;
import com.yk.markdown.span.MdBoldSpan;
import com.yk.markdown.span.MdCodeBlockSpan;
import com.yk.markdown.span.MdCodeSpan;
import com.yk.markdown.span.MdItalicsSpan;
import com.yk.markdown.span.MdNormalSpan;
import com.yk.markdown.span.MdOrderedListSpan;
import com.yk.markdown.span.MdQuoteSpan;
import com.yk.markdown.span.MdSeparatorSpan;
import com.yk.markdown.span.MdTitleSpan;
import com.yk.markdown.span.MdUnorderedListSpan;
import com.yk.markdown.style.style.BaseMdStyle;

import java.util.List;

/**
 * Markdown渲染器
 */
public class MdRender {

    /**
     * 获取Span Builder
     *
     * @param sectionList 段落列表
     * @return Span Builder
     */
    public static SpannableStringBuilder getSpanStrBuilder(List<MdSection> sectionList, BaseMdStyle style) {
        if (sectionList == null) {
            return null;
        }

        if (sectionList.isEmpty()) {
            return null;
        }

        SpannableStringBuilder spanStrBuilder = new SpannableStringBuilder();

        for (int i = 0; i < sectionList.size(); i++) {
            MdSection section = sectionList.get(i);
            spanStrBuilder.append(getSpanStr(section, style));
            if (i != sectionList.size() - 1) {
                // 最后一个段落不需要换行
                spanStrBuilder.append("\n");
            }
        }

        return spanStrBuilder;
    }

    /**
     * 获取Span Builder
     *
     * @param section 段落
     * @return Span Builder
     */
    private static SpannableStringBuilder getSpanStr(MdSection section, BaseMdStyle style) {
        MdType type = section.getType();

        SpannableStringBuilder spanStrBuilder = new SpannableStringBuilder();

        switch (type) {
            case QUOTE:
                spanStrBuilder.append(dealQuote(section, style));
                break;
            case CODE_BLOCK:
                spanStrBuilder.append(dealCodeBlock(section, style));
                break;
            case ORDERED_LIST:
                spanStrBuilder.append(dealOrderedList(section, style));
                break;
            case UNORDERED_LIST:
                spanStrBuilder.append(dealUnorderedList(section, style));
                break;
            case TITLE:
                spanStrBuilder.append(dealTitle(section, style));
                break;
            case SEPARATOR:
                spanStrBuilder.append(dealSeparator(section, style));
                break;
            default:
                spanStrBuilder.append(dealNormal(section, style));
                break;
        }

        return spanStrBuilder;
    }

    /**
     * 处理引用
     */
    private static SpannableString dealQuote(MdSection section, BaseMdStyle style) {
        String src = section.getSrc();
        src = src.substring(src.indexOf(" ") + 1);

        if (TextUtils.isEmpty(src)) {
            src = " ";
        }

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdQuoteSpan(style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    /**
     * 处理代码块
     */
    private static SpannableString dealCodeBlock(MdSection section, BaseMdStyle style) {
        String src = section.getSrc();
        src = "\n" + src.substring(src.indexOf("```") + 4, src.lastIndexOf("```")) + "\n";

        if (TextUtils.isEmpty(src)) {
            src = " ";
        }

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdCodeBlockSpan(style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    /**
     * 处理有序列表
     */
    private static SpannableString dealOrderedList(MdSection section, BaseMdStyle style) {
        String src = section.getSrc();
        int index = Integer.parseInt(src.substring(0, 1));
        src = src.substring(src.indexOf(" ") + 1);

        if (TextUtils.isEmpty(src)) {
            src = " ";
        }

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdOrderedListSpan(index, style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    /**
     * 处理无序列表
     */
    private static SpannableString dealUnorderedList(MdSection section, BaseMdStyle style) {
        String src = section.getSrc();
        src = src.substring(src.indexOf(" ") + 1);

        if (TextUtils.isEmpty(src)) {
            src = " ";
        }

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdUnorderedListSpan(style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    /**
     * 处理标题
     */
    private static SpannableString dealTitle(MdSection section, BaseMdStyle style) {
        String src = section.getSrc();
        int level = src.indexOf(" ");
        src = src.substring(src.indexOf(" ") + 1);

        if (TextUtils.isEmpty(src)) {
            src = " ";
        }

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdTitleSpan(level, style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    /**
     * 处理分隔符
     */
    private static SpannableString dealSeparator(MdSection section, BaseMdStyle style) {
        String src = " ";

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdSeparatorSpan(style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    /**
     * 处理其他普通字符
     */
    private static SpannableStringBuilder dealNormal(MdSection section, BaseMdStyle style) {
        String src = section.getSrc();

        SpannableStringBuilder spanStrBuilder = new SpannableStringBuilder();

        List<MdWord> wordList = section.getWordList();

        if (wordList == null || wordList.isEmpty()) {
            spanStrBuilder.append(dealWordNormal(new MdWord(MdType.NORMAL, src, 0, src.length()), style));
            return spanStrBuilder;
        }

        for (MdWord word : wordList) {
            MdType type = word.getType();

            switch (type) {
                case NORMAL:
                    spanStrBuilder.append(dealWordNormal(word, style));
                    break;
                case CODE:
                    spanStrBuilder.append(dealWordCode(word, style));
                    break;
                case BOLD_ITALICS:
                    spanStrBuilder.append(dealWordBoldItalics(word, style));
                    break;
                case BOLD:
                    spanStrBuilder.append(dealWordBold(word, style));
                    break;
                case ITALICS:
                    spanStrBuilder.append(dealWordItalics(word, style));
                    break;
            }
        }

        return spanStrBuilder;
    }

    /**
     * 处理普通字符
     */
    private static SpannableString dealWordNormal(MdWord word, BaseMdStyle style) {
        String src = word.getSrc();

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdNormalSpan(style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    /**
     * 处理代码
     */
    private static SpannableString dealWordCode(MdWord word, BaseMdStyle style) {
        String src = word.getSrc();
        src = src.replace("`", " ");

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdCodeSpan(style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    /**
     * 处理粗斜体
     */
    private static SpannableString dealWordBoldItalics(MdWord word, BaseMdStyle style) {
        String src = word.getSrc();
        src = src.substring(src.indexOf("***") + 3, src.lastIndexOf("***"));

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdBoldItalicsSpan(style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    /**
     * 处理粗体
     */
    private static SpannableString dealWordBold(MdWord word, BaseMdStyle style) {
        String src = word.getSrc();
        src = src.substring(src.indexOf("**") + 2, src.lastIndexOf("**"));

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdBoldSpan(style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    /**
     * 处理斜体
     */
    private static SpannableString dealWordItalics(MdWord word, BaseMdStyle style) {
        String src = word.getSrc();
        src = src.substring(src.indexOf("*") + 1, src.lastIndexOf("*"));

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdItalicsSpan(style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }
}
