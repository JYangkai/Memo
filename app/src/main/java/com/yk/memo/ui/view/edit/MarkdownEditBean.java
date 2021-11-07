package com.yk.memo.ui.view.edit;

import com.yk.markdown.bean.MdType;

public class MarkdownEditBean {
    private MdType type;
    private String name;

    public MarkdownEditBean(MdType type, String name) {
        this.type = type;
        this.name = name;
    }

    public MdType getType() {
        return type;
    }

    public void setType(MdType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MarkdownEditBean{" +
                "type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}
