package com.east.east_utils.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by Administrator on 2016/7/19.
 */
public class WifiUtils {
    private boolean isFirst=true;
    // 定义WifiManager对象
    private WifiManager mWifiManager;
    // 定义WifiInfo对象
    private WifiInfo mWifiInfo;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList;
    // 网络连接列表
    private List<WifiConfiguration> mWifiConfiguration;
    // 定义一个WifiLock
    WifiManager.WifiLock mWifiLock;


    // 构造器
    public WifiUtils(Context context) {
        // 取得WifiManager对象
        mWifiManager = (WifiManager) context
                .getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // 取得WifiInfo对象
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    //查看Wifi是否连接
    public boolean isWifiOpen() {
        return mWifiManager.isWifiEnabled();
    }

    public WifiInfo getmWifiInfo(){
        return mWifiManager.getConnectionInfo();
    }

    // 打开WIFI
    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    // 关闭WIFI
    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    // 检查当前WIFI状态
    public int checkState() {
        return mWifiManager.getWifiState();
    }

    // 锁定WifiLock
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    // 解锁WifiLock
    public void releaseWifiLock() {
        // 判断时候锁定
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    // 创建一个WifiLock
    public void creatWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("Test");
    }

    // 得到配置好的网络
    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfiguration;
    }

    public void startScan() {
        mWifiManager.startScan();
        // 得到扫描结果
        mWifiList = mWifiManager.getScanResults();
        // 得到配置好的网络连接
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
    }

