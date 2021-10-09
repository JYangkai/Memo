package com.yk.memo.ui.main;

import android.util.Log;

import com.yk.base.mvp.BaseMvpPresenter;
import com.yk.base.rxjava.Observable;
import com.yk.base.rxjava.Subscriber;
import com.yk.markdown.Markdown;
import com.yk.memo.data.bean.Note;
import com.yk.memo.data.db.NoteDbManager;

import java.util.List;

public class MainPresenter extends BaseMvpPresenter<IMainView> {
    private static final String TAG = "MainPresenter";

    public void loadAllNote() {
        Observable.fromCallable(new Observable.OnCallable<List<Note>>() {
            @Override
            public List<Note> call() {
                Log.d(TAG, "call: loadAllNote");
                return NoteDbManager.getAllNote();
            }
        })
                .map(new Observable.Function1<List<Note>, List<Note>>() {
                    @Override
                    public List<Note> call(List<Note> noteList) {
                        if (noteList == null || noteList.isEmpty()) {
                            return null;
                        }

                        for (Note note : noteList) {
                            note.setSpanStrBuilder(Markdown.load(note.getSrc()).getMd());
                        }

                        return noteList;
                    }
                })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<List<Note>>() {
                    @Override
                    public void onNext(List<Note> noteList) {
                        Log.d(TAG, "onNext: loadAllNote");
                        if (getView() != null) {
                            getView().onLoadNoteList(noteList);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: loadAllNote");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: loadAllNote ", e);
                    }
                });
    }

}
