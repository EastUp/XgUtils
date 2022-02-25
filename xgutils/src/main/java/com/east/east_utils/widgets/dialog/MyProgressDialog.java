package com.east.east_utils.widgets.dialog;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * 描述：
 * Created by East at 2018/1/31 14:55
 */

public class MyProgressDialog extends ProgressDialog{
    private Activity mActivity;
    public MyProgressDialog(Activity activity) {
        super(activity);
        this.mActivity = activity;
    }

    @Override
    public void dismiss() {
        if(!mActivity.isFinishing()){
            super.dismiss();
        }
    }

    @Override
    public void show() {
        if(!mActivity.isFinishing()){
            super.show();
        }
    }
}