    // 得到网络列表
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }

    // 查看扫描结果
    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder
                    .append("Index_" + new Integer(i + 1).toString() + ":");
            // 将ScanResult信息转换成一个字符串包
            // 其中把包括：BSSID、SSID、capabilities、frequency、level
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("\n");
        }
        return stringBuilder;
    }

    // 得到MAC地址
    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    // 得到接入点的BSSID
    public String getBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    // 得到IP地址
    public int getIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    // 得到连接的ID
    public int getNetworkId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    // 得到WifiInfo的所有信息包
    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    // 添加一个网络并连接
    public void addNetwork(WifiConfiguration wcg) {
        int wcgID = mWifiManager.addNetwork(wcg);
        boolean b = mWifiManager.enableNetwork(wcgID, true);
        System.out.println("a--" + wcgID);
        System.out.println("b--" + b);
    }

    // 断开指定ID的网络
    public void disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    // 指定配置好的网络进行连接
    public void connectConfiguration(int index) {
        // 索引大于配置好的网络索引返回
        if (index > mWifiConfiguration.size()) {
            return;
        }
        // 连接配置好的指定ID的网络
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
                true);
    }


    //判定指定WIFI是否已经配置好,依据WIFI的地址BSSID,返回NetId
    public int IsConfiguration(String SSID){
        ShowLog.i("IsConfiguration", String.valueOf(mWifiConfiguration.size()));
        for(int i = 0; i < mWifiConfiguration.size(); i++){
            ShowLog.i(mWifiConfiguration.get(i).SSID, String.valueOf( mWifiConfiguration.get(i).networkId));
            if(mWifiConfiguration.get(i).SSID.equals("\""+SSID+"\"")){//地址相同
                ShowLog.v("index",i+"---i");
                return mWifiConfiguration.get(i).networkId;
            }
        }
        return -1;
    }

    /**     ScanResule
     * @param SSID  删除保存过的密码
     */
    public void deleteSavePwd(String SSID){
        int index=-1;
        for(int i = 0; i < mWifiConfiguration.size(); i++){
            ShowLog.i(mWifiConfiguration.get(i).SSID, String.valueOf( mWifiConfiguration.get(i).networkId));
            ShowLog.v("name",mWifiConfiguration.get(i).SSID+"--"+"\""+SSID+"\"");
            if(mWifiConfiguration.get(i).SSID.equals("\""+SSID+"\"")){//地址相同
                index=i;
                mWifiManager.removeNetwork(mWifiConfiguration.get(i).networkId);
                ShowLog.v("index",index+"");
                break;
            }
        }
        if(index!=-1){
            mWifiConfiguration.remove(index);
        }
        mWifiManager.saveConfiguration();

    }

    /**     WifiInfo
     * @param SSID  删除保存过的密码
     */
    public void deleteSavePwdByInfo(String SSID){
        int index=-1;
        for(int i = 0; i < mWifiConfiguration.size(); i++){
            ShowLog.i(mWifiConfiguration.get(i).SSID, String.valueOf( mWifiConfiguration.get(i).networkId));
            ShowLog.v("name",mWifiConfiguration.get(i).SSID+"--"+SSID);
            if(mWifiConfiguration.get(i).SSID.equals(SSID)){//地址相同
                index=i;
                mWifiManager.removeNetwork(mWifiConfiguration.get(i).networkId);
                ShowLog.v("index",index+"");
                break;
            }
        }
        if(index!=-1){
            mWifiConfiguration.remove(index);
        }
        mWifiManager.saveConfiguration();

    }


    //添加指定WIFI的配置信息,原列表不存在此SSID
    public void AddWifiConfig(List<ScanResult> wifiList, String ssid, String pwd){
        for(int i = 0;i < wifiList.size(); i++){
            ScanResult wifi = wifiList.get(i);
            if(wifi.SSID.equals(ssid)){
                WifiConfiguration wc = new WifiConfiguration();
                wc.SSID = ssid;//ssid
                wc.preSharedKey = pwd;//ssid密码
                wc.hiddenSSID = true;
                wc.status = WifiConfiguration.Status.ENABLED;
                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                int res = mWifiManager.addNetwork(wc);
                ShowLog.e("WifiPreference", "add Network returned " + res );
                boolean b = mWifiManager.enableNetwork(res, true);
                ShowLog.e("WifiPreference", "enableNetwork returned " + b );
            }
        }
    }

    //添加指定WIFI的配置信息 并连接
    public boolean addWifiConfigAndCon(List<ScanResult> wifiList, String ssid, String pwd){
        int wifiId = -1;
        for(int i = 0;i < wifiList.size(); i++){
            ScanResult wifi = wifiList.get(i);
            if(wifi.SSID.equals(ssid)){
                WifiConfiguration wifiCong = new WifiConfiguration();
                wifiCong.SSID = "\""+wifi.SSID+"\"";//\"转义字符，代表"
                wifiCong.preSharedKey = "\""+pwd+"\"";//WPA-PSK密码
                wifiCong.hiddenSSID = false;
                wifiCong.status = WifiConfiguration.Status.ENABLED;

                wifiId = mWifiManager.addNetwork(wifiCong);//将配置好的特定WIFI密码信息添加,添加完成后默认是不激活状态，成功返回ID，否则为-1
                ShowLog.d("wifiId",wifiId+"");
                if(wifiId != -1){
                    boolean enableNetwork = mWifiManager.enableNetwork(wifiId, true);
                    mWifiManager.saveConfiguration();//保存已经连接上的wifi信息
//                    ShowLog.d("wifiIdEnableNetwork",enableNetwork+"");
                    return enableNetwork;
                }
            }
        }
        return false;
    }

    //连接没有密码的WIFI
    public void addWifiConfigNoPWD(List<ScanResult> wifiList, String ssid){
        for(int i = 0;i < wifiList.size(); i++){
            ScanResult wifi = wifiList.get(i);
            if(wifi.SSID.equals(ssid)){
                WifiConfiguration config = new WifiConfiguration();
                config.allowedAuthAlgorithms.clear();
                config.allowedGroupCiphers.clear();
                config.allowedKeyManagement.clear();
                config.allowedPairwiseCiphers.clear();
                config.allowedProtocols.clear();
                config.SSID = "\"" + ssid + "\"";
                // 没有密码
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                mWifiManager.enableNetwork(mWifiManager.addNetwork(config), true);
            }
        }
    }




    //连接指定Id的WIFI
    public boolean ConnectWifi(int wifiId){
        for(int i = 0; i < mWifiConfiguration.size(); i++){
            WifiConfiguration wifi = mWifiConfiguration.get(i);
            if(wifi.networkId == wifiId){
                while(!(mWifiManager.enableNetwork(wifiId, true))){//激活该Id，建立连接
                    //status:0--已经连接，1--不可连接，2--可以连接
                    ShowLog.i("ConnectWifi", String.valueOf(mWifiConfiguration.get(wifiId).status));
                }
                return true;
            }
        }
        return false;
    }

    public boolean connectWifi(int wifiId){
        return mWifiManager.enableNetwork(wifiId,true);
    }


    //将搜索到的wifi根据信号强度从强到弱进行排序
    public List<ScanResult> sortByLevel(List<ScanResult> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = i+1; j < list.size(); j++) {
                if (list.get(i).level < list.get(j).level)    //level属性即为强度
                {
                    ScanResult temp = null;
                    temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }
        return list;
    }

}
