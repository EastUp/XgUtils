package com.east.east_utils.utils.networkmonitor

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build

import java.lang.reflect.InvocationTargetException

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 *  @description: 管理类
 *  @author: East
 *  @date: 2019-10-09
 * |---------------------------------------------------------------------------------------------------------------|
 */
class NetWorkMonitorManager private constructor() {
    private var application: Application? = null

    /**
     * 存储接受网络状态变化消息的方法的map
     */
    internal var netWorkStateChangeListeners = ArrayList<NetworkChangeListener>()

    internal var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action!!.equals(ANDROID_NET_CHANGE_ACTION, ignoreCase = true)) {
                //网络发生变化 没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
                val netType = NetStateUtils.getAPNType(context)
                var netWorkState = when (netType) {
                    0//None
                    -> NetWorkState.NONE

                    1//Wifi
                    -> NetWorkState.WIFI

                    else//GPRS
                    -> NetWorkState.GPRS
                }
                postNetState(netWorkState)
            }
        }
    }


    internal var networkCallback: ConnectivityManager.NetworkCallback =
        object : ConnectivityManager.NetworkCallback() {
            /**
             * 网络可用的回调连接成功
             */
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val netType = NetStateUtils.getAPNType(this@NetWorkMonitorManager.application!!)
                var netWorkState = when (netType) {
                    0//None
                    -> NetWorkState.NONE

                    1//Wifi
                    -> NetWorkState.WIFI

                    else//GPRS
                    -> NetWorkState.GPRS
                }
                postNetState(netWorkState)
            }

            /**
             * 网络不可用时调用和onAvailable成对出现
             */
            override fun onLost(network: Network) {
                super.onLost(network)
                postNetState(NetWorkState.NONE)
            }

            /**
             * 在网络连接正常的情况下，丢失数据会有回调 即将断开时
             */
            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
            }

            /**
             * 网络功能更改 满足需求时调用
             * @param network
             * @param networkCapabilities
             */
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
            }

            /**
             * 网络连接属性修改时调用
             * @param network
             * @param linkProperties
             */
            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties)
            }

            /**
             * 网络缺失network时调用
             */
            override fun onUnavailable() {
                super.onUnavailable()
            }
        }

    /**
     * 初始化 传入application
     *
     * @param application
     */
    fun init(application: Application?) {
        if (application == null) {
            throw NullPointerException("application can not be null")
        }
        this.application = application
        initMonitor()
    }

    /**
     * 初始化网络监听 根据不同版本做不同的处理
     */
    private fun initMonitor() {
        val connectivityManager =
            this.application!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//API 大于26时
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//API 大于21时
            val builder = NetworkRequest.Builder()
            val request = builder.build()
            connectivityManager.registerNetworkCallback(request, networkCallback)
        } else {//低版本
            val intentFilter = IntentFilter()
            intentFilter.addAction(ANDROID_NET_CHANGE_ACTION)
            this.application!!.registerReceiver(receiver, intentFilter)
        }
    }

    /**
     * 反注册广播
     */
    private fun onDestroy() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            this.application!!.unregisterReceiver(receiver)
        }
    }

    /**
     * 注入
     * @param object
     */
    fun register(listener: NetworkChangeListener) {
        if (this.application == null) {
            throw NullPointerException("application can not be null,please call the method init(Application application) to add the Application")
        }
        if (!netWorkStateChangeListeners.contains(listener))
            netWorkStateChangeListeners.add(listener)
    }

    /**
     * 删除
     *
     * @param object
     */


    fun unregister(listener: NetworkChangeListener) {
        netWorkStateChangeListeners.remove(listener)
    }

    /**
     * 网络状态发生变化，需要去通知更改
     * @param netWorkState
     */
    private fun postNetState(netWorkState: NetWorkState) {
        netWorkStateChangeListeners.forEach {
            it.onNetWorkStateChange(netWorkState)
        }
    }

    /**
     * 具体执行方法
     *
     * @param netWorkStateReceiverMethod
     * @param netWorkState
     */
    private fun invokeMethod(
        netWorkStateReceiverMethod: NetWorkStateReceiverMethod?,
        netWorkState: NetWorkState
    ) {
        if (netWorkStateReceiverMethod != null) {
            try {
                val netWorkStates = netWorkStateReceiverMethod.netWorkState
                for (myState in netWorkStates) {
                    if (myState === netWorkState) {
                        netWorkStateReceiverMethod.method!!.invoke(
                            netWorkStateReceiverMethod.`object`,
                            netWorkState
                        )
                        return
                    }
                }

            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 找到对应的方法
     *
     * @param object
     * @return
     */
    private fun findMethod(`object`: Any?): NetWorkStateReceiverMethod? {
        var targetMethod: NetWorkStateReceiverMethod? = null
        if (`object` != null) {
            val myClass = `object`.javaClass
            //获取所有的方法
            val methods = myClass.declaredMethods
            for (method in methods) {
                //如果参数个数不是1个 直接忽略
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (method.parameterCount != 1) {
                        continue
                    }
                }
                //获取方法参数
                val parameters = method.parameterTypes
                if (parameters == null || parameters.size != 1) {
                    continue
                }
                //参数的类型需要时NetWorkState类型
                if (parameters[0].name == NetWorkState::class.java.name) {
                    //是NetWorkState类型的参数
                    val netWorkMonitor = method.getAnnotation(NetWorkMonitor::class.java)
                    targetMethod = NetWorkStateReceiverMethod()
                    //如果没有添加注解，默认就是所有网络状态变化都通知
                    if (netWorkMonitor != null) {
                        val netWorkStates = netWorkMonitor.monitorFilter
                        targetMethod.netWorkState = netWorkStates
                    }
                    targetMethod.method = method
                    targetMethod.`object` = `object`
                    //只添加第一个符合的方法
                    return targetMethod
                }
            }
        }
        return targetMethod
    }

    companion object {
        val TAG = "NetWorkMonitor >>> : "
        private var ourInstance: NetWorkMonitorManager? = null

        val instance: NetWorkMonitorManager
            get() {
                synchronized(NetWorkMonitorManager::class.java) {
                    if (ourInstance == null) {
                        ourInstance = NetWorkMonitorManager()
                    }
                }
                return ourInstance!!
            }


        private val ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
    }

}