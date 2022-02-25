package com.east.east_utils.utils.click;

import android.view.View;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *  @description:  Click的工具类
 *  @author: East
 *  @date: 2019-12-30 18:10
 * |---------------------------------------------------------------------------------------------------------------|
 */
public class ClickCountUtils {

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;

    static long mLastClickTime; //上一次点击的事件

    static int count = 0;//点击view记录的次数

    /**
     * 点击次数监听
     */
    public interface OnCountClickListener {
        void onCountClickListener();
    }

    /**
     * @param view               监听点击视图的View
     * @param clickCount         连续点击多少次才能触发监听
     * @param countClickListener 监听
     */
    public static void setOnCountClickListener(View view, final int clickCount, final OnCountClickListener countClickListener) {
        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((System.currentTimeMillis() - mLastClickTime < 800) || mLastClickTime == 0) {
                    count++;
                    if (count == clickCount) {
                        countClickListener.onCountClickListener();
                        count = 0;
                        mLastClickTime = 0;
                    }
                } else{
                    count = 1;
                }
                mLastClickTime = System.currentTimeMillis();
            }
        };
        view.setOnClickListener(mOnClickListener);
    }

    /**
     *  判断是否是快速的点击
     * @return
     */
    public static boolean notQuickClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - mLastClickTime) >= MIN_CLICK_DELAY_TIME) {
            mLastClickTime = curClickTime;
            flag = true;
        }
        return flag;
    }


}
