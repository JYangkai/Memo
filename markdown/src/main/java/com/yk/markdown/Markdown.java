package com.yk.markdown;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.TextView;

import com.yk.markdown.core.MdParser;
import com.yk.markdown.core.MdRender;

public class Markdown {
    private final String src;
    private int padding;

    private Markdown(String src) {
        this.src = src;
    }

    public static Markdown load(String src) {
        return new Markdown(src);
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

        tv.setPadding(padding, padding, padding, padding);

        tv.setText(MdRender.getSpanStrBuilder(MdParser.dealMarkdown(src)));
    }

    public static SpannableStringBuilder getMd(String src) {
        return MdRender.getSpanStrBuilder(MdParser.dealMarkdown(src));
    }

}
