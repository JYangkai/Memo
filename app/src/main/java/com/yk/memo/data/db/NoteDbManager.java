package com.yk.memo.data.db;

import com.yk.memo.data.bean.Note;

import org.litepal.LitePal;

import java.util.List;

public class NoteDbManager {
    public static Note addNote(String src) {
        Note note = new Note(src, System.currentTimeMillis(), System.currentTimeMillis());
        boolean success = note.save();
        return success ? note : null;
    }

    public static boolean addNote(Note note) {
        return note.save();
    }

    public static void deleteNote(long id) {
        LitePal.delete(Note.class, id);
    }

    public static void deleteNote(Note note) {
        deleteNote(note.getId());
    }

    public static boolean updateNote(long id, String src) {
        return updateNote(getNote(id), src);
    }

    public static boolean updateNote(Note note, String src) {
        note.setSrc(src);
        note.setUpdateTime(System.currentTimeMillis());
        return note.save();
    }

    public static Note getNote(long id) {
        return LitePal.find(Note.class, id);
    }

    public static List<Note> getAllNote() {
        return LitePal.order("updateTime desc").find(Note.class);
    }
}
