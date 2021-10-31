package com.yk.memo.ui.main;

import android.net.Uri;

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
     * 分享note文件
     */
    void onShareNoteFile(Uri uri);

    /**
     * 打包分享
     *
     * @param uri uri
     */
    void onZipShare(Uri uri);

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

    /**
     * 分享note文件 错误
     */
    void onShareNoteFileError(Exception e);

    /**
     * 打包分享 错误
     */
    void onZipShareError(Exception e);
}
