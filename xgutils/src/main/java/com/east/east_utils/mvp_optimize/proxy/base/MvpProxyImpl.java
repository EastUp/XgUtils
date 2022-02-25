package com.east.east_utils.mvp_optimize.proxy.base;



import androidx.lifecycle.LifecycleOwner;

import com.east.east_utils.mvp_optimize.BasePresenter;
import com.east.east_utils.mvp_optimize.BaseView;
import com.east.east_utils.mvp_optimize.inject.InjectPresenter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hcDarren on 2018/1/6.
 * 实现
 */
public class MvpProxyImpl<V extends BaseView> implements IMvpProxy{
    private V mView;
    private List<BasePresenter> mPresenters;
    public MvpProxyImpl(V view){
        // 做一下判断 是不是 NULL
        this.mView = view;
        mPresenters = new ArrayList<>();
    }
    @Override
    public void bindAndCreatePresenter() {
        // 这个地方要去注入 Presenter 通过反射 (Dagger) soEasy
        Field[] fields = mView.getClass().getDeclaredFields();
        for (Field field : fields) {
            InjectPresenter injectPresenter = field.getAnnotation(InjectPresenter.class);
            if(injectPresenter != null){
                // 创建注入
                Class<? extends BasePresenter> presenterClazz = null;
                // 自己去判断一下类型？ 获取继承的父类，如果不是 继承 BasePresenter 抛异常
                try {
                    presenterClazz = (Class<? extends BasePresenter>) field.getType();
//                    presenterClazz.asSubclass(BasePresenter.class);//判断是否是BasePresenter的子类，如果不是就会抛出异常
                    boolean from = BasePresenter.class.isAssignableFrom(presenterClazz);//A.class.isAssignableFrom(B.class) 判断A是否是B的父接口或超类或A跟B是同一个类或接口
                    if(!from){
                        throw new RuntimeException("No support inject presenter type " + field.getType().getName());
                    }
                } catch (Exception e){
                    // 乱七八糟一些注入
                    throw new RuntimeException("No support inject presenter type " + field.getType().getName());
                }

                try {
                    // 创建 Presenter 对象
                    BasePresenter presenter = presenterClazz.newInstance();
                    // 并没有解绑，还是会有问题，这个怎么办？1 2
                    presenter.attachView(mView);
                    // 设置
                    field.setAccessible(true);
                    field.set(mView,presenter);
                    if(mView instanceof LifecycleOwner){
                        ((LifecycleOwner)mView).getLifecycle().addObserver(presenter);
                    }
                    mPresenters.add(presenter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void unbindPresenter() {
        // 一定要解绑
        for (BasePresenter presenter : mPresenters) {
            if(mView instanceof LifecycleOwner){
                ((LifecycleOwner)mView).getLifecycle().removeObserver(presenter);
            }
            presenter.detachView();
        }
        mView = null;
    }
}
