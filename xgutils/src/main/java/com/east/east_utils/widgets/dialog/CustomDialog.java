package com.east.east_utils.widgets.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.east.east_utils.R;

import java.lang.ref.WeakReference;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 *  @description: 自定义半透明dialog解决顶部状态栏的黑色背景问题,默认是居中的
 *                  initData(Window dialogWindow) 可以重新为 Dialog 设置宽高，动画，透明度等等，宽高可以是具体的值
 *  @author: East
 *  @date: 2019-10-27
 * |---------------------------------------------------------------------------------------------------------------|
 */
public abstract class CustomDialog extends Dialog {

    private int mWidth = WindowManager.LayoutParams.WRAP_CONTENT;
    private int mHeight = WindowManager.LayoutParams.WRAP_CONTENT;

    private WeakReference<Context> mContextWeakReference;

    public CustomDialog(@NonNull Context context) {
        this(context,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public CustomDialog(@NonNull Context context, int width, int height) {
        this(context, R.style.Theme_Light_NoTitle_Dialog);
        mWidth = width;
        mHeight = height;
        mContextWeakReference = new WeakReference(context);
    }

    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        Window dialogWindow = getWindow();
        if(getWindowAnimations() != -1){
            dialogWindow.setWindowAnimations(getWindowAnimations());
        }
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width =mWidth; //设置为屏幕宽度 (直接设置具体的值可以避免显示的时候尺寸问题)
        lp.height = mHeight; //设置为屏幕高度  如果设置成MATCH_PARENT则状态栏的背景颜色会变成黑色
        //        lp.dimAmount = 0.3f;//外围遮罩透明度0.0f-1.0f
        dialogWindow.setAttributes(lp);
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0); //设置边距
//        dialogWindow.setGravity(Gravity.BOTTOM);//内容区域底部显示

        initData(dialogWindow);
    }

    /**
     * 获取Dialog的布局文件id
     */
    protected abstract int getLayoutId();

    /**
     *  获取Dialig的显示动画id
     */
    protected int getWindowAnimations(){ return -1;}

    /**
     * 初始化Data,可以重新为 Dialog 设置宽高，动画，透明度等等
     * @param dialogWindow
     */
    protected abstract void initData(Window dialogWindow);

    protected  <T extends View> T  findWidget(int id, Class<T> c) {
        return (T)findViewById(id);
    }

    @Override
    public void dismiss() {
        Context context = mContextWeakReference.get();
        if (isShowing() && context != null && context instanceof Activity && !((Activity) context).isFinishing())
        super.dismiss();
    }
}
