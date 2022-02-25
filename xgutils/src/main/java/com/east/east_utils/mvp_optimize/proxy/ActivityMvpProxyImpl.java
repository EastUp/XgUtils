package com.east.east_utils.mvp_optimize.proxy;


import com.east.east_utils.mvp_optimize.BaseView;
import com.east.east_utils.mvp_optimize.proxy.base.ActivityMvpProxy;
import com.east.east_utils.mvp_optimize.proxy.base.MvpProxyImpl;


/**
 * |---------------------------------------------------------------------------------------------------------------|
 *  @description:  Activity的Mvp代理
 *  @author: jamin
 *  @date: 2020/4/3 14:36
 * |---------------------------------------------------------------------------------------------------------------|
 */
public class ActivityMvpProxyImpl<V extends BaseView> extends MvpProxyImpl<V> implements ActivityMvpProxy {
    public ActivityMvpProxyImpl(V view) {
        super(view);
    }
    // 不同对待，一般可能不会


}
