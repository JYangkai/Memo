package com.yk.markdown2.core;

import android.util.Log;

import com.yk.markdown2.bean.MdSection;
import com.yk.markdown2.bean.MdType;
import com.yk.markdown2.core.parser.MdSectionParser;
import com.yk.markdown2.core.parser.MdWordParser;

import java.util.List;

/**
 * Markdown解析
 */
public class MdParser {
    private static final String TAG = "MdParser";

    private final MdSectionParser sectionParser = new MdSectionParser();
    private final MdWordParser wordParser = new MdWordParser();

    /**
     * 解析
     */
    public List<MdSection> parser(String src) {
        List<MdSection> sectionList = sectionParser.parser(src);

        if (sectionList == null || sectionList.isEmpty()) {
            return null;
        }

        for (MdSection section : sectionList) {
            MdType type = section.getType();
            if (type != MdType.NORMAL) {
                continue;
            }
            section.setWordList(wordParser.parser(section.getSrc()));
        }

        Log.d(TAG, "parser: " + sectionList);

        return sectionList;
    }

}
