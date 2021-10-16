package com.yk.memo.data.event;

import com.yk.memo.data.bean.Note;

public class NoteRemoveEvent {
    private Note note;

    public NoteRemoveEvent(Note note) {
        this.note = note;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "NoteRemoveEvent{" +
                "note=" + note +
                '}';
    }
}
