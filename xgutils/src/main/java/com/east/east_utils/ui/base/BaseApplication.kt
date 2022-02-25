package com.east.east_utils.ui.base

import android.app.Activity
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import com.east.east_utils.ui.util.ActivityManager
import com.east.east_utils.ui.util.AppManager
import com.east.east_utils.utils.crash_handler.CrashHandler
import com.east.east_utils.utils.SPUtils
import com.east.east_utils.utils.Utils
import com.east.east_utils.utils.language.LanguageUtils


/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @date：2018/10/23 17:23
 * @author East
 * |---------------------------------------------------------------------------------------------------------------|
 */
open class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

//        instance = this
        initEventAndData()
    }


    /**
     * 这还有一系列的第三方SDK的初始化
     */
    private fun initEventAndData() {
        SPUtils.init(this)
        Utils.init(this)
        //        CrashUtils.getInstance().init();
        //        LogUtils2.getBuilder().setTag("MyTag").setLog2FileSwitch(true).create();

//        CrashHandler.getInstance().init(this)
//        val formatStrategy = PrettyFormatStrategy.newBuilder()
//            .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
//            //                .methodCount(0)         // (Optional) How many method line to show. Default 2
//            //                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
//            .tag("EastRiseWM")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
//            .build()
//        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
//            override fun isLoggable(priority: Int, tag: String?): Boolean {
//                return BuildConfig.DEBUG
//            }
//        })

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivityDestroyed(activity: Activity) {
                AppManager.appManager.removeActivity(activity)
                ActivityManager.getInstance().detach(activity)
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                LanguageUtils.applyLanguage(activity)  //更换语言重启后，需要重新依赖Activity
                AppManager.appManager.addActivity(activity)
                ActivityManager.getInstance().attach(activity)
            }

        })

    }

//    companion object {
//
//        @get:Synchronized
//        var instance: BaseApplication? = null
//            private set
//
//        val appResources: Resources
//            get() = instance!!.resources
//    }


}
