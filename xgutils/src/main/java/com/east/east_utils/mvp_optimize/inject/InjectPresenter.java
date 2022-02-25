package com.east.east_utils.mvp_optimize.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 *  @description: 用于一个BaseView拥有多个Presenter的时候依赖注入
 *  @author: jamin
 *  @date: 2020/4/3
 * |---------------------------------------------------------------------------------------------------------------|
 */
@Target(ElementType.FIELD) //属性
@Retention(RetentionPolicy.RUNTIME) //运行时
public @interface InjectPresenter {
}
