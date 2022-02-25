package com.east.east_utils.utils.networkmonitor

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @description: 标注注解
 * @author: East
 * @date: 2019-10-09
 * |---------------------------------------------------------------------------------------------------------------|
 */
@Retention(RetentionPolicy.RUNTIME)//运行时注解
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)//标记在方法上
annotation class NetWorkMonitor(//监听的网络状态变化 默认全部监听并提示
    val monitorFilter: Array<NetWorkState> = [NetWorkState.GPRS, NetWorkState.WIFI, NetWorkState.NONE]
)
