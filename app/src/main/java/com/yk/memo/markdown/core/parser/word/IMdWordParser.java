package com.yk.memo.markdown.core.parser.word;

import com.yk.memo.markdown.bean.MdWord;

import java.util.List;

public interface IMdWordParser {
    List<MdWord> parser(String src, int offset);
}
