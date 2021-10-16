package com.yk.memo.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.PopupMenu;

import com.yk.markdown.bean.MdType;
import com.yk.memo.R;

public class MarkdownToolView extends FrameLayout {
    private AppCompatImageView ivFormat;
    private AppCompatImageView ivQuote;
    private AppCompatImageView ivCode;
    private AppCompatImageView ivOrderedList;
    private AppCompatImageView ivUnorderedList;
    private AppCompatImageView ivTitle;
    private AppCompatImageView ivSeparator;

    public MarkdownToolView(@NonNull Context context) {
        this(context, null);
    }

    public MarkdownToolView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarkdownToolView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_markdown_tool, this);
        findView();
        bindEvent();
    }

    private void findView() {
        ivFormat = findViewById(R.id.ivFormat);
        ivQuote = findViewById(R.id.ivQuote);
        ivCode = findViewById(R.id.ivCode);
        ivOrderedList = findViewById(R.id.ivOrderedList);
        ivUnorderedList = findViewById(R.id.ivUnorderedList);
        ivTitle = findViewById(R.id.ivTitle);
        ivSeparator = findViewById(R.id.ivSeparator);
    }

    private void bindEvent() {
        ivFormat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFormat(v);
            }
        });

        ivQuote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickQuote(v);
            }
        });

        ivCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCode(v);
            }
        });

        ivOrderedList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOrderedList(v);
            }
        });

        ivUnorderedList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUnorderedList(v);
            }
        });

        ivTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTitle(v);
            }
        });

        ivSeparator.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSeparator(v);
            }
        });
    }

    private void onClickFormat(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_iv_format, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_iv_format_bold_italics) {
                    onAppend(MdType.BOLD_ITALICS, "******", 3);
                } else if (item.getItemId() == R.id.menu_iv_format_bold) {
                    onAppend(MdType.BOLD, "****", 2);
                } else if (item.getItemId() == R.id.menu_iv_format_italics) {
                    onAppend(MdType.ITALICS, "**", 1);
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void onClickQuote(View v) {
        onAppend(MdType.QUOTE, "> ", 0);
    }

    private void onClickCode(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_iv_code, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_iv_code_code_block) {
                    onAppend(MdType.CODE_BLOCK, "```\n\n```", 4);
                } else if (item.getItemId() == R.id.menu_iv_code_code) {
                    onAppend(MdType.CODE, "``", 1);
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void onClickOrderedList(View v) {
        onAppend(MdType.ORDERED_LIST, "1. ", 0);
    }

    private void onClickUnorderedList(View v) {
        onAppend(MdType.UNORDERED_LIST, "- ", 0);
    }

    private void onClickTitle(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_iv_title, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_iv_title_1) {
                    onAppend(MdType.TITLE, "# ", 0);
                } else if (item.getItemId() == R.id.menu_iv_title_2) {
                    onAppend(MdType.TITLE, "## ", 0);
                } else if (item.getItemId() == R.id.menu_iv_title_3) {
                    onAppend(MdType.TITLE, "### ", 0);
                } else if (item.getItemId() == R.id.menu_iv_title_4) {
                    onAppend(MdType.TITLE, "#### ", 0);
                } else if (item.getItemId() == R.id.menu_iv_title_5) {
                    onAppend(MdType.TITLE, "##### ", 0);
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void onClickSeparator(View v) {
        onAppend(MdType.SEPARATOR, "---\n", 0);
    }

    private void onAppend(MdType type, String src, int selectOffset) {
        if (onMarkdownToolClick == null) {
            return;
        }
        onMarkdownToolClick.onTextAppend(type, src, selectOffset);
    }

    private OnMarkdownToolClick onMarkdownToolClick;

    public void setOnMarkdownToolClick(OnMarkdownToolClick onMarkdownToolClick) {
        this.onMarkdownToolClick = onMarkdownToolClick;
    }

    public interface OnMarkdownToolClick {
        void onTextAppend(MdType type, String src, int selectOffset);
    }
}
