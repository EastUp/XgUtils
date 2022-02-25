package com.east.east_utils.widgets.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

import com.east.east_utils.R;


/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 *  @description:可设置TextView 上下左右图片大小的自定义控件
 *  @author: East
 *  @date: 2019-10-23
 * |---------------------------------------------------------------------------------------------------------------|
 */
public class DrawableTextView extends AppCompatTextView {
    private Drawable drawableLeft = null, drawableTop = null, drawableRight = null,
            drawableBottom = null;
    private int drawableWidth, drawableHeight;
    public DrawableTextView(Context context) {
        this(context, null);
    }

    public DrawableTextView(Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableTextView(Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView);
        drawableLeft = typedArray.getDrawable(R.styleable.DrawableTextView_textViewDrawableLeft);
        drawableRight = typedArray.getDrawable(R.styleable.DrawableTextView_textViewDrawableRight);
        drawableTop = typedArray.getDrawable(R.styleable.DrawableTextView_textViewDrawableTop);
        drawableBottom = typedArray.getDrawable(R.styleable.DrawableTextView_textViewDrawableBottom);
        drawableWidth = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_textViewDrawableWidth, 0);
        drawableHeight = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_textViewDrawableHeight, 0);
        if(null != drawableLeft){
            drawableLeft.setBounds(0,0, drawableWidth, drawableHeight);
        }
        if(null != drawableRight){
            drawableRight.setBounds(0,0, drawableWidth, drawableHeight);
        }
        if(null != drawableTop){
            drawableTop.setBounds(0,0, drawableWidth, drawableHeight);
        }
        if(null != drawableBottom){
            drawableBottom.setBounds(0,0, drawableWidth, drawableHeight);
        }
        setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
        typedArray.recycle();
    }


}
