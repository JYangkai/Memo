package com.yk.memo.data.bean;

import androidx.annotation.Nullable;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Note extends LitePalSupport implements Serializable {
    /**
     * 标识id（主键）
     */
    private long id;

    /**
     * 原文本
     */
    private String src;

    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 更新时间
     */
    private long updateTime;

    /**
     * 是否选择（数据库忽略字段）
     */
    @Column(ignore = true)
    private boolean isSelect = false;

    public Note() {
    }

    public Note(String src, long createTime, long updateTime) {
        this.src = src;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public long getId() {
        return id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Note) {
            Note note = (Note) obj;
            return note.id == this.id;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", src='" + src + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isSelect=" + isSelect +
                '}';
    }
}
