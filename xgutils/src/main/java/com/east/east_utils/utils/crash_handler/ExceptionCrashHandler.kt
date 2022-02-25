package com.east.east_utils.utils.crash_handler

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.reflect.Field
import java.text.DateFormat
import java.text.SimpleDateFormat


/**
 * |---------------------------------------------------------------------------------------------------------------|
 *  @description: 可以上传到服务器中！！！崩溃信息收集到本地,每次崩溃都会删除以前的崩溃文件
 *  @author: jamin
 *  @date: 2020/5/6
 * |---------------------------------------------------------------------------------------------------------------|
 */
class ExceptionCrashHandler private constructor() : Thread.UncaughtExceptionHandler {

    companion object {

        const val TAG = "ExceptionCrashHandler"

        @Volatile
        private var instance: ExceptionCrashHandler? = null

        fun getInstance() =
            instance ?: synchronized(ExceptionCrashHandler::class.java) {
                instance ?: ExceptionCrashHandler().also { instance = it }
            }
    }

    private lateinit var mContext: Context
    private lateinit var mDefaultHandler: Thread.UncaughtExceptionHandler


    fun init(context: Context) {
        mContext = context

        //获取默认的处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()

        //设置这个类为默认的Crash处理器，只有调用了这行，崩溃信息才会走到uncaughtException方法
        Thread.setDefaultUncaughtExceptionHandler(this)
    }


    /**
     *  需要先调用Thread.setDefaultUncaughtExceptionHandler(this)，所有的崩溃信息才会走到这
     */
    override fun uncaughtException(t: Thread, e: Throwable) {
        // 全局异常
//        Log.e(TAG, "程序崩溃了")

        //写入到本地文件   ex  当前的版本   手机信息

        // 1.崩溃的详细信息
        // 2.应用信息 包名  版本号
        // 3.手机信息
        // 4.保存当前文件，等应用再次启动再上传  （上传文件不再这处理）

        val crashFileName = saveInfoToSD(e)

//        Log.e(TAG, "fileName --> $crashFileName")

        // 3. 缓存崩溃日志文件

        // 3. 缓存崩溃日志文件
        cacheCrashFile(crashFileName!!)

        //交给系统默认的错误处理器去处理
        mDefaultHandler.uncaughtException(t, e)
    }


    /**
     * 缓存崩溃日志文件
     *
     * @param fileName
     */
    private fun cacheCrashFile(fileName: String) {
        val sp =
            mContext.getSharedPreferences("crash", Context.MODE_PRIVATE)
        sp.edit().putString("CRASH_FILE_NAME", fileName).commit()
    }

    /**
     * 获取崩溃文件名称
     *
     * @return
     */
    fun getCrashFile(): File? {
        val crashFileName = mContext.getSharedPreferences(
            "crash",
            Context.MODE_PRIVATE
        ).getString("CRASH_FILE_NAME", "")
        return if (TextUtils.isEmpty(crashFileName)) null else
            File(crashFileName)
    }

    /**
     * 保存获取的 软件信息，设备信息和出错信息保存在SDcard中
     *
     * @param ex
     * @return
     */
    private fun saveInfoToSD(ex: Throwable): String? {
        var fileName: String? = null
        val sb = StringBuffer()

        // 1. 手机信息 + 应用信息   --> obtainSimpleInfo()
        for ((key, value) in obtainSimpleInfo(mContext).entries) {
            sb.append(key).append(" = ").append(value).append("\n")
        }

        // 2.崩溃的详细信息
        sb.append(obtainExceptionInfo(ex))

        // 保存文件  手机应用的目录，并没有拿手机sdCard目录， 6.0 以上需要动态申请权限
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val dir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath + File.separator + "crash")
            } else {
                File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "crash")
            }

            // 先删除之前的异常信息
            if (dir.exists()) {
                // 删除该目录下的所有子文件
                deleteDir(dir)
            }

            // 再从新创建文件夹
            if (!dir.exists()) {
                dir.mkdir()
            }
            try {
                fileName =
                    """${dir.absolutePath}${File.separator}crash_${getAssignTime("yyyy_MM_dd_HH_mm")}.log"""
                val fos = FileOutputStream(fileName)
                fos.write(sb.toString().toByteArray())
                fos.flush()
                fos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return fileName
    }

    private fun getAssignTime(dateFormatStr: String): String {
        val dataFormat: DateFormat = SimpleDateFormat(dateFormatStr)
        val currentTime = System.currentTimeMillis()
        return dataFormat.format(currentTime)
    }

    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在HashMap中
     *
     * @return
     */
    private fun obtainSimpleInfo(context: Context): HashMap<String, String> {
        val map: HashMap<String, String> = HashMap()
        val mPackageManager = context.packageManager
        var mPackageInfo: PackageInfo? = null
        try {
            mPackageInfo = mPackageManager.getPackageInfo(
                context.packageName, PackageManager.GET_ACTIVITIES
            )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        map["versionName"] = mPackageInfo!!.versionName
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            map["versionCode"] = "" + mPackageInfo.longVersionCode
        else
            map["versionCode"] = "" + mPackageInfo.versionCode
        map["MOBLE_INFO"] = getMobileInfo()
        return map
    }

    /**
     * 获取手机信息  HomiNote 6.0
     *
     * @return
     */
    fun getMobileInfo(): String {
        val sb = StringBuffer()
        try {
            // 利用反射获取 Build 的所有属性
            val fields: Array<Field> = Build::class.java.declaredFields
            for (field in fields) {
                field.isAccessible = true
                val name: String = field.name
                val value: String = field.get(null).toString()
                sb.append("$name=$value")
                sb.append("\n")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sb.toString()
    }


    /**
     * 获取系统未捕捉的错误信息
     *
     * @param throwable
     * @return
     */
    private fun obtainExceptionInfo(throwable: Throwable): String? {
        // Java基础 异常
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        throwable.printStackTrace(printWriter)
        printWriter.close()
        return stringWriter.toString()
    }


    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful. If a
     * deletion fails, the method stops attempting to delete and returns
     * "false".
     */
    private fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children: Array<File> = dir.listFiles()
            // 递归删除目录中的子目录下
            for (child in children) {
                child.delete()
            }
        }
        // 目录此时为空，可以删除
        return true
    }
}