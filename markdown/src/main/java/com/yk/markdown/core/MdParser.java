package com.yk.markdown.core;

import android.text.TextUtils;
import android.util.Log;

import com.yk.markdown.bean.MdSection;
import com.yk.markdown.bean.MdType;
import com.yk.markdown.bean.MdWord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Markdown解析器
 */
public class MdParser {
    private static final String TAG = "MdParser";

    public static List<MdSection> dealMarkdown(String mdSrc) {
        if (TextUtils.isEmpty(mdSrc)) {
            return null;
        }

        List<MdSection> sectionList = new ArrayList<>();

        // 处理段落
        dealSection(mdSrc, sectionList);

        // 处理普通字符
        dealNormal(sectionList);

        for (MdSection section : sectionList) {
            Log.d(TAG, "dealMarkdown: " + section);
        }

        return sectionList;
    }

    private static void dealSection(String mdSrc, List<MdSection> sectionList) {
        String[] mdSrcSplit = mdSrc.split("\n");

        if (mdSrcSplit.length == 0) {
            return;
        }

        for (int i = 0; i < mdSrcSplit.length; i++) {
            String src = mdSrcSplit[i];

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
                    if (lastSection.getType() == MdType.TITLE
                            || lastSection.getType() == MdType.SEPARATOR
                            || mdSrcSplit[i - 1].isEmpty()) {
                        sectionList.add(new MdSection(type, src, new ArrayList<>()));
                    } else {
                        lastSection.setSrc(lastSection.getSrc() + "\n" + src);
                    }
                } else {
                    sectionList.add(new MdSection(type, src, new ArrayList<>()));
                }
            }
        }
    }

    /**
     * 处理段落中被标记为普通的部分
     */
    private static void dealNormal(List<MdSection> sectionList) {
        if (sectionList.isEmpty()) {
            return;
        }

        for (MdSection section : sectionList) {
            if (section.getType() != MdType.NORMAL) {
                continue;
            }
            dealWord(section);
        }
    }

    /**
     * 处理单行
     */
    private static void dealWord(MdSection section) {
        String src = section.getSrc();

        List<MdWord> wordList = dealCode(src, 0);

        section.setWordList(wordList);
    }

    /**
     * 处理代码
     */
    private static List<MdWord> dealCode(String src, int offset) {
        List<MdWord> wordList = new ArrayList<>();

        if (TextUtils.isEmpty(src)) {
            return wordList;
        }

        List<MdWord> codeList = new ArrayList<>();

        Pattern pattern = Pattern.compile("[`][^`]+[`]");
        Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String group = matcher.group();

            if (codeList.isEmpty()) {
                // 如果之前未发现code
                if (start != 0) {
                    // 此次发现code，但起始位置不是0，则需要将前面的字段解析
                    addBoldItalicsToList(wordList, src.substring(0, start), 0);
                }
            } else {
                // 如果之前有发现code，则将上一个code到此code之间的字段解析
                MdWord lastCode = codeList.get(codeList.size() - 1);
                int lastEnd = lastCode.getEnd();
                addBoldItalicsToList(wordList, src.substring(lastEnd, start), lastEnd);
            }

            // 将此次发现的code加入列表
            codeList.add(new MdWord(MdType.CODE, group, start, end));
        }

        if (!codeList.isEmpty()) {
            // 如果经过上面的匹配搜索后，有发现code
            MdWord lastCode = codeList.get(codeList.size() - 1);
            int lastEnd = lastCode.getEnd();
            if (lastEnd != src.length()) {
                // 判断最后一个code的结束位置不是src的长度，则解析后面的字段
                addBoldItalicsToList(wordList, src.substring(lastEnd), lastEnd);
            }
        } else {
            // 如果一个找不到，则直接解析整段src
            addBoldItalicsToList(wordList, src, 0);
        }

        // 将code加入列表
        wordList.addAll(codeList);

        // 计算偏移位置
        for (MdWord word : wordList) {
            word.setStart(word.getStart() + offset);
            word.setEnd(word.getEnd() + offset);
        }

        // 进行一次排序
        Collections.sort(wordList, new Comparator<MdWord>() {
            @Override
            public int compare(MdWord o1, MdWord o2) {
                return o1.getStart() - o2.getStart();
            }
        });

        return wordList;
    }

    /**
     * 将粗斜体解析的数据加入列表
     */
    private static void addBoldItalicsToList(List<MdWord> wordList, String src, int offset) {
        List<MdWord> otherWordList = dealBoldItalics(src, offset);
        wordList.addAll(otherWordList);
    }

    /**
     * 处理粗斜体
     */
    private static List<MdWord> dealBoldItalics(String src, int offset) {
        List<MdWord> wordList = new ArrayList<>();

        if (TextUtils.isEmpty(src)) {
            return wordList;
        }

        List<MdWord> boldItalicsList = new ArrayList<>();

        Pattern pattern = Pattern.compile("[*]{3}[^*]+[*]{3}");
        Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String group = matcher.group();

            if (boldItalicsList.isEmpty()) {
                // 如果之前未发现boldItalics
                if (start != 0) {
                    // 此次发现boldItalics，但起始位置不是0，则需要将前面的字段解析
                    addBoldToList(wordList, src.substring(0, start), 0);
                }
            } else {
                // 如果之前有发现boldItalics，则将上一个boldItalics到此boldItalics之间的字段解析
                MdWord lastCode = boldItalicsList.get(boldItalicsList.size() - 1);
                int lastEnd = lastCode.getEnd();
                addBoldToList(wordList, src.substring(lastEnd, start), lastEnd);
            }

            // 将此次发现的boldItalics加入列表
            boldItalicsList.add(new MdWord(MdType.BOLD_ITALICS, group, start, end));
        }

        if (!boldItalicsList.isEmpty()) {
            // 如果经过上面的匹配搜索后，有发现boldItalics
            MdWord lastCode = boldItalicsList.get(boldItalicsList.size() - 1);
            int lastEnd = lastCode.getEnd();
            if (lastEnd != src.length()) {
                // 判断最后一个boldItalics的结束位置不是src的长度，则解析后面的字段
                addBoldToList(wordList, src.substring(lastEnd), lastEnd);
            }
        } else {
            // 如果一个找不到，则直接解析整段src
            addBoldToList(wordList, src, 0);
        }

        // 将boldItalics加入列表
        wordList.addAll(boldItalicsList);

        // 计算偏移位置
        for (MdWord word : wordList) {
            word.setStart(word.getStart() + offset);
            word.setEnd(word.getEnd() + offset);
        }

        // 进行一次排序
        Collections.sort(wordList, new Comparator<MdWord>() {
            @Override
            public int compare(MdWord o1, MdWord o2) {
                return o1.getStart() - o2.getStart();
            }
        });

        return wordList;
    }

    /**
     * 将粗体解析的数据加入列表
     */
    private static void addBoldToList(List<MdWord> wordList, String src, int offset) {
        List<MdWord> otherWordList = dealBold(src, offset);
        wordList.addAll(otherWordList);
    }

    /**
     * 处理粗体
     */
    private static List<MdWord> dealBold(String src, int offset) {
        List<MdWord> wordList = new ArrayList<>();

        if (TextUtils.isEmpty(src)) {
            return wordList;
        }

        List<MdWord> boldList = new ArrayList<>();

        Pattern pattern = Pattern.compile("[*]{2}[^*]+[*]{2}");
        Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String group = matcher.group();

            if (boldList.isEmpty()) {
                // 如果之前未发现bold
                if (start != 0) {
                    // 此次发现bold，但起始位置不是0，则需要将前面的字段解析
                    addItalicsToList(wordList, src.substring(0, start), 0);
                }
            } else {
                // 如果之前有发现bold，则将上一个bold到此bold之间的字段解析
                MdWord lastCode = boldList.get(boldList.size() - 1);
                int lastEnd = lastCode.getEnd();
                addItalicsToList(wordList, src.substring(lastEnd, start), lastEnd);
            }

            // 将此次发现的bold加入列表
            boldList.add(new MdWord(MdType.BOLD, group, start, end));
        }

        if (!boldList.isEmpty()) {
            // 如果经过上面的匹配搜索后，有发现bold
            MdWord lastCode = boldList.get(boldList.size() - 1);
            int lastEnd = lastCode.getEnd();
            if (lastEnd != src.length()) {
                // 判断最后一个bold的结束位置不是src的长度，则解析后面的字段
                addItalicsToList(wordList, src.substring(lastEnd), lastEnd);
            }
        } else {
            // 如果一个找不到，则直接解析整段src
            addItalicsToList(wordList, src, 0);
        }

        // 将bold加入列表
        wordList.addAll(boldList);

        // 计算偏移位置
        for (MdWord word : wordList) {
            word.setStart(word.getStart() + offset);
            word.setEnd(word.getEnd() + offset);
        }

        // 进行一次排序
        Collections.sort(wordList, new Comparator<MdWord>() {
            @Override
            public int compare(MdWord o1, MdWord o2) {
                return o1.getStart() - o2.getStart();
            }
        });

        return wordList;
    }

    /**
     * 将斜体解析的数据加入列表
     */
    private static void addItalicsToList(List<MdWord> wordList, String src, int offset) {
        List<MdWord> otherWordList = dealItalics(src, offset);
        wordList.addAll(otherWordList);
    }

    /**
     * 处理斜体
     */
    private static List<MdWord> dealItalics(String src, int offset) {
        List<MdWord> wordList = new ArrayList<>();

        if (TextUtils.isEmpty(src)) {
            return wordList;
        }

        List<MdWord> italicsList = new ArrayList<>();

        Pattern pattern = Pattern.compile("[*][^*]+[*]");
        Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String group = matcher.group();

            if (italicsList.isEmpty()) {
                // 如果之前未发现italics
                if (start != 0) {
                    // 此次发现italics，但起始位置不是0，则需要将前面的字段解析（直接是普通字符）
                    wordList.add(new MdWord(MdType.NORMAL, src.substring(0, start), 0, start));
                }
            } else {
                // 如果之前有发现italics，则将上一个italics到此italics之间的字段解析（直接是普通字符）
                MdWord lastCode = italicsList.get(italicsList.size() - 1);
                int lastEnd = lastCode.getEnd();
                wordList.add(new MdWord(MdType.NORMAL, src.substring(lastEnd, start), lastEnd, start));
            }

            // 将此次发现的italics加入列表
            italicsList.add(new MdWord(MdType.ITALICS, group, start, end));
        }

        if (!italicsList.isEmpty()) {
            // 如果经过上面的匹配搜索后，有发现italics
            MdWord lastCode = italicsList.get(italicsList.size() - 1);
            int lastEnd = lastCode.getEnd();
            if (lastEnd != src.length()) {
                // 判断最后一个italics的结束位置不是src的长度，则解析后面的字段（直接是普通字符）
                wordList.add(new MdWord(MdType.NORMAL, src.substring(lastEnd), lastEnd, src.length()));
            }
        } else {
            // 如果一个找不到，则直接解析整段src
            wordList.add(new MdWord(MdType.NORMAL, src, 0, src.length()));
        }

        // 将italics加入列表
        wordList.addAll(italicsList);

        // 计算偏移位置
        for (MdWord word : wordList) {
            word.setStart(word.getStart() + offset);
            word.setEnd(word.getEnd() + offset);
        }

        // 进行一次排序
        Collections.sort(wordList, new Comparator<MdWord>() {
            @Override
            public int compare(MdWord o1, MdWord o2) {
                return o1.getStart() - o2.getStart();
            }
        });

        return wordList;
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
        } else if (src.matches("[+*-]{3,}")) {
            type = MdType.SEPARATOR;
        }
        return type;
    }
}
