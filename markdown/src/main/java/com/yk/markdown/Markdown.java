package com.yk.markdown;

import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.TextView;

import com.yk.markdown.core.MdParser;
import com.yk.markdown.core.MdRender;
import com.yk.markdown.core.MdThreadManager;

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

    /**
     * 占位文本
     */
    public Markdown placeHolder(String placeHolder) {
        this.placeHolder = placeHolder;
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
    public SpannableStringBuilder getMd() {
        return MdRender.getSpanStrBuilder(MdParser.dealMarkdown(src));
    }

    /**
     * 判断主线程
     */
    private boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

}
