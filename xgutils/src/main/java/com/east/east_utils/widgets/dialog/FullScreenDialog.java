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
import com.east.east_utils.utils.statusbar.StatusBarUtil;

import java.lang.ref.WeakReference;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 *  @description: 自定义半透明全屏dialog解决顶部状态栏的黑色背景问题
 *  @author: East
 *  @date: 2019-10-27
 * |---------------------------------------------------------------------------------------------------------------|
 */
public abstract class FullScreenDialog extends Dialog {

    private WeakReference<Context> mContextWeakReference;

    public FullScreenDialog(@NonNull Context context) {
        this(context, R.style.Theme_Light_NoTitle_Dialog);
        mContextWeakReference = new WeakReference(context);
    }

    public FullScreenDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected FullScreenDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        Window dialogWindow = getWindow();
        StatusBarUtil.setTranslucentStatus(dialogWindow);
        StatusBarUtil.setStatusBarDarkTheme(dialogWindow,false);
        if(getWindowAnimations() != -1){
            dialogWindow.setWindowAnimations(getWindowAnimations());
        }
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; //设置为屏幕宽度
        lp.height = WindowManager.LayoutParams.MATCH_PARENT; //设置为屏幕高度  如果设置成MATCH_PARENT则状态栏的背景颜色会变成黑色
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
     * 初始化Data
     * @param dialogWindow
     */
    protected abstract void initData(Window dialogWindow);

    protected <T extends View> T  findWidget(int id, Class<T> c) {
        return (T)findViewById(id);
    }

    @Override
    public void dismiss() {
        Context context = mContextWeakReference.get();
        if (isShowing() && context != null && context instanceof Activity && !((Activity) context).isFinishing())
        super.dismiss();
    }
}
