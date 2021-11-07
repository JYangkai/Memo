package com.yk.memo.ui.preview;

import android.content.Context;
import android.net.Uri;
import android.widget.TextView;

import com.yk.db.bean.Note;
import com.yk.db.manager.NoteDbManager;
import com.yk.eventposter.EventPoster;
import com.yk.memo.data.event.NoteRemoveEvent;
import com.yk.memo.utils.NoteUtils;
import com.yk.memo.utils.ViewShotUtils;
import com.yk.mvp.BaseMvpPresenter;
import com.yk.rxsample.Observable;
import com.yk.rxsample.Subscriber;

import java.io.File;

public class PreviewPresenter extends BaseMvpPresenter<IPreviewView> {

    private final Context context;

    public PreviewPresenter(Context context) {
        this.context = context;
    }

    public void deleteNote(Note note) {
        Observable.fromCallable(new Observable.OnCallable<Note>() {
            @Override
            public Note call() {
                return NoteDbManager.getInstance().deleteNote(note);
            }
        })
                .map(new Observable.Function1<Note, Boolean>() {
                    @Override
                    public Boolean call(Note note) {
                        if (note != null) {
                            EventPoster.getInstance().post(new NoteRemoveEvent(note));
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
                            getView().onDeleteNote(success);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Exception e) {
                        if (getView() != null) {
                            getView().onDeleteNote(false);
                        }
                    }
                });
    }

    public void shareFile(Note note) {
        Observable.fromCallable(new Observable.OnCallable<File>() {
            @Override
            public File call() {
                boolean isOutput = NoteUtils.isOutputNote(context, note);
                if (!isOutput) {
                    NoteUtils.outputNote(context, note);
                }

                return new File(NoteUtils.generateNotePath(context, note));
            }
        })
                .map(new Observable.Function1<File, Uri>() {
                    @Override
                    public Uri call(File file) {
                        if (file == null) {
                            return null;
                        }
                        return NoteUtils.getFileUri(context, file);
                    }
                })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<Uri>() {
                    @Override
                    public void onNext(Uri uri) {
                        if (uri == null) {
                            onError(new RuntimeException("uri is null"));
                            return;
                        }

                        if (getView() != null) {
                            getView().onShareFile(uri);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Exception e) {
                        if (getView() != null) {
                            getView().onShareFileError(e);
                        }
                    }
                });
    }

    public void shareImage(TextView view) {
        Observable.fromCallable(new Observable.OnCallable<File>() {
            @Override
            public File call() {
                String output = NoteUtils.generateShotPath(context);
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
                        return NoteUtils.getFileUri(context, file);
                    }
                })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<Uri>() {
                    @Override
                    public void onNext(Uri uri) {
                        if (uri == null) {
                            onError(new RuntimeException("uri is null"));
                            return;
                        }

                        if (getView() != null) {
                            getView().onShareImage(uri);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Exception e) {
                        if (getView() != null) {
                            getView().onShareImageError(e);
                        }
                    }
                });
    }
}
