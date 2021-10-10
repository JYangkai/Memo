package com.yk.memo.data.event;

import com.yk.memo.data.bean.Note;

public class NoteAddEvent {
    private Note note;

    public NoteAddEvent(Note note) {
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
        return "NoteAddEvent{" +
                "note=" + note +
                '}';
    }
}
