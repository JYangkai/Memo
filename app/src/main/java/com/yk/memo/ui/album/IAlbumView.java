package com.yk.memo.ui.album;

import com.yk.memo.data.bean.Image;
import com.yk.mvp.BaseMvpView;

import java.util.List;

public interface IAlbumView extends BaseMvpView {
    void onLoadData(List<Image> imageList);

    void onLoadDataError(Exception e);
}
