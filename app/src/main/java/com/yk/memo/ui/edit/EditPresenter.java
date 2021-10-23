package com.yk.memo.ui.edit;

import android.content.Context;
import android.util.Log;

import com.yk.eventposter.EventPoster;
import com.yk.memo.data.bean.Note;
import com.yk.memo.data.db.NoteDbManager;
import com.yk.memo.data.event.NoteAddEvent;
import com.yk.memo.data.event.NoteRemoveEvent;
import com.yk.memo.data.event.NoteUpdateEvent;
import com.yk.memo.utils.FileUtils;
import com.yk.memo.utils.SpManager;
import com.yk.mvp.BaseMvpPresenter;
import com.yk.rxsample.Observable;
import com.yk.rxsample.Subscriber;

public class EditPresenter extends BaseMvpPresenter<IEditView> {
    private static final String TAG = "EditPresenter2";

    private Context context;

    public EditPresenter(Context context) {
        this.context = context;
    }

    /**
     * 新增note
     *
     * @param src 原文本
     */
    public void saveNote(String src) {
        Observable.fromCallable(new Observable.OnCallable<Note>() {
            @Override
            public Note call() {
                Log.d(TAG, "call: save note:" + src);
                return NoteDbManager.getInstance().addNote(src);
            }
        })
                .map(new Observable.Function1<Note, Note>() {
                    @Override
                    public Note call(Note note) {
                        EventPoster.getInstance().post(new NoteAddEvent(true, note));
                        return note;
                    }
                })
                .map(new Observable.Function1<Note, Note>() {
                    @Override
                    public Note call(Note note) {
                        if (SpManager.getInstance().getOutputMarkdown()) {
                            boolean success = FileUtils.outputNoteToMarkdownFolder(context, note);
                            Log.d(TAG, "call: save note output markdown:" + success);
                        }
                        return note;
                    }
                })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<Note>() {
                    @Override
                    public void onNext(Note note) {
                        Log.d(TAG, "onNext: save note:" + note);
                        if (getView() != null) {
                            getView().onSaveNote(note);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: save note");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: save note:", e);
                        if (getView() != null) {
                            getView().onSaveNoteError(e);
                        }
                    }
                });
    }

    /**
     * 更新note
     *
     * @param note note
     * @param src  更新的文本
     */
    public void updateNote(Note note, String src) {
        Observable.fromCallable(new Observable.OnCallable<Note>() {
            @Override
            public Note call() {
                Log.d(TAG, "call: update note:" + note);
                return NoteDbManager.getInstance().updateNote(note, src);
            }
        })
                .map(new Observable.Function1<Note, Note>() {
                    @Override
                    public Note call(Note note) {
                        EventPoster.getInstance().post(new NoteUpdateEvent(true, note));
                        return note;
                    }
                })
                .map(new Observable.Function1<Note, Note>() {
                    @Override
                    public Note call(Note note) {
                        if (SpManager.getInstance().getOutputMarkdown()) {
                            boolean success = FileUtils.outputNoteToMarkdownFolder(context, note);
                            Log.d(TAG, "call: update note output markdown:" + success);
                        }
                        return note;
                    }
                })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<Note>() {
                    @Override
                    public void onNext(Note note) {
                        Log.d(TAG, "onNext: update note:" + note);
                        if (getView() != null) {
                            getView().onUpdateNote(note);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: update note");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: update note:", e);
                        if (getView() != null) {
                            getView().onUpdateNoteError(e);
                        }
                    }
                });
    }

    /**
     * 删除note
     *
     * @param note 待删除的note
     */
    public void deleteNote(Note note) {
        Observable.fromCallable(new Observable.OnCallable<Note>() {
            @Override
            public Note call() {
                Log.d(TAG, "call: delete note:" + note);
                return NoteDbManager.getInstance().deleteNote(note);
            }
        })
                .map(new Observable.Function1<Note, Note>() {
                    @Override
                    public Note call(Note note) {
                        EventPoster.getInstance().post(new NoteRemoveEvent(note));
                        return note;
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
}
