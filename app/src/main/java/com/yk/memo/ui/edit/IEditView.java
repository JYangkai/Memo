package com.yk.memo.ui.edit;

import com.yk.base.mvp.BaseMvpView;
import com.yk.memo.data.bean.Note;

public interface IEditView extends BaseMvpView {

    void onLoadNote(Note note);

    void onSaveNote(boolean success);

}
