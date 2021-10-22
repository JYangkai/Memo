package com.yk.memo.ui.album;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.yk.eventposter.EventPoster;
import com.yk.memo.R;
import com.yk.memo.data.adapter.ImageAdapter;
import com.yk.memo.data.bean.Image;
import com.yk.memo.data.event.MdImageAddEvent;
import com.yk.memo.utils.SnackBarUtils;
import com.yk.mvp.BaseMvpActivity;

import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends BaseMvpActivity<IAlbumView, AlbumPresenter> implements IAlbumView {

    private Toolbar toolbar;
    private RecyclerView rvImage;

    private final List<Image> imageList = new ArrayList<>();
    private final ImageAdapter imageAdapter = new ImageAdapter(imageList);

    public static void start(Context context) {
        Intent intent = new Intent(context, AlbumActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        toolbar = findViewById(R.id.toolbar);
        rvImage = findViewById(R.id.rvImage);
    }

    private void initData() {
        initToolbar();
        initRvImage();

        presenter.loadData();
    }

    private void initToolbar() {
        toolbar.setTitle("图片");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initRvImage() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        rvImage.setLayoutManager(layoutManager);
        rvImage.setAdapter(imageAdapter);
    }

    private void bindEvent() {
        imageAdapter.setOnImageClickListener(new ImageAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(Image image) {
                EventPoster.getInstance().post(new MdImageAddEvent(image));
                finish();
            }
        });
    }

    @Override
    public AlbumPresenter createPresenter() {
        return new AlbumPresenter(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onLoadData(List<Image> imageList) {
        if (imageList == null || imageList.isEmpty()) {
            return;
        }

        this.imageList.clear();
        this.imageList.addAll(imageList);
        imageAdapter.notifyDataSetChanged();

        SnackBarUtils.showMsgShort(getWindow().getDecorView(), "加载 " + imageList.size() + " 条数据");
    }

    @Override
    public void onLoadDataError(Exception e) {
    }
}
