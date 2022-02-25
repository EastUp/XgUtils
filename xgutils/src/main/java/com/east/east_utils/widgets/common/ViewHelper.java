package com.east.east_utils.widgets.common;

import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.east.east_utils.utils.click.ThrottleClickUtils;

import java.lang.ref.WeakReference;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 *  @description: View的辅助处理类
 *  @author: jamin
 *  @date: 2020/5/13
 * |---------------------------------------------------------------------------------------------------------------|
 */
public class ViewHelper {

    private View mContentView;

    //防止内存泄露
    private SparseArray<WeakReference<View>> mViews = new SparseArray<>();

    public void setContentView(View view) {
        mContentView = view;
    }

    /**
     * 设置文本
     */
    public void setText(int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.setText(text);
    }

    /**
     * 设置图片
     */
    public void setImageResource(int viewId, Integer imageResourceId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResourceId);
    }


    /**
     * 设置图片
     */
    public void setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
    }


    /**
     * 设置点击事件
     */
    public void setOnClickListener(int viewId, ClickEntry clickEntry, Long mTimeMillisecond) {
        View view = getView(viewId);
        if (clickEntry.isThrottleClick()) {
            ThrottleClickUtils.bind(view).throttleTime(mTimeMillisecond).throttleClick(clickEntry.getOnClickListener());
        } else
            view.setOnClickListener(clickEntry.getOnClickListener());
    }

    /**
     * 设置长按事件
     */
    public void setOnLongClickListener(int viewId, View.OnLongClickListener onLongClickListener) {
        getView(viewId).setOnLongClickListener(onLongClickListener);
    }


    /**
     * 获取View
     */
    public <T extends View> T getView(int viewId) {
        WeakReference<View> viewWeakReference = mViews.get(viewId);
        View view = null;
        if (viewWeakReference != null) {
            view = viewWeakReference.get();
        }

        if (view == null) {
            view = mContentView.findViewById(viewId);
            if (view != null)
                mViews.put(viewId, new WeakReference<>(view));
        }
        return (T) view;
    }


}
