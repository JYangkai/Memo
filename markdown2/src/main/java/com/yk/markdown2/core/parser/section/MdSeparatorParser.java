package com.yk.markdown2.core.parser.section;

import com.yk.markdown2.bean.MdSection;
import com.yk.markdown2.bean.MdType;

public class MdSeparatorParser extends BaseMdSectionParser {
    @Override
    public MdType getType() {
        return MdType.SEPARATOR;
    }

    @Override
    public boolean matcher(String src) {
        return src.matches("[+*-]{3,}");
    }

    @Override
    public boolean needAppendLastSection(MdSection lastSection, boolean codeBlockClose) {
        return !codeBlockClose;
    }
}
