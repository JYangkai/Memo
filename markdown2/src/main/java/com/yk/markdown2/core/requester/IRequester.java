package com.yk.markdown2.core.requester;

import android.widget.TextView;

import com.yk.markdown2.style.MdStyleManager;

public interface IRequester {
    IRequester load(String src);

    IRequester style(MdStyleManager.Style style);

    void into(TextView tv);
}
