package com.east.east_utils.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 保留小数
 */
public class DecimalUtil {
    public static String getDecimal(double value){//保留1位小数
        DecimalFormat df = new DecimalFormat("#.0");
        return df.format(value);
    }

    public static String getDecimal2(double value){//保留2位小数
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(value);
    }

    public static String getDecimal3(double value){//保留3位小数
        DecimalFormat df = new DecimalFormat("#.000");
        return df.format(value);
    }

    public static String getDecimal(double value,int saveNumber){//保留1位小数
        DecimalFormat formater = new DecimalFormat();
        //保留几位小数
        formater.setMaximumFractionDigits(saveNumber);
        //模式  四舍五入
//        formater.setRoundingMode(RoundingMode.UP);
        //模式 直接舍弃后面的数字
        formater.setRoundingMode(RoundingMode.FLOOR);
        return formater.format(value);
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * @param list  把所有命令合并成一包发
     * @return
     */
    public static byte[] getTogher(List<byte[]> list) {
        int size=0;
        for(int i=0;i<list.size();i++){
            size+=list.get(i).length;
        }
        byte[] sorce=new byte[size];
        for(int i=0;i<list.size();i++){
            int index=0;
            for(int k=0;k<i;k++){
                index+=list.get(k).length;
            }
            byte[] bytes = list.get(i);
            for (int j = index; j < bytes.length + index; j++) {
                sorce[j] = bytes[j - index];
            }
        }
        return sorce;
    }

}
