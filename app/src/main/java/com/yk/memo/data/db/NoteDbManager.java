package com.yk.memo.data.db;

import com.yk.memo.data.bean.Note;

import org.litepal.LitePal;

import java.util.List;

public class NoteDbManager {
    private static volatile NoteDbManager instance;

    private NoteDbManager() {
    }

    public static NoteDbManager getInstance() {
        if (instance == null) {
            synchronized (NoteDbManager.class) {
                if (instance == null) {
                    instance = new NoteDbManager();
                }
            }
        }
        return instance;
    }

    /**
     * 新增Note
     *
     * @param src 源文本
     * @return note
     */
    public Note addNote(String src) {
        Note note = new Note(
                src,
                System.currentTimeMillis(),
                System.currentTimeMillis()
        );
        return note.save() ? note : null;
    }

    /**
     * 新增Note
     *
     * @param note 待新增的Note
     * @return note
     */
    public Note addNote(Note note) {
        return note.save() ? note : null;
    }

    /**
     * 删除note
     *
     * @param id id
     */
    public void deleteNote(long id) {
        LitePal.delete(Note.class, id);
    }

    /**
     * 删除note
     *
     * @param note note
     */
    public Note deleteNote(Note note) {
        deleteNote(note.getId());
        return note;
    }

    /**
     * 删除note list
     *
     * @param noteList note list
     * @return note list
     */
    public List<Note> deleteNoteList(List<Note> noteList) {
        for (Note note : noteList) {
            deleteNote(note);
        }
        return noteList;
    }

    /**
     * 更新note
     *
     * @param id  id
     * @param src 源文本
     * @return note
     */
    public Note updateNote(long id, String src) {
        return updateNote(getNote(id), src);
    }

    /**
     * 更新note
     *
     * @param note note
     * @param src  源文本
     * @return note
     */
    public Note updateNote(Note note, String src) {
        note.setSrc(src);
        note.setUpdateTime(System.currentTimeMillis());
        note.update(note.getId());
        return note;
    }

    /**
     * 获取note
     *
     * @param id id
     * @return note
     */
    public Note getNote(long id) {
        return LitePal.find(Note.class, id);
    }

    /**
     * 获取所有note
     *
     * @return note list
     */
    public List<Note> getAllNote() {
        return LitePal.order("updateTime desc").find(Note.class);
    }
}
