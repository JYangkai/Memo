package com.yk.memo.markdown.core.parser.section;

import com.yk.memo.markdown.bean.MdSection;
import com.yk.memo.markdown.bean.MdType;

public class MdUnorderedListParser extends BaseMdSectionParser {
    @Override
    public MdType getType() {
        return MdType.UNORDERED_LIST;
    }

    @Override
    public boolean matcher(String src) {
        return src.matches("[+*-]\\s.*");
    }

    @Override
    public boolean needAppendLastSection(MdSection lastSection, boolean codeBlockClose) {
        return !codeBlockClose;
    }
}
