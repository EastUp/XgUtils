package com.east.east_utils.widgets.headerbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.east.east_utils.widgets.common.ClickEntry;
import com.east.east_utils.widgets.common.ViewHelper;

import java.lang.ref.WeakReference;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 *  @description:  HeaderBar的控制类
 *  @author: jamin
 *  @date: 2020/5/15
 * |---------------------------------------------------------------------------------------------------------------|
 */
public class HeaderController {


    //Dialog中的ContentView辅助类（用于查找viewId设置onClickListener之类）
    private ViewHelper mViewHelper;

    public HeaderController() {
        mViewHelper = new ViewHelper();
    }
    public ViewHelper getViewHelper() {
        return mViewHelper;
    }




    public static class HeaderParams{

        public WeakReference<Context> mContextReference;
        public ViewGroup mParent;

        public View mView; //布局View
        public int mViewLayoutResId;//布局的LayoutId
        public Long mTimeMillisecond = 1000L;//点击事件的间隔,默认为1秒
        public SparseArray<CharSequence> mTextArray = new SparseArray<>(); //设置控件的文字显示
        public SparseArray<Integer> mImageArray = new SparseArray<>(); //设置控件的图片显示
        public SparseArray<Bitmap> mBitmapArray = new SparseArray<>(); //设置控件的图片显示
        public SparseArray<ClickEntry> mClickArray = new SparseArray<>(); //设置控件的点击事件,包括是否限制快速点击
        public SparseArray<View.OnLongClickListener> mLongClickArray = new SparseArray<>(); //设置控件的长按点击事件

        public HeaderParams(Context context, ViewGroup parent) {
            this.mContextReference = new WeakReference<>(context);
            this.mParent = parent;
        }

        /**
         * 绑定HeaderBar参数
         */
        public void apply(HeaderController  headerController) {

            // 1.设置Header 布局并添加到Parent中
            if(mParent == null){
                if(mContextReference == null || mContextReference.get() == null)
                    return;

                if(!(mContextReference.get() instanceof Activity))
                    throw new IllegalArgumentException("viewGroup为null的情况下,context只能传Activity");

                // 获取activity的根布局，View源码
                ViewGroup activityRoot = (ViewGroup) ((Activity)(mContextReference.get()))
                        .getWindow().getDecorView();  //DecorView是FrameLayout
                mParent = (ViewGroup) activityRoot.getChildAt(0);  //获取到的是LinearLayout
                //android.R.id.content获取到的是LinearLayout中的FrameLayout
            }

            // 处理Activity的源码，后面再去看
            if(mParent == null){
                return;
            }

            if(mViewLayoutResId == 0)
                mViewLayoutResId = CommonHeaderBar.mDefaultLayoutId;

            if (mViewLayoutResId != 0 && mView == null) {
                if (mContextReference != null && mContextReference.get() != null)
                    mView = LayoutInflater.from(mContextReference.get()).inflate(mViewLayoutResId, mParent,false);
            }

            //如果mView为null 证明没有设置ContentView
            if (mView == null)
                throw new IllegalArgumentException("请设置CommonHeaderBar的布局");

            headerController.getViewHelper().setContentView(mView);

            // 添加View到Parent中
            mParent.addView(mView,0);


            // 2.设置文本
            for (int i = 0; i < mTextArray.size(); i++) {
                headerController.getViewHelper().setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }

            // 4.设置图片
            for (int i = 0; i < mImageArray.size(); i++) {
                headerController.getViewHelper().setImageResource(mImageArray.keyAt(i), mImageArray.valueAt(i));
            }

            for (int i = 0; i < mBitmapArray.size(); i++) {
                headerController.getViewHelper().setImageBitmap(mBitmapArray.keyAt(i), mBitmapArray.valueAt(i));
            }


            // 5.设置点击事件
            for (int i = 0; i < mClickArray.size(); i++) {
                headerController.getViewHelper().setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i), mTimeMillisecond);
            }


            // 6.设置长按点击事件
            for (int i = 0; i < mLongClickArray.size(); i++) {
                headerController.getViewHelper().setOnLongClickListener(mLongClickArray.keyAt(i), mLongClickArray.valueAt(i));
            }


        }
    }
}
