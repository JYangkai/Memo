package com.yk.markdown2.core.parser.word;

import com.yk.markdown2.bean.MdType;

public class MdImageParser extends BaseMdWordParser {
    @Override
    public MdType getType() {
        return MdType.IMAGE;
    }

    @Override
    public String getRegex() {
        return "!\\[.*\\]\\(.*\\)";
    }
}
