package com.east.east_utils.ui.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *  @description: 普通的BaseAppActivity
 *  @author: jamin
 *  @date: 2020/8/10
 * |---------------------------------------------------------------------------------------------------------------|
 */
abstract class BaseNormalActivity: AppCompatActivity() {

    val TAG: String
        get() {
            return javaClass.simpleName
        }

    abstract val layoutId: Int

    /**
     * 在superOnCreate之前调用
     */
    open fun beforeSuperOnCreate() {}

    /**
     *  在superOnCreate之后调用
     */
    open fun initData(){}


    open fun initViewObservable() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeSuperOnCreate()
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        initData()

        initViewObservable()
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    fun startActivity(clz: Class<*>) {
        startActivity(Intent(this, clz))
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

}