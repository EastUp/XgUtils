package com.east.east_utils.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.east.east_utils.R;

/**
 * 描述：渐变色圆弧进度条
 * Created by East at 2018/5/29 15:23
 */
public class GradientArcProgressBar extends View {
    /**
     * 画笔对象的引用
     */
    private Paint paint_bg,paint_progress;

    /**
     * 圆环的颜色
     */
    private int arcColor;
    /**
     * 圆环的宽度
     */
    private float arcWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private float progress;

    private int min;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    //分段颜色
    private int[] mColors = {Color.parseColor("#0988fe"), Color.parseColor("#01ede5"), Color.parseColor("#24ff00"), Color.parseColor("#fce521"),
            Color.parseColor("#ff5d2b")};

    public static final int STROKE = 0;
    public static final int FILL = 1;

    public GradientArcProgressBar(Context context) {
        this(context, null);
    }

    public GradientArcProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientArcProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint_bg = new Paint();
        paint_progress = new Paint();


        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.GradientArcProgressBar);

        //获取自定义属性和默认值
        arcColor = mTypedArray.getColor(R.styleable.GradientArcProgressBar_arcColor, Color.RED);
        arcWidth = mTypedArray.getDimension(R.styleable.GradientArcProgressBar_arcWidth, 5);
        max = mTypedArray.getInteger(R.styleable.GradientArcProgressBar_arcMax, 100);
        min = mTypedArray.getInteger(R.styleable.GradientArcProgressBar_arcMin, 0);
        progress = mTypedArray.getFloat(R.styleable.GradientArcProgressBar_arcProgress, 0);
        style = mTypedArray.getInt(R.styleable.GradientArcProgressBar_arcStyle, 0);

        mTypedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 画最外层没值时的大圆弧
         */
        int center = getWidth() / 2; //获取圆心的x坐标
        int radius = (int) (center - arcWidth /*/ 2*/); //圆环的半径(是圆环中心点到原点的距离，不是外半径，也不是内半径)
        paint_bg.setColor(arcColor); //设置圆环的颜色
        paint_bg.setStyle(Paint.Style.STROKE); //设置空心
        paint_bg.setStrokeWidth(arcWidth); //设置圆环的宽度
        paint_bg.setStrokeCap(Paint.Cap.ROUND); //设置笔刷的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形
        paint_bg.setAntiAlias(true);  //消除锯齿
        //为圆弧添加发光的效果
        BlurMaskFilter maskFilter = new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID);
        paint_bg.setMaskFilter(maskFilter);
        RectF oval = new RectF(center - radius, center - radius, center
                + radius, center + radius);  //用于定义的圆弧的形状和大小的界限
        canvas.drawArc(oval, -234, 288, false, paint_bg);  //根据进度画圆弧

        /**
         * 画圆弧 ，画圆环的进度
         */

        paint_progress.setAntiAlias(true);  //消除锯齿
        paint_progress.setStrokeCap(Paint.Cap.ROUND); //设置笔刷的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形
        paint_progress.setStrokeWidth(arcWidth); //设置圆环的宽度(有了宽度后，限制其实就在roundWidth的一半位置)
        //为圆弧添加发光的效果
        paint_progress.setMaskFilter(maskFilter);
        int count = mColors.length;
        int[] colors = new int[count];
        System.arraycopy(mColors, 0, colors, 0, count);
        //线性渐变
//        LinearGradient shader = new LinearGradient(3, 3, getWidth() - 3, 0, colors, null,
//                Shader.TileMode.CLAMP);
        //扫描渐变像雷达一样
        float[] positions = {0.1f, 0.3f,0.5f,0.7f,0.9f};//position的长度必须和color一样
        Matrix matrix = new Matrix();
        matrix.postRotate(90, center, center);
        SweepGradient shader = new SweepGradient(center, center, colors, positions);
        shader.setLocalMatrix(matrix);
        paint_progress.setShader(shader);

        switch (style) {
            case STROKE: {
                paint_progress.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, -234, 288 * (progress-min) / (max-min), false, paint_progress);  //根据进度画圆弧
                break;
            }
            case FILL: {
                paint_progress.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, -234, 288 * (progress-min) / (max-min), false, paint_progress);  //根据进度画圆弧
                break;
            }
        }

    }


    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    /**
     *  设置进度的最小值
     * @param min
     */
    public void setMin(int min) {
        this.min = min;
        postInvalidate();
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized float getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(float progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
        }
        postInvalidate();
    }



    public int getArcColor() {
        return arcColor;
    }
    //    设置没有进度时的背景
    public void setArcBgColor(int cricleColor) {
        this.arcColor = cricleColor;
    }

//    设置有进度时的颜色
    public void setProgressColors(int[] colors){
        mColors = colors;
    }

    public int[] getProgressColors(int[] colors){
        return mColors;
    }


    public float getArcWidth() {
        return arcWidth;
    }

    public void setArcWidth(float arcWidth) {
        this.arcWidth = arcWidth;
    }
}
