package com.yk.memo.ui.main;

import android.util.Log;

import com.yk.base.mvp.BaseMvpPresenter;
import com.yk.base.rxSimple.Observable;
import com.yk.base.rxSimple.Subscriber;
import com.yk.memo.data.bean.Note;
import com.yk.memo.data.db.NoteDbManager;

import java.util.List;

public class MainPresenter extends BaseMvpPresenter<IMainView> {
    private static final String TAG = "MainPresenter2";

    /**
     * 加载全部note
     */
    public void loadAllNote() {
        Observable.fromCallable(new Observable.OnCallable<List<Note>>() {
            @Override
            public List<Note> call() {
                Log.d(TAG, "call: load all note");
                return NoteDbManager.getInstance().getAllNote();
            }
        })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<List<Note>>() {
                    @Override
                    public void onNext(List<Note> noteList) {
                        Log.d(TAG, "onNext: load all note:" + noteList.size());
                        if (getView() != null) {
                            getView().onLoadNoteList(noteList);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: load all note");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: load all note:", e);
                        if (getView() != null) {
                            getView().onLoadNoteListError(e);
                        }
                    }
                });
    }

    /**
     * 删除 note
     *
     * @param note note
     */
    public void deleteNote(Note note) {
        Observable.fromCallable(new Observable.OnCallable<Note>() {
            @Override
            public Note call() {
                Log.d(TAG, "call: delete note:" + note);
                return NoteDbManager.getInstance().deleteNote(note);
            }
        })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<Note>() {
                    @Override
                    public void onNext(Note note) {
                        Log.d(TAG, "onNext: delete note:" + note);
                        if (getView() != null) {
                            getView().onDeleteNote(note);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: delete note");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: delete note:", e);
                        if (getView() != null) {
                            getView().onDeleteNoteError(e);
                        }
                    }
                });
    }

    /**
     * 删除note list
     *
     * @param noteList note list
     */
    public void deleteNoteList(List<Note> noteList) {
        Observable.fromCallable(new Observable.OnCallable<List<Note>>() {
            @Override
            public List<Note> call() {
                Log.d(TAG, "call: delete note list:" + noteList.size());
                return NoteDbManager.getInstance().deleteNoteList(noteList);
            }
        })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<List<Note>>() {
                    @Override
                    public void onNext(List<Note> noteList) {
                        Log.d(TAG, "onNext: delete note list:" + noteList.size());
                        if (getView() != null) {
                            getView().onDeleteNoteList(noteList);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: delete note list");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: delete note list:", e);
                        if (getView() != null) {
                            getView().onDeleteNoteListError(e);
                        }
                    }
                });
    }

}
