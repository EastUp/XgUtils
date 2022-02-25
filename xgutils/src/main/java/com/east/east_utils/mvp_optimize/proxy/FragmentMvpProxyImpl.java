package com.east.east_utils.mvp_optimize.proxy;

import com.east.east_utils.mvp_optimize.BaseView;
import com.east.east_utils.mvp_optimize.proxy.base.FragmentMvpProxy;
import com.east.east_utils.mvp_optimize.proxy.base.MvpProxyImpl;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 *  @description:   Fragment的Mvp代理
 *  @author: jamin
 *  @date: 2020/4/3
 * |---------------------------------------------------------------------------------------------------------------|
 */
public class FragmentMvpProxyImpl<V extends BaseView> extends MvpProxyImpl<V> implements FragmentMvpProxy {
    public FragmentMvpProxyImpl(V view) {
        super(view);
    }
}
