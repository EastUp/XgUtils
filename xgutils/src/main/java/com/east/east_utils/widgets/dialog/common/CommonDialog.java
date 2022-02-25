package com.east.east_utils.widgets.dialog.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.east.east_utils.R;
import com.east.east_utils.widgets.common.ClickEntry;


/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @description: 万能Dialog 仿照AlertDialog
 * @author: jamin
 * @date: 2020/5/13
 * |---------------------------------------------------------------------------------------------------------------|
 */
public class CommonDialog extends Dialog {

    private AlertController mAlert;

    private CommonDialog(Context mContext, int mThemeResId) {
        super(mContext, mThemeResId);
        mAlert = new AlertController(this, getWindow());
    }

    /**
     * 获取View
     */
    public <T extends View> T getView(int viewId) {
        return mAlert.getViewHelper().getView(viewId);
    }

    public static class Builder {

        private final AlertController.AlertParams P;

        public Builder(Context context) {
            this(context, R.style.dialog);
        }

        /**
         * @param themeResId Dialog的Theme
         */
        public Builder(Context context, int themeResId) {
            this.P = new AlertController.AlertParams(context, themeResId);
        }

        //是否能够消失
        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        //是否能够消失
        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            P.mCanceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        //设置布局View
        public Builder setContentView(View view) {
            P.mView = view;
            P.mViewLayoutResId = 0;
            return this;
        }

        //设置布局View的LayoutId
        public Builder setContentView(int layoutId) {
            P.mView = null;
            P.mViewLayoutResId = layoutId;
            return this;
        }

        //设置文本
        public Builder setText(int viewId, CharSequence text) {
            P.mTextArray.put(viewId, text);
            return this;
        }

        //设置文本和监听事件
        public Builder setText(int viewId,CharSequence text,@Nullable View.OnClickListener onClickListener){
            P.mTextArray.put(viewId,text);
            return setOnClickListener(viewId,onClickListener);
        }

        //设置图片
        public Builder setImageResource(int viewId,int imageResourceId){
            P.mImageArray.put(viewId,imageResourceId);
            return this;
        }


        //设置图片
        public Builder setImageBitmap(int viewId, Bitmap bitmap){
            P.mBitmapArray.put(viewId,bitmap);
            return this;
        }

        //如果设置点击事件的间隔
        public Builder setClickThrottleTime(Long timeMillisecond) {
            P.mTimeMillisecond = timeMillisecond;
            return this;
        }

        //设置点击事件
        public Builder setOnClickListener(int viewId, @Nullable View.OnClickListener onClickListener) {
            return setOnClickListener(true, viewId, onClickListener);
        }

        public Builder setOnClickListener(boolean throttle, int viewId, @Nullable View.OnClickListener onClickListener) {
            ClickEntry clickEntry = new ClickEntry(onClickListener, throttle);
            P.mClickArray.put(viewId, clickEntry);
            return this;
        }

        //设置点击事件
        public Builder setOnLongClickListener(int viewId, @Nullable View.OnLongClickListener onLongClickListener) {
            P.mLongClickArray.put(viewId, onLongClickListener);
            return this;
        }

        // dialog Cancel监听
        public Builder setOnCancelListener(@Nullable OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        //dialog Dismiss监听
        public Builder setOnDismissListener(@Nullable OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        // dialog Key监听
        public Builder setOnKeyListener(@Nullable OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder fullWidth() {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        public Builder fullHeight() {
            P.mHeight = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        public Builder fullWidthHeight() {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            P.mHeight = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        public Builder setWidth(int width) {
            P.mWidth = width;
            return this;
        }

        public Builder setHeight(int height) {
            P.mHeight = height;
            return this;
        }

        /**
         * 设置背景色色值
         *
         * @param percentage 0.0f~1.0f 1.0f为完全不透明
         * @return Builder
         */
        public Builder setDimAmount(float percentage) {
            if(percentage < 0f)
                percentage = 0f;
            else if(percentage > 1f)
                percentage = 1f;
            P.mDimAmount = percentage;
            return this;
        }

        /**
         * 设置Gravity
         *
         * @param gravity Gravity
         * @return Builder
         */
        public Builder setGravity(int gravity) {
            P.mGravity = gravity;
            return this;
        }

        /**
         * 从底部弹出
         *
         * @param isAnimation 是否有动画
         * @return
         */
        public Builder fromBottom(boolean isAnimation) {
            if (isAnimation) {
                P.mAnimation = R.style.dialog_from_bottom_anim;
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        /**
         * 设置Dialog的宽高
         */
        public Builder setWidthAndHeight(int width, int height) {
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        /**
         * 取消默认动画
         */
        public Builder clearDefaultAnimation() {
            P.mAnimation = 0;
            return this;
        }

        /**
         * 设置动画
         */
        public Builder setAnimation(int styleAnimation) {
            P.mAnimation = styleAnimation;
            return this;
        }


        /**
         * 构建dialog
         *
         * @return
         */
        public CommonDialog create() {
            final CommonDialog dialog = new CommonDialog(P.mContextReference.get(), P.mThemeResId);
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(P.mCanceledOnTouchOutside);
            }
            if (P.mOnCancelListener != null)
                dialog.setOnCancelListener(P.mOnCancelListener);
            if (P.mOnDismissListener != null)
                dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        /**
         * 显示dialog
         *
         * @return
         */
        public CommonDialog show() {
            final CommonDialog dialog = create();
            dialog.show();
            return dialog;
        }

    }
}
