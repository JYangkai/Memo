package com.yk.memo.ui.main;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.yk.memo.data.bean.Note;
import com.yk.memo.data.db.NoteDbManager;
import com.yk.memo.utils.FileUtils;
import com.yk.memo.utils.ZipUtils;
import com.yk.mvp.BaseMvpPresenter;
import com.yk.rxsample.Observable;
import com.yk.rxsample.Subscriber;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainPresenter extends BaseMvpPresenter<IMainView> {
    private static final String TAG = "MainPresenter2";

    private final Context context;

    public MainPresenter(Context context) {
        this.context = context;
    }

    /**
     * 打包分享
     */
    public void zipShare() {
        Observable.fromCallable(new Observable.OnCallable<File>() {
            @Override
            public File call() {
                Log.d(TAG, "call: zipShare get file");
                File zipFile = null;
                try {
                    zipFile = ZipUtils.zipAllMarkdown(context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return zipFile;
            }
        })
                .map(new Observable.Function1<File, Uri>() {
                    @Override
                    public Uri call(File file) {
                        Log.d(TAG, "call: zipShare get uri");
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
                        Log.d(TAG, "onNext: zipShare:" + uri);
                        if (getView() != null) {
                            getView().onZipShare(uri);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: zipShare");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: zipShare:", e);
                        if (getView() != null) {
                            getView().onZipShareError(e);
                        }
                    }
                });
    }

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

    /**
     * 导出note
     *
     * @param note note
     */
    public void outputNote(Note note) {
        Observable.fromCallable(new Observable.OnCallable<Note>() {
            @Override
            public Note call() {
                Log.d(TAG, "call: output note:" + note);
                boolean success = FileUtils.outputNoteToMarkdownFolder(context, note);
                return success ? note : null;
            }
        })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<Note>() {
                    @Override
                    public void onNext(Note note) {
                        Log.d(TAG, "onNext: output note:" + note);
                        if (getView() != null) {
                            if (note != null) {
                                getView().onOutputNote(note);
                            } else {
                                getView().onOutputNoteError(new RuntimeException("note is null, output error"));
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: output note");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: output note", e);
                        if (getView() != null) {
                            getView().onOutputNoteError(e);
                        }
                    }
                });
    }
}
