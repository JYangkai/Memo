package com.yk.markdown.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.LineBackgroundSpan;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;

import com.yk.markdown.bean.MdType;
import com.yk.markdown.style.MdStyleManager;
import com.yk.markdown.style.bean.MdStyleSeparator;
import com.yk.markdown.style.bean.MdStyleTitle;

public class MdTitleSpan extends MetricAffectingSpan implements LineBackgroundSpan, ParcelableSpan {
    private final int level;
    private final int textColor;
    private final int textSize;

    public MdTitleSpan() {
        this(1);
    }

    public MdTitleSpan(int level) {
        this.level = level;

        MdStyleTitle title = MdStyleManager.getInstance().getMdStyle().getTitle();
        textColor = title.getTextColor();
        switch (level) {
            case 1:
                textSize = title.getTextSize1();
                break;
            case 2:
                textSize = title.getTextSize2();
                break;
            case 3:
                textSize = title.getTextSize3();
                break;
            case 4:
                textSize = title.getTextSize4();
                break;
            case 5:
            default:
                textSize = title.getTextSize5();
                break;
        }
    }

    @Override
    public int getSpanTypeId() {
        return MdType.TITLE.ordinal();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(level);
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
        tp.setFakeBoldText(true);
        tp.setColor(textColor);
        tp.setTextSize(textSize);
    }

    @Override
    public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence text, int start, int end, int lineNumber) {
        if (level > 2) {
            return;
        }

        int preColor = paint.getColor();
        float preStrokeWidth = paint.getStrokeWidth();

        MdStyleSeparator separator = MdStyleManager.getInstance().getMdStyle().getSeparator();

        paint.setColor(separator.getColor());
        paint.setStrokeWidth(separator.getSize());
        canvas.drawLine(left, bottom, right, bottom, paint);

        paint.setColor(preColor);
        paint.setStrokeWidth(preStrokeWidth);
    }
}
