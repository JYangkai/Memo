package com.yk.memo.markdown.core;

import com.yk.memo.markdown.bean.MdSection;
import com.yk.memo.markdown.bean.MdType;
import com.yk.memo.markdown.core.parser.MdSectionParser;
import com.yk.memo.markdown.core.parser.MdWordParser;

import java.util.List;

public class MdParser {
    private final MdSectionParser sectionParser = new MdSectionParser();
    private final MdWordParser wordParser = new MdWordParser();

    public List<MdSection> parser(String src) {
        List<MdSection> sectionList = sectionParser.dealSection(src);

        if (sectionList == null || sectionList.isEmpty()) {
            return null;
        }

        for (MdSection section : sectionList) {
            MdType type = section.getType();
            if (type != MdType.NORMAL) {
                continue;
            }
            section.setWordList(wordParser.dealWord(section.getSrc()));
        }

        return sectionList;
    }

}