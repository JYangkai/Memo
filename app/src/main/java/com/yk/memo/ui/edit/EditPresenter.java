package com.yk.memo.ui.edit;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.yk.db.bean.Note;
import com.yk.db.manager.NoteDbManager;
import com.yk.eventposter.EventPoster;
import com.yk.memo.data.event.NoteAddEvent;
import com.yk.memo.data.event.NoteUpdateEvent;
import com.yk.memo.utils.NoteUtils;
import com.yk.mvp.BaseMvpPresenter;
import com.yk.rxsample.Observable;
import com.yk.rxsample.Subscriber;

public class EditPresenter extends BaseMvpPresenter<IEditView> {

    private final Context context;

    private ClipboardManager clipboardManager;

    public EditPresenter(Context context) {
        this.context = context;
    }

    public void copyToClipBoard(String src) {
        Observable.fromCallable(new Observable.OnCallable<Boolean>() {
            @Override
            public Boolean call() {
                if (clipboardManager == null) {
                    clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                }
                if (clipboardManager == null) {
                    return false;
                }
                ClipData clipData = ClipData.newPlainText("note_src", src);
                clipboardManager.setPrimaryClip(clipData);

                return true;
            }
        })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean success) {
                        if (getView() != null) {
                            getView().onCopyToClipBoard(success);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Exception e) {
                        if (getView() != null) {
                            getView().onCopyToClipBoard(false);
                        }
                    }
                });
    }

    public void saveNote(String src) {
        Observable.fromCallable(new Observable.OnCallable<Note>() {
            @Override
            public Note call() {
                return NoteDbManager.getInstance().addNote(src);
            }
        })
                .map(new Observable.Function1<Note, Boolean>() {
                    @Override
                    public Boolean call(Note note) {
                        if (note != null) {
                            NoteUtils.outputNote(context, note);
                            EventPoster.getInstance().post(new NoteAddEvent(true, note));
                        }
                        return note != null;
                    }
                })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean success) {
                        if (getView() != null) {
                            getView().onSaveNote(success);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Exception e) {
                        if (getView() != null) {
                            getView().onSaveNote(false);
                        }
                    }
                });
    }

    public void updateNote(Note note, String src) {
        Observable.fromCallable(new Observable.OnCallable<Note>() {
            @Override
            public Note call() {
                return NoteDbManager.getInstance().updateNote(note, src);
            }
        })
                .map(new Observable.Function1<Note, Boolean>() {
                    @Override
                    public Boolean call(Note note) {
                        if (note != null) {
                            NoteUtils.outputNote(context, note);
                            EventPoster.getInstance().post(new NoteUpdateEvent(true, note));
                        }
                        return note != null;
                    }
                })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean success) {
                        if (getView() != null) {
                            getView().onUpdateNote(success);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Exception e) {
                        if (getView() != null) {
                            getView().onUpdateNote(false);
                        }
                    }
                });
    }
}
