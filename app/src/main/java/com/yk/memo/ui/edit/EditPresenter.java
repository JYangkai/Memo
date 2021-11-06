package com.yk.memo.ui.edit;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.core.content.FileProvider;

import com.yk.db.bean.Note;
import com.yk.db.manager.NoteDbManager;
import com.yk.eventposter.EventPoster;
import com.yk.memo.data.event.NoteAddEvent;
import com.yk.memo.data.event.NoteRemoveEvent;
import com.yk.memo.data.event.NoteUpdateEvent;
import com.yk.memo.utils.NoteUtils;
import com.yk.memo.utils.SpManager;
import com.yk.memo.utils.TimeUtils;
import com.yk.memo.utils.ViewShotUtils;
import com.yk.mvp.BaseMvpPresenter;
import com.yk.rxsample.Observable;
import com.yk.rxsample.Subscriber;

import java.io.File;

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
                            boolean success = NoteUtils.outputNote(context, note);
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
                            boolean success = NoteUtils.outputNote(context, note);
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

    public void shareShot(View view) {
        Observable.fromCallable(new Observable.OnCallable<File>() {
            @Override
            public File call() {
                Log.d(TAG, "call: shareShot");
                String output = NoteUtils.getMarkdownShotFolder(context)
                        + TimeUtils.getTime(System.currentTimeMillis(), "yyyy-MM-dd_HH-mm-ss")
                        + ".jpeg";
                boolean success = ViewShotUtils.shotAndSave(view, output);
                return success ? new File(output) : null;
            }
        })
                .map(new Observable.Function1<File, Uri>() {
                    @Override
                    public Uri call(File file) {
                        if (file == null) {
                            return null;
                        }
                        return FileProvider.getUriForFile(context, "com.yk.memo.fileprovider", file);
                    }
                })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<Uri>() {
                    @Override
                    public void onNext(Uri uri) {
                        Log.d(TAG, "onNext: shareShot:" + uri);
                        if (uri == null) {
                            if (getView() != null) {
                                getView().onShareShotError(new RuntimeException("uri is null"));
                            }
                            return;
                        }
                        if (getView() != null) {
                            getView().onShareShot(uri);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: shareShot");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: shareShot:", e);
                        if (getView() != null) {
                            getView().onShareShotError(e);
                        }
                    }
                });
    }
}
