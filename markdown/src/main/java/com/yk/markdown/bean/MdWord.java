package com.yk.markdown.bean;

public class MdWord {
    private MdType type;
    private String src;
    private int start;
    private int end;

    public MdWord(MdType type, String src, int start, int end) {
        this.type = type;
        this.src = src;
        this.start = start;
        this.end = end;
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

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "MdWord{" +
                "type=" + type +
                ", src='" + src + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
