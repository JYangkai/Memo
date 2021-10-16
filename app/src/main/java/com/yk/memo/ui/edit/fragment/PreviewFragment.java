package com.yk.memo.ui.edit.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.yk.markdown.Markdown;
import com.yk.memo.R;

public class PreviewFragment extends Fragment {
    private static final String TAG = "PreviewFragment";

    private static final String EXTRA_SRC = "extra_src";

    private AppCompatTextView tvPreview;

    public static PreviewFragment newInstance(String src) {
        PreviewFragment fragment = new PreviewFragment();
        loadData(fragment, src);
        return fragment;
    }

    public static void loadData(PreviewFragment fragment, String src) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_SRC, src);
        fragment.setArguments(bundle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        findView(view);
        initData();
        bindEvent();
    }

    private void findView(View view) {
        tvPreview = view.findViewById(R.id.tvPreview);
    }

    private void initData() {
        if (getArguments() != null) {
            String src = getArguments().getString(EXTRA_SRC);
            Markdown.load(src).into(tvPreview);
        }
    }

    private void bindEvent() {
        tvPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPreviewListener != null) {
                    onPreviewListener.onClickPreview();
                }
            }
        });
    }

    private OnPreviewListener onPreviewListener;

    public void setOnPreviewListener(OnPreviewListener onPreviewListener) {
        this.onPreviewListener = onPreviewListener;
    }

    public interface OnPreviewListener {
        void onClickPreview();
    }
}
