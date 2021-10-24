package com.yk.memo.markdown.core.parser.section;

import com.yk.memo.markdown.bean.MdSection;
import com.yk.memo.markdown.bean.MdType;

public class MdNormalParser extends BaseMdSectionParser {
    @Override
    public MdType getType() {
        return MdType.NORMAL;
    }

    @Override
    public boolean matcher(String src) {
        return true;
    }

    @Override
    public boolean needAppendLastSection(MdSection lastSection, boolean codeBlockClose) {
        MdType type = lastSection.getType();

        if (type == MdType.TITLE) {
            return false;
        }

        if (type == MdType.SEPARATOR) {
            return false;
        }

        String lastSrc = lastSection.getSrc().split("\n")[0];

        return !lastSrc.isEmpty();
    }
}
