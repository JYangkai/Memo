package com.yk.markdown.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;

import com.yk.markdown.bean.MdType;
import com.yk.markdown.style.MdStyleManager;
import com.yk.markdown.style.bean.MdStyleCodeBlock;

public class MdCodeBlockSpan extends MetricAffectingSpan implements LeadingMarginSpan, LineBackgroundSpan, ParcelableSpan {
    private final int gapWidth;
    private final int backgroundColor;
    private final int textColor;
    private final int textSize;

    public MdCodeBlockSpan() {
        MdStyleCodeBlock codeBlock = MdStyleManager.getInstance().getMdStyle().getCodeBlock();
        gapWidth = codeBlock.getGapWidth();
        backgroundColor = codeBlock.getBackgroundColor();
        textColor = codeBlock.getTextColor();
        textSize = codeBlock.getTextSize();
    }

    @Override
    public int getSpanTypeId() {
        return MdType.CODE_BLOCK.ordinal();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(gapWidth);
        dest.writeInt(backgroundColor);
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
    public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence text, int start, int end, int lineNumber) {
        int preColor = paint.getColor();

        paint.setColor(backgroundColor);

        canvas.drawRect(new Rect(left, top, right, bottom), paint);

        paint.setColor(preColor);
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return gapWidth;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {

    }
}
