package com.east.east_utils.ui.base;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.east.east_utils.utils.ToastUtils;
import com.east.east_utils.widgets.dialog.MyProgressDialog;


/**
 * Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Intent mIntent;
    private boolean mIsStarting;
    protected MyProgressDialog mProgressDialog;
    @Override
    public void finish() {
        super.finish();
    }

    /**
     * 返回上下文对象
     *
     * @return 当前BaseActivity
     */
    public BaseActivity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mProgressDialog = new MyProgressDialog(this);
        mProgressDialog.setMessage("正在请求网络数据...");
        mProgressDialog.setCancelable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIntent = null;
        mIsStarting = false;
    }

    /**
     * 显示浮动消息
     *
     * @param content 显示内容
     */
    public void showToast(CharSequence content) {
        ToastUtils.show(content);
    }
    @Override
    public synchronized void startActivityForResult(Intent intent, int requestCode) {
        if (intent == null) {
            return;
        }
        if (mIsStarting && mIntent != null) {
            if (mIntent.getComponent() != null && mIntent.getComponent().equals(intent.getComponent())) {
                return;
            }
        }
        mIntent = intent;
        mIsStarting = true;
        super.startActivityForResult(intent, requestCode);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK)
//        {
//            exitBy2Click(); //调用双击退出函数
////            SetAndGetDataUtil.DeleteData(this,"userinfo");
//        }
//        return false;
//    }
//
//    /**
//     * 双击退出函数
//     */
//    private static Boolean isExit = false;
//
//    private void exitBy2Click() {
//        Timer tExit = null;
//        if (isExit == false) {
//            isExit = true; // 准备退出
//            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//            tExit = new Timer();
//            tExit.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    isExit = false; // 取消退出
//                }
//            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
//
//        } else {
//            ActivityManager.getInstance().AppExit(this);
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
