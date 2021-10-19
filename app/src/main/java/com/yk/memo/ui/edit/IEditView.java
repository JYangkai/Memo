package com.yk.memo.ui.edit;

import com.yk.mvp.BaseMvpView;
import com.yk.memo.data.bean.Note;

public interface IEditView extends BaseMvpView {
    /**
     * 新增note
     *
     * @param note note
     */
    void onSaveNote(Note note);

    /**
     * 更新note
     *
     * @param note note
     */
    void onUpdateNote(Note note);

    /**
     * 删除note
     *
     * @param note note
     */
    void onDeleteNote(Note note);

    /**
     * 新增note错误
     */
    void onSaveNoteError(Exception e);

    /**
     * 更新note错误
     */
    void onUpdateNoteError(Exception e);

    /**
     * 删除note错误
     */
    void onDeleteNoteError(Exception e);
}
