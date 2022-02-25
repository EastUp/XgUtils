package com.east.east_utils.utils;

/**
 * Created by EastRiseWM on 2016/12/29.
 */

public class IpUtils {
    public static int ipToInt(String ip) {
        String[] ips = ip.split("\\.");
        return (Integer.parseInt(ips[0]) << 24) + (Integer.parseInt(ips[1]) << 16)
                + (Integer.parseInt(ips[2]) << 8) + Integer.parseInt(ips[3]);
    }

    /**
     * @param i 反的
     * @return
     */
    public static String intToIp(int i) {
        return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
                + ((i >> 8) & 0xFF) + "." + (i & 0xFF);
    }

    /**
     * @param i  正的
     * @return
     */
    public static String intToIp2(int i) {
        return (i & 0xFF)+"."+((i >> 8) & 0xFF)+"."+((i >> 16) & 0xFF)+"."+((i >> 24) & 0xFF);
    }
}
