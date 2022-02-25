package com.east.east_utils.mvp_optimize.proxy.base;

/**
 * Created by hcDarren on 2018/1/6.
 */

public interface IMvpProxy {
    void bindAndCreatePresenter();// 一个是和创建绑定
    void unbindPresenter();// 一个是解绑
}
