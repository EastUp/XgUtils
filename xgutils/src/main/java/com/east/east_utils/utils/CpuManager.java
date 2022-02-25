package com.east.east_utils.utils;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.east.east_utils.utils.log.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.regex.Pattern;


/**
 * 项目名称：DataBaseDemo
 * 创建人：East
 * 创建时间：2017/8/3 11:10
 * 邮箱：EastRiseWM@163.com
 */

public class CpuManager {

    private static final String TAG = CpuManager.class.getSimpleName();
    private static final long ERROR = -1;

    //CPU个数
    public static int getNumCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if(Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            LogUtils.d(TAG, "CPU Count: "+files.length);
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch(Exception e) {
            //Print exception
            LogUtils.d(TAG, "CPU Count: Failed.");
            e.printStackTrace();
            //Default to return 1 core
            return 1;
        }
    }


    // 获取CPU最大频率（单位KHZ）
    // "/system/bin/cat" 命令行
    // "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" 存储最大频率的文件的路径
    public static String getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    // 获取CPU最小频率（单位KHZ）
    public static String getMinCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    // 实时获取CPU当前频率（单位KHZ）
    public static String getCurCpuFreq() {
        String result = "N/A";
        try {
            FileReader fr = new FileReader(
                    "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            result = text.trim();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 获取CPU名字
    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            LogUtils.d(TAG,array[1]);
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 内存：/proc/meminfo
     */
    public static void getTotalMemory(){
        String str1="/proc/meminfo";
        String str2="";
        try{
            FileReader fr=new FileReader(str1);
            BufferedReader localBufferedReader=new BufferedReader(fr,8192);
            while((str2=localBufferedReader.readLine())!=null){
                LogUtils.i(TAG,"---"+str2);
            }
        }catch(IOException e){
        }
    }


    /**
     * 获取可用手机内存(RAM)
     * @return
     */
    public static long getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        String formatSize = formatSize(mi.availMem);
        LogUtils.d(TAG,"RAM为："+formatSize);
        return mi.availMem;
    }


    /**
     * 获取手机内部空间大小
     * @return
     */
    public static long getTotalInternalStorgeSize() {
        File path = Environment.getDataDirectory();
        StatFs mStatFs = new StatFs(path.getPath());
        long blockSize = mStatFs.getBlockSize();
        long totalBlocks = mStatFs.getBlockCount();
        return totalBlocks * blockSize;
    }
    /**
     * 获取手机内部可用空间大小
     * @return
     */
    public static long getAvailableInternalStorgeSize() {
        File path = Environment.getDataDirectory();
        StatFs mStatFs = new StatFs(path.getPath());
        long blockSize = mStatFs.getBlockSize();
        long availableBlocks = mStatFs.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机外部空间大小
     * @return
     */
    public static long getTotalExternalStorgeSize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs mStatFs = new StatFs(path.getPath());
            long blockSize = mStatFs.getBlockSize();
            long totalBlocks = mStatFs.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    /**
     * 获取手机外部可用空间大小
     * @return
     */
    public static long getAvailableExternalStorgeSize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs mStatFs = new StatFs(path.getPath());
            long blockSize = mStatFs.getBlockSize();
            long availableBlocks = mStatFs.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return ERROR;
        }
    }


    /* 返回为字符串数组[0]为大小[1]为单位KB或MB */
    public static String formatSize(long size) {
        String suffix = "";
        float fSzie = 0;
        if (size >= 1024) {
            suffix = "KB";
            fSzie = size / 1024;
            if (fSzie >= 1024) {
                suffix = "MB";
                fSzie /= 1024;
                if (fSzie >= 1024) {
                    suffix = "GB";
                    fSzie /= 1024;
                }
            }
        }

        DecimalFormat formatter = new DecimalFormat("#0.00");// 字符显示格式
        /* 每3个数字用,分隔，如1,000 */
        formatter.setGroupingSize(3);
        StringBuilder resultBuffer = new StringBuilder(formatter.format(fSzie));
        if (suffix != null) {
            resultBuffer.append(suffix);
        }
        return resultBuffer.toString();
    }

    /**
     * 外部存储(SDCard)是否可用
     * @return
     */
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }


    /**
     *  Rom大小
     *
     * @return
     */
    public static long[]getRomMemroy(){
        long[]romInfo=new long[2];
        //Total rom memory
        romInfo[0]=getTotalInternalMemorySize();

        //Available rom memory
        File path=Environment.getDataDirectory();
        StatFs stat=new StatFs(path.getPath());
        long blockSize=stat.getBlockSize();
        long availableBlocks=stat.getAvailableBlocks();
        romInfo[1]=blockSize*availableBlocks;
        LogUtils.d(TAG,"可用的内部存储大小："+romInfo[1]);
        getVersion();
        return romInfo;
    }

    public static long getTotalInternalMemorySize(){
        File path= Environment.getDataDirectory();
        StatFs stat=new StatFs(path.getPath());
        long blockSize=stat.getBlockSize();
        long totalBlocks=stat.getBlockCount();
        LogUtils.d(TAG,"内部存储大小："+totalBlocks*blockSize);
        return totalBlocks*blockSize;
    }

    /**
     *  sdCard大小
     *
     * @return
     */
    public static long[]getSDCardMemory(){
        long[]sdCardInfo=new long[2];
        String state=Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            File sdcardDir=Environment.getExternalStorageDirectory();
            StatFs sf=new StatFs(sdcardDir.getPath());
            long bSize=sf.getBlockSize();
            long bCount=sf.getBlockCount();
            long availBlocks=sf.getAvailableBlocks();

            sdCardInfo[0]=bSize*bCount;//总大小
            sdCardInfo[1]=bSize*availBlocks;//可用大小
        }
        return sdCardInfo;
    }

    /**
     *  电池电量
     */
    private static BroadcastReceiver batteryReceiver=new BroadcastReceiver(){

        @Override
        public void onReceive(Context context,Intent intent){
            int level=intent.getIntExtra("level",0);
            //  level加%就是当前电量了
        }
    };
//    registerReceiver(batteryReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


    /**
     *
     *  系统的版本信息
     *
     * @return
     */
    public static String[]getVersion(){
        String[]version={"null","null","null","null"};
        String str1="/proc/version";
        String str2;
        String[]arrayOfString;
        try{
            FileReader localFileReader=new FileReader(str1);
            BufferedReader localBufferedReader=new BufferedReader(
                    localFileReader,8192);
            str2=localBufferedReader.readLine();
            arrayOfString=str2.split("\\s+");
            version[0]=arrayOfString[2];//KernelVersion
            localBufferedReader.close();
        }catch(IOException e){
        }
        version[1]= Build.VERSION.RELEASE;// firmware version
        version[2]=Build.MODEL;//model
        version[3]=Build.DISPLAY;//system version
        return version;
    }

//    public String[] getOtherInfo(){
//        String[]other={"null","null"};
//        WifiManager wifiManager=(WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo=wifiManager.getConnectionInfo();
//        if(wifiInfo.getMacAddress()!=null){
//            other[0]=wifiInfo.getMacAddress();
//        }else{
//            other[0]="Fail";
//        }
//        other[1]=getTimes();
//        return other;
//    }
//    private String getTimes(){
//        long ut= SystemClock.elapsedRealtime()/1000;
//        if(ut==0){
//            ut=1;
//        }
//        int m=(int)((ut/60)%60);
//        int h=(int)((ut/3600));
//        return h+" "+mContext.getString(R.string.info_times_hour)+m+" "
//                +mContext.getString(R.string.info_times_minute);
//    }
}





