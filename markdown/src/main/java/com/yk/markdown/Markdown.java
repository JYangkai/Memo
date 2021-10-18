package com.yk.markdown;

import android.content.Context;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.TextView;

import com.yk.markdown.core.MdParser;
import com.yk.markdown.core.MdRender;
import com.yk.markdown.core.MdThreadManager;
import com.yk.markdown.style.MdStyleManager;

public class Markdown {
    /**
     * Markdown源码
     */
    private final String src;

    /**
     * 占位字符
     */
    private String placeHolder;

    /**
     * 风格
     */
    private MdStyleManager.Style style;

    /**
     * 设置的Tv
     */
    private TextView tv;

    /**
     * 构造方法
     *
     * @param src Markdown源码
     */
    private Markdown(String src) {
        this.src = src;
    }

    /**
     * 加载文本
     *
     * @param src Markdown源码
     * @return Markdown实例
     */
    public static Markdown load(String src) {
        return new Markdown(src);
    }

    public static void initStyle(MdStyleManager.Style style) {
        MdStyleManager.getInstance().choose(style);
    }

    public static void initStyle(Context context, String style) {
        MdStyleManager.getInstance().choose(context, style);
    }

    /**
     * 占位文本
     */
    public Markdown placeHolder(String placeHolder) {
        this.placeHolder = placeHolder;
        return this;
    }

    public Markdown style(MdStyleManager.Style style) {
        this.style = style;
        return this;
    }

    /**
     * 设置到tv
     */
    public void into(TextView tv) {
        if (tv == null) {
            return;
        }

        if (TextUtils.isEmpty(src)) {
            return;
        }

        this.tv = tv;

        if (isMainThread()) {
            executeTask();
        } else {
            MdThreadManager.getInstance().postUi(new Runnable() {
                @Override
                public void run() {
                    executeTask();
                }
            });
        }
    }

    /**
     * 执行任务
     */
    private void executeTask() {
        tv.setText(placeHolder);
        MdThreadManager.getInstance().postIo(new Runnable() {
            @Override
            public void run() {
                refreshUi(getMd());
            }
        });
    }

    /**
     * 刷新界面
     */
    private void refreshUi(SpannableStringBuilder spanStrBuilder) {
        MdThreadManager.getInstance().postUi(new Runnable() {
            @Override
            public void run() {
                tv.setText(spanStrBuilder);
            }
        });
    }

    /**
     * 获取Md Span Builder
     */
    private SpannableStringBuilder getMd() {
        return MdRender.getSpanStrBuilder(
                MdParser.dealMarkdown(src),
                MdStyleManager.getInstance().getStyle(style)
        );
    }

    /**
     * 判断主线程
     */
    private boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

}
