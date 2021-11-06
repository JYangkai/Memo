package com.yk.memo.data.event;

import com.yk.db.bean.Note;

public class NoteUpdateEvent {
    private boolean needTop;
    private Note note;

    public NoteUpdateEvent(boolean needTop, Note note) {
        this.needTop = needTop;
        this.note = note;
    }

    public boolean isNeedTop() {
        return needTop;
    }

    public void setNeedTop(boolean needTop) {
        this.needTop = needTop;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "NoteUpdateEvent{" +
                "needTop=" + needTop +
                ", note=" + note +
                '}';
    }
}
