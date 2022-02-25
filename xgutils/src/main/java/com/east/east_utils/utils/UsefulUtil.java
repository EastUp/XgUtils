package com.east.east_utils.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;

/**
 * Created by Administrator on 2016/3/23.
 */
public class UsefulUtil {

    /**唤醒屏幕并解锁
     * @param context
     */
    public static void wakeUpAndUnlock(Context context){

        KeyguardManager km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");

        //解锁

        kl.disableKeyguard();

        //获取电源管理器对象

        PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);

        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag

        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");

        //点亮屏幕

        wl.acquire();

        //释放

        wl.release();

    }


//    需要添加权限
//
//
//
//            <uses-permission android:name="android.permission.WAKE_LOCK" />
//
//    <uses-permission android:name="android.permission.DISABLE_KEYGUARD" />








    /**判断当前是否有网络连接
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context

                .getSystemService(Activity.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {

            return true;

        }

        return false;

    }






    /**判断当前是否是WIFI连接状态
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context

                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetworkInfo = connectivityManager

                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifiNetworkInfo.isConnected()) {

            return true;

        }

        return false;

    }






    /**安装APK
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {

        Intent intent = new Intent();

        intent.setAction("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.setType("application/vnd.android.package-archive");

        intent.setDataAndType(Uri.fromFile(file),

                "application/vnd.android.package-archive");

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);

    }






    /**判断当前设备是否为手机
     * @param context
     * @return
     */
    public static boolean isPhone(Context context) {

        TelephonyManager telephony = (TelephonyManager) context

                .getSystemService(Context.TELEPHONY_SERVICE);

        if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {

            return false;

        } else {

            return true;

        }

    }





    @SuppressWarnings("deprecation")

    /**获取当前设备宽高，单位px
     * @param context
     * @return
     */
    public static int getDeviceWidth(Context context) {

        WindowManager manager = (WindowManager) context

                .getSystemService(Context.WINDOW_SERVICE);

        return manager.getDefaultDisplay().getWidth();

    }



    @SuppressWarnings("deprecation")

    public static int getDeviceHeight(Context context) {

        WindowManager manager = (WindowManager) context

                .getSystemService(Context.WINDOW_SERVICE);

        return manager.getDefaultDisplay().getHeight();

    }






    /**获取当前设备的IMEI，需要与上面的isPhone()一起使用
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)

    public static String getDeviceIMEI(Context context) {

        String deviceId;

        if (isPhone(context)) {

            TelephonyManager telephony = (TelephonyManager) context

                    .getSystemService(Context.TELEPHONY_SERVICE);

            deviceId = telephony.getDeviceId();

        } else {

            deviceId = Settings.Secure.getString(context.getContentResolver(),

                    Settings.Secure.ANDROID_ID);



        }

        return deviceId;

    }






    /**获取当前设备的MAC地址
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {

        String macAddress;

        WifiManager wifi = (WifiManager) context

                .getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        WifiInfo info = wifi.getConnectionInfo();

        macAddress = info.getMacAddress();

        if (null == macAddress) {

            return "";

        }

        macAddress = macAddress.replace(":", "");

        return macAddress;

    }






    /**获取当前程序的版本号
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {

        String version = "0";

        try {

            version = context.getPackageManager().getPackageInfo(

                    context.getPackageName(), 0).versionName;

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

        }

        return version;

    }




    @TargetApi(Build.VERSION_CODES.CUPCAKE)

    /**动态隐藏软键盘
     * @param activity
     */
    public static void hideSoftInput(Activity activity) {

        View view = activity.getWindow().peekDecorView();

        if (view != null) {

            InputMethodManager inputmanger = (InputMethodManager) activity

                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }

    }



    @TargetApi(Build.VERSION_CODES.CUPCAKE)

    public static void hideSoftInput(Context context, EditText edit) {

        edit.clearFocus();

        InputMethodManager inputmanger = (InputMethodManager) context

                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputmanger.hideSoftInputFromWindow(edit.getWindowToken(), 0);

    }





    @TargetApi(Build.VERSION_CODES.CUPCAKE)

    /**动态显示软键盘
     * @param context
     * @param edit
     */
    public static void showSoftInput(Context context, EditText edit) {

        edit.setFocusable(true);

        edit.setFocusableInTouchMode(true);

        edit.requestFocus();

        InputMethodManager inputManager = (InputMethodManager) context

                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.showSoftInput(edit, 0);

    }





    @TargetApi(Build.VERSION_CODES.CUPCAKE)

    /**动态显示或者是隐藏软键盘
     * @param context
     * @param edit
     */
    public static void toggleSoftInput(Context context, EditText edit) {

        edit.setFocusable(true);

        edit.setFocusableInTouchMode(true);

        edit.requestFocus();

        InputMethodManager inputManager = (InputMethodManager) context

                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }






    /**主动回到Home，后台运行
     * @param context
     */
    public static void goHome(Context context) {

        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);

        mHomeIntent.addCategory(Intent.CATEGORY_HOME);

        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK

                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        context.startActivity(mHomeIntent);

    }







    @TargetApi(Build.VERSION_CODES.CUPCAKE)

    /**获取状态栏高度
     注意，要在onWindowFocusChanged中调用，在onCreate中获取高度为0
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {

        Rect frame = new Rect();

        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        return frame.top;

    }






    /**获取状态栏高度＋标题栏(ActionBar)高度
     (注意，如果没有ActionBar，那么获取的高度将和上面的是一样的，只有状态栏的高度)
     * @param activity
     * @return
     */
    public static int getTopBarHeight(Activity activity) {

        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT)

                .getTop();

    }









    /**返回移动网络运营商的名字
     (例：中国联通、中国移动、中国电信) 仅当用户已在网络注册时有效, CDMA 可能会无效)
     * @param context
     * @return
     */
    public static String getNetworkOperatorName(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context

                .getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager.getNetworkOperatorName();

    }



//    返回移动终端类型
//    PHONE_TYPE_NONE :0 手机制式未知
//    PHONE_TYPE_GSM :1 手机制式为GSM，移动和联通
//    PHONE_TYPE_CDMA :2 手机制式为CDMA，电信
//    PHONE_TYPE_SIP:3


    public static int getPhoneType(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context

                .getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager.getPhoneType();

    }

}
