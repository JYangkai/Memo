package com.yk.memo.ui.preview;

import android.content.Context;
import android.net.Uri;
import android.widget.TextView;

import com.yk.db.bean.Note;
import com.yk.memo.utils.NoteUtils;
import com.yk.memo.utils.TimeUtils;
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
