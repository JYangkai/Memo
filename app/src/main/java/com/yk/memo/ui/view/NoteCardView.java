package com.yk.memo.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.yk.memo.R;

public class NoteCardView extends CardView {
    private Paint paint;

    private boolean isSelect = false;

    public NoteCardView(@NonNull Context context) {
        this(context, null);
    }

    public NoteCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoteCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.design_default_color_primary));
        paint.setStrokeWidth(30);
        paint.setAntiAlias(true);
    }

    public void select(boolean isSelect) {
        this.isSelect = isSelect;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isSelect) {
            return;
        }

        canvas.drawLine(0, 0, 0, getHeight(), paint);
    }
}
