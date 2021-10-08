package com.yk.markdown.span;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;

import com.yk.markdown.bean.MdStyle;
import com.yk.markdown.bean.MdType;

public class MdNormalSpan extends MetricAffectingSpan implements ParcelableSpan {
    private final int textColor;
    private final int textSize;

    public MdNormalSpan() {
        this(MdStyle.Normal.TEXT_COLOR,
                MdStyle.Normal.TEXT_SIZE);
    }

    public MdNormalSpan(int textColor, int textSize) {
        this.textColor = textColor;
        this.textSize = textSize;
    }

    @Override
    public int getSpanTypeId() {
        return MdType.NORMAL.ordinal();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(textColor);
        dest.writeInt(textSize);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setColor(textColor);
        tp.setTextSize(textSize);
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint tp) {
        tp.setColor(textColor);
        tp.setTextSize(textSize);
    }
}
