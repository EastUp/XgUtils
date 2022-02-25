package com.east.east_utils.ui.recycleview.divider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.regex.Pattern;

/**
 * 应该是最万能的分割线了
 * 下面有调用方式
 * 注意: 1 如果设置gridborder为true则一定在recycleview中paddingRight:thickness的值
 *
 * @author eastrise
 * @date 2018/5/27
 */
/*recyclerView. addItemDecoration(new RecyclerViewItemDecoration.Builder(getActivity())
        .mode(RecyclerViewItemDecoration.MODE_GRID)
        // .dashWidth(8)
        //  .dashGap(5)
        .gridBorderVisible(true)  //一定在recycleview中paddingTop和paddingLeft:thickness的值
        .thickness(DisplayUtil.dip2px(this,2f))
        //.drawableID(R.color.line3)
        .create());*/
public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    /**
     * mode for direction
     * 分割线的方式
     */
    public static final int MODE_HORIZONTAL = 0;
    public static final int MODE_VERTICAL = 1;
    public static final int MODE_GRID = 2;

    /**
     * default decoration color
     * 默认的颜色
     */
    private static final String DEFAULT_COLOR = "#cccccc";

    /**
     * image resource id for R.java
     * 分割线的图片id
     */
    private int mDrawableRid = 0;
    /**
     * decoration color
     * 分割线颜色
     */
    private int mColor = Color.parseColor(DEFAULT_COLOR);
    /**
     * decoration thickness
     * 分割线的厚度
     */
    private float mThickness;
    /**
     * decoration dash withd
     * 分割线虚线宽度
     */
    private int mDashWidth = 0;
    /**
     * decoration dash gap
     * 分割线虚线的间隙
     */
    private int mDashGap = 0;
    private boolean mFirstLineVisible;
    private boolean mLastLineVisible;
    private boolean mGridBorderVisible; //是否绘制grid的外边框线
    private int mPaddingStart = 0;
    private int mPaddingEnd = 0;
    /**
     * direction mode for decoration
     */
    private int mMode;

    private Paint mPaint;

    private Bitmap mBmp;
    private NinePatch mNinePatch;
    /**
     * choose the real thickness for image or thickness
     */
    private float mCurrentThickness;
    /**
     * sign for if the resource image is a ninepatch image
     */
    private Boolean hasNinePatch = false;

    public RecyclerViewItemDecoration() {
    }

    @Deprecated
    public RecyclerViewItemDecoration(int recyclerviewMode, Context context, int drawableRid) {
        this.mMode = recyclerviewMode;
        this.mDrawableRid = drawableRid;

        this.mBmp = BitmapFactory.decodeResource(context.getResources(), drawableRid);
        if (mBmp.getNinePatchChunk() != null) {
            hasNinePatch = true;
            mNinePatch = new NinePatch(mBmp, mBmp.getNinePatchChunk(), null);
        }
        initPaint();

    }

    @Deprecated
    public RecyclerViewItemDecoration(int recyclerviewMode, int color, float thick, int dashWidth, int dashGap) {
        this.mMode = recyclerviewMode;
        this.mColor = color;
        this.mThickness = thick;
        this.mDashWidth = dashWidth;
        this.mDashGap = dashGap;

        initPaint();

    }

    @Deprecated
    public RecyclerViewItemDecoration(int recyclerviewMode, String color, int thick, int dashWidth, int dashGap) {
        this.mMode = recyclerviewMode;
        if (isColorString(color)) {
            this.mColor = Color.parseColor(color);
        } else {
            this.mColor = Color.parseColor(DEFAULT_COLOR);
        }
        this.mThickness = thick;
        this.mDashWidth = dashWidth;
        this.mDashGap = dashGap;

        initPaint();
    }

    public void setParams(Context context, Param params) {

        this.mMode = params.mode;
        this.mDrawableRid = params.drawableRid;
        this.mColor = params.color;
        this.mThickness = params.thickness;
        this.mDashGap = params.dashGap;
        this.mDashWidth = params.dashWidth;
        this.mPaddingStart = params.paddingStart;
        this.mPaddingEnd = params.paddingEnd;
        this.mFirstLineVisible = params.firstLineVisible;
        this.mLastLineVisible = params.lastLineVisible;
        this.mGridBorderVisible = params.gridBorderVisible;

        this.mBmp = BitmapFactory.decodeResource(context.getResources(), mDrawableRid);
        if (mBmp != null) {

            if (mBmp.getNinePatchChunk() != null) {
                hasNinePatch = true;
                mNinePatch = new NinePatch(mBmp, mBmp.getNinePatchChunk(), null);
            }

            if (mMode == MODE_HORIZONTAL)
                mCurrentThickness = mThickness == 0 ? mBmp.getHeight() : mThickness;
            if (mMode == MODE_VERTICAL)
                mCurrentThickness = mThickness == 0 ? mBmp.getWidth() : mThickness;
        }

        initPaint();

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mThickness);
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        mPaint.setColor(mColor);
        if (mMode == MODE_HORIZONTAL) {
            drawHorinzonal(c, parent);
        } else if (mMode == MODE_VERTICAL) {
            drawVertical(c, parent);
        } else if (mMode == MODE_GRID) {
            drawGrid(c, parent);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (mMode == MODE_HORIZONTAL) {

            if (!(!mLastLineVisible &&
                    parent.getChildLayoutPosition(view) == parent.getAdapter().getItemCount() - 1)) {
                if (mDrawableRid != 0) {
                    outRect.set(0, 0, 0, (int)mCurrentThickness);
                } else {
                    outRect.set(0, 0, 0, (int)mThickness);
                }
            }

            if (mFirstLineVisible && parent.getChildLayoutPosition(view) == 0) {
                if (mDrawableRid != 0) {
                    outRect.set(0, (int)mCurrentThickness, 0, (int)mCurrentThickness);
                } else {
                    outRect.set(0, (int)mThickness, 0, (int)mThickness);
                }
            }

        } else if (mMode == MODE_VERTICAL) {
            if (!(!mLastLineVisible &&
                    parent.getChildLayoutPosition(view) == parent.getAdapter().getItemCount() - 1)) {
                if (mDrawableRid != 0) {
                    outRect.set(0, 0, (int)mCurrentThickness, 0);
                } else {
                    outRect.set(0, 0, (int)mThickness, 0);
                }
            }
            if (mFirstLineVisible && parent.getChildLayoutPosition(view) == 0) {
                if (mDrawableRid != 0) {
                    outRect.set((int)mCurrentThickness, 0, (int)mCurrentThickness, 0);
                } else {
                    outRect.set((int)mThickness, 0, (int)mThickness, 0);
                }
            }

        } else if (mMode == MODE_GRID) {
            int columnSize = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            int itemSzie = parent.getAdapter().getItemCount();
            if(!mGridBorderVisible){//没有边框
                if (mDrawableRid != 0) {
                    if (isLastRowGrid(parent.getChildLayoutPosition(view), itemSzie, columnSize)
                            && isLastGridColumn(parent.getChildLayoutPosition(view), columnSize)) {
                        outRect.set(0, 0, 0, 0);
                    } else if (isLastRowGrid(parent.getChildLayoutPosition(view), itemSzie, columnSize)) {
                        outRect.set(0, 0, mBmp.getWidth(), 0);
                    } else if ((parent.getChildLayoutPosition(view) + 1) % columnSize != 0) {
                        outRect.set(0, 0, mBmp.getWidth(), mBmp.getHeight());
                    } else {
                        outRect.set(0, 0, 0, mBmp.getHeight());
                    }
                } else {
                    if (isLastRowGrid(parent.getChildLayoutPosition(view), itemSzie, columnSize)
                            && isLastGridColumn(parent.getChildLayoutPosition(view), columnSize)) {
                        outRect.set(0, 0, 0, 0);
                    } else if (isLastRowGrid(parent.getChildLayoutPosition(view), itemSzie, columnSize)) {
                        outRect.set(0, 0, (int)mThickness, 0);
                    } else if ((parent.getChildLayoutPosition(view) + 1) % columnSize != 0) {
                        outRect.set(0, 0, (int)mThickness, (int)mThickness);
                    } else {
                        outRect.set(0, 0, 0, (int)mThickness);
                    }

                }
            }else{//有边框
                if (mDrawableRid != 0) {
                    if (isLastRowGrid(parent.getChildLayoutPosition(view), itemSzie, columnSize)
                            && isLastGridColumn(parent.getChildLayoutPosition(view), columnSize)) {
                        outRect.set(mBmp.getWidth(), mBmp.getHeight(), mBmp.getWidth(), mBmp.getHeight());
                    } else if (isLastRowGrid(parent.getChildLayoutPosition(view), itemSzie, columnSize)) {
                        outRect.set(mBmp.getWidth(), mBmp.getHeight(), 0, mBmp.getHeight());
                    } else if (isLastGridColumn(parent.getChildLayoutPosition(view), columnSize)) {
                        outRect.set(mBmp.getWidth(), mBmp.getHeight(), mBmp.getWidth(), 0);
                    } else {
                        outRect.set(mBmp.getWidth(), mBmp.getHeight(),0, 0);
                    }
                } else {
                    if (isLastRowGrid(parent.getChildLayoutPosition(view), itemSzie, columnSize)
                            && isLastGridColumn(parent.getChildLayoutPosition(view), columnSize)) {
                        outRect.set((int)mThickness, (int)mThickness, (int)mThickness, (int)mThickness);
                    } else if (isLastRowGrid(parent.getChildLayoutPosition(view), itemSzie, columnSize)) {
                        outRect.set((int)mThickness, (int)mThickness, 0, (int)mThickness);
                    } else if (isLastGridColumn(parent.getChildLayoutPosition(view), columnSize)) {
                        outRect.set((int)mThickness, (int)mThickness,  (int)mThickness, 0);
                    } else {
                        outRect.set((int)mThickness, (int)mThickness, 0, 0);
                    }

                }
            }
        }

    }

    /**
     * judge is a color string like #xxxxxx or #xxxxxxxx
     *
     * @param colorStr
     * @return
     */
    public static boolean isColorString(String colorStr) {
        return Pattern.matches("^#([0-9a-fA-F]{6}||[0-9a-fA-F]{8})$", colorStr);
    }

    private boolean isPureLine() {
        if (mDashGap == 0 && mDashWidth == 0)
            return true;
        return false;
    }

    /**
     * draw horizonal decoration
     *
     * @param c
     * @param parent
     */
    private void drawHorinzonal(Canvas c, RecyclerView parent) {
        int childrentCount = parent.getChildCount();

        if (mDrawableRid != 0) {

            if (mFirstLineVisible) {
                View childView = parent.getChildAt(0);
                int myY = childView.getTop();

                if (hasNinePatch) {
                    Rect rect = new Rect(mPaddingStart, myY - (int)mCurrentThickness, parent.getWidth() - mPaddingEnd, myY);
                    mNinePatch.draw(c, rect);
                } else {
                    c.drawBitmap(mBmp, mPaddingStart, myY - mCurrentThickness, mPaint);
                }
            }

            for (int i = 0; i < childrentCount; i++) {
                if (!mLastLineVisible && i == childrentCount - 1)
                    break;
                View childView = parent.getChildAt(i);
                int myY = childView.getBottom();

                if (hasNinePatch) {
                    Rect rect = new Rect(mPaddingStart, myY, parent.getWidth() - mPaddingEnd, myY + (int)mCurrentThickness);
                    mNinePatch.draw(c, rect);
                } else {
                    c.drawBitmap(mBmp, mPaddingStart, myY, mPaint);
                }

            }

        } else {

            boolean isPureLine = isPureLine();
            if (!isPureLine) {
                PathEffect effects = new DashPathEffect(new float[]{0, 0, mDashWidth, mThickness}, mDashGap);
                mPaint.setPathEffect(effects);
            }

            if (mFirstLineVisible) {
                View childView = parent.getChildAt(0);
                float myY = childView.getTop() - mThickness / 2;

                if (isPureLine) {
                    c.drawLine(mPaddingStart, myY, parent.getWidth() - mPaddingEnd, myY, mPaint);
                } else {
                    Path path = new Path();
                    path.moveTo(mPaddingStart, myY);
                    path.lineTo(parent.getWidth() - mPaddingEnd, myY);
                    c.drawPath(path, mPaint);
                }
            }

            for (int i = 0; i < childrentCount; i++) {
                if (!mLastLineVisible && i == childrentCount - 1)
                    break;
                View childView = parent.getChildAt(i);
                float myY = childView.getBottom() + mThickness / 2;

                if (isPureLine) {
                    c.drawLine(mPaddingStart, myY, parent.getWidth() - mPaddingEnd, myY, mPaint);
                } else {
                    Path path = new Path();
                    path.moveTo(mPaddingStart, myY);
                    path.lineTo(parent.getWidth() - mPaddingEnd, myY);
                    c.drawPath(path, mPaint);
                }

            }

        }
    }

    /**
     * draw vertival decoration
     *
     * @param c
     * @param parent
     */
    private void drawVertical(Canvas c, RecyclerView parent) {
        int childrentCount = parent.getChildCount();
        if (mDrawableRid != 0) {

            if (mFirstLineVisible) {
                View childView = parent.getChildAt(0);
                int myX = childView.getLeft();
                if (hasNinePatch) {
                    Rect rect = new Rect(myX - (int)mCurrentThickness, mPaddingStart, myX, parent.getHeight() - mPaddingEnd);
                    mNinePatch.draw(c, rect);
                } else {
                    c.drawBitmap(mBmp, myX - mCurrentThickness, mPaddingStart, mPaint);
                }
            }
            for (int i = 0; i < childrentCount; i++) {
                if (!mLastLineVisible && i == childrentCount - 1)
                    break;
                View childView = parent.getChildAt(i);
                int myX = childView.getRight();
                if (hasNinePatch) {
                    Rect rect = new Rect(myX, mPaddingStart, myX + (int)mCurrentThickness, parent.getHeight() - mPaddingEnd);
                    mNinePatch.draw(c, rect);
                } else {
                    c.drawBitmap(mBmp, myX, mPaddingStart, mPaint);
                }
            }

        } else {

            boolean isPureLine = isPureLine();
            if (!isPureLine) {
                PathEffect effects = new DashPathEffect(new float[]{0, 0, mDashWidth, mThickness}, mDashGap);
                mPaint.setPathEffect(effects);
            }

            if (mFirstLineVisible) {
                View childView = parent.getChildAt(0);
                float myX = childView.getLeft() - mThickness / 2;
                if (isPureLine) {
                    c.drawLine(myX, mPaddingStart, myX, parent.getHeight() - mPaddingEnd, mPaint);
                } else {
                    Path path = new Path();
                    path.moveTo(myX, mPaddingStart);
                    path.lineTo(myX, parent.getHeight() - mPaddingEnd);
                    c.drawPath(path, mPaint);
                }
            }

            for (int i = 0; i < childrentCount; i++) {
                if (!mLastLineVisible && i == childrentCount - 1)
                    break;
                View childView = parent.getChildAt(i);
                float myX = childView.getRight() + mThickness / 2;
                if (isPureLine) {
                    c.drawLine(myX, mPaddingStart, myX, parent.getHeight() - mPaddingEnd, mPaint);
                } else {
                    Path path = new Path();
                    path.moveTo(myX, mPaddingStart);
                    path.lineTo(myX, parent.getHeight() - mPaddingEnd);
                    c.drawPath(path, mPaint);
                }

            }
        }
    }

    /**
     * draw grid decoration
     *
     * @param c
     * @param parent
     */
    private void drawGrid(Canvas c, RecyclerView parent) {

        int childrentCount = parent.getChildCount();
        int columnSize = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        int adapterChildrenCount = parent.getAdapter().getItemCount();

        if (mDrawableRid != 0) {
            if (hasNinePatch) {
                for (int i = 0; i < childrentCount; i++) {
                    View childView = parent.getChildAt(i);
                    int myX = childView.getRight();
                    int myY = childView.getBottom();

                    /**
                     *  没有边框的
                     */
                    if(!mGridBorderVisible){
                        //horizonal
                        if (!isLastRowGrid(i, adapterChildrenCount, columnSize)) {
                            Rect rect = new Rect(0, myY, myX, myY + mBmp.getHeight());
                            mNinePatch.draw(c, rect);
                        }

                        //vertical
                        if (isLastRowGrid(i, adapterChildrenCount, columnSize)
                                && !isLastGridColumn(i, columnSize)) {
                            Rect rect = new Rect(myX, childView.getTop(), myX + mBmp.getWidth(), myY);
                            mNinePatch.draw(c, rect);
                        } else if (!isLastGridColumn(i, columnSize)) {
                            Rect rect = new Rect(myX, childView.getTop(), myX + mBmp.getWidth(), myY + mBmp.getHeight());
                            mNinePatch.draw(c, rect);
                        }
                    }else{//需要边框

                        if (isLastRowGrid(i, adapterChildrenCount, columnSize)) {
                            //horizonal
                            Rect rect = new Rect(0, childView.getTop()-mBmp.getHeight(), myX,childView.getTop());
                            mNinePatch.draw(c, rect);
                            //horizonal
                            Rect rect1 = new Rect(0, myY, myX, myY + mBmp.getHeight());
                            mNinePatch.draw(c, rect1);

                            //vertical
                            Rect rect3 = new Rect(childView.getLeft()-mBmp.getWidth(), childView.getTop() - mBmp.getHeight(), childView.getLeft(), myY+mBmp.getHeight());
                            mNinePatch.draw(c, rect3);
                            if(isLastGridColumn(i, columnSize)){
                                //vertical
                                Rect rect4 = new Rect(myX, childView.getTop() - mBmp.getHeight(), myX + mBmp.getWidth(), myY+mBmp.getHeight());
                                mNinePatch.draw(c, rect4);
                            }
                        }else{
                            //horizonal
                            Rect rect = new Rect(0, childView.getTop()-mBmp.getHeight(), myX,childView.getTop());
                            mNinePatch.draw(c, rect);

                            //vertical
                            Rect rect3 = new Rect(childView.getLeft()-mBmp.getWidth(), childView.getTop() - mBmp.getHeight(), childView.getLeft(), myY);
                            mNinePatch.draw(c, rect3);
                            if(isLastGridColumn(i, columnSize)){
                                //vertical
                                Rect rect4 = new Rect(myX, childView.getTop() - mBmp.getHeight(), myX + mBmp.getWidth(), myY+mBmp.getHeight());
                                mNinePatch.draw(c, rect4);
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < childrentCount; i++) {
                    View childView = parent.getChildAt(i);
                    int myX = childView.getRight();
                    int myY = childView.getBottom();

                    /**
                     *  没有边框的
                     */
                    if(!mGridBorderVisible){
                        //horizonal
                        if (!isLastRowGrid(i, adapterChildrenCount, columnSize)) {
                            c.drawBitmap(mBmp, childView.getLeft(), myY, mPaint);
                        }

                        //vertical
                        if (!isLastGridColumn(i, columnSize)) {
                            c.drawBitmap(mBmp, myX, childView.getTop(), mPaint);
                        }
                    }else{//需要边框
                        if (isLastRowGrid(i, adapterChildrenCount, columnSize)) {
                            //horizonal
                            c.drawBitmap(mBmp, childView.getLeft(), childView.getTop()-mBmp.getHeight(), mPaint);
                            //horizonal
                            c.drawBitmap(mBmp, childView.getLeft(), myY, mPaint);

                            //vertical
                            c.drawBitmap(mBmp, childView.getLeft()-mBmp.getWidth(), childView.getTop()-mBmp.getHeight(), mPaint);
                            if(isLastGridColumn(i, columnSize)){
                                //vertical
                                c.drawBitmap(mBmp, myX+mBmp.getWidth(), childView.getTop()-mBmp.getHeight(), mPaint);
                            }
                        }else{
                            //horizonal
                            c.drawBitmap(mBmp, childView.getLeft(), childView.getTop()-mBmp.getHeight(), mPaint);

                            //vertical
                            c.drawBitmap(mBmp, childView.getLeft()-mBmp.getWidth(), childView.getTop()-mBmp.getHeight(), mPaint);
                            if(isLastGridColumn(i, columnSize)){
                                //vertical
                                c.drawBitmap(mBmp, myX+mBmp.getWidth(), childView.getTop()-mBmp.getHeight(), mPaint);
                            }
                        }
                    }
                }
            }
        } else if (mDashWidth == 0 && mDashGap == 0) {

            for (int i = 0; i < childrentCount; i++) {
                View childView = parent.getChildAt(i);
                float myX = childView.getRight() + mThickness / 2;
                float myY = childView.getBottom() + mThickness / 2;

                /**
                 *  没有边框的
                 */
                if(!mGridBorderVisible){
                    //horizonal
                    if (!isLastRowGrid(i, adapterChildrenCount, columnSize)) {
                        c.drawLine(childView.getLeft(), myY, childView.getRight(), myY, mPaint);
                    }

                    //vertical
                    if (isLastRowGrid(i, adapterChildrenCount, columnSize)
                            && !isLastGridColumn(i, columnSize)) {
                        c.drawLine(myX, childView.getTop(), myX, childView.getBottom(), mPaint);
                    } else if (!isLastGridColumn(i, columnSize)) {
                        c.drawLine(myX, childView.getTop(), myX, childView.getBottom() + mThickness, mPaint);
                    }
                }else{//有边框
                    if (isLastRowGrid(i, adapterChildrenCount, columnSize)) {
                        //horizonal
                        c.drawLine(childView.getLeft(), childView.getTop() - mThickness / 2, childView.getRight(), childView.getTop() - mThickness / 2, mPaint);
                        //horizonal
                        c.drawLine(childView.getLeft(), myY, childView.getRight(), myY, mPaint);

                        //vertical
                        c.drawLine(childView.getLeft() - mThickness / 2, childView.getTop()-mThickness, childView.getLeft() - mThickness / 2, childView.getBottom()+mThickness, mPaint);
                        if(isLastGridColumn(i, columnSize)){
                            //vertical
                            c.drawLine(myX, childView.getTop()-mThickness, myX, childView.getBottom()+mThickness, mPaint);
                        }
                    }else{
                        //horizonal
                        c.drawLine(childView.getLeft(), childView.getTop() - mThickness / 2, childView.getRight(), childView.getTop() - mThickness / 2, mPaint);

                        //vertical
                        c.drawLine(childView.getLeft() - mThickness / 2, childView.getTop()-mThickness, childView.getLeft() - mThickness / 2, childView.getBottom(), mPaint);
                        if(isLastGridColumn(i, columnSize)){
                            //vertical
                            c.drawLine(myX, childView.getTop()-mThickness, myX, childView.getBottom(), mPaint);
                        }
                    }
                }
            }
        } else {
            PathEffect effects = new DashPathEffect(new float[]{0, 0, mDashWidth, mThickness}, mDashGap);
            mPaint.setPathEffect(effects);
            for (int i = 0; i < childrentCount; i++) {
                View childView = parent.getChildAt(i);
                float myX = childView.getRight() + mThickness / 2;
                float myY = childView.getBottom() + mThickness / 2;

                /**
                 *  没有边框的
                 */
                if(!mGridBorderVisible){
                    //horizonal
                    if (!isLastRowGrid(i, adapterChildrenCount, columnSize)) {
                        Path path = new Path();
                        path.moveTo(0, myY);
                        path.lineTo(childView.getRight(), myY);
                        c.drawPath(path, mPaint);
                    }

                    //vertical
                    if (isLastRowGrid(i, adapterChildrenCount, columnSize)
                            && !isLastGridColumn(i, columnSize)) {
                        Path path = new Path();
                        path.moveTo(myX, childView.getTop());
                        path.lineTo(myX, childView.getBottom());
                        c.drawPath(path, mPaint);
                    } else if (!isLastGridColumn(i, columnSize)) {
                        Path path = new Path();
                        path.moveTo(myX, childView.getTop());
                        path.lineTo(myX, childView.getBottom() + mThickness);
                        c.drawPath(path, mPaint);
                    }
                }else{//有边框
                    if (isLastRowGrid(i, adapterChildrenCount, columnSize)) {
                        //horizonal
                        Path path = new Path();
                        path.moveTo(childView.getLeft(), childView.getTop() - mThickness / 2);
                        path.lineTo(childView.getRight(), childView.getTop() - mThickness / 2);
                        c.drawPath(path, mPaint);
                        //horizonal
                        Path path1 = new Path();
                        path1.moveTo(childView.getLeft(), myY);
                        path1.lineTo(childView.getRight(), myY);
                        c.drawPath(path1, mPaint);

                        //vertical
                        Path path2 = new Path();
                        path2.moveTo(childView.getLeft()- mThickness / 2, childView.getTop()-mThickness);
                        path2.lineTo(childView.getLeft()- mThickness / 2, childView.getBottom()+mThickness);
                        c.drawPath(path2, mPaint);
                        if(isLastGridColumn(i, columnSize)){
                            //vertical
                            Path path3 = new Path();
                            path3.moveTo(myX, childView.getTop()-mThickness);
                            path3.lineTo(myX, childView.getBottom()+mThickness);
                            c.drawPath(path3, mPaint);
                        }
                    }else{
                        //horizonal
                        Path path = new Path();
                        path.moveTo(childView.getLeft(), childView.getTop() - mThickness / 2);
                        path.lineTo(childView.getRight(), childView.getTop() - mThickness / 2);
                        c.drawPath(path, mPaint);

                        //vertical
                        Path path1 = new Path();
                        path1.moveTo(childView.getLeft()- mThickness / 2, childView.getTop()-mThickness);
                        path1.lineTo(childView.getLeft()- mThickness / 2, childView.getBottom());
                        c.drawPath(path1, mPaint);
                        if(isLastGridColumn(i, columnSize)){
                            //vertical
                            Path path2 = new Path();
                            path2.moveTo(myX, childView.getTop()-mThickness);
                            path2.lineTo(myX, childView.getBottom());
                            c.drawPath(path2, mPaint);
                        }
                    }
                }

            }
        }
    }

    /**
     * check if is one of the last columns
     *
     * @param position
     * @param columnSize
     * @return
     */
    private boolean isLastGridColumn(int position, int columnSize) {
        boolean isLast = false;
        if ((position + 1) % columnSize == 0) {
            isLast = true;
        }
        return isLast;
    }

    /**
     * check if is the last row of the grid
     *
     * @param position
     * @param itemSize
     * @param columnSize
     * @return
     */
    private boolean isLastRowGrid(int position, int itemSize, int columnSize) {
        return position / columnSize == (itemSize - 1) / columnSize;
    }

    public static class Builder {

        private Param params;
        private Context context;

        public Builder(Context context) {

            params = new Param();
            this.context = context;

        }

        public RecyclerViewItemDecoration create() {
            RecyclerViewItemDecoration recyclerViewItemDecoration = new RecyclerViewItemDecoration();
            recyclerViewItemDecoration.setParams(context, params);
            return recyclerViewItemDecoration;
        }

        public Builder mode(int mode) {
            params.mode = mode;
            return this;
        }

        public Builder drawableID(int drawableID) {
            params.drawableRid = drawableID;
            return this;
        }

        public Builder color(@ColorInt int color) {
            params.color = color;
            return this;
        }

        public Builder color(String color) {
            if (isColorString(color)) {
                params.color = Color.parseColor(color);
            }
            return this;
        }

        public Builder thickness(int thickness) {
            params.thickness = thickness;
            return this;
        }

        public Builder dashWidth(int dashWidth) {
            params.dashWidth = dashWidth;
            return this;
        }

        public Builder dashGap(int dashGap) {
            params.dashGap = dashGap;
            return this;
        }

        public Builder lastLineVisible(boolean visible) {
            params.lastLineVisible = visible;
            return this;
        }

        public Builder firstLineVisible(boolean visible) {
            params.firstLineVisible = visible;
            return this;
        }
        public Builder gridBorderVisible(boolean visible) {
            params.gridBorderVisible = visible;
            return this;
        }

        public Builder paddingStart(int padding) {
            params.paddingStart = padding;
            return this;
        }

        public Builder paddingEnd(int padding) {
            params.paddingEnd = padding;
            return this;
        }
    }

    private static class Param {

        public int mode = MODE_HORIZONTAL;
        public int drawableRid = 0;
        public int color = Color.parseColor(DEFAULT_COLOR);
        public int thickness;
        public int dashWidth = 0;
        public int dashGap = 0;
        public boolean lastLineVisible;
        public boolean firstLineVisible;
        public boolean gridBorderVisible;
        public int paddingStart;
        public int paddingEnd;
    }
}
