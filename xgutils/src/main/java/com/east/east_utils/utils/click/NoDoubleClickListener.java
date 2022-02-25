package com.east.east_utils.utils.click;

import android.view.View;

import java.util.Calendar;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @description: 防止快速点击按钮两次造成问题的ClickListener
 * @author: East
 * @date: 2019-12-30
 * |---------------------------------------------------------------------------------------------------------------|
 */
public abstract class NoDoubleClickListener implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    protected abstract void onNoDoubleClick(View v);

}
