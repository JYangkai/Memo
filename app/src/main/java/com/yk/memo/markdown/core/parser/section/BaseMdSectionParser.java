package com.yk.memo.markdown.core.parser.section;

import com.yk.memo.markdown.bean.MdSection;
import com.yk.memo.markdown.bean.MdType;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMdSectionParser implements IMdSectionParser {
    @Override
    public boolean parser(List<MdSection> sectionList, String src) {
        if (sectionList == null) {
            return false;
        }

        if (!matcher(src)) {
            return false;
        }

        MdType type = getType();

        boolean codeBlockClose = isCodeBlockClose(getCodeBlockCount(sectionList));

        MdSection lastSection = null;
        if (sectionList.size() > 0) {
            lastSection = sectionList.get(sectionList.size() - 1);
        }

        if (lastSection == null) {
            sectionList.add(new MdSection(type, src, new ArrayList<>()));
            return true;
        }

        if (needAppendLastSection(lastSection, codeBlockClose)) {
            lastSection.appendSrc("\n").appendSrc(src);
        } else {
            sectionList.add(new MdSection(type, src, new ArrayList<>()));
        }

        return true;
    }

    /**
     * 获取Md类型
     */
    public abstract MdType getType();

    /**
     * 匹配规则
     */
    public abstract boolean matcher(String src);

    /**
     * 需要追加到上一个section
     */
    public abstract boolean needAppendLastSection(MdSection lastSection, boolean codeBlockClose);

    /**
     * 代码块关闭
     */
    private static boolean isCodeBlockClose(int codeBlockCount) {
        return codeBlockCount % 2 == 0;
    }

    /**
     * 获取代码块标识数量
     */
    private static int getCodeBlockCount(List<MdSection> sectionList) {
        if (sectionList == null || sectionList.isEmpty()) {
            return 0;
        }

        int count = 0;

        for (MdSection section : sectionList) {
            String[] srcSplit = section.getSrc().split("\n");

            if (srcSplit.length == 0) {
                continue;
            }

            for (String src : srcSplit) {
                if (!src.matches("`{3}.*")) {
                    continue;
                }
                count++;
            }
        }

        return count;
    }
}
