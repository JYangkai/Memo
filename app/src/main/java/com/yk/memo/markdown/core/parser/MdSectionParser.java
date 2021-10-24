package com.yk.memo.markdown.core.parser;

import android.text.TextUtils;

import com.yk.memo.markdown.bean.MdSection;
import com.yk.memo.markdown.core.parser.section.IMdSectionParser;
import com.yk.memo.markdown.core.parser.section.MdCodeBlockParser;
import com.yk.memo.markdown.core.parser.section.MdNormalParser;
import com.yk.memo.markdown.core.parser.section.MdOrderedListParser;
import com.yk.memo.markdown.core.parser.section.MdQuoteParser;
import com.yk.memo.markdown.core.parser.section.MdSeparatorParser;
import com.yk.memo.markdown.core.parser.section.MdTitleParser;
import com.yk.memo.markdown.core.parser.section.MdUnorderedListParser;

import java.util.ArrayList;
import java.util.List;

public class MdSectionParser {
    private final IMdSectionParser quoteParser = new MdQuoteParser();
    private final IMdSectionParser codeBlockParser = new MdCodeBlockParser();
    private final IMdSectionParser orderedListParser = new MdOrderedListParser();
    private final IMdSectionParser unorderedListParser = new MdUnorderedListParser();
    private final IMdSectionParser titleParser = new MdTitleParser();
    private final IMdSectionParser separatorParser = new MdSeparatorParser();
    private final IMdSectionParser normalParser = new MdNormalParser();

    public List<MdSection> dealSection(String src) {
        if (TextUtils.isEmpty(src)) {
            return null;
        }

        String[] srcSplit = src.split("\n");

        if (srcSplit.length == 0) {
            return null;
        }

        List<MdSection> sectionList = new ArrayList<>();

        for (String s : srcSplit) {
            if (quoteParser.parser(sectionList, s)) {
                continue;
            }
            if (codeBlockParser.parser(sectionList, s)) {
                continue;
            }
            if (orderedListParser.parser(sectionList, s)) {
                continue;
            }
            if (unorderedListParser.parser(sectionList, s)) {
                continue;
            }
            if (titleParser.parser(sectionList, s)) {
                continue;
            }
            if (separatorParser.parser(sectionList, s)) {
                continue;
            }

            normalParser.parser(sectionList, s);
        }

        return sectionList;
    }
}
