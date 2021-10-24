package com.yk.memo.markdown.core.parser.word;

import com.yk.memo.markdown.bean.MdType;

public class MdBoldParser extends BaseMdWordParser {
    @Override
    public MdType getType() {
        return MdType.BOLD;
    }

    @Override
    public String getRegex() {
        return "[*]{2}[^*]+[*]{2}";
    }
}
