package com.east.east_utils.utils;

import android.app.ActivityManager;
import android.content.Context;


import java.io.File;
import java.util.List;

/**
 * Created by EastRiseWM on 2017/1/11.
 */
/*内存清理*/
public class CleanUtils {
    //定时清理
    private void clear(File mFile, Context context) {
        if (mFile.exists()) {
            PictureUtil.deleteChildFile(mFile);
        }
        DataCleanManager.cleanApplicationData(context);//清除本应用的缓存
        //清理内存
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
        List<ActivityManager.RunningServiceInfo> serviceInfos = am.getRunningServices(100);

        long beforeMem = MemoryClear.getAvailMemory(context);
        ShowLog.d("MemoryClearBefore", "-----------before memory info : " + beforeMem);
        int count = 0;
        if (infoList != null) {
            for (int i = 0; i < infoList.size(); ++i) {
                ActivityManager.RunningAppProcessInfo appProcessInfo = infoList.get(i);
                ShowLog.d("Memory", "process name : " + appProcessInfo.processName);
                //importance 该进程的重要程度  分为几个级别，数值越低就越重要。
                ShowLog.d("Memory", "importance : " + appProcessInfo.importance);

                if (appProcessInfo.processName.contains("com.android.system")
                        || appProcessInfo.pid == android.os.Process.myPid())//跳过系统 及当前进程
                    continue;

                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
                if (appProcessInfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    String[] pkgList = appProcessInfo.pkgList;
                    for (int j = 0; j < pkgList.length; ++j) {//pkgList 得到该进程下运行的包名
                        ShowLog.d("Memorykill", "It will be killed, package name : " + pkgList[j]);
                        am.killBackgroundProcesses(pkgList[j]);
                        count++;
                    }
                }
            }
        }

        long afterMem = MemoryClear.getAvailMemory(context);
        ShowLog.d("MemoryClearAfter", "----------- after memory info : " + afterMem);
        ToastUtils.show("clear " + count + " process, "
                + (afterMem - beforeMem) + "M");
    }
}
