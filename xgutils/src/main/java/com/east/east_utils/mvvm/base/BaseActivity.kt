package com.east.east_utils.mvvm.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.east.east_utils.mvvm.extension.viewModel
import java.lang.reflect.ParameterizedType

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *  @description: MVVM中的基类
 *  @author: East
 *  @date: 2019-09-16
 * |---------------------------------------------------------------------------------------------------------------|
 */
abstract class BaseActivity<T : ViewDataBinding,VM:BaseViewModel> : AppCompatActivity() {
    val TAG: String
        get() {
            return javaClass.simpleName
        }

    abstract val layoutId: Int

    abstract val viewModelId : Int

    lateinit var binding: T

    lateinit var vm :VM

    /**
     * 在superOnCreate之前调用
     */
    open fun beforeSuperOnCreate() {}

    /**
     * 在viewmodel创建之前调用
     */
    open fun beforeVMInit() {}

    open fun initViewModel() : VM = kotlin.run {
        val modelClass: Class<out ViewModel>
        val type = javaClass.genericSuperclass
        //根据反射获取泛型的class
        modelClass = if (type is ParameterizedType) {
            type.actualTypeArguments[1] as Class<out ViewModel>
        } else {
            //如果没有指定泛型参数，则默认使用BaseViewModel
            ViewModel::class.java
        }
        viewModel(modelClass) as VM
    }

    /**
     *  在superOnCreate之后调用
     */
    open fun initData(){}


    open fun initViewObservable() {}

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        beforeSuperOnCreate()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        beforeVMInit()
        vm = initViewModel()
//        if(vm == null){
//            val modelClass: Class<out ViewModel>
//            val type = javaClass.genericSuperclass
//            //根据反射获取泛型的class
//            modelClass = if (type is ParameterizedType) {
//                type.actualTypeArguments[1] as Class<out ViewModel>
//            } else {
//                //如果没有指定泛型参数，则默认使用BaseViewModel
//                ViewModel::class.java
//            }
//            vm = viewModel(modelClass) as VM
//        }
        //关联ViewModel
        binding.setVariable(viewModelId,vm)
        registorUIChangeLiveDataCallBack()

        initData()

        initViewObservable()
    }


    /**
     * 注册ViewModel与View的契约UI回调事件
     */
    protected fun registorUIChangeLiveDataCallBack() {
        //加载对话框显示
        vm.uc.showDialogEvent.observe(this) {

        }
        //加载对话框消失
        vm.uc.dismissDialogEvent.observe(this) {

        }
        //跳入新页面
        vm.uc.startActivityEvent
            .observe(this, Observer {
                val clz = it[ParameterField.CLASS] as Class<*>
                val bundle = it[ParameterField.BUNDLE] as Bundle?
                startActivity(clz, bundle)
            })
        //跳入ContainerActivity
        vm.uc.startContainerActivityEvent
            .observe(this) {

            }
        //关闭界面
        vm.uc.finishEvent.observe(this) {
            finish()
        }
        //关闭上一层
        vm.uc.onBackPressedEvent.observe(this) {
            onBackPressed()
        }
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