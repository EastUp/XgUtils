package com.east.east_utils.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    //判断是否有中文字符
    static String regEx = "[\u4e00-\u9fa5]";
    static Pattern pat = Pattern.compile(regEx);

    /**
     * 判断字符串中是否包含有中文文字
     */
    public static boolean isContainsChinese(String str) {
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    /**
     * String 转double类型(保留两位小数)
     */
    public static String convertToDouble(String str) {

        if (!TextUtils.isEmpty(str)) {
            try {
                double d = Double.parseDouble(str);
                if (d > 0) {
                    DecimalFormat format = new DecimalFormat("0.00");
                    String result = format.format(d);
                    return result;
                } else {
                    return "0.00";
                }
            } catch (Exception e) {
                return "0.00";
            }
        } else {
            return "0.00";
        }
    }

    /**
     * 提供字符串到字符串数组的转变,
     * 转变后的字符串以sStr作为分割符
     */
    public static String[] Str2Strs(String tStr, String sStr) {
        StringTokenizer st = new StringTokenizer(tStr, sStr);
        String[] reStrs = new String[st.countTokens()];
        int n = 0;
        while (st.hasMoreTokens()) {
            reStrs[n] = st.nextToken();
            n++;
        }
        return reStrs;
    }

    /**
     * 将String 替换操作，将str1替换为str2 *
     */
    public static String replace(String str, String str1, String str2) {
        int n = -1;
        String subStr = "";
        String re = "";
        if ((n = str.indexOf(str1)) > -1) {
            subStr = str.substring(n + str1.length(), str.length());
            re = str.substring(0, n) + str2 + replace(subStr, str1, str2);
        } else {
            re = str;
        }
        return re;
    }

    /**
     * 判断邮箱地址的正确性
     */
    public static boolean isMail(String string) {
        if (null != string) {
            if (string.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是数字
     */
    public static boolean isNumber(String mobiles) {

        if (null != mobiles) {
            Pattern p = Pattern
                    .compile("[0-9]+");
            Matcher m = p.matcher(mobiles);
            return m.matches();
        } else {
            return false;
        }
    }

    /**
     * 判断手机号码的正确性
     */
    public static boolean isMobileNumber(String mobiles) {

        if (null != mobiles) {
//            Pattern p = Pattern
//                    .compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
            Pattern p = Pattern
                    .compile("^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}$");
            Matcher m = p.matcher(mobiles);
            return m.matches();
        } else {
            return false;
        }
    }

    /**
     * 检测身份证号格式是否正确
     */
    public static boolean checkIdNum(String idNum) {
        String regExp = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(idNum);
        return m.find();
    }

    public static boolean isEmpty(String s) {
        if (s==null|| TextUtils.isEmpty(s)) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String s) {
        if (s==null|| TextUtils.isEmpty(s)) {
            return false;
        }
        return true;
    }

    public static boolean isEmptyTrimed(String s) {
        if (s==null|| TextUtils.isEmpty(s.trim())) {
            return true;
        }
        return false;
    }


    public static int getAge(String date){
        String[] split = date.split("-");
        int year = Integer.valueOf(split[0]);//2016
        int month = Integer.valueOf(split[1]);//10
        int day = Integer.valueOf(split[2]);//18
        String currentTime = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
        String[] split1 = currentTime.split("-");
        int current_year = Integer.valueOf(split1[0]);//2017
        int current_month = Integer.valueOf(split1[1]);//5
        int current_day = Integer.valueOf(split1[2]);
        if(current_month < month ){
            current_year -= 1;
        }else if(current_month == month){
            if(current_day < day){
                current_year -= 1;
            }
        }
        return current_year - year;
    }


    /**
     * 中文數字转阿拉伯数组【十万九千零六十  --> 109060】
     * @author 雪见烟寒
     * @param chineseNumber
     * @return
     */
    @SuppressWarnings("unused")
    private static int chineseNumber2Int(String chineseNumber){
        int result = 0;
        int temp = 1;//存放一个单位的数字如：十万
        int count = 0;//判断是否有chArr
        char[] cnArr = new char[]{'一','二','三','四','五','六','七','八','九'};
        char[] chArr = new char[]{'十','百','千','万','亿'};
        for (int i = 0; i < chineseNumber.length(); i++) {
            boolean b = true;//判断是否是chArr
            char c = chineseNumber.charAt(i);
            for (int j = 0; j < cnArr.length; j++) {//非单位，即数字
                if (c == cnArr[j]) {
                    if(0 != count){//添加下一个单位之前，先把上一个单位值添加到结果中
                        result += temp;
                        temp = 1;
                        count = 0;
                    }
                    // 下标+1，就是对应的值
                    temp = j + 1;
                    b = false;
                    break;
                }
            }
            if(b){//单位{'十','百','千','万','亿'}
                for (int j = 0; j < chArr.length; j++) {
                    if (c == chArr[j]) {
                        switch (j) {
                            case 0:
                                temp *= 10;
                                break;
                            case 1:
                                temp *= 100;
                                break;
                            case 2:
                                temp *= 1000;
                                break;
                            case 3:
                                temp *= 10000;
                                break;
                            case 4:
                                temp *= 100000000;
                                break;
                            default:
                                break;
                        }
                        count++;
                    }
                }
            }
            if (i == chineseNumber.length() - 1) {//遍历到最后一个字符
                result += temp;
            }
        }
        return result;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

}