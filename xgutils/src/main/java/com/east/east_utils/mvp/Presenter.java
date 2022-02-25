package com.east.east_utils.mvp;

/**
 * 描述：
 * Created by East at 2018/2/5 11:15
 */

public interface Presenter<T extends BaseView> {
    void attachView(T view);

    void detachView();
}
