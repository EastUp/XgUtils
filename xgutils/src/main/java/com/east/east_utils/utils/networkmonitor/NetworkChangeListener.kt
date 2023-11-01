package com.east.east_utils.utils.networkmonitor

import com.east.east_utils.utils.networkmonitor.NetWorkState

interface NetworkChangeListener {
    fun onNetWorkStateChange(netWorkState: NetWorkState)
}