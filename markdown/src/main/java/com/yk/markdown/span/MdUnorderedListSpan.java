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
import com.yk.markdown.style.bean.MdStyleUnorderedList;

public class MdUnorderedListSpan extends MetricAffectingSpan implements LeadingMarginSpan, ParcelableSpan {
    private final int circleColor;
    private final int circleRadius;
    private final int gapWidth;
    private final int textColor;
    private final int textSize;

    public MdUnorderedListSpan() {
        MdStyleUnorderedList unorderedList = MdStyleManager.getInstance().getMdStyle().getUnorderedList();
        circleColor = unorderedList.getCircleColor();
        circleRadius = unorderedList.getCircleRadius();
        gapWidth = unorderedList.getGapWidth();
        textColor = unorderedList.getTextColor();
        textSize = unorderedList.getTextSize();
    }

    @Override
    public int getSpanTypeId() {
        return MdType.UNORDERED_LIST.ordinal();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(circleColor);
        dest.writeInt(circleRadius);
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
        return 2 * circleRadius + gapWidth;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        if (((Spanned) text).getSpanStart(this) == start) {
            // 如果是第一行，则画一个小圆点
            Paint.Style preStyle = p.getStyle();
            int preColor = p.getColor();

            p.setStyle(Paint.Style.FILL);
            p.setColor(circleColor);

            c.drawCircle(x + dir * circleRadius, (top + bottom) / 2.0f, circleRadius, p);

            p.setStyle(preStyle);
            p.setColor(preColor);
        }
    }
}
