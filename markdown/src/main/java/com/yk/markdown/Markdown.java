package com.yk.markdown;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.TextView;

import com.yk.markdown.core.MdParser;
import com.yk.markdown.core.MdRender;
import com.yk.markdown.core.MdThreadManager;

public class Markdown {
    private final String src;
    private boolean usePlaceHolder;
    private int padding;

    private Markdown(String src) {
        this.src = src;
    }

    public static Markdown load(String src) {
        return new Markdown(src);
    }

    public Markdown usePlaceHolder(boolean usePlaceHolder) {
        this.usePlaceHolder = usePlaceHolder;
        return this;
    }

    public Markdown padding(int padding) {
        this.padding = padding;
        return this;
    }

    public void into(TextView tv) {
        if (tv == null) {
            return;
        }

        if (TextUtils.isEmpty(src)) {
            return;
        }

        MdThreadManager.getInstance().postUi(new Runnable() {
            @Override
            public void run() {
                tv.setPadding(padding, padding, padding, padding);
                tv.setText(src);

                MdThreadManager.getInstance().postIo(new Runnable() {
                    @Override
                    public void run() {
                        SpannableStringBuilder spanStrBuilder = getMd();

                        MdThreadManager.getInstance().postUi(new Runnable() {
                            @Override
                            public void run() {

                                tv.setText(spanStrBuilder);
                            }
                        });
                    }
                });
            }
        });
    }

    public SpannableStringBuilder getMd() {
        return MdRender.getSpanStrBuilder(MdParser.dealMarkdown(src));
    }

}
