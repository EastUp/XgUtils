package com.east.east_utils.mvvm.base

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.util.HashMap

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @description:
 * @author: East
 * @date: 2019-09-27
 * |---------------------------------------------------------------------------------------------------------------|
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    var mUc : UIChangeEvent = UIChangeEvent()

    fun showDialog() {
        showDialog("请稍后...")
    }

    fun showDialog(title: String) {
        mUc.showDialogEvent.postValue(title)
    }

    fun dismissDialog() {
        mUc.dismissDialogEvent.value = null
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    fun startActivity(clz: Class<*>) {
        startActivity(clz, null)
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val params = HashMap<String, Any>()
        params[ParameterField.CLASS] = clz
        if (bundle != null) {
            params[ParameterField.BUNDLE] = bundle
        }
        mUc.startActivityEvent.postValue(params)
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    fun startContainerActivity(canonicalName: String) {
        startContainerActivity(canonicalName, null)
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    fun startContainerActivity(canonicalName: String, bundle: Bundle?) {
        val params = HashMap<String, Any>()
        params[ParameterField.CANONICAL_NAME] = canonicalName
        if (bundle != null) {
            params[ParameterField.BUNDLE] = bundle
        }
        mUc.startContainerActivityEvent.postValue(params)
    }

    /**
     * 关闭界面
     */
    fun finish() {
        mUc.finishEvent.value = null
    }

    /**
     * 返回上一层
     */
    fun onBackPressed() {
        mUc.onBackPressedEvent.value = null
    }


    /**
     *  界面跳转,加载框弹出
     */
    class UIChangeEvent {
        val showDialogEvent: MutableLiveData<String>  by lazy { MutableLiveData<String>() }
        val dismissDialogEvent: MutableLiveData<Void> by lazy { MutableLiveData<Void>() }
        val startActivityEvent: MutableLiveData<Map<String, Any>> by lazy { MutableLiveData<Map<String, Any>>() }
        val startContainerActivityEvent: MutableLiveData<Map<String, Any>> by lazy { MutableLiveData<Map<String, Any>>() }
        val finishEvent: MutableLiveData<Void> by lazy { MutableLiveData<Void>() }
        val onBackPressedEvent: MutableLiveData<Void> by lazy { MutableLiveData<Void>() }
    }
}
