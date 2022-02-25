package com.east.east_utils.mvvm.binding.viewadapter.image

import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *  @description:
 *  @author: East
 *  @date: 2019-09-27
 * |---------------------------------------------------------------------------------------------------------------|
 */
object ViewAdapter {

    @BindingAdapter(value = ["url","placeholderRes"],requireAll = false)
    @JvmStatic
    fun setImageUri(imageView: ImageView, url:String, placeholderRes : Int){
        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            Glide.with(imageView.context)
                .load(url)
                .apply(RequestOptions().placeholder(placeholderRes))
                .into(imageView)
        }
    }

    @BindingAdapter("android:src")
    @JvmStatic
    fun setImageResource(imageView: ImageView,resourceId:Int){
        imageView.setImageResource(resourceId)
    }


}