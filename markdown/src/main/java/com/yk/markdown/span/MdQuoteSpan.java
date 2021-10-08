package com.yk.markdown.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.LeadingMarginSpan;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;

import com.yk.markdown.bean.MdStyle;
import com.yk.markdown.bean.MdType;

public class MdQuoteSpan extends MetricAffectingSpan implements LeadingMarginSpan, ParcelableSpan {
    private final int stripeColor;
    private final int stripeWidth;
    private final int gapWidth;
    private final int textColor;
    private final int textSize;

    public MdQuoteSpan() {
        this(MdStyle.Quote.STRIPE_COLOR,
                MdStyle.Quote.STRIPE_WIDTH,
                MdStyle.Quote.GAP_WIDTH,
                MdStyle.Quote.TEXT_COLOR,
                MdStyle.Quote.TEXT_SIZE);
    }

    public MdQuoteSpan(int stripeColor, int stripeWidth, int gapWidth, int textColor, int textSize) {
        this.stripeColor = stripeColor;
        this.stripeWidth = stripeWidth;
        this.gapWidth = gapWidth;
        this.textColor = textColor;
        this.textSize = textSize;
    }

    @Override
    public int getSpanTypeId() {
        return MdType.QUOTE.ordinal();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(stripeColor);
        dest.writeInt(stripeWidth);
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
        return stripeWidth + gapWidth;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        Paint.Style preStyle = p.getStyle();
        int preColor = p.getColor();

        p.setStyle(Paint.Style.FILL);
        p.setColor(stripeColor);

        c.drawRect(x, top, x + dir * stripeWidth, bottom, p);

        p.setStyle(preStyle);
        p.setColor(preColor);
    }
}
