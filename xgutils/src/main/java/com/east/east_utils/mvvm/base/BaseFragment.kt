package com.east.east_utils.mvvm.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.east.east_utils.mvvm.extension.viewModel
import java.lang.reflect.ParameterizedType

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *  @description: MVVM框架的基类
 *  @author: East
 *  @date: 2019-09-16
 * |---------------------------------------------------------------------------------------------------------------|
 */
abstract class BaseFragment<T:ViewDataBinding, VM: BaseViewModel> :Fragment(){
    val TAG: String
        get() {
            return javaClass.simpleName
        }

    var mIsPrepared = false

    abstract val layoutId: Int

    abstract val viewModelId : Int

    lateinit var mBinding: T

    var mVm :VM ?= null

    open fun initViewModel() : VM?{return null}

    /**
     *  在superOnCreate之后调用
     */
    open fun initData(){}


    open fun initViewObservable() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mIsPrepared = true
        mBinding = DataBindingUtil.inflate(inflater,layoutId,container,false)
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mVm = initViewModel()
        if(mVm == null){
            val modelClass: Class<out ViewModel>
            val type = javaClass.genericSuperclass
            //根据反射获取泛型的class
            modelClass = if (type is ParameterizedType) {
                type.actualTypeArguments[1] as Class<out ViewModel>
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                ViewModel::class.java
            }
            mVm = viewModel(modelClass) as VM
        }
        //关联ViewModel
        mBinding.setVariable(viewModelId,mVm)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        registorUIChangeLiveDataCallBack()
        initData()
        initViewObservable()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mIsPrepared = false
    }

    /**
     * 注册ViewModel与View的契约UI回调事件
     */
    protected fun registorUIChangeLiveDataCallBack() {
        //加载对话框显示
        mVm!!.mUc.showDialogEvent.observe(viewLifecycleOwner, Observer {

        })
        //加载对话框消失
        mVm!!.mUc.dismissDialogEvent.observe(viewLifecycleOwner, Observer {

        })
        //跳入新页面
        mVm!!.mUc.startActivityEvent
            .observe(viewLifecycleOwner, Observer {
                val clz = it[ParameterField.CLASS] as Class<*>
                val bundle = it[ParameterField.BUNDLE] as Bundle?
                startActivity(clz, bundle)
            })
        //跳入ContainerActivity
        mVm!!.mUc.startContainerActivityEvent
            .observe(viewLifecycleOwner, Observer {

            })
        //关闭界面
        mVm!!.mUc.finishEvent.observe(viewLifecycleOwner, Observer {
            activity?.finish()
        })
        //关闭上一层
        mVm!!.mUc.onBackPressedEvent.observe(viewLifecycleOwner, Observer {
            activity?.onBackPressed()
        })
    }


    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    fun startActivity(clz: Class<*>) {
        startActivity(Intent(context, clz))
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val intent = Intent(context, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

}