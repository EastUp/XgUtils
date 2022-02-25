package com.east.east_utils.widgets.dialog.loading_dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.east.east_utils.R;
import com.east.east_utils.utils.ScreenUtils;

import java.lang.ref.WeakReference;

/**
 * 描述：网络加载转圈的UI
 * Created by East at 2018/6/11 12:59
 */
public class LoadingDialogUtils {

    private AlertDialog loadingDialog;
    private WeakReference<Context> mContextRefrence;
    //是否可以关闭
    private  boolean isCancelable = true;
    //是否可以点击外面关闭
    private  boolean isTouchOutsideCancelable;
    private  Handler mHandler;

    private volatile static LoadingDialogUtils mInstance;

    public static LoadingDialogUtils getInstance(){
        if(mInstance == null){
            synchronized (LoadingDialogUtils.class){
                if(mInstance == null)
                    mInstance = new LoadingDialogUtils();
            }
        }
        return mInstance;
    }

    private LoadingDialogUtils(){}

    public void show(Context context, String msg) {
        show(context, msg, null, null);
    }

    public void show(final Context context, final String msg, final DialogInterface.OnDismissListener onDismissListener) {
        show(context, msg, onDismissListener, null);
    }

    public void show(final Context context, final String msg, final DialogInterface.OnCancelListener onCancelListener) {
        show(context, msg, null, onCancelListener);
    }

    public void show(Context context, String msg, final DialogInterface.OnDismissListener onDismissListener, final DialogInterface.OnCancelListener onCancelListener) {
        if (loadingDialog == null || mContextRefrence ==null || mContextRefrence.get() != context) {
            mContextRefrence = new WeakReference<Context>(context);
            LayoutInflater inflater = LayoutInflater.from(mContextRefrence.get());
            View v;
            if (ScreenUtils.isTablet())
                v = inflater.inflate(R.layout.dialog_loading_pad, null);// 得到加载view
            else
                v = inflater.inflate(R.layout.dialog_loading_phone, null);// 得到加载view
            if (!TextUtils.isEmpty(msg)) {
                TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
                if (tipTextView != null) {
                    tipTextView.setVisibility(View.VISIBLE);
                    tipTextView.setText(msg);// 设置加载信息
                }
            }

//            loadingDialog = new Dialog(context, R.style.MyLoadingDialogStyle);// 创建自定义样式dialog
            loadingDialog = new AlertDialog.Builder(mContextRefrence.get(), R.style.MyLoadingDialogStyle).create();// 创建自定义样式dialog
            loadingDialog.show();
            loadingDialog.getWindow()
                    .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            loadingDialog.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE);

            loadingDialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));

            loadingDialog.setCancelable(isCancelable); // 是否可以按“返回键”消失
            loadingDialog.setCanceledOnTouchOutside(isTouchOutsideCancelable); // 点击加载框以外的区域
            loadingDialog.setContentView(v);// 设置布局

            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (onDismissListener != null) onDismissListener.onDismiss(dialog);
                }
            });
            loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (onCancelListener != null) onCancelListener.onCancel(dialog);
                }
            });

            /**
             *将显示Dialog的方法封装在这里面（如果需要下面的代码则需要传入activity的context）
             */
//            Window window = loadingDialog.getWindow();
//            WindowManager.LayoutParams lp = window.getAttributes();
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//            window.setGravity(Gravity.CENTER);
//            window.setAttributes(lp);
//            window.setWindowAnimations(R.style.PopWindowAnimStyle);
        } else {
            TextView tipTextView = (TextView) loadingDialog.findViewById(R.id.tipTextView);
            if (tipTextView != null) {
                tipTextView.setVisibility(View.VISIBLE);
                tipTextView.setText(msg);
            }
            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (onDismissListener != null) onDismissListener.onDismiss(dialog);
                }
            });
            loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (onCancelListener != null) onCancelListener.onCancel(dialog);
                }
            });
            loadingDialog.setCanceledOnTouchOutside(isTouchOutsideCancelable);
            loadingDialog.setCancelable(isCancelable);
            isTouchOutsideCancelable = false;
            isCancelable = true;
            loadingDialog.show();
        }
    }

    /**
     * 设置是否可取消
     *
     * @param b
     */
    public void setCancelable(boolean b) {
        isCancelable = b;
        if (loadingDialog != null) {
            loadingDialog.setCancelable(b);
            isCancelable = true;
        }
    }

    /**
     * 设置触摸空白区域取消dialog
     *
     * @param isTouchOutsideCancelable
     */
    public void setIsTouchOutsideCancelable(boolean isTouchOutsideCancelable) {
        this.isTouchOutsideCancelable = isTouchOutsideCancelable;
        if (loadingDialog != null) {
            loadingDialog.setCanceledOnTouchOutside(isTouchOutsideCancelable);
            this.isTouchOutsideCancelable = false;
        }
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        if(mContextRefrence == null || mContextRefrence.get() == null)
            return;
        if (mContextRefrence.get()  instanceof Activity && ((Activity) mContextRefrence.get() ).isFinishing()) {
            return;
        }
        if (loadingDialog != null) {
            try {
                loadingDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断是否是在主线程
     *
     * @param thread
     * @return
     */
    public boolean isMainThread(Thread thread) {
        boolean b = Looper.getMainLooper().getThread() == thread;
        thread = null;
        return b;
    }

    public boolean isShowing(){
        if(loadingDialog!=null){
            return loadingDialog.isShowing();
        }else
            return false;
    }


}
