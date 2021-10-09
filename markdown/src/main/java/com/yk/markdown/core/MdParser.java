package com.yk.markdown.core;

import android.text.TextUtils;

import com.yk.markdown.bean.MdSection;
import com.yk.markdown.bean.MdType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Markdown解析器
 */
public class MdParser {

    public static List<MdSection> dealMarkdown(String mdSrc) {
        if (TextUtils.isEmpty(mdSrc)) {
            return null;
        }

        String[] mdSrcSplit = mdSrc.trim().split("\n");

        if (mdSrcSplit.length == 0) {
            return null;
        }

        List<MdSection> sectionList = new ArrayList<>();

        for (String src : mdSrcSplit) {
            int codeBlockCount = getCodeBlockCount(sectionList);

            MdType type = getMdType(src);

            if (codeBlockCount % 2 != 0) {
                // 代码块未关闭
                MdSection lastSection = sectionList.get(sectionList.size() - 1);
                lastSection.setSrc(lastSection.getSrc() + "\n" + src);
            } else {
                // 代码块不存在，或已关闭
                if (type == MdType.NORMAL && sectionList.size() > 0) {
                    MdSection lastSection = sectionList.get(sectionList.size() - 1);
                    if (lastSection.getType() == MdType.TITLE || lastSection.getType() == MdType.SEPARATOR) {
                        sectionList.add(new MdSection(type, src, new ArrayList<>()));
                    } else {
                        lastSection.setSrc(lastSection.getSrc() + "\n" + src);
                    }
                } else {
                    sectionList.add(new MdSection(type, src, new ArrayList<>()));
                }
            }
        }

        return sectionList;
    }

    /**
     * 获取```开头的行数
     */
    private static int getCodeBlockCount(List<MdSection> sectionList) {
        int count = 0;

        if (sectionList == null || sectionList.isEmpty()) {
            return count;
        }

        for (MdSection section : sectionList) {
            String[] srcSplit = section.getSrc().split("\n");

            if (srcSplit.length == 0) {
                continue;
            }

            for (String src : srcSplit) {
                if (!Pattern.matches("`{3}.*", src)) {
                    continue;
                }
                count++;
            }
        }

        return count;
    }

    /**
     * 获取Markdown语法类型
     */
    private static MdType getMdType(String src) {
        MdType type = MdType.NORMAL;

        if (src.matches(">\\s.*")) {
            type = MdType.QUOTE;
        } else if (src.matches("`{3}.*")) {
            type = MdType.CODE_BLOCK;
        } else if (src.matches("\\d+\\.\\s.*")) {
            type = MdType.ORDERED_LIST;
        } else if (src.matches("[+*-]\\s.*")) {
            type = MdType.UNORDERED_LIST;
        } else if (src.matches("#+\\s.*")) {
            type = MdType.TITLE;
        } else if (src.matches("[+*-]{3,}.*")) {
            type = MdType.SEPARATOR;
        }
        return type;
    }
}
