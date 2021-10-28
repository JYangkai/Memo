package com.yk.markdown2.core.requester;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yk.markdown2.Markdown;
import com.yk.markdown2.core.RequestManager;
import com.yk.markdown2.style.MdStyleManager;
import com.yk.markdown2.style.style.BaseMdStyle;
import com.yk.markdown2.utils.KeyUtils;

public abstract class BaseRequester implements IRequester {
    public enum RequestState {
        READY_REQUEST, // 准备
        START_REQUEST, // 开始
        STOP_REQUEST, // 停止
        DONE_REQUEST, // 完成
    }

    private RequestState requestState = RequestState.READY_REQUEST;

    private String src;
    private MdStyleManager.Style style = Markdown.getDefaultStyle();

    private SpannableStringBuilder spanStrBuilder;

    private TextView tv;

    public BaseMdStyle getMdStyle() {
        return MdStyleManager.getStyle(getContext(), getStyle());
    }

    public abstract Context getContext();

    @Override
    public IRequester load(String src) {
        this.src = src;
        return this;
    }

    @Override
    public IRequester style(MdStyleManager.Style style) {
        this.style = style;
        return this;
    }

    @Override
    public void into(TextView tv) {
        this.tv = tv;
        tv.setTag(KeyUtils.hashKey(src));
        RequestManager.getInstance().addRequester(this);
    }

    public RequestState getRequestState() {
        return requestState;
    }

    public void setRequestState(RequestState requestState) {
        this.requestState = requestState;
    }

    public String getSrc() {
        return src;
    }

    public MdStyleManager.Style getStyle() {
        return style;
    }

    public SpannableStringBuilder getSpanStrBuilder() {
        return spanStrBuilder;
    }

    public void setSpanStrBuilder(SpannableStringBuilder spanStrBuilder) {
        this.spanStrBuilder = spanStrBuilder;
    }

    public TextView getTv() {
        return tv;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof BaseRequester) {
            BaseRequester requester = (BaseRequester) obj;
            return requester.getTv().getTag().equals(this.getTv().getTag());
        }
        return super.equals(obj);
    }
}
