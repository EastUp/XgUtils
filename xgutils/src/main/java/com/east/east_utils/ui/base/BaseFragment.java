package com.east.east_utils.ui.base;

import androidx.fragment.app.Fragment;


public abstract class BaseFragment extends Fragment{
    //是否正在显示
    public Boolean isVisible = false;

    /**
     *  这个方法比onCreateView要先调用,当fragment结合viewpager使用的时候 这个方法会调用
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) { ////当可见的时候执行操作
            isVisible = true;
            onVisible();
        } else { ////不可见时执行相应的操作
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible(){
        lazyLoadForViewPager();
    }

    /**
     *  子类实现懒加载(ViewPager的时候使用)
     */
    protected void lazyLoadForViewPager(){}//子类实现
    protected void onInvisible(){}

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
