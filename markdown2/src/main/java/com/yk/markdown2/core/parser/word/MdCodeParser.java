package com.yk.markdown2.core.parser.word;

import com.yk.markdown2.bean.MdType;

public class MdCodeParser extends BaseMdWordParser {
    @Override
    public MdType getType() {
        return MdType.CODE;
    }

    @Override
    public String getRegex() {
        return "[`][^`]+[`]";
    }
}
