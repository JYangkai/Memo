package com.yk.memo.markdown.core.parser.word;

import com.yk.memo.markdown.bean.MdType;

public class MdItalicsParser extends BaseMdWordParser {
    @Override
    public MdType getType() {
        return MdType.ITALICS;
    }

    @Override
    public String getRegex() {
        return "[*][^*]+[*]";
    }
}
