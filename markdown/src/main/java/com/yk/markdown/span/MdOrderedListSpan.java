package com.yk.markdown.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.LeadingMarginSpan;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;

import com.yk.markdown.bean.MdType;
import com.yk.markdown.style.MdStyleManager;
import com.yk.markdown.style.bean.MdStyleOrderedList;

public class MdOrderedListSpan extends MetricAffectingSpan implements LeadingMarginSpan, ParcelableSpan {
    private final int index;

    private final int indexColor;
    private final int indexSize;
    private final int indexWidth;
    private final int gapWidth;
    private final int textColor;
    private final int textSize;

    public MdOrderedListSpan() {
        this(1);
    }

    public MdOrderedListSpan(int index) {
        MdStyleOrderedList orderedList = MdStyleManager.getInstance().getMdStyle().getOrderedList();
        indexColor = orderedList.getIndexColor();
        indexSize = orderedList.getIndexSize();
        indexWidth = orderedList.getIndexWidth();
        gapWidth = orderedList.getGapWidth();
        textColor = orderedList.getTextColor();
        textSize = orderedList.getTextSize();

        this.index = index;
    }

    @Override
    public int getSpanTypeId() {
        return MdType.ORDERED_LIST.ordinal();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(index);
        dest.writeInt(indexColor);
        dest.writeInt(indexSize);
        dest.writeInt(indexWidth);
        dest.writeInt(gapWidth);
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

    @Override
    public int getLeadingMargin(boolean first) {
        return indexWidth + gapWidth;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        if (((Spanned) text).getSpanStart(this) == start) {
            // 如果是第一行，则画index
            Paint.Style preStyle = p.getStyle();
            int preColor = p.getColor();
            float preTextSize = p.getTextSize();
            boolean isBold = p.isFakeBoldText();

            p.setStyle(Paint.Style.FILL);
            p.setColor(indexColor);
            p.setTextSize(indexSize);
            p.setFakeBoldText(true);

            String indexStr = index + ". ";
            c.drawText(indexStr, x, baseline, p);

            p.setStyle(preStyle);
            p.setColor(preColor);
            p.setTextSize(preTextSize);
            p.setFakeBoldText(isBold);
        }
    }
}
