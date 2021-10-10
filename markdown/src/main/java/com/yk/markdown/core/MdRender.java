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

        for (int i = 0; i < sectionList.size(); i++) {
            MdSection section = sectionList.get(i);
            spanStrBuilder.append(getSpanStr(section));
            if (i != sectionList.size() - 1) {
                spanStrBuilder.append("\n");
            }
        }

        return spanStrBuilder;
    }

    private static SpannableStringBuilder getSpanStr(MdSection section) {
        MdType type = section.getType();

        SpannableStringBuilder spanStrBuilder = new SpannableStringBuilder();

        switch (type) {
            case QUOTE:
                spanStrBuilder.append(dealQuote(section));
                break;
            case CODE_BLOCK:
                spanStrBuilder.append(dealCodeBlock(section));
                break;
            case ORDERED_LIST:
                spanStrBuilder.append(dealOrderedList(section));
                break;
            case UNORDERED_LIST:
                spanStrBuilder.append(dealUnorderedList(section));
                break;
            case TITLE:
                spanStrBuilder.append(dealTitle(section));
                break;
            case SEPARATOR:
                spanStrBuilder.append(dealSeparator(section));
                break;
            default:
                spanStrBuilder.append(dealNormal(section));
                break;
        }

        return spanStrBuilder;
    }

    private static SpannableString dealQuote(MdSection section) {
        String src = section.getSrc();
        src = src.substring(src.indexOf(" ") + 1);

        if (TextUtils.isEmpty(src)) {
            src = " ";
        }

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdQuoteSpan(), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    private static SpannableString dealCodeBlock(MdSection section) {
        String src = section.getSrc();
        src = "\n" + src.substring(src.indexOf("```") + 4, src.lastIndexOf("```")) + "\n";

        if (TextUtils.isEmpty(src)) {
            src = " ";
        }

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdCodeBlockSpan(), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    private static SpannableString dealOrderedList(MdSection section) {
        String src = section.getSrc();
        int index = Integer.parseInt(src.substring(0, 1));
        src = src.substring(src.indexOf(" ") + 1);

        if (TextUtils.isEmpty(src)) {
            src = " ";
        }

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdOrderedListSpan(index), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    private static SpannableString dealUnorderedList(MdSection section) {
        String src = section.getSrc();
        src = src.substring(src.indexOf(" ") + 1);

        if (TextUtils.isEmpty(src)) {
            src = " ";
        }

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdUnorderedListSpan(), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    private static SpannableString dealTitle(MdSection section) {
        String src = section.getSrc();
        int level = src.indexOf(" ");
        src = src.substring(src.indexOf(" ") + 1);

        if (TextUtils.isEmpty(src)) {
            src = " ";
        }

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

    private static SpannableStringBuilder dealNormal(MdSection section) {
        String src = section.getSrc().trim();

        SpannableStringBuilder spanStrBuilder = new SpannableStringBuilder();

        List<MdWord> wordList = section.getWordList();

        if (wordList == null || wordList.isEmpty()) {
            spanStrBuilder.append(dealWordNormal(new MdWord(MdType.NORMAL, src, 0, src.length())));
            return spanStrBuilder;
        }

        for (MdWord word : wordList) {
            MdType type = word.getType();

            switch (type) {
                case NORMAL:
                    spanStrBuilder.append(dealWordNormal(word));
                    break;
                case CODE:
                    spanStrBuilder.append(dealWordCode(word));
                    break;
                case BOLD_ITALICS:
                    spanStrBuilder.append(dealWordBoldItalics(word));
                    break;
                case BOLD:
                    spanStrBuilder.append(dealWordBold(word));
                    break;
                case ITALICS:
                    spanStrBuilder.append(dealWordItalics(word));
                    break;
            }
        }

        return spanStrBuilder;
    }

    private static SpannableString dealWordNormal(MdWord word) {
        String src = word.getSrc();

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdNormalSpan(), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    private static SpannableString dealWordCode(MdWord word) {
        String src = word.getSrc();
        src = src.substring(src.indexOf('`') + 1, src.lastIndexOf('`'));

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdCodeSpan(), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    private static SpannableString dealWordBoldItalics(MdWord word) {
        String src = word.getSrc();
        src = src.substring(src.indexOf("***") + 3, src.lastIndexOf("***"));

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdBoldItalicsSpan(), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    private static SpannableString dealWordBold(MdWord word) {
        String src = word.getSrc();
        src = src.substring(src.indexOf("**") + 2, src.lastIndexOf("**"));

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdBoldSpan(), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    private static SpannableString dealWordItalics(MdWord word) {
        String src = word.getSrc();
        src = src.substring(src.indexOf("*") + 1, src.lastIndexOf("*"));

        SpannableString spanStr = new SpannableString(src);
        spanStr.setSpan(new MdItalicsSpan(), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }
}
