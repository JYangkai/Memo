package com.yk.memo.ui.album;

import android.content.Context;
import android.util.Log;

import com.yk.memo.data.bean.Image;
import com.yk.memo.utils.MediaUtils;
import com.yk.mvp.BaseMvpPresenter;
import com.yk.rxsample.Observable;
import com.yk.rxsample.Subscriber;

import java.util.List;

public class AlbumPresenter extends BaseMvpPresenter<IAlbumView> {
    private static final String TAG = "AlbumPresenter";

    private final Context context;

    public AlbumPresenter(Context context) {
        this.context = context;
    }

    public void loadData() {
        Observable.fromCallable(new Observable.OnCallable<List<Image>>() {
            @Override
            public List<Image> call() {
                Log.d(TAG, "call: load data");
                return MediaUtils.getAllImage(context);
            }
        })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<List<Image>>() {
                    @Override
                    public void onNext(List<Image> imageList) {
                        Log.d(TAG, "onNext: load data:" + imageList.size());
                        if (getView() != null) {
                            getView().onLoadData(imageList);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: load data");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: load data:", e);
                        if (getView() != null) {
                            getView().onLoadDataError(e);
                        }
                    }
                });
    }
}
