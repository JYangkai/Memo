package com.yk.memo.markdown.core.parser.word;

import com.yk.memo.markdown.bean.MdType;

public class MdBoldItalicsParser extends BaseMdWordParser {
    @Override
    public MdType getType() {
        return MdType.BOLD_ITALICS;
    }

    @Override
    public String getRegex() {
        return "[*]{3}[^*]+[*]{3}";
    }
}
