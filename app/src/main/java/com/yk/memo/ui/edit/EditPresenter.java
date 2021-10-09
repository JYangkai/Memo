package com.yk.memo.ui.edit;

import android.util.Log;

import com.yk.base.mvp.BaseMvpPresenter;
import com.yk.base.rxjava.Observable;
import com.yk.base.rxjava.Subscriber;
import com.yk.memo.data.db.NoteDbManager;

public class EditPresenter extends BaseMvpPresenter<IEditView> {
    private static final String TAG = "EditPresenter";

    public void saveNote(String content) {
        Observable.fromCallable(new Observable.OnCallable<Boolean>() {
            @Override
            public Boolean call() {
                Log.d(TAG, "call: saveNote");
                return NoteDbManager.addNote(content);
            }
        })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean success) {
                        Log.d(TAG, "onNext: saveNote");
                        if (getView() != null) {
                            getView().onSaveNote(success);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: saveNote");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: saveNote ", e);
                    }
                });
    }

}
