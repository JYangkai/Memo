package com.yk.markdown.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;

public class MarkdownEditUtils {
    public static void insertQuote(EditText et) {
        String curContent = getContent(et);
        int curSelection = getSelection(et);

        if (TextUtils.isEmpty(curContent)) {
            // 如果当前文本为空，则直接插入
            curContent = "> ";
            curSelection = curContent.length();
        } else {
            // 文本不为空，则需要判断光标位置
            int beforeLineFeed = getBeforeLineFeed(curContent, curSelection); // 上一个换行符
            int afterLineFeed = getAfterLineFeed(curContent, curSelection); // 下一个换行符

            // 如果当前行的头一个字符已标识，则直接返回
            char headChar = curContent.charAt(beforeLineFeed + 1);
            if (headChar == '>') {
                return;
            }

            if (beforeLineFeed != -1) {
                // 如果上一个换行符存在，则在当前行头部插入
                String before = curContent.substring(0, beforeLineFeed + 1);
                String after = curContent.substring(beforeLineFeed + 1);
                curContent = before + "> " + after;
            } else {
                // 如果上一个换行符不存在，则直接插入
                curContent = "> " + curContent;
            }

            // 将光标移动到当前行的末尾
            curSelection = afterLineFeed != -1 ? (afterLineFeed + "> ".length()) : curContent.length();
        }

        et.setText(curContent);
        et.setSelection(curSelection);
    }

    public static void insertCodeBlock(EditText et) {
        String curContent = getContent(et);
        int curSelection = getSelection(et);

        if (TextUtils.isEmpty(curContent)) {
            // 如果当前文本为空，则直接插入
            curContent = "```\n\n```";
            curSelection = curContent.length() - 4;
        } else {
            // 文本不为空，则需要判断光标位置
            int beforeLineFeed = getBeforeLineFeed(curContent, curSelection); // 上一个换行符
            int afterLineFeed = getAfterLineFeed(curContent, curSelection); // 下一个换行符

            String before = "";
            if (beforeLineFeed != -1) {
                before = curContent.substring(0, beforeLineFeed + 1);
            }

            String after = "";
            if (afterLineFeed != -1) {
                after = curContent.substring(afterLineFeed);
            }

            String curLine = curContent.substring(beforeLineFeed + 1, afterLineFeed != -1 ? afterLineFeed : curContent.length());

            curContent = before + "```\n" + curLine + "\n```" + after;

            curSelection = before.length() + "```\n".length() + curLine.length();
        }

        et.setText(curContent);
        et.setSelection(curSelection);
    }

    public static void insertOrderedList(EditText et) {
        String curContent = getContent(et);
        int curSelection = getSelection(et);

        if (TextUtils.isEmpty(curContent)) {
            // 如果当前文本为空，则直接插入
            curContent = "1. ";
            curSelection = curContent.length();
        } else {
            // 文本不为空，则需要判断光标位置

            int beforeLineFeed = getBeforeLineFeed(curContent, curSelection); // 上一个换行符
            int afterLineFeed = getAfterLineFeed(curContent, curSelection); // 下一个换行符

            // 如果当前行的头一个字符已标识，则直接返回
            String headStr = curContent.substring(beforeLineFeed + 1, beforeLineFeed + 2);
            if (headStr.matches("\\d")) {
                return;
            }

            String numStr;
            if (beforeLineFeed != -1) {
                // 如果上一个换行符存在，则在当前行头部插入
                String before = curContent.substring(0, beforeLineFeed + 1);
                String after = curContent.substring(beforeLineFeed + 1);
                numStr = (getBeforeNum(before) + 1) + ". ";
                curContent = before + numStr + after;
            } else {
                // 如果上一个换行符不存在，则直接插入
                numStr = "1. ";
                curContent = numStr + curContent;
            }

            // 将光标移动到当前行的末尾
            curSelection = afterLineFeed != -1 ? (afterLineFeed + numStr.length()) : curContent.length();
        }

        et.setText(curContent);
        et.setSelection(curSelection);
    }

    private static String getContent(EditText et) {
        Editable editable = et.getText();

        if (editable == null) {
            return "";
        }

        return editable.toString();
    }

    private static int getSelection(EditText et) {
        return et.getSelectionStart();
    }

    private static int getBeforeLineFeed(String content, int selection) {
        String before = content.substring(0, selection);
        return before.lastIndexOf("\n");
    }

    private static int getAfterLineFeed(String content, int selection) {
        String before = content.substring(selection);
        int index = before.indexOf("\n");
        return index != -1 ? (index + selection) : -1;
    }

    private static int getBeforeNum(String content) {
        if (TextUtils.isEmpty(content)) {
            return 0;
        }

        String[] split = content.split("\n");
        if (split == null) {
            return 0;
        }
        if (split.length == 0) {
            return 0;
        }

        String num = split[split.length - 1].substring(0, 1);
        if (!num.matches("\\d")) {
            return 0;
        }

        return Integer.parseInt(num);
    }
}
