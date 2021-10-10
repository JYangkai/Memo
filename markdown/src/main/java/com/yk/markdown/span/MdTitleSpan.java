package com.yk.markdown.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.LineBackgroundSpan;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;

import com.yk.markdown.bean.MdStyle;
import com.yk.markdown.bean.MdType;

public class MdTitleSpan extends MetricAffectingSpan implements LineBackgroundSpan, ParcelableSpan {
    private final int level;

    private final int textColor;

    public MdTitleSpan() {
        this(1);
    }

    public MdTitleSpan(int level) {
        this(level, MdStyle.Title.TEXT_COLOR);
    }

    public MdTitleSpan(int level, int textColor) {
        this.level = level;
        this.textColor = textColor;
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
        int textSize;
        switch (level) {
            case 1:
                textSize = MdStyle.Title.TEXT_SIZE_1;
                break;
            case 2:
                textSize = MdStyle.Title.TEXT_SIZE_2;
                break;
            case 3:
                textSize = MdStyle.Title.TEXT_SIZE_3;
                break;
            case 4:
                textSize = MdStyle.Title.TEXT_SIZE_4;
                break;
            case 5:
            default:
                textSize = MdStyle.Title.TEXT_SIZE_5;
                break;
        }

        tp.setColor(textColor);
        tp.setTextSize(textSize);
    }

    @Override
    public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence text, int start, int end, int lineNumber) {
        int preColor = paint.getColor();
        float preStrokeWidth = paint.getStrokeWidth();

        paint.setColor(MdStyle.Separator.COLOR);
        paint.setStrokeWidth(MdStyle.Separator.SIZE);
        canvas.drawLine(left, bottom, right, bottom, paint);

        paint.setColor(preColor);
        paint.setStrokeWidth(preStrokeWidth);
    }
}
