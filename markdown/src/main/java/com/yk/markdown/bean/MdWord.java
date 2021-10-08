package com.yk.markdown.bean;

public class MdWord {
    private MdType type;
    private String src;

    public MdWord(MdType type, String src) {
        this.type = type;
        this.src = src;
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

    @Override
    public String toString() {
        return "MdWord{" +
                "type=" + type +
                ", src='" + src + '\'' +
                '}';
    }
}
