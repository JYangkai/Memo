package com.yk.memo.ui.main;

import com.yk.base.mvp.BaseMvpView;
import com.yk.memo.data.bean.Note;

import java.util.List;

public interface IMainView extends BaseMvpView {

    void onLoadNoteList(List<Note> noteList);

    void onDeleteNote(boolean success, Note note);

}
