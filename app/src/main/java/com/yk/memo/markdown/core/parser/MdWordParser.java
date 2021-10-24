package com.yk.memo.markdown.core.parser;

import android.text.TextUtils;

import com.yk.memo.markdown.bean.MdType;
import com.yk.memo.markdown.bean.MdWord;
import com.yk.memo.markdown.core.parser.word.IMdWordParser;
import com.yk.memo.markdown.core.parser.word.MdBoldItalicsParser;
import com.yk.memo.markdown.core.parser.word.MdBoldParser;
import com.yk.memo.markdown.core.parser.word.MdCodeParser;
import com.yk.memo.markdown.core.parser.word.MdImageParser;
import com.yk.memo.markdown.core.parser.word.MdItalicsParser;

import java.util.ArrayList;
import java.util.List;

public class MdWordParser {
    private final IMdWordParser codeParser = new MdCodeParser();
    private final IMdWordParser boldItalicsParser = new MdBoldItalicsParser();
    private final IMdWordParser boldParser = new MdBoldParser();
    private final IMdWordParser italicsParser = new MdItalicsParser();
    private final IMdWordParser imageParser = new MdImageParser();

    public List<MdWord> dealWord(String src) {
        if (TextUtils.isEmpty(src)) {
            return null;
        }

        List<MdWord> wordList = new ArrayList<>();
        dealCode(wordList, src, 0);

        return wordList;
    }

    private void dealCode(List<MdWord> wordList, String src, int offset) {
        List<MdWord> codeList = codeParser.parser(src, offset);

        for (MdWord word : codeList) {
            MdType type = word.getType();
            if (type == MdType.NORMAL) {
                dealBoldItalics(wordList, word.getSrc(), word.getStart());
            } else {
                wordList.add(word);
            }
        }
    }

    private void dealBoldItalics(List<MdWord> wordList, String src, int offset) {
        List<MdWord> boldItalicsList = boldItalicsParser.parser(src, offset);

        for (MdWord word : boldItalicsList) {
            MdType type = word.getType();
            if (type == MdType.NORMAL) {
                dealBold(wordList, word.getSrc(), word.getStart());
            } else {
                wordList.add(word);
            }
        }
    }

    private void dealBold(List<MdWord> wordList, String src, int offset) {
        List<MdWord> boldList = boldParser.parser(src, offset);

        for (MdWord word : boldList) {
            MdType type = word.getType();
            if (type == MdType.NORMAL) {
                dealItalics(wordList, word.getSrc(), word.getStart());
            } else {
                wordList.add(word);
            }
        }
    }

    private void dealItalics(List<MdWord> wordList, String src, int offset) {
        List<MdWord> italicsList = italicsParser.parser(src, offset);

        for (MdWord word : italicsList) {
            MdType type = word.getType();
            if (type == MdType.NORMAL) {
                dealImage(wordList, word.getSrc(), word.getStart());
            } else {
                wordList.add(word);
            }
        }
    }

    private void dealImage(List<MdWord> wordList, String src, int offset) {
        List<MdWord> imageList = imageParser.parser(src, offset);

        wordList.addAll(imageList);
    }
}
