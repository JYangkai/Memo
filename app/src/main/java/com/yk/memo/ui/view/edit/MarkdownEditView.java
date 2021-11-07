package com.yk.memo.ui.view.edit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yk.markdown.bean.MdType;
import com.yk.memo.R;

import java.util.ArrayList;
import java.util.List;

public class MarkdownEditView extends FrameLayout implements MarkdownEditAdapter.OnItemClickListener {
    public MarkdownEditView(@NonNull Context context) {
        this(context, null);
    }

    public MarkdownEditView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarkdownEditView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_markdown_edit, this);
        init();
    }

    private void init() {
        List<MarkdownEditBean> list = new ArrayList<>();
        list.add(new MarkdownEditBean(MdType.QUOTE, "> 引用"));
        list.add(new MarkdownEditBean(MdType.CODE_BLOCK, "```代码块```"));
        list.add(new MarkdownEditBean(MdType.ORDERED_LIST, "1. 有序列表"));
        list.add(new MarkdownEditBean(MdType.UNORDERED_LIST, "- 无序列表"));
        list.add(new MarkdownEditBean(MdType.TITLE, "# 标题"));
        list.add(new MarkdownEditBean(MdType.SEPARATOR, "--- 分隔符"));
        list.add(new MarkdownEditBean(MdType.CODE, "`代码`"));
        list.add(new MarkdownEditBean(MdType.BOLD, "**粗体**"));
        list.add(new MarkdownEditBean(MdType.ITALICS, "*斜体*"));
        list.add(new MarkdownEditBean(MdType.BOLD_ITALICS, "***粗斜体***"));

        MarkdownEditAdapter adapter = new MarkdownEditAdapter(list);
        adapter.setOnItemClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        RecyclerView rvMarkdownEdit = findViewById(R.id.rvMarkdownEdit);
        rvMarkdownEdit.setLayoutManager(layoutManager);
        rvMarkdownEdit.setAdapter(adapter);
    }

    private MarkdownEditAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MarkdownEditAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onItemClick(MarkdownEditBean bean) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(bean);
        }
    }
}
