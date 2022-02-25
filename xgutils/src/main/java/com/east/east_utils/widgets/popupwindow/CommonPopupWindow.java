package com.east.east_utils.widgets.popupwindow;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;

import com.east.east_utils.widgets.common.ClickEntry;


/**
 * |---------------------------------------------------------------------------------------------------------------|
 *  @description:  万能PopupWindow
 *  @author: East
 *  @date: 2019-12-17 10:53
 * |---------------------------------------------------------------------------------------------------------------|
 */
public class CommonPopupWindow extends PopupWindow {
    final PopupController controller;

    @Override
    public int getWidth() {
        return controller.mPopupView.getMeasuredWidth();
    }

    @Override
    public int getHeight() {
        return controller.mPopupView.getMeasuredHeight();
    }

    private CommonPopupWindow(Context context) {
        controller = new PopupController(context, this);
    }

    /**
     * 获取View
     */
    public <T extends View> T getView(int viewId) {
        return controller.getViewHelper().getView(viewId);
    }


    /**
     * 显示在View的上方并居中对齐，前提是Width是WrapContent
     */
    public void showUpWithViewCenter(View view){
        showAsDropDown(view,(view.getMeasuredWidth()-getWidth())/2,-view.getMeasuredHeight()-getHeight());
//        int[] location = new  int[2] ;
//        view.getLocationOnScreen(location);
//        int measuredWidth = view.getMeasuredWidth();
//        int width1 = getWidth();
//        showAtLocation(view, Gravity.NO_GRAVITY,location[0]+(measuredWidth-width1)/2,location[1]-getHeight());
    }

    /**
     * 显示在View的下方并居中对齐，前提是Width是WrapContent
     */
    public void showDownWithViewCenter(View view){
        showAsDropDown(view,(view.getMeasuredWidth()-getWidth())/2,0);
//        int[] location = new  int[2] ;
//        view.getLocationOnScreen(location);
//        int measuredWidth = view.getMeasuredWidth();
//        int width1 = getWidth();
//        showAtLocation(view, Gravity.NO_GRAVITY,location[0]+(measuredWidth-width1)/2,location[1]+view.getMeasuredHeight());
    }

    /**
     * 显示在View的右边并居中对齐，前提是Width是WrapContent
     */
    public void showRightWithViewCenter(View view){
        showAsDropDown(view, view.getWidth(), (view.getMeasuredHeight() - getHeight())/2-view.getMeasuredHeight());
//        int[] location = new  int[2] ;
//        view.getLocationOnScreen(location);
//        int measuredWidth = view.getMeasuredWidth();
//        int width1 = getWidth();
//        showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getMeasuredWidth(), location[1] + (view.getMeasuredHeight() - getHeight()) / 2);
    }

    /**
     * 显示在View的左边并居中对齐，前提是Width是WrapContent
     */
    public void showLeftWithViewCenter(View view){
        showAsDropDown(view, -getWidth(), (view.getMeasuredHeight() - getHeight())/2-view.getMeasuredHeight());
//        int[] location = new  int[2] ;
//        view.getLocationOnScreen(location);
//        int measuredWidth = view.getMeasuredWidth();
//        int width1 = getWidth();
//        showAtLocation(view, Gravity.NO_GRAVITY, location[0] - getWidth(), location[1] + (view.getMeasuredHeight() - getHeight()) / 2);
    }



    @Override
    public void dismiss() {
        super.dismiss();
        controller.setBackGroundLevel(1.0f);
    }

    public static class Builder {
        private final PopupController.PopupParams params;

        public Builder(Context context) {
            params = new PopupController.PopupParams(context);
        }

        /**
         * @param layoutResId 设置PopupWindow 布局ID
         */
        public Builder setView(int layoutResId) {
            params.mView = null;
            params.layoutResId = layoutResId;
            return this;
        }

        /**
         * @param view 设置PopupWindow布局
         */
        public Builder setView(View view) {
            params.mView = view;
            params.layoutResId = 0;
            return this;
        }

        // 宽度占满屏幕
        public Builder fullWidth(){
            params.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }


        /**
         * 设置宽度和高度 如果不设置 默认是wrap_content
         */
        public Builder setWidthAndHeight(int width, int height) {
            params.mWidth = width;
            params.mHeight = height;
            return this;
        }

        /**
         * 设置背景灰色程度
         * @param level 0.0f-1.0f
         */
        public Builder setBackGroundLevel(float level) {
            params.mBgLevel = level;
            return this;
        }

        /**
         * 是否可点击Outside消失
         * @param touchable 是否可点击
         */
        public Builder setOutsideTouchable(boolean touchable) {
            params.isOutsideTouchable = touchable;
            return this;
        }

        /**
         * 取消默认动画
         */
        public Builder clearDefaultAnimationStyle() {
            params.mAnimation = 0;
            return this;
        }

        /**
         * 设置动画
         */
        public Builder setAnimationStyle(int animationStyle) {
            params.mAnimation = animationStyle;
            return this;
        }

        //dialog Dismiss监听
        public Builder setOnDismissListener(@Nullable OnDismissListener onDismissListener) {
            params.mOnDismissListener = onDismissListener;
            return this;
        }

        //设置文本
        public Builder setText(int viewId, CharSequence text) {
            params.mTextArray.put(viewId, text);
            return this;
        }


        //设置文本和监听事件
        public Builder setText(int viewId,CharSequence text,@Nullable View.OnClickListener onClickListener){
            params.mTextArray.put(viewId,text);
            return setOnClickListener(viewId,onClickListener);
        }

        //设置图片
        public Builder setImageResource(int viewId,int imageResourceId){
            params.mImageArray.put(viewId,imageResourceId);
            return this;
        }


        //设置图片
        public Builder setImageBitmap(int viewId, Bitmap bitmap){
            params.mBitmapArray.put(viewId,bitmap);
            return this;
        }

        //如果设置点击事件的间隔
        public Builder setClickThrottleTime(Long timeMillisecond) {
            params.mTimeMillisecond = timeMillisecond;
            return this;
        }

        //设置点击事件
        public Builder setOnClickListener(int viewId, @Nullable View.OnClickListener onClickListener) {
            return setOnClickListener(true, viewId, onClickListener);
        }

        public Builder setOnClickListener(boolean throttle, int viewId, @Nullable View.OnClickListener onClickListener) {
            ClickEntry clickEntry = new ClickEntry(onClickListener, throttle);
            params.mClickArray.put(viewId, clickEntry);
            return this;
        }

        //设置点击事件
        public Builder setOnLongClickListener(int viewId, @Nullable View.OnLongClickListener onLongClickListener) {
            params.mLongClickArray.put(viewId, onLongClickListener);
            return this;
        }

        public CommonPopupWindow create() {
            final CommonPopupWindow popupWindow = new CommonPopupWindow(params.mContextReference.get());
            params.apply(popupWindow.controller);
            if(params.mOnDismissListener!=null)
                popupWindow.setOnDismissListener(params.mOnDismissListener);
            measureWidthAndHeight(popupWindow.controller.mPopupView);
            return popupWindow;
        }
    }

    public static void measureWidthAndHeight(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }
}
