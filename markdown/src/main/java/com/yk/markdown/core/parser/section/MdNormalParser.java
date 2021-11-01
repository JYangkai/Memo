package com.yk.markdown.core.parser.section;

import com.yk.markdown.bean.MdSection;
import com.yk.markdown.bean.MdType;

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

        String[] lastSrcSplit = lastSection.getSrc().split("\n");
        String lastSrc = lastSrcSplit[lastSrcSplit.length - 1];

        return !lastSrc.isEmpty();
    }
}
