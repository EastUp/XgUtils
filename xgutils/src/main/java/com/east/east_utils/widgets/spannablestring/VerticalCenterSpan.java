package com.east.east_utils.widgets.spannablestring;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @author East
 * |---------------------------------------------------------------------------------------------------------------|
 * @date：2018/10/10 14:45
 */

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;

import com.east.east_utils.utils.SizeUtils;

/**
 * 使TextView中不同大小字体垂直居中
 */
public class VerticalCenterSpan extends ReplacementSpan {
    private float fontSizeSp;    //字体大小sp

    public VerticalCenterSpan(float fontSizeSp) {
        this.fontSizeSp = fontSizeSp;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        text = text.subSequence(start, end);
        Paint p = getCustomTextPaint(paint);
        return (int) p.measureText(text.toString());
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        text = text.subSequence(start, end);
        Paint p = getCustomTextPaint(paint);
        Paint.FontMetricsInt fm = p.getFontMetricsInt();
        // 此处重新计算y坐标，使字体居中
        canvas.drawText(text.toString(), x, y - ((y + fm.descent + y + fm.ascent) / 2 - (bottom + top) / 2), p);
    }

    private TextPaint getCustomTextPaint(Paint srcPaint) {
        TextPaint paint = new TextPaint(srcPaint);
        paint.setTextSize(SizeUtils.sp2px(fontSizeSp));   //设定字体大小, sp转换为px
        return paint;
    }
}

