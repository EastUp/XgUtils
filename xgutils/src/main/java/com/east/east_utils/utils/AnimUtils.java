package com.east.east_utils.utils;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 描述：动画工具类
 * Created by East at 2018/6/5 17:52
 */
public class AnimUtils {

    /**
     * View Animation实现的抖动动画  startShakeByViewAnim(mIvShake, 0.9f, 1.1f, 10f, 1000);
     * @param view
     * @param scaleSmall
     * @param scaleLarge
     * @param shakeDegrees
     * @param duration
     */
    public static void startShakeByViewAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }

        //由小变大
        Animation scaleAnim = new ScaleAnimation(scaleSmall, scaleLarge, scaleSmall, scaleLarge);
        //从左向右
        Animation rotateAnim = new RotateAnimation(-shakeDegrees, shakeDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnim.setDuration(duration);
        rotateAnim.setDuration(duration / 10);
        rotateAnim.setRepeatMode(Animation.REVERSE);
        rotateAnim.setRepeatCount(10);

        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(scaleAnim);
        smallAnimationSet.addAnimation(rotateAnim);

        view.startAnimation(smallAnimationSet);
    }

    /**
     *  属性动画实现的抖动   startShakeByPropertyAnim(mIvShake, 0.9f, 1.1f, 10f, 1000);
     * @param view
     * @param scaleSmall
     * @param scaleLarge
     * @param shakeDegrees
     * @param duration
     */
    public static void startShakeByPropertyAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );

        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    /**
     * 抖动动画，左右抖动
     * @param context 上下文
     * @param v 要执行动画的view
     */
    public static void shake(Context context, View v){
        Animation shake = new TranslateAnimation(0, 10, 0, 0);//移动方向
        shake.setDuration(1000);//执行总时间
        shake.setInterpolator(new CycleInterpolator(7));//循环次数
        v.startAnimation(shake);
    }

    /**
     * 缩放动画，按下时缩放，抬起时恢复
     * @param v 要执行动画的view
     * @param event 触摸事件
     * @param listener 点击事件
     * @return  触摸结果
     */
    public static boolean setClickAnim(View v, MotionEvent event, View.OnClickListener listener){
        float start = 1.0f;
        float end = 0.95f;
        Animation scaleAnimation = new ScaleAnimation(start, end, start, end,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        Animation endAnimation = new ScaleAnimation(end, start, end, start,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnimation.setDuration(200);
        scaleAnimation.setFillAfter(true);
        endAnimation.setDuration(200);
        endAnimation.setFillAfter(true);
        switch (event.getAction()) {
            // 按下时调用
            case MotionEvent.ACTION_DOWN:
                v.startAnimation(scaleAnimation);
                v.invalidate();
                break;
            // 抬起时调用
            case MotionEvent.ACTION_UP:
                v.startAnimation(endAnimation);
                v.invalidate();
                if(listener!=null){
                    listener.onClick(v);
                }
                break;
            // 滑动出去不会调用action_up,调用action_cancel
            case MotionEvent.ACTION_CANCEL:
                v.startAnimation(endAnimation);
                v.invalidate();
                break;
        }
        // 不返回true，Action_up就响应不了
        return true;
    }

    /** 定义动画的时间. */
    public final static long aniDurationMillis = 1L;

    /**
     * 用来改变当前选中区域的放大动画效果 从1.0f放大1.2f倍数
     *
     * @param view
     *          the view
     * @param scale
     *          the scale
     */
    public static void largerView(View view, float scale) {
        if (view == null)
            return;

        // 置于所有view最上层
        view.bringToFront();
        int width = view.getWidth();
        float animationSize = 1 + scale / width;
        scaleView(view, animationSize);
    }

    /**
     * 用来还原当前选中区域的还原动画效果.
     *
     * @param view
     *          the view
     * @param scale
     *          the scale
     */
    public static void restoreLargerView(View view, float scale) {
        if (view == null)
            return;
        int width = view.getWidth();
        float toSize = 1 + scale / width;
        // 从1.2f缩小1.0f倍数
        scaleView(view, -1 * toSize);
    }

    /**
     * 缩放View的显示.
     *
     * @param view
     *          需要改变的View
     * @param toSize
     *          缩放的大小，其中正值代表放大，负值代表缩小，数值代表缩放的倍数
     */
    private static void scaleView(final View view, float toSize) {
        ScaleAnimation scale = null;
        if (toSize == 0) {
            return;
        } else if (toSize > 0) {
            scale = new ScaleAnimation(1.0f, toSize, 1.0f, toSize, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            scale = new ScaleAnimation(toSize * (-1), 1.0f, toSize * (-1), 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
        }
        scale.setDuration(aniDurationMillis);
        scale.setInterpolator(new AccelerateDecelerateInterpolator());
        scale.setFillAfter(true);
        view.startAnimation(scale);
    }

    /**
     * 跳动-跳起动画.
     *
     * @param view
     *          the view
     * @param offsetY
     *          the offset y
     */
    private void playJumpAnimation(final View view, final float offsetY) {
        float originalY = 0;
        float finalY = -offsetY;
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new TranslateAnimation(0, 0, originalY, finalY));
        animationSet.setDuration(300);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.setFillAfter(true);

        animationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                playLandAnimation(view, offsetY);
            }
        });

        view.startAnimation(animationSet);
    }

    /**
     * 跳动-落下动画.
     *
     * @param view
     *          the view
     * @param offsetY
     *          the offset y
     */
    private void playLandAnimation(final View view, final float offsetY) {
        float originalY = -offsetY;
        float finalY = 0;
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new TranslateAnimation(0, 0, originalY, finalY));
        animationSet.setDuration(200);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setFillAfter(true);

        animationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 两秒后再调
                view.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        playJumpAnimation(view, offsetY);
                    }
                }, 2000);
            }
        });

        view.startAnimation(animationSet);
    }

    /**
     * 旋转动画
     *
     * @param v
     * @param durationMillis
     * @param repeatCount
     *          Animation.INFINITE
     * @param repeatMode
     *          Animation.RESTART
     */
    public static void playRotateAnimation(View v, long durationMillis, int repeatCount, int repeatMode) {

        // 创建AnimationSet对象
        AnimationSet animationSet = new AnimationSet(true);
        // 创建RotateAnimation对象
        RotateAnimation rotateAnimation = new RotateAnimation(0f, +360f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        // 设置动画持续
        rotateAnimation.setDuration(durationMillis);
        rotateAnimation.setRepeatCount(repeatCount);
        // Animation.RESTART
        rotateAnimation.setRepeatMode(repeatMode);
        // 动画插入器
        rotateAnimation.setInterpolator(v.getContext(), android.R.anim.decelerate_interpolator);
        // 添加到AnimationSet
        animationSet.addAnimation(rotateAnimation);

        // 开始动画
        v.startAnimation(animationSet);
    }

}
