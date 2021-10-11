package com.yk.markdown.span;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;

import com.yk.markdown.bean.MdType;
import com.yk.markdown.style.MdStyleManager;
import com.yk.markdown.style.bean.MdStyleNormal;

public class MdNormalSpan extends MetricAffectingSpan implements ParcelableSpan {
    private final int textColor;
    private final int textSize;

    public MdNormalSpan() {
        MdStyleNormal normal = MdStyleManager.getInstance().getMdStyle().getNormal();
        textColor = normal.getTextColor();
        textSize = normal.getTextSize();
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
        updateState(tp);
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint tp) {
        updateState(tp);
    }

    private void updateState(TextPaint tp) {
        tp.setColor(textColor);
        tp.setTextSize(textSize);
    }
}
