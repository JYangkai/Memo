package com.yk.memo.ui.main;

import com.yk.memo.data.bean.Note;
import com.yk.mvp.BaseMvpView;

import java.util.List;

public interface IMainView extends BaseMvpView {
    /**
     * 加载note list
     *
     * @param noteList note list
     */
    void onLoadNoteList(List<Note> noteList);

    /**
     * 删除note
     *
     * @param note note
     */
    void onDeleteNote(Note note);

    /**
     * 删除note list
     *
     * @param noteList note list
     */
    void onDeleteNoteList(List<Note> noteList);

    /**
     * 导出note
     *
     * @param note note
     */
    void onOutputNote(Note note);

    /**
     * 加载note list错误
     */
    void onLoadNoteListError(Exception e);

    /**
     * 删除note错误
     */
    void onDeleteNoteError(Exception e);

    /**
     * 删除note list错误
     */
    void onDeleteNoteListError(Exception e);

    /**
     * 导出note 错误
     */
    void onOutputNoteError(Exception e);
}
