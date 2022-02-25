package com.east.east_utils.utils.language;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.NonNull;

import com.east.east_utils.ui.util.ActivityManager;
import com.east.east_utils.utils.SPUtils;
import com.east.east_utils.utils.Utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/06/20
 *     desc  : utils about language
 * </pre>
 */
public class LanguageUtils {
    private static final String KEY_LOCALE = "KEY_LOCALE";
    private static final String VALUE_FOLLOW_SYSTEM = "VALUE_FOLLOW_SYSTEM";

    private LanguageUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Apply the system language.
     * It will not restart Activity. u can put it in ur
     */
    public static void applySystemLanguage() {
        if (isAppliedSystemLanguage()) return;
        applyLanguage(Resources.getSystem().getConfiguration().locale, "", true, false);
    }

    /**
     * Apply the system language.
     *
     * @param activityClz The class of activity will be started after apply system language.
     */
    public static void applySystemLanguage(final Class<? extends Activity> activityClz) {
        applyLanguage(Resources.getSystem().getConfiguration().locale, activityClz, true, true);
    }

    /**
     * Apply the system language.
     *
     * @param activityClassName The full class name of activity will be started after apply system language.
     */
    public static void applySystemLanguage(final String activityClassName) {
        applyLanguage(Resources.getSystem().getConfiguration().locale, activityClassName, true, true);
    }

    /**
     * Apply the language.
     * It will not restart Activity. u can put it in ur
     *
     * @param locale The language of locale.
     */
    public static void applyLanguage(@NonNull final Locale locale) {
        if (isAppliedLanguage()) return;
        applyLanguage(locale, "", false, false);
    }

    /**
     * Apply the language.
     *
     * @param locale      The language of locale.
     * @param activityClz The class of activity will be started after apply system language.
     *                    It will start the launcher activity if the class is null.
     *                    注意 如果调用这个方法的Activity A和要启动的Activity A是同一个的话，会先调用新的Activity A的onCreate方法
     *                    再调用旧的Activity A的onDestroy方法
     */
    public static void applyLanguage(@NonNull final Locale locale,
                                     final Class<? extends Activity> activityClz) {
        applyLanguage(locale, activityClz, false, true);
    }

    /**
     * Apply the language.
     *
     * @param locale            The language of locale.
     * @param activityClassName The class of activity will be started after apply system language.
     *                          It will start the launcher activity if the class name is null.
     *                          注意 如果调用这个方法的Activity A和要启动的Activity A是同一个的话，会先调用新的Activity A的onCreate方法
     *                          再调用旧的Activity A的onDestroy方法
     */
    public static void applyLanguage(@NonNull final Locale locale,
                                     final String activityClassName) {
        applyLanguage(locale, activityClassName, false, true);
    }

    private static void applyLanguage(@NonNull final Locale locale,
                                      final Class<? extends Activity> activityClz,
                                      final boolean isFollowSystem,
                                      final boolean isNeedStartActivity) {
        if (activityClz == null) {
            applyLanguage(locale, "", isFollowSystem, isNeedStartActivity);
            return;
        }
        applyLanguage(locale, activityClz.getName(), isFollowSystem, isNeedStartActivity);
    }

    private static void applyLanguage(@NonNull final Locale locale,
                                      final String activityClassName,
                                      final boolean isFollowSystem,
                                      final boolean isNeedStartActivity) {
        if (isFollowSystem) {
            SPUtils.put(KEY_LOCALE, VALUE_FOLLOW_SYSTEM);
        } else {
            String localLanguage = locale.getLanguage();
            String localCountry = locale.getCountry();
            SPUtils.put(KEY_LOCALE, localLanguage + "$" + localCountry);
        }

        updateLanguage(Utils.getApp(), locale);

        if (isNeedStartActivity) {
            Intent intent = new Intent();
            String realActivityClassName = TextUtils.isEmpty(activityClassName) ? getLauncherActivity() : activityClassName;
            intent.setComponent(new ComponentName(Utils.getApp(), realActivityClassName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Utils.getApp().startActivity(intent);
        }
    }

    /**
     * Return whether applied the system language by {@link LanguageUtils}.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppliedSystemLanguage() {
        return VALUE_FOLLOW_SYSTEM.equals(SPUtils.getString(KEY_LOCALE));
    }

    /**
     * Return whether applied the language by {@link LanguageUtils}.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppliedLanguage() {
        return !TextUtils.isEmpty(SPUtils.getString(KEY_LOCALE));
    }

    /**
     * Return the locale.
     *
     * @return the locale
     */
    public static Locale getCurrentLocale() {
        return Utils.getApp().getResources().getConfiguration().locale;
    }

    public static void applyLanguage(@NonNull final Activity activity) {
        final String spLocale = SPUtils.getString(KEY_LOCALE);
        if (TextUtils.isEmpty(spLocale)) {
            return;
        }

        if (VALUE_FOLLOW_SYSTEM.equals(spLocale)) {
            Locale sysLocale = Resources.getSystem().getConfiguration().locale;
            updateLanguage(Utils.getApp(), sysLocale);
            updateLanguage(activity, sysLocale);
            return;
        }

        String[] language_country = spLocale.split("\\$");
        if (language_country.length != 2) {
            Log.e("LanguageUtils", "The string of " + spLocale + " is not in the correct format.");
            return;
        }

        Locale settingLocale = new Locale(language_country[0], language_country[1]);
        updateLanguage(Utils.getApp(), settingLocale);
        updateLanguage(activity, settingLocale);
    }

    public static void updateLanguage(final Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        Locale contextLocale = config.locale;
        if (isSameLocale(contextLocale, locale)) {
            return;
        }
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            if (context instanceof Application) {
                Context newContext = context.createConfigurationContext(config);
                try {
                    //noinspection JavaReflectionMemberAccess
                    Field mBaseField = ContextWrapper.class.getDeclaredField("mBase");
                    mBaseField.setAccessible(true);
                    mBaseField.set(context, newContext);
                } catch (Exception ignored) {/**/}
            }
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, dm);
    }

    private static boolean isSameLocale(Locale locale, Locale contextLocale) {
        return TextUtils.equals(contextLocale.getLanguage(), locale.getLanguage())
                && TextUtils.equals(contextLocale.getCountry(), locale.getCountry());
    }

    private static String getLauncherActivity() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(Utils.getApp().getPackageName());
        PackageManager pm = Utils.getApp().getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        ResolveInfo next = info.iterator().next();
        if (next != null) {
            return next.activityInfo.name;
        }
        return "no launcher activity";
    }
}