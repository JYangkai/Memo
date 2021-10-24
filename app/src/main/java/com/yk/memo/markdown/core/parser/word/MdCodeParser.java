package com.yk.memo.markdown.core.parser.word;

import com.yk.memo.markdown.bean.MdType;

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
