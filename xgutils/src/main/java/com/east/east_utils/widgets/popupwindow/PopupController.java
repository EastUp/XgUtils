package com.east.east_utils.widgets.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.east.east_utils.R;
import com.east.east_utils.widgets.common.ClickEntry;
import com.east.east_utils.widgets.common.ViewHelper;

import java.lang.ref.WeakReference;

/**
 * Created by MQ on 2017/5/2.
 */

class PopupController {
    private Context context;
    private PopupWindow popupWindow;
    private Window mWindow;
    View mPopupView;//弹窗布局View
    //Dialog中的ContentView辅助类（用于查找viewId设置onClickListener之类）
    private ViewHelper mViewHelper;

    PopupController(Context context, PopupWindow popupWindow) {
        this.context = context;
        this.popupWindow = popupWindow;
        mViewHelper = new ViewHelper();
    }


    public ViewHelper getViewHelper() {
        return mViewHelper;
    }

    public void setView(View view) {
        mPopupView = view;
        popupWindow.setContentView(view);
    }

    /**
     * 设置宽度
     */
    private void setWidthAndHeight(int width, int height) {
        if (width == 0 || height == 0) {
            //如果没设置宽高，默认是WRAP_CONTENT
            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            popupWindow.setWidth(width);
            popupWindow.setHeight(height);
        }
    }


    /**
     * 设置背景灰色程度
     * @param level 0.0f-1.0f
     */
    void setBackGroundLevel(float level) {
        mWindow = ((Activity) context).getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.alpha = level;
        mWindow.setAttributes(params);
    }


    /**
     * 设置动画
     */
    private void setAnimationStyle(int animationStyle) {
        popupWindow.setAnimationStyle(animationStyle);
    }

    /**
     * 设置Outside是否可点击
     * @param touchable 是否可点击
     */
    private void setOutsideTouchable(boolean touchable) {
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));//设置透明背景
        popupWindow.setOutsideTouchable(touchable);//设置outside可点击
        popupWindow.setFocusable(touchable);
    }




    static class PopupParams {
        public int layoutResId;//布局id
        public WeakReference<Context> mContextReference;
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;  // 宽度
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;// 高度
        public float mBgLevel = -1f;//屏幕背景灰色程度
        public int mAnimation = R.style.dialog_scale_anim;// 默认弹出动画
        public View mView;//布局View
        public boolean isOutsideTouchable = true;//设置outside可点击
        public PopupWindow.OnDismissListener mOnDismissListener;//设置消失的监听

        public Long mTimeMillisecond = 1000L;//点击事件的间隔,默认为1秒
        public SparseArray<CharSequence> mTextArray = new SparseArray<>(); //设置控件的文字显示
        public SparseArray<Integer> mImageArray = new SparseArray<>(); //设置控件的图片显示
        public SparseArray<Bitmap> mBitmapArray = new SparseArray<>(); //设置控件的图片显示
        public SparseArray<ClickEntry> mClickArray = new SparseArray<>(); //设置控件的点击事件,包括是否限制快速点击
        public SparseArray<View.OnLongClickListener> mLongClickArray = new SparseArray<>(); //设置控件的长按点击事件

        public PopupParams(Context context) {
            mContextReference = new WeakReference<>(context);
        }

        public void apply(PopupController controller) {
            // 1.设置popupwindow 布局
            if (layoutResId != 0 && mView == null) {
                if (mContextReference != null && mContextReference.get() != null)
                    mView = LayoutInflater.from(mContextReference.get()).inflate(layoutResId, null);
            }

            //如果mView为null 证明没有设置ContentView
            if (mView == null)
                throw new IllegalArgumentException("PopupView's contentView is null");

            controller.getViewHelper().setContentView(mView);

            // 2.给popupwindow 设置布局
            controller.setView(mView);

            // 3.设置popupwindow是否可以点击外部消失
            controller.setOutsideTouchable(isOutsideTouchable);//设置outside可点击

            // 4. 设置背景的灰色透明度  范围是 （0f-1f）
            if (mBgLevel >0.0f && mBgLevel<1.0f) {
                //设置背景
                controller.setBackGroundLevel(mBgLevel);
            }

            // 5. 设置动画
            if (mAnimation!=0) {
                controller.setAnimationStyle(mAnimation);
            }

            // 6.设置文本
            for (int i = 0; i < mTextArray.size(); i++) {
                controller.getViewHelper().setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }

            // 7.设置图片
            for (int i = 0; i < mImageArray.size(); i++) {
                controller.getViewHelper().setImageResource(mImageArray.keyAt(i), mImageArray.valueAt(i));
            }

            for (int i = 0; i < mBitmapArray.size(); i++) {
                controller.getViewHelper().setImageBitmap(mBitmapArray.keyAt(i), mBitmapArray.valueAt(i));
            }

            // 8.设置点击事件
            for (int i = 0; i < mClickArray.size(); i++) {
                controller.getViewHelper().setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i), mTimeMillisecond);
            }


            // 9.设置长按点击事件
            for (int i = 0; i < mLongClickArray.size(); i++) {
                controller.getViewHelper().setOnLongClickListener(mLongClickArray.keyAt(i), mLongClickArray.valueAt(i));
            }


            // 10.设置宽度和高度
            controller.setWidthAndHeight(mWidth, mHeight);


        }
    }
}
