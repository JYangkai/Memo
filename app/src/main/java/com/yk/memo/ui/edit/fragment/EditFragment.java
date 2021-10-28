package com.yk.memo.ui.edit.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.yk.eventposter.EventPoster;
import com.yk.eventposter.Subscribe;
import com.yk.markdown2.bean.MdType;
import com.yk.memo.R;
import com.yk.memo.data.bean.Image;
import com.yk.memo.data.event.MdImageAddEvent;
import com.yk.memo.ui.view.MarkdownToolView;

public class EditFragment extends Fragment {
    private static final String TAG = "EditFragment";

    private static final String EXTRA_SRC = "extra_src";

    private AppCompatEditText etContent;
    private MarkdownToolView markdownToolView;

    public static EditFragment newInstance(String src) {
        EditFragment fragment = new EditFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_SRC, src);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        EventPoster.getInstance().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventPoster.getInstance().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        initData();
        bindEvent();
    }

    private void findView(View view) {
        etContent = view.findViewById(R.id.etContent);
        markdownToolView = view.findViewById(R.id.markdownToolView);
    }

    private void initData() {
        if (getArguments() != null) {
            String src = getArguments().getString(EXTRA_SRC);
            etContent.setText(src);
        }
        etContent.requestFocus();
    }

    private void bindEvent() {
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (onEditListener != null) {
                    onEditListener.onTextChange(s.toString());
                }
            }
        });

        markdownToolView.setOnMarkdownToolClick(new MarkdownToolView.OnMarkdownToolClick() {
            @Override
            public void onTextAppend(MdType type, String src, int selectOffset) {
                String append;
                switch (type) {
                    case QUOTE:
                    case CODE_BLOCK:
                    case UNORDERED_LIST:
                    case TITLE:
                        if (getLastChar() == '\n') {
                            append = src;
                        } else {
                            append = '\n' + src;
                        }
                        break;
                    case ORDERED_LIST:
                        if (getLastChar() == '\n') {
                            append = src;
                        } else {
                            int index = getCurLineIndex() + 1;
                            append = '\n' + src.replace("1", String.valueOf(index));
                        }
                        break;
                    default:
                        append = src;
                        break;
                }

                etContent.append(append);
                etContent.setSelection(getSrc().length() - selectOffset);
            }
        });
    }

    private char getLastChar() {
        String src = getSrc();
        if (TextUtils.isEmpty(src)) {
            return '\n';
        }
        return src.charAt(src.length() - 1);
    }

    private int getCurLineIndex() {
        String content = getSrc();
        if (TextUtils.isEmpty(content)) {
            return 0;
        }

        String[] lines = content.split("\n");

        if (lines.length == 0) {
            return 0;
        }

        String line = lines[lines.length - 1];

        if (line.matches("\\d+\\.\\s.*")) {
            String index = line.substring(0, line.indexOf('.'));
            return Integer.parseInt(index);
        }

        return 0;
    }

    public String getSrc() {
        if (etContent == null) {
            return "";
        }
        Editable text = etContent.getText();
        if (text == null) {
            return "";
        }
        return text.toString();
    }

    public void setSrc(String src) {
        etContent.setText(src);
        etContent.setSelection(src.length());
    }

    public void clear() {
        etContent.setText(null);
    }

    @Subscribe(threadMode = Subscribe.Thread.UI)
    public void onMdImageAddEvent(MdImageAddEvent event) {
        if (event == null) {
            return;
        }

        Image image = event.getImage();
        if (image == null) {
            return;
        }

        String name = image.getName();
        String path = image.getPath();
        if (TextUtils.isEmpty(path)) {
            return;
        }

        String src = "![" + name + "](" + path + ")";

        etContent.append(src);
        etContent.setSelection(getSrc().length());
    }

    private OnEditListener onEditListener;

    public void setOnEditListener(OnEditListener onEditListener) {
        this.onEditListener = onEditListener;
    }

    public interface OnEditListener {
        void onTextChange(String src);
    }
}
