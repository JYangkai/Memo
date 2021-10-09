package com.yk.memo.data.bean;

import android.text.SpannableStringBuilder;

import androidx.annotation.Nullable;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Note extends LitePalSupport implements Serializable {
    private long id;
    private String src;
    private long createTime;
    private long updateTime;

    @Column(ignore = true)
    private SpannableStringBuilder spanStrBuilder;

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

    public SpannableStringBuilder getSpanStrBuilder() {
        return spanStrBuilder;
    }

    public void setSpanStrBuilder(SpannableStringBuilder spanStrBuilder) {
        this.spanStrBuilder = spanStrBuilder;
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
                ", spanStrBuilder=" + spanStrBuilder +
                '}';
    }
}
