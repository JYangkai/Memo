package com.yk.memo.ui.edit;

import android.util.Log;

import com.yk.base.mvp.BaseMvpPresenter;
import com.yk.base.rxjava.Observable;
import com.yk.base.rxjava.Subscriber;
import com.yk.memo.data.bean.Note;
import com.yk.memo.data.db.NoteDbManager;

public class EditPresenter extends BaseMvpPresenter<IEditView> {
    private static final String TAG = "EditPresenter";

    public void loadNote(long noteId) {
        Observable.fromCallable(new Observable.OnCallable<Note>() {
            @Override
            public Note call() {
                Log.d(TAG, "call: load note:" + noteId);
                return NoteDbManager.getNote(noteId);
            }
        })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<Note>() {
                    @Override
                    public void onNext(Note note) {
                        Log.d(TAG, "onNext: load note:" + note);
                        if (getView() != null) {
                            getView().onLoadNote(note);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: load note");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: load note ", e);
                    }
                });
    }

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

    public void updateNote(long noteId, String content) {
        Observable.fromCallable(new Observable.OnCallable<Boolean>() {
            @Override
            public Boolean call() {
                Log.d(TAG, "call: update note");
                return NoteDbManager.updateNote(noteId, content);
            }
        })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean success) {
                        Log.d(TAG, "onNext: update note");
                        if (getView() != null) {
                            getView().onSaveNote(success);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: update note");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: update note ", e);
                    }
                });
    }

}
