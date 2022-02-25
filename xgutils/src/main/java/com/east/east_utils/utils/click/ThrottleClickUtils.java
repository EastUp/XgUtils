package com.east.east_utils.utils.click;

import android.os.SystemClock;
import android.view.View;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 *  @description: 防止快速点击
 *  @author: jamin
 *  @date: 2020/4/17
 * |---------------------------------------------------------------------------------------------------------------|
 */
public class ThrottleClickUtils {
    /**
     * 最后一次点击的时间
     */
    private Long mLastClickTime = 0L;

    /**
     * 点击间隔时间多少毫秒
     */
    private Long mThrottleTime = 1000L;

    /**
     * 点击的View
     */
    private View mClickView;

    private ThrottleClickUtils() {
    }


    /**
     *  绑定View
     * @param view
     * @return
     */
    public static ThrottleClickUtils bind(View view){
        //这里不能涉及成单例模式，是因为如果绑定了多个View，之前绑定View的mThrottleTime可能会发生变化。
        ThrottleClickUtils instance = new ThrottleClickUtils();
        instance.mClickView = view;
        return instance;
    }

    /**
     *  设置多少秒点击才响应
     */
    public ThrottleClickUtils throttleTime(Long throttleTime){
        this.mThrottleTime = throttleTime;
        return this;
    }

    /**
     * 限制快速的多次点击
     */
    public void throttleClick(View.OnClickListener clickListener){
       if(mClickView == null)
           throw new RuntimeException("no bind view");

        mClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用：System.currentTimeMillis()用于和日期相关的地方，比如日志。SystemClock.elapsedRealtime()用于某个事件经历的时间，比如两次点击的时间间隔。
                long time = SystemClock.elapsedRealtime();
                long timeInterval = Math.abs(time - mLastClickTime);
                if(timeInterval >= mThrottleTime){
                    clickListener.onClick(v);
                    mLastClickTime = time;
                }
            }
        });

    }



}
