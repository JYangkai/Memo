package com.yk.markdown.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.style.LineBackgroundSpan;

import androidx.annotation.NonNull;

import com.yk.markdown.bean.MdType;
import com.yk.markdown.style.MdStyleManager;
import com.yk.markdown.style.bean.MdStyleSeparator;

public class MdSeparatorSpan implements LineBackgroundSpan, ParcelableSpan {
    private final int color;
    private final int size;

    public MdSeparatorSpan() {
        MdStyleSeparator separator = MdStyleManager.getInstance().getMdStyle().getSeparator();
        color = separator.getColor();
        size = separator.getSize();
    }

    @Override
    public int getSpanTypeId() {
        return MdType.SEPARATOR.ordinal();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(color);
        dest.writeInt(size);
    }

    @Override
    public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence text, int start, int end, int lineNumber) {
        int preColor = paint.getColor();
        float preStrokeWidth = paint.getStrokeWidth();

        paint.setColor(color);
        paint.setStrokeWidth(size);
        canvas.drawLine(left, (top + bottom) / 2.0f, right, (top + bottom) / 2.0f, paint);

        paint.setColor(preColor);
        paint.setStrokeWidth(preStrokeWidth);
    }
}
