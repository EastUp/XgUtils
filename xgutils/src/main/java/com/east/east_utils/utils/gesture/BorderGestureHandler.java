package com.east.east_utils.utils.gesture;

import android.graphics.PointF;
import android.view.MotionEvent;

import com.east.east_utils.utils.DisplayUtil;
import com.east.east_utils.utils.Utils;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 *  @description:边缘手势滑动处理
 *  @author: East
 *  @date: 2019-10-12
 * |---------------------------------------------------------------------------------------------------------------|
 */
public class BorderGestureHandler {

    private BorderSliderMethod borderSlider;
    //屏幕宽高
    int sWidth = DisplayUtil.getScreenWidth(Utils.getApp());
    int sHeight = DisplayUtil.getScreenHeight(Utils.getApp());
    //按下的点
    PointF down;
    //按下时的时间
    long downTime;
    //边缘判定距离，
    double marginX = sWidth * 0.035;
    double marginY = sHeight * 0.035;
    //边缘滑动触发的最短滑动距离
    double minSlideDistance = Math.min(sHeight,sWidth) * 0.05;
    //是否处于此次滑动事件(workHorizontal:水平边缘,workVertical:竖直边缘)
    boolean workHorizontal = false,workVertical = false;

    public BorderGestureHandler(BorderSliderMethod borderSlider) {
        this.borderSlider = borderSlider;
    }

    public boolean doEventFling(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //记录下按下的点
                downTime = System.currentTimeMillis();
                down = new PointF(event.getX(), event.getY());
                //判定是否处于边缘侧滑
                if (down.x < marginX || (sWidth - down.x) < marginX) workHorizontal = true;
                else workHorizontal=false;
                if(down.y < marginY || (sHeight-down.y) < marginY) workVertical = true;
                else workVertical = false;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                PointF up = new PointF(event.getX(), event.getY());
                if (workHorizontal || workVertical) {
                    handle(up);
                    return true;
                }else{
                   return borderSlider.other(down,up);
                }
        }
        //如果当前处于边缘滑动判定过程中，则消费掉此事件不往下传递。
        return workHorizontal||workVertical;
    }

    public boolean handle(PointF up) {
        long upTime = System.currentTimeMillis();
        float tWidth = Math.abs(down.x - up.x);
        float tHeight = Math.abs(down.y - up.y);
        if (workHorizontal && tWidth > minSlideDistance && (upTime - downTime)  < 1500) {
            //起点在左边
            if (down.x < marginX) {
                borderSlider.left();
                return true;
            }
            //起点在右边
            if ((sWidth - down.x) < marginX) {
                borderSlider.right();
                return true;
            }
        }else if(workVertical && tHeight > minSlideDistance && (upTime - downTime)  < 1500){
            //起点在上
            if (down.y < marginY) {
                borderSlider.top();
                return true;
            }
            //起点在下
            if ((sHeight - down.y) < marginY) {
                borderSlider.bottom();
                return true;
            }
        }
        return false;
    }


    /**
     *  边缘滑动触发
     */
    public interface BorderSliderMethod {
        default void left() { }
        default void right() { }
        default void top() { }
        default void bottom() { }
        default boolean other(PointF down,PointF up){return false;}
    }
}
