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

import com.yk.markdown.bean.MdStyle;
import com.yk.markdown.bean.MdType;

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
        this(index, MdStyle.OrderedList.INDEX_COLOR,
                MdStyle.OrderedList.INDEX_SIZE,
                MdStyle.OrderedList.INDEX_WIDTH,
                MdStyle.OrderedList.GAP_WIDTH,
                MdStyle.OrderedList.TEXT_COLOR,
                MdStyle.OrderedList.TEXT_SIZE);
    }

    public MdOrderedListSpan(int index, int indexColor, int indexSize, int indexWidth, int gapWidth, int textColor, int textSize) {
        this.index = index;
        this.indexColor = indexColor;
        this.indexSize = indexSize;
        this.indexWidth = indexWidth;
        this.gapWidth = gapWidth;
        this.textColor = textColor;
        this.textSize = textSize;
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
        tp.setColor(textColor);
        tp.setTextSize(textSize);
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint tp) {
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

            p.setStyle(Paint.Style.FILL);
            p.setColor(indexColor);
            p.setTextSize(indexSize);

            String indexStr = index + ". ";
            c.drawText(indexStr, x, baseline, p);

            p.setStyle(preStyle);
            p.setColor(preColor);
            p.setTextSize(preTextSize);
        }
    }
}
