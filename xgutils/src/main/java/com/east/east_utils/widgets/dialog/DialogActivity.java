package com.east.east_utils.widgets.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @description: Dialog的Activity(记得在Manifest文件中, 加入theme : dialog_acitivity)
 * @author: East
 * @date: 2019-10-28
 * |---------------------------------------------------------------------------------------------------------------|
 */
public abstract class DialogActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (fullScreen())
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        initData(savedInstanceState);
    }

    /**
     * 布局文件ID
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 是否是全屏
     * @return
     */
    public abstract boolean fullScreen();

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    protected void startActivity(Class<?> clz) {
        startActivity(new Intent(this, clz));
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    protected void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
