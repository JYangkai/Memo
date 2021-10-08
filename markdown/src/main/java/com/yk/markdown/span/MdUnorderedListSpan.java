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

public class MdUnorderedListSpan extends MetricAffectingSpan implements LeadingMarginSpan, ParcelableSpan {
    private final int circleColor;
    private final int circleRadius;
    private final int gapWidth;
    private final int textColor;
    private final int textSize;

    public MdUnorderedListSpan() {
        this(MdStyle.UnorderedList.CIRCLE_COLOR,
                MdStyle.UnorderedList.CIRCLE_RADIUS,
                MdStyle.UnorderedList.GAP_WIDTH,
                MdStyle.UnorderedList.TEXT_COLOR,
                MdStyle.UnorderedList.TEXT_SIZE);
    }

    public MdUnorderedListSpan(int circleColor, int circleRadius, int gapWidth, int textColor, int textSize) {
        this.circleColor = circleColor;
        this.circleRadius = circleRadius;
        this.gapWidth = gapWidth;
        this.textColor = textColor;
        this.textSize = textSize;
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
