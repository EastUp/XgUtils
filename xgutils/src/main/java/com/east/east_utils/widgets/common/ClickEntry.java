package com.east.east_utils.widgets.common;

import android.view.View;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 *  @description: 点击事件对象
 *  @author: jamin
 *  @date: 2020/5/13
 * |---------------------------------------------------------------------------------------------------------------|
 */
public class ClickEntry {
    private View.OnClickListener onClickListener;  //设置控件的点击事件
    private boolean throttleClick = false;  ////设置控件的点击事件是否必须间隔

    public ClickEntry(View.OnClickListener onClickListener, boolean throttleClick) {
        this.onClickListener = onClickListener;
        this.throttleClick = throttleClick;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public boolean isThrottleClick() {
        return throttleClick;
    }

    public void setThrottleClick(boolean throttleClick) {
        this.throttleClick = throttleClick;
    }
}
