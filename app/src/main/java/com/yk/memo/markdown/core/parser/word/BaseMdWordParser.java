package com.yk.memo.markdown.core.parser.word;

import com.yk.memo.markdown.bean.MdType;
import com.yk.memo.markdown.bean.MdWord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseMdWordParser implements IMdWordParser {
    @Override
    public List<MdWord> parser(String src, int offset) {
        List<MdWord> wordList = new ArrayList<>();

        MdType type = getType();

        if (type == MdType.NORMAL) {
            wordList.add(new MdWord(type, src, offset, offset + src.length()));
            return wordList;
        }

        List<MdWord> curList = new ArrayList<>();

        Pattern pattern = Pattern.compile(getRegex());
        Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String group = matcher.group();

            if (curList.isEmpty()) {
                if (start != 0) {
                    wordList.add(new MdWord(MdType.NORMAL, src.substring(0, start), 0, start));
                }
            } else {
                MdWord lastWord = curList.get(curList.size() - 1);
                int lastEnd = lastWord.getEnd();
                wordList.add(new MdWord(MdType.NORMAL, src.substring(lastEnd, start), lastEnd, start));
            }

            curList.add(new MdWord(type, group, start, end));
        }

        if (!curList.isEmpty()) {
            MdWord lastWord = curList.get(curList.size() - 1);
            int lastEnd = lastWord.getEnd();
            if (lastEnd != src.length()) {
                wordList.add(new MdWord(MdType.NORMAL, src.substring(lastEnd), lastEnd, src.length()));
            }
        } else {
            wordList.add(new MdWord(MdType.NORMAL, src, 0, src.length()));
        }

        wordList.addAll(curList);

        for (MdWord word : wordList) {
            word.addOffset(offset);
        }

        Collections.sort(wordList, new Comparator<MdWord>() {
            @Override
            public int compare(MdWord o1, MdWord o2) {
                return o1.getStart() - o2.getStart();
            }
        });

        return wordList;
    }

    /**
     * 获取Md类型
     */
    public abstract MdType getType();

    /**
     * 获取匹配规则
     */
    public abstract String getRegex();
}
