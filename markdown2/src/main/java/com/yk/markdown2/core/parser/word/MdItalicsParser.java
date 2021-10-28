package com.yk.markdown2.core.parser.word;

import com.yk.markdown2.bean.MdType;

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
