package com.east.east_utils.mvp_optimize;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * 描述：
 * Created by East at 2018/2/5 11:16
 */

public class BasePresenter<V extends BaseView,M extends BaseModel> implements Presenter<V>, LifecycleObserver {
    private SoftReference<V> mViewRefrence;
    //解决重复的判断view是否为空的冗余代码，aop思想采用动态代理
    private V mProxyView;
    // View 一般都是 Activity ,涉及到内存泄漏，但是已经解绑了不会，如果没解绑就会泄漏
    // 最好还是用一下软引用

    //动态创建的 model 的对象
    private M mModel;

    public BasePresenter() {
    }

    // View 有一个特点，都是接口
    // GC 回收的算法机制（哪几种）标记清楚法
    @Override
    public void attachView(V mvpView) {
        mViewRefrence = new SoftReference<>(mvpView);
        //使用动态代理来避免重复的判断 View是否detach了
        mProxyView = (V) Proxy.newProxyInstance(
                mvpView.getClass().getClassLoader(),
                mvpView.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (mViewRefrence == null || mViewRefrence.get() == null)
                            return null;
                        return method.invoke(mViewRefrence.get(),args);
                    }
                }
        );

        // 创建我们的 Model ，动态创建？ 获取 Class 通过反射 （Activity实例创建的？class 反射创建的，布局的 View 对象怎么创建的？反射）
        // 获取 Class 对象
        Type[] params = ((ParameterizedType) Objects.requireNonNull(this.getClass().getGenericSuperclass())).getActualTypeArguments();
        try {
            mModel = (M) ((Class<? extends BaseModel>)params[1]).newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void detachView() {
        // 不解绑的问题 Activity -> Presenter  ,Presenter 持有了 Activity 应该是会有内存泄漏
        mViewRefrence.clear();
        mViewRefrence = null;
        //注释掉，如果不注释的话getMvpView方法返回的就是Null了
//        mProxyView = null;
    }

    public V getMvpView() {
        return mProxyView;
    }

    public M getModel(){
        return mModel;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate(LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart(LifecycleOwner owner) {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume(LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause(LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop(LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner source) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    public void onLifecycleChanged(LifecycleOwner source, Lifecycle.Event event) {

    }

}
