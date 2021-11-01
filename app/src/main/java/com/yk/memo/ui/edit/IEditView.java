package com.yk.memo.ui.edit;

import android.net.Uri;

import com.yk.memo.data.bean.Note;
import com.yk.mvp.BaseMvpView;

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
     * 分享图片
     *
     * @param uri uri
     */
    void onShareShot(Uri uri);

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

    /**
     * 分享图片失败
     */
    void onShareShotError(Exception e);
}
