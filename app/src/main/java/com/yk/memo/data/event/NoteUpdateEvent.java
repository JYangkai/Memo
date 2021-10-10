package com.yk.memo.data.event;

import android.text.SpannableStringBuilder;

public class NoteUpdateEvent {
    private long noteId;
    private String src;
    private long updateTime;
    private SpannableStringBuilder spanStrBuilder;

    public NoteUpdateEvent(long noteId, String src, long updateTime, SpannableStringBuilder spanStrBuilder) {
        this.noteId = noteId;
        this.src = src;
        this.updateTime = updateTime;
        this.spanStrBuilder = spanStrBuilder;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
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
    public String toString() {
        return "NoteUpdateEvent{" +
                "noteId=" + noteId +
                ", src='" + src + '\'' +
                ", updateTime=" + updateTime +
                ", spanStrBuilder=" + spanStrBuilder +
                '}';
    }
}
