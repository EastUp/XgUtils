package com.east.east_utils.widgets.headerbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.east.east_utils.widgets.common.ClickEntry;


/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 *  @description:  通用的头部控制，是添加到Parent的第一个位置，需要加上margin
 *  @author: jamin
 *  @date: 2020/5/15
 * |---------------------------------------------------------------------------------------------------------------|
 */
public class CommonHeaderBar {

    public static int mDefaultLayoutId = 0; //设置默认的Header布局Id

    private HeaderController mHeaderController;

    public CommonHeaderBar() {
        mHeaderController = new HeaderController();
    }

    /**
     * 获取View
     */
    public <T extends View> T getView(int viewId) {
        return mHeaderController.getViewHelper().getView(viewId);
    }

    public static class Builder{
        private final HeaderController.HeaderParams P;

        /**
         *  如果传入的是Activity的话，可以直接通过获取Activity的根视图进行添加
         */
        public Builder(Activity activity){
            this(activity,null);
        }

        public Builder(Context context, ViewGroup parent) {
            P = new HeaderController.HeaderParams(context,parent);
        }

        //设置布局View
        public Builder setContentView(View view){
            P.mView = view;
            P.mViewLayoutResId = 0;
            return this;
        }

        //设置布局View的LayoutId
        public Builder bindLayoutId(int layoutId){
            P.mView = null;
            P.mViewLayoutResId = layoutId;
            return this;
        }

        //设置文本
        public Builder setText(int viewId,CharSequence text){
            P.mTextArray.put(viewId,text);
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
        public Builder setClickThrottleTime(Long timeMillisecond){
            P.mTimeMillisecond = timeMillisecond;
            return this;
        }

        //设置点击事件
        public Builder setOnClickListener(int viewId, @Nullable View.OnClickListener onClickListener){
            return setOnClickListener(true,viewId,onClickListener);
        }

        public Builder setOnClickListener(boolean throttle,int viewId, @Nullable View.OnClickListener onClickListener){
            ClickEntry clickEntry = new ClickEntry(onClickListener,throttle);
            P.mClickArray.put(viewId,clickEntry);
            return this;
        }

        //设置点击事件
        public Builder setOnLongClickListener(int viewId, @Nullable View.OnLongClickListener onLongClickListener){
            P.mLongClickArray.put(viewId,onLongClickListener);
            return this;
        }

        /**
         * 构建dialog
         * @return
         */
        public CommonHeaderBar create(){
            final CommonHeaderBar headerBar = new CommonHeaderBar();
            P.apply(headerBar.mHeaderController);
            return headerBar;
        }

    }
}
