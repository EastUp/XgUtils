package com.east.east_utils.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import com.east.east_utils.R;

/**
 * 描述：宽高全屏的Dialog
 * Created by East at 2018/1/31 17:09
 *  注意：
 *      dialog.setCancelable(false);
 *      dialog.setContentView(layout);// show方法要在前面
 */

public class MyDialog extends Dialog {
    Context mContext;
    private OnDialogShowing mOnDialogShowing;


    public MyDialog(Context context,int themeId) {
        super(context, themeId); // 自定义全屏style
        this.mContext=context;
    }

    public MyDialog(Context context) {
        super(context, R.style.MyDialog); // 自定义全屏style
        this.mContext=context;
    }

    public MyDialog(Context context,OnDialogShowing onDialogShowing) {
        super(context, R.style.MyDialog); // 自定义全屏style
        this.mContext=context;
        mOnDialogShowing = onDialogShowing;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，设置在show的后面确保是在setContentView之前调用的（）
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity=Gravity.BOTTOM;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        if (mOnDialogShowing != null) {
            mOnDialogShowing.onDialogShowing();
        }
    }

    public interface OnDialogShowing{
        void onDialogShowing();
    }

}