package com.yk.markdown.bean;

import java.util.List;

public class MdSection {
    private MdType type;
    private String src;
    private List<MdWord> wordList;

    public MdSection(MdType type, String src, List<MdWord> wordList) {
        this.type = type;
        this.src = src;
        this.wordList = wordList;
    }

    public MdType getType() {
        return type;
    }

    public void setType(MdType type) {
        this.type = type;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public List<MdWord> getWordList() {
        return wordList;
    }

    public void setWordList(List<MdWord> wordList) {
        this.wordList = wordList;
    }

    @Override
    public String toString() {
        return "MdSection{" +
                "type=" + type +
                ", src='" + src + '\'' +
                ", wordList=" + wordList +
                '}';
    }
}
