package com.yk.memo.ui.edit;

import com.yk.mvp.BaseMvpView;

public interface IEditView extends BaseMvpView {
    void onCopyToClipBoard(boolean success);

    void onSaveNote(boolean success);

    void onUpdateNote(boolean success);
}
