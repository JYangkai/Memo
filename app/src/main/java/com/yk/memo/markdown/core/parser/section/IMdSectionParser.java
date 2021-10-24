package com.yk.memo.markdown.core.parser.section;

import com.yk.memo.markdown.bean.MdSection;

import java.util.List;

public interface IMdSectionParser {
    boolean parser(List<MdSection> sectionList, String src);
}
