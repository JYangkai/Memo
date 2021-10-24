package com.yk.memo.markdown;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.yk.memo.markdown.core.MdParser;
import com.yk.memo.markdown.core.MdRender;
import com.yk.memo.markdown.core.MdThreadManager;
import com.yk.memo.markdown.style.MdStyleManager;

public class Markdown {
    /**
     * context
     */
    private final Context context;

    /**
     * Markdown源码
     */
    private String src;

    /**
     * 风格
     */
    private MdStyleManager.Style style;

    /**
     * 设置的Tv
     */
    private TextView tv;

    private final MdParser parser = new MdParser();
    private final MdRender render = new MdRender();

    private Markdown(Context context) {
        this.context = context;
    }

    public static Markdown with(Context context) {
        return new Markdown(context);
    }

    public Markdown load(String src) {
        this.src = src;
        return this;
    }

    public Markdown style(MdStyleManager.Style style) {
        this.style = style;
        return this;
    }

    public void into(TextView tv) {
        if (tv == null) {
            return;
        }

        this.tv = tv;

        executeTask();
    }

    private void executeTask() {
        MdThreadManager.getInstance().postIo(new Runnable() {
            @Override
            public void run() {
                refreshUi(
                        render.render(
                                context,
                                parser.parser(
                                        src
                                ),
                                MdStyleManager.getInstance().getStyle(context, style)
                        )
                );
            }
        });
    }

    private void refreshUi(SpannableStringBuilder spanStrBuilder) {
        MdThreadManager.getInstance().postUi(new Runnable() {
            @Override
            public void run() {
                tv.setText(spanStrBuilder);
            }
        });
    }
}
