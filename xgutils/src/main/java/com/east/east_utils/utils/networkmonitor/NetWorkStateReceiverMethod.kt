package com.east.east_utils.utils.networkmonitor


import java.lang.reflect.Method

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @description: 保存接受状态变化的方法对象
 * @author: East
 * @date: 2019-10-09
 * |---------------------------------------------------------------------------------------------------------------|
 */
class NetWorkStateReceiverMethod {
    /**
     * 网络改变执行的方法
     */
    var method: Method?=null
    /**
     * 网络改变执行的方法所属的类
     */
    var `object`: Any?=null
    /**
     * 监听的网络改变类型
     */
    var netWorkState = arrayOf(NetWorkState.GPRS, NetWorkState.WIFI, NetWorkState.NONE)
}
