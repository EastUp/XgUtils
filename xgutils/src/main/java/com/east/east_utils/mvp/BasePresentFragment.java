package com.east.east_utils.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.east.east_utils.ui.base.BaseFragment;
import com.east.east_utils.utils.log.LogUtils;


/**
 * 描述：MVP的fragment父类
 * Created by East at 2018/5/11 14:12
 */
public abstract class BasePresentFragment<T extends BasePresenter> extends BaseFragment implements BaseView{
    private final String TAG = BasePresentFragment.class.getSimpleName();
    public T mPresenter;
    protected Activity mContext;
    // 标志位，标志已经初始化完成。
    public boolean isPrepared;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isPrepared = true;
        LogUtils.e(TAG,this.getClass().getSimpleName()+"：创建视图");
        return inflater.inflate(getLayout(), container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.e(TAG,this.getClass().getSimpleName()+"：onActivityCreated");
        mContext = getActivity();

        createPresenter();
        if (mPresenter != null){
            mPresenter.attachView(this);
            getLifecycle().addObserver(mPresenter);
        }
        initEventAndData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.e(TAG,this.getClass().getSimpleName()+"：onDestroyView");
        isPrepared = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            getLifecycle().removeObserver(mPresenter);
        }
        LogUtils.e(TAG,this.getClass().getSimpleName()+"：onDestroy");
    }

    protected abstract int getLayout();

    protected abstract void initEventAndData();

    protected abstract void createPresenter();



}
