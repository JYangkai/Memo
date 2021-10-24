package com.yk.memo.markdown.core;

import android.content.Context;
import android.text.SpannableStringBuilder;

import com.yk.memo.markdown.bean.MdSection;
import com.yk.memo.markdown.bean.MdType;
import com.yk.memo.markdown.bean.MdWord;
import com.yk.memo.markdown.core.render.MdSectionRender;
import com.yk.memo.markdown.core.render.MdWordRender;
import com.yk.memo.markdown.style.style.BaseMdStyle;

import java.util.List;

public class MdRender {
    private final MdSectionRender sectionRender = new MdSectionRender();
    private final MdWordRender wordRender = new MdWordRender();

    public SpannableStringBuilder render(Context context, BaseMdStyle style, List<MdSection> sectionList) {
        if (sectionList == null || sectionList.isEmpty()) {
            return null;
        }

        SpannableStringBuilder spanStrBuilder = new SpannableStringBuilder();

        for (int i = 0; i < sectionList.size(); i++) {
            MdSection section = sectionList.get(i);

            MdType type = section.getType();

            if (type == MdType.NORMAL) {
                spanStrBuilder.append(renderNormal(context, section, style));
            } else {
                spanStrBuilder.append(sectionRender.dealSection(context, section, style));
            }

            if (i != sectionList.size() - 1) {
                spanStrBuilder.append("\n");
            }
        }

        return spanStrBuilder;
    }

    private SpannableStringBuilder renderNormal(Context context, MdSection section, BaseMdStyle style) {
        List<MdWord> wordList = section.getWordList();

        if (wordList == null || wordList.isEmpty()) {
            return null;
        }

        SpannableStringBuilder spanStrBuilder = new SpannableStringBuilder();

        for (MdWord word : wordList) {
            MdType type = word.getType();

            if (type == MdType.IMAGE) {
                spanStrBuilder.append("\n");
            }

            spanStrBuilder.append(wordRender.dealWord(context, word, style));

            if (type == MdType.IMAGE) {
                spanStrBuilder.append("\n");
            }
        }

        return spanStrBuilder;
    }
}
