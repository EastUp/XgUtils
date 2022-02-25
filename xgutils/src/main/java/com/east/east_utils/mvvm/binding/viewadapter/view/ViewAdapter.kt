package com.east.east_utils.mvvm.binding.viewadapter.view

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *  @description:  自定义一些BindingAdapter的属性
 *  @author: East
 *  @date: 2019-09-16
 * |---------------------------------------------------------------------------------------------------------------|
 */
object ViewAdapter {

    @BindingAdapter("onClickCommand")
    @JvmStatic
    fun onClickCommand(view: View,bindingCommand:()->Unit){
        view.setOnClickListener {
            bindingCommand()
        }
    }
}