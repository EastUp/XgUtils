package com.east.east_utils.mvp_optimize;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.east.east_utils.mvp_optimize.proxy.base.ActivityMvpProxy;
import com.east.east_utils.mvp_optimize.proxy.ActivityMvpProxyImpl;
import com.east.east_utils.utils.ToastUtils;


/**
 * 描述：MVP模式下的activity的基础类
 * Created by East at 2018/2/5 11:00
 */

public abstract class BaseMvpActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected P mPresenter;
    protected Activity mContext;
    private ActivityMvpProxy mMvpProxy;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ScreenUtils.adaptScreenCustom(this,1920,true);
        setContentView(getLayout());
        mContext = this;
        mPresenter = createPresenter();

        if (mPresenter != null){
            mPresenter.attachView(this);
            getLifecycle().addObserver(mPresenter);
        }

        mMvpProxy = createMvpProxy();
        mMvpProxy.bindAndCreatePresenter();

        initEventAndData();
    }

    /**
     * 创建 Mvp 的代理
     * @return
     */
    private ActivityMvpProxy createMvpProxy() {
        return new ActivityMvpProxyImpl<>(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            getLifecycle().removeObserver(mPresenter);
            mPresenter.detachView();
        }
        mMvpProxy.unbindPresenter();
    }

    protected void setToolBar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    protected abstract int getLayout();

    protected abstract void initEventAndData();

//    protected abstract boolean toggleOverridePendingTransition();

//    protected abstract TransitionMode getOverridePendingTransitionMode();

    protected abstract P createPresenter();


    @Override
    public void showToast(String msg) {
        ToastUtils.show(msg);
    }
}
