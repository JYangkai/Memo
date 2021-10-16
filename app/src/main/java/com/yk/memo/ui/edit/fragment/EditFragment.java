package com.yk.memo.ui.edit.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.yk.markdown.bean.MdType;
import com.yk.memo.R;
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
        Editable text = etContent.getText();
        if (text == null) {
            return "";
        }
        return text.toString();
    }

    public void clear() {
        etContent.setText(null);
    }
}
