package com.yk.memo.ui.main;

import android.net.Uri;

import com.yk.db.bean.Note;
import com.yk.mvp.BaseMvpView;

import java.util.List;

public interface IMainView extends BaseMvpView {

    void onLoadData(List<Note> noteList);

    void onShareFile(Uri uri);

    void onDeleteNote(Note note);

    void onDeleteNoteList(List<Note> noteList);

    void onShareZip(Uri uri);

    void onLoadDataError(Exception e);

    void onShareFileError(Exception e);

    void onDeleteNoteError(Exception e);

    void onDeleteNoteListError(Exception e);

    void onShareZipError(Exception e);

}
