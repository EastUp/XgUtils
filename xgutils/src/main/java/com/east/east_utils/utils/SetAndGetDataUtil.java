package com.east.east_utils.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2015/12/2.
 * 保存数据的工具类
 */
public class SetAndGetDataUtil {
    private static SharedPreferences sp;

    @SuppressWarnings("static-access")
    public static void SetData(Context context, String filename, String key,
                               String value) {
        sp = context.getSharedPreferences(filename, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }
    @SuppressWarnings("static-access")
    public static void SetData(Context context, String filename, String key,
                               boolean value) {
        sp = context.getSharedPreferences(filename, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    @SuppressWarnings("static-access")
    public static String GetData(Context context, String filename, String key) {
        String value = "";
        sp = context.getSharedPreferences(filename, context.MODE_PRIVATE);
        value = sp.getString(key, "");
        return value;
    }
    @SuppressWarnings("static-access")
    public static boolean GetBooleanData(Context context, String filename, String key) {
        sp = context.getSharedPreferences(filename, context.MODE_PRIVATE);
        boolean value = sp.getBoolean(key, true);
        return value;
    }

    @SuppressWarnings("static-access")
    public static void DeleteData(Context context, String filename) {
        sp = context.getSharedPreferences(filename, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static void DeleteKeyData(Context context, String filename, String key) {
        sp = context.getSharedPreferences(filename, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }


}
