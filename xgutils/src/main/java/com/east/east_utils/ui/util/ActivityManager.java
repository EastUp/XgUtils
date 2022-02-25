package com.east.east_utils.ui.util;

import android.app.Activity;

import java.util.Stack;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 *  @description: Activity管理类
 *  @author: East
 *  @date: 2020-02-15
 * |---------------------------------------------------------------------------------------------------------------|
 */
public class ActivityManager {
    private static volatile ActivityManager mInstance;
    // 集合用谁 ArrayList LinkedList Stack ?? 删除和添加比较多  Stack比较安全/内部还是使用的数组
    private Stack<Activity> mActivities;

    private ActivityManager(){
        mActivities = new Stack<>();
    }

    // 即保证线程的安全同是效率也是比较高的
    public static ActivityManager getInstance(){
        if(mInstance == null){  //如果不为null则不会进入同步锁,所以效率要高些
            synchronized (ActivityManager.class){
                if(mInstance == null){
                    mInstance = new ActivityManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取存储的activity个数
     * @return
     */
    public int getCount() {
        return mActivities.size();
    }

    /**
     * 添加统一管理
     * @param activity
     */
    public void attach(Activity activity){
        mActivities.add(activity);
    }

    /**
     * 移除解绑 - 防止内存泄漏
     * @param detachActivity
     */
    public void detach(Activity detachActivity){
        // for 去移除有没有问题？ 一边循环一边移除会出问题 ，
        // 既然这个写法有问题，自己又想不到什么解决方法，参考一下别人怎么写的
        /*for (Activity activity : mActivities) {
            if(activity == detachActivity){
                mActivities.remove(activity);
            }
        }*/

        int size = mActivities.size();
        for (int i=0; i<size; i++){
            Activity activity = mActivities.get(i);
            if(activity == detachActivity){
                mActivities.remove(i);
                i--;
                size--;
            }
        }
    }

    /**
     * 关闭当前Activity
     * @param finishActivity
     */
    public void finish(Activity finishActivity){
        int size = mActivities.size();
        for (int i=0; i<size; i++){
            Activity activity = mActivities.get(i);
            if(activity == finishActivity){
                mActivities.remove(i);
                activity.finish();
                i--;
                size--;
            }
        }
    }

    /**
     * 根据Activity的类名关闭 Activity
     */
    public void finish(Class<? extends Activity> activityClass){
        int size = mActivities.size();
        for (int i=0; i<size; i++){
            Activity activity = mActivities.get(i);
            if(activity.getClass().getCanonicalName().equals(activityClass.getCanonicalName())){
                mActivities.remove(i);
                activity.finish();
                i--;
                size--;
            }
        }
    }

    /**
     *  退出程序
     */
    public void exitApplication(){
        int size = mActivities.size();
        for (int i=0; i<size; i++){
            Activity activity = mActivities.get(i);
            mActivities.remove(i);
            activity.finish();
            i--;
            size--;
        }
    }

    /**
     * 获取指定的Activity
     *
     * @author kymjs
     */
    public Activity getActivity(Class<? extends Activity> cls){
        if (mActivities != null){
            for (Activity activity : mActivities) {
                if(activity.getClass() == cls)
                    return activity;
            }
        }
        return null;
    }


    /**
     * 获取当前的Activity (最前面)  当需要弹框的时候,弹框属于子window需要依附于应用window activity,获取到最前面的activity就好弹框了.
     * @return
     */
    public Activity stackTopActivity(){
        return mActivities.lastElement();
    }

    /**
     * 获取栈底的Activity
     * @return
     */
    public Activity stackBottomActivity(){
        return mActivities.firstElement();
    }
}
