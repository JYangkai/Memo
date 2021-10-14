package com.yk.markdown.span;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;

import com.yk.markdown.style.MdStyleManager;
import com.yk.markdown.style.bean.MdStyleCode;

public class MdCodeSpan extends MetricAffectingSpan {
    private final int backgroundColor;
    private final int textColor;
    private final int textSize;

    public MdCodeSpan() {
        MdStyleCode code = MdStyleManager.getInstance().getMdStyle().getCode();
        backgroundColor = code.getBackgroundColor();
        textColor = code.getTextColor();
        textSize = code.getTextSize();
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint textPaint) {
        updateState(textPaint);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        updateState(tp);
    }

    private void updateState(TextPaint tp) {
        tp.bgColor = backgroundColor;
        tp.setColor(textColor);
        tp.setTextSize(textSize);
    }
}
