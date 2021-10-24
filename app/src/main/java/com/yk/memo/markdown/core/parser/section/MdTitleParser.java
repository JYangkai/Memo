package com.yk.memo.markdown.core.parser.section;

import com.yk.memo.markdown.bean.MdSection;
import com.yk.memo.markdown.bean.MdType;

public class MdTitleParser extends BaseMdSectionParser {
    @Override
    public MdType getType() {
        return MdType.TITLE;
    }

    @Override
    public boolean matcher(String src) {
        return src.matches("#+\\s.*");
    }

    @Override
    public boolean needAppendLastSection(MdSection lastSection, boolean codeBlockClose) {
        return !codeBlockClose;
    }
}
