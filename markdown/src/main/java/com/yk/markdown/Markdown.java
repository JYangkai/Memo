package com.yk.markdown;

import android.text.SpannableStringBuilder;

import com.yk.markdown.core.MdParser;
import com.yk.markdown.core.MdRender;

public class Markdown {

    public static SpannableStringBuilder getMd(String src) {
        return MdRender.getSpanStrBuilder(MdParser.dealMarkdown(src));
    }

}
