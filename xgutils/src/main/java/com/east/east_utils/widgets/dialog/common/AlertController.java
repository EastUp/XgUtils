package com.east.east_utils.widgets.dialog.common;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.east.east_utils.R;
import com.east.east_utils.widgets.common.ClickEntry;
import com.east.east_utils.widgets.common.ViewHelper;

import java.lang.ref.WeakReference;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @description: Dialog的控制类
 * @author: jamin
 * @date: 2020/5/13
 * |---------------------------------------------------------------------------------------------------------------|
 */
class AlertController {

    private CommonDialog mDialog;
    private Window mWindow;

    //Dialog中的ContentView辅助类（用于查找viewId设置onClickListener之类）
    private ViewHelper mViewHelper;

    public AlertController(CommonDialog commonDialog, Window window) {
        this.mDialog = commonDialog;
        this.mWindow = window;
        mViewHelper = new ViewHelper();
    }

    public CommonDialog getDialog() {
        return mDialog;
    }

    public Window getWindow() {
        return mWindow;
    }

    public ViewHelper getViewHelper() {
        return mViewHelper;
    }

    /**
     * Dialog的参数
     */
    public static class AlertParams {

        //防止内存泄露
        public WeakReference<Context> mContextReference;
        public int mThemeResId; //dialog的style
        public boolean mCancelable = true; //是否可以消失
        public boolean mCanceledOnTouchOutside;// 点击dialog外面是否可以取消，必须要mCancelable为true才有效
        public View mView; //布局View
        public int mViewLayoutResId;//布局的LayoutId
        public Long mTimeMillisecond = 1000L;//点击事件的间隔,默认为1秒
        public SparseArray<CharSequence> mTextArray = new SparseArray<>(); //设置控件的文字显示
        public SparseArray<Integer> mImageArray = new SparseArray<>(); //设置控件的图片显示
        public SparseArray<Bitmap> mBitmapArray = new SparseArray<>(); //设置控件的图片显示
        public SparseArray<ClickEntry> mClickArray = new SparseArray<>(); //设置控件的点击事件,包括是否限制快速点击
        public SparseArray<View.OnLongClickListener> mLongClickArray = new SparseArray<>(); //设置控件的长按点击事件
        public DialogInterface.OnCancelListener mOnCancelListener; // dialog Cancel监听
        public DialogInterface.OnDismissListener mOnDismissListener;// dialog Dismiss监听
        public DialogInterface.OnKeyListener mOnKeyListener;// dialog Key监听

        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;  // 宽度
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;// 高度
        public float mDimAmount = -1f; // 背景的透明   0.0f~1.0f 1.0f为完全不透明
        public int mAnimation = R.style.dialog_scale_anim;// 默认动画

        public int mGravity = Gravity.CENTER;// 位置


        public AlertParams(Context context, int themeResId) {
            mContextReference = new WeakReference<>(context);
            mThemeResId = themeResId;
        }


        /**
         * 绑定和设置参数
         */
        public void apply(AlertController mAlert) {


            // 1.设置Dialog 布局
            if (mViewLayoutResId != 0 && mView == null) {
                if (mContextReference != null && mContextReference.get() != null)
                    mView = LayoutInflater.from(mContextReference.get()).inflate(mViewLayoutResId, null);
            }
            //如果mView为null 证明没有设置ContentView
            if (mView == null)
                throw new IllegalArgumentException("请显示设置CommonDialog的布局：setContentView(..)");

            mAlert.getViewHelper().setContentView(mView);

            // 2.给Dialog 设置布局
            mAlert.getDialog().setContentView(mView);

            // 3.设置文本
            for (int i = 0; i < mTextArray.size(); i++) {
                mAlert.getViewHelper().setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }

            // 4.设置图片
            for (int i = 0; i < mImageArray.size(); i++) {
                mAlert.getViewHelper().setImageResource(mImageArray.keyAt(i), mImageArray.valueAt(i));
            }

            for (int i = 0; i < mBitmapArray.size(); i++) {
                mAlert.getViewHelper().setImageBitmap(mBitmapArray.keyAt(i), mBitmapArray.valueAt(i));
            }

            // 5.设置点击事件
            for (int i = 0; i < mClickArray.size(); i++) {
                mAlert.getViewHelper().setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i), mTimeMillisecond);
            }


            // 6.设置长按点击事件
            for (int i = 0; i < mLongClickArray.size(); i++) {
                mAlert.getViewHelper().setOnLongClickListener(mLongClickArray.keyAt(i), mLongClickArray.valueAt(i));
            }

            // 7.设置Dialog的宽高
            Window window = mAlert.getWindow();

            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = mWidth;
            layoutParams.height = mHeight;
            if (mDimAmount >= 0)
                layoutParams.dimAmount = mDimAmount;
            window.setAttributes(layoutParams);
            window.getDecorView().setPadding(0, 0, 0, 0); //设置边距
            // 8. 设置动画
            if (mAnimation != 0)
                window.setWindowAnimations(mAnimation);

            // 9.设置位置
            window.setGravity(mGravity);

        }
    }
}
