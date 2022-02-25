package com.east.east_utils.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.app.NotificationManagerCompat;

import com.east.east_utils.BuildConfig;
import com.east.east_utils.R;

import java.lang.reflect.Field;

/**
 * ToastUtils
 */

public class ToastUtils {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private static Context context = Utils.getContext();

    private static IToast iToast;
    private static SystemToast systemToast;
    private static ToastWithoutNotification toastWithoutNotification;
    private static final int COLOR_DEFAULT = 0xFEFFFFFF;
    private static final String NULL = "null";

    private static int sGravity = -1;
    private static int sXOffset = -1;
    private static int sYOffset = -1;
    private static int sBgColor = COLOR_DEFAULT;
    private static int sBgResource = -1;
    private static int sMsgColor = COLOR_DEFAULT;
    private static int sMsgTextSize = -1;

    public static void show(int resId) {
        show(context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(int resId, int duration) {
        show(context.getResources().getText(resId), duration);
    }

    public static void show(CharSequence text) {
        show(text, Toast.LENGTH_SHORT);
    }

    public static void showDebug(CharSequence text) {
        if (BuildConfig.DEBUG) {
            show(text, Toast.LENGTH_SHORT);
        }
    }

    public static void show(final CharSequence text, int duration) {
//        if (toast == null) {
//            toast = Toast.makeText(context, text, duration);
//        } else {
//            toast.setText(text);
//        }
//        toast.show();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cancel();
                iToast = newToast();
                iToast.setMsgView(text);
                iToast.setDuration(duration);
                if (sGravity != -1 || sXOffset != -1 || sYOffset != -1) {
                    iToast.setGravity(sGravity, sXOffset, sYOffset);
                }
                iToast.show();
            }
        });
    }

    public static void show(int resId, Object... args) {
        show(String.format(context.getResources().getString(resId), args),
                Toast.LENGTH_SHORT);
    }

    public static void show(String format, Object... args) {
        show(String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(int resId, int duration, Object... args) {
        show(String.format(context.getResources().getString(resId), args),
                duration);
    }

    public static void show(String format, int duration, Object... args) {
        show(String.format(format, args), duration);
    }

    private static IToast newToast() {
        if (NotificationManagerCompat.from(Utils.getApp()).areNotificationsEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(Utils.getApp())) {
//                    if(ToastUtils.systemToast == null){
                    systemToast = new SystemToast(new Toast(Utils.getApp()));
//                    }
                    return systemToast;
                }
            }
        }
//        if(toastWithoutNotification==null){
        toastWithoutNotification = new ToastWithoutNotification(new Toast(Utils.getApp()));
//        }
        return toastWithoutNotification;
    }

    private static View getView(@LayoutRes final int layoutId) {
        LayoutInflater inflate =
                (LayoutInflater) Utils.getApp().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflate.inflate(layoutId, null);
    }

    static class SystemToast extends AbsToast {
        SystemToast(Toast toast) {
            super(toast);
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                try {
                    //noinspection JavaReflectionMemberAccess
                    Field mTNField = Toast.class.getDeclaredField("mTN");
                    mTNField.setAccessible(true);
                    Object mTN = mTNField.get(toast);
                    Field mTNmHandlerField = mTNField.getType().getDeclaredField("mHandler");
                    mTNmHandlerField.setAccessible(true);
                    Handler tnHandler = (Handler) mTNmHandlerField.get(mTN);
                    mTNmHandlerField.set(mTN, new SafeHandler(tnHandler));
                } catch (Exception ignored) {/**/}
            }
        }

        @Override
        public void show() {
            if (mToast != null)
                mToast.show();
        }

        @Override
        public void cancel() {
            if (mToast != null)
                mToast.cancel();
            super.cancel();
        }

        static class SafeHandler extends Handler {
            private Handler impl;

            SafeHandler(Handler impl) {
                this.impl = impl;
            }

            @Override
            public void handleMessage(Message msg) {
                impl.handleMessage(msg);
            }

            @Override
            public void dispatchMessage(Message msg) {
                try {
                    impl.dispatchMessage(msg);
                } catch (Exception e) {
                    Log.e("ToastUtils", e.toString());
                }
            }
        }
    }

    static class ToastWithoutNotification extends AbsToast {

        private WindowManager mWM;

        private WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

        ToastWithoutNotification(Toast toast) {
            super(toast);
        }

        @Override
        public void show() {
            if (mToast == null) return;
            boolean isActivityContext = false;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
                mWM = (WindowManager) Utils.getApp().getSystemService(Context.WINDOW_SERVICE);
                mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            } else if (Settings.canDrawOverlays(Utils.getApp())) {
                mWM = (WindowManager) Utils.getApp().getSystemService(Context.WINDOW_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                }
            } else {
                Context topActivityOrApp = Utils.getTopActivityOrApp();
                if (!(topActivityOrApp instanceof Activity)) {
                    Log.w("ToastUtils", "Couldn't get top Activity.");
                    // try to use system toast
                    new SystemToast(mToast).show();
                    return;
                }
                Activity topActivity = (Activity) topActivityOrApp;
                if (topActivity.isFinishing() || topActivity.isDestroyed()) {
                    Log.w("ToastUtils", topActivity + " is useless");
                    // try to use system toast
                    new SystemToast(mToast).show();
                    return;
                }
                isActivityContext = true;
                mWM = topActivity.getWindowManager();
                mParams.type = WindowManager.LayoutParams.LAST_APPLICATION_WINDOW;
                Utils.ACTIVITY_LIFECYCLE.addActivityListener(topActivity, getActivityLifecycleCallbacks());
            }

            setToastParams();

            final long duration = mToast.getDuration() == Toast.LENGTH_SHORT ? 2000 : 3500;
            if (isActivityContext) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setToast(duration);
                    }
                });
            } else {
                setToast(duration);
            }
        }

        private void setToastParams() {
            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.format = PixelFormat.TRANSLUCENT;
            mParams.windowAnimations = android.R.style.Animation_Toast;
            mParams.setTitle("ToastWithoutNotification");
            mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            mParams.packageName = Utils.getApp().getPackageName();

            mParams.gravity = mToast.getGravity();
            if ((mParams.gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
                mParams.horizontalWeight = 1.0f;
            }
            if ((mParams.gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
                mParams.verticalWeight = 1.0f;
            }

            mParams.x = mToast.getXOffset();
            mParams.y = mToast.getYOffset();
            mParams.horizontalMargin = mToast.getHorizontalMargin();
            mParams.verticalMargin = mToast.getVerticalMargin();
        }

        private void setToast(long duration) {
            try {
                if (mWM != null) {
                    mWM.addView(mToastView, mParams);
                }
            } catch (Exception ignored) {/**/
                // 必须要重新 new Toast 才不会报错：Unable to add window -- token android.os.BinderProxy@1e0bad is not valid; is your activity running?
            }

            HANDLER.removeCallbacksAndMessages(null);
            runOnUiThreadDelayed(new Runnable() {
                @Override
                public void run() {
                    cancel();
                }
            }, duration);
        }

        private Utils.AtyLifecycleCallbacks getActivityLifecycleCallbacks() {
            return new Utils.AtyLifecycleCallbacks() {
                @Override
                public void onActivityDestroyed(@NonNull Activity activity) {
                    if (iToast == null) return;
                    activity.getWindow().getDecorView().setVisibility(View.GONE);
                    iToast.cancel();
                }
            };
        }


        @Override
        public void cancel() {
            try {
                if (mWM != null) {
                    mWM.removeViewImmediate(mToastView);
                }
            } catch (Exception ignored) {/**/}
            mWM = null;
            super.cancel();
        }
    }


    static abstract class AbsToast implements IToast {

        protected Toast mToast;
        protected View mToastView;

        AbsToast(Toast toast) {
            mToast = toast;
            mToastView = mToast.getView();
        }

        @Override
        public void setView(View view) {
            mToastView = view;
            mToast.setView(mToastView);
        }

        @Override
        public void setMsgView(CharSequence text) {
            if (mToastView == null || mToastView.findViewById(android.R.id.message) == null) {
                mToastView = ToastUtils.getView(R.layout.toast_layout);
                mToast.setView(mToastView);
            }

            TextView tvMessage = mToastView.findViewById(android.R.id.message);
            tvMessage.setText(text);
            if (sMsgColor != COLOR_DEFAULT) {
                tvMessage.setTextColor(sMsgColor);
            }
            if (sMsgTextSize != -1) {
                tvMessage.setTextSize(sMsgTextSize);
            }
            setBg(tvMessage);
        }

        private void setBg(final TextView tvMsg) {
            if (sBgResource != -1) {
                mToastView.setBackgroundResource(sBgResource);
                tvMsg.setBackgroundColor(Color.TRANSPARENT);
            } else if (sBgColor != COLOR_DEFAULT) {
                Drawable tvBg = mToastView.getBackground();
                Drawable msgBg = tvMsg.getBackground();
                if (tvBg != null && msgBg != null) {
                    tvBg.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));
                    tvMsg.setBackgroundColor(Color.TRANSPARENT);
                } else if (tvBg != null) {
                    tvBg.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));
                } else if (msgBg != null) {
                    msgBg.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));
                } else {
                    mToastView.setBackgroundColor(sBgColor);
                }
            }
        }

        @Override
        public View getView() {
            return mToastView;
        }

        @Override
        public void setDuration(int duration) {
            mToast.setDuration(duration);
        }

        @Override
        public void setGravity(int gravity, int xOffset, int yOffset) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }

        @Override
        public void setText(int resId) {
            mToast.setText(resId);
        }

        @Override
        public void setText(CharSequence s) {
            mToast.setText(s);
        }

        @Override
        @CallSuper
        public void cancel() {
            mToast = null;
            mToastView = null;
        }
    }

    interface IToast {

        void show();

        void cancel();

        void setView(View view);

        void setMsgView(CharSequence text);

        View getView();

        void setDuration(int duration);

        void setGravity(int gravity, int xOffset, int yOffset);

        void setText(@StringRes int resId);

        void setText(CharSequence s);
    }

    /**
     * Cancel the toast.
     */
    public static void cancel() {
        if (iToast != null) {
            iToast.cancel();
        }
    }

    public static void runOnUiThread(final Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            HANDLER.post(runnable);
        }
    }

    public static void runOnUiThreadDelayed(final Runnable runnable, long delayMillis) {
        HANDLER.postDelayed(runnable, delayMillis);
    }
}
