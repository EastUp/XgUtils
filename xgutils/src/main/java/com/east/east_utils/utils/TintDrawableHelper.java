package com.east.east_utils.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * 描述：给图片着色的类
 * Created by East at 2018/5/28 10:27
 */
public class TintDrawableHelper {

    /**
     * 对目标Drawable 进行着色
     * 注意这种方法tint过后还是会有着色的效果
     * @param drawable 目标Drawable
     * @param color    着色的颜色值
     * @return 着色处理后的Drawable
     */
    public static Drawable tintDrawable( Drawable drawable, int color) {
        if(drawable == null)
            return null;
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }

    /**
     *  取消图片的着色
     * @param drawable
     * @return
     */
    public static Drawable unTintDrawable( Drawable drawable) {
        if(drawable == null)
            return null;
        Drawable wrappedDrawable = DrawableCompat.unwrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, null);
        return wrappedDrawable;
    }

    /**
     * 对目标Drawable 进行着色
     *
     * @param drawable 目标Drawable
     * @param colors   着色值
     * @return 着色处理后的Drawable
     */
    public static Drawable tintListDrawable( Drawable drawable, ColorStateList colors) {
        if(drawable == null)
            return null;
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        // 进行着色
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }
}
