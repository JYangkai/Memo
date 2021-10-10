package com.yk.markdown.span;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;

import com.yk.markdown.bean.MdStyle;
import com.yk.markdown.bean.MdType;

public class MdBoldItalicsSpan extends MetricAffectingSpan implements ParcelableSpan {
    private final int textColor;
    private final int textSize;

    public MdBoldItalicsSpan() {
        this(MdStyle.BoldItalics.TEXT_COLOR,
                MdStyle.BoldItalics.TEXT_SIZE);
    }

    public MdBoldItalicsSpan(int textColor, int textSize) {
        this.textColor = textColor;
        this.textSize = textSize;
    }

    @Override
    public int getSpanTypeId() {
        return MdType.CODE.ordinal();
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
    public void updateMeasureState(@NonNull TextPaint textPaint) {
        updateState(textPaint);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        updateState(tp);
    }

    private void updateState(TextPaint tp) {
        tp.setColor(textColor);
        tp.setTextSize(textSize);
        tp.setFakeBoldText(true);
        tp.setTextSkewX(-0.25f);
    }
}
