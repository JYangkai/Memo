package com.yk.memo.ui.main;

import android.content.Context;
import android.net.Uri;

import com.yk.db.bean.Note;
import com.yk.db.manager.NoteDbManager;
import com.yk.memo.utils.NoteUtils;
import com.yk.mvp.BaseMvpPresenter;
import com.yk.rxsample.Observable;
import com.yk.rxsample.Subscriber;

import java.io.File;
import java.util.List;

public class MainPresenter extends BaseMvpPresenter<IMainView> {

    private final Context context;

    public MainPresenter(Context context) {
        this.context = context;
    }

    public void loadData() {
        Observable.fromCallable(new Observable.OnCallable<List<Note>>() {
            @Override
            public List<Note> call() {
                return NoteDbManager.getInstance().getAllNote();
            }
        })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<List<Note>>() {
                    @Override
                    public void onNext(List<Note> noteList) {
                        if (noteList == null || noteList.isEmpty()) {
                            onError(new RuntimeException("noteList is empty"));
                            return;
                        }

                        if (getView() != null) {
                            getView().onLoadData(noteList);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Exception e) {
                        if (getView() != null) {
                            getView().onLoadDataError(e);
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

    public void deleteNote(Note note) {
        Observable.fromCallable(new Observable.OnCallable<Note>() {
            @Override
            public Note call() {
                return NoteDbManager.getInstance().deleteNote(note);
            }
        })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<Note>() {
                    @Override
                    public void onNext(Note note) {
                        if (note == null) {
                            onError(new RuntimeException("note is null"));
                            return;
                        }

                        if (getView() != null) {
                            getView().onDeleteNote(note);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Exception e) {
                        if (getView() != null) {
                            getView().onDeleteNoteError(e);
                        }
                    }
                });
    }

    public void deleteNoteList(List<Note> noteList) {
        Observable.fromCallable(new Observable.OnCallable<List<Note>>() {
            @Override
            public List<Note> call() {
                return NoteDbManager.getInstance().deleteNoteList(noteList);
            }
        })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<List<Note>>() {
                    @Override
                    public void onNext(List<Note> noteList) {
                        if (noteList == null || noteList.isEmpty()) {
                            onError(new RuntimeException("noteList is empty"));
                            return;
                        }

                        if (getView() != null) {
                            getView().onDeleteNoteList(noteList);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Exception e) {
                        if (getView() != null) {
                            getView().onDeleteNoteListError(e);
                        }
                    }
                });
    }

    public void shareZip() {
        Observable.fromCallable(new Observable.OnCallable<File>() {
            @Override
            public File call() {
                return new File(NoteUtils.zipAllMarkdown(context));
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
                            getView().onShareZip(uri);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Exception e) {
                        if (getView() != null) {
                            getView().onShareZipError(e);
                        }
                    }
                });
    }
}
