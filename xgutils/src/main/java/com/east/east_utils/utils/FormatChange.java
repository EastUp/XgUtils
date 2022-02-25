package com.east.east_utils.utils;

import java.math.BigInteger;

/**
 * 进制转换工具类
 * Created by Administrator on 2015/12/9.
 */
public class FormatChange {
    public static String intToHexString(int value, int length) {
        String hex = Integer.toHexString(value);
        int count = length * 2 - hex.length();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                hex = "0" + hex;
            }
        }
        if (count < 0) {
            hex = hex.substring(0 - count);
        }
        return hex;
    }

    public static String byteArrayToHexString(byte[] bytes) {
        String hexString = "";
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString((int) bytes[i]);
            if (hex.length() < 2) {
                hex = "0" + hex;
            }
            if (hex.length() > 2) {
                hex = hex.substring(hex.length() - 2, hex.length());
            }
            hexString = hexString + hex;
        }
        return hexString;
    }


    /**
     * 字节数组转16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * byteArr转hexString
     * <p>例如：</p>
     * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
     *
     * @param bytes 字节数组
     * @return 16进制大写字符串
     */
    public static String ByteArrToHex(byte[] bytes) {
        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }


    public static String Byte2Hex(Byte paramByte) {
        return String.format("%02x", new Object[]{paramByte}).toLowerCase();
    }

//    public static String ByteArrToHex(byte[] paramArrayOfByte) {
//        StringBuilder localStringBuilder = new StringBuilder();
//        int j = paramArrayOfByte.length;
//        int i = 0;
//        for (; ; ) {
//            if (i >= j) {
//                return localStringBuilder.toString();
//            }
//            localStringBuilder.append(Byte2Hex(Byte.valueOf(paramArrayOfByte[i])));
////            localStringBuilder.append(" ");
//            i += 1;
//        }
//    }

    /**
     * 16进制字符串转字节数组
     *
     * @param hexString
     * @return
     */

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    public static byte[] change(String inputStr) {
        byte[] result = new byte[inputStr.length() / 2];
        for (int i = 0; i < inputStr.length() / 2; ++i)
            result[i] = (byte) (Integer.parseInt(inputStr.substring(i * 2, i * 2 + 2), 16) & 0xff);
        return result;
    }

    /*将int转为低字节在前，高字节在后的byte数组
    b[0] = 11111111(0xff) & 01100001
    b[1] = 11111111(0xff) & (n >> 8)00000000
    b[2] = 11111111(0xff) & (n >> 8)00000000
    b[3] = 11111111(0xff) & (n >> 8)00000000
    */
    public static byte[] IntToByteArray(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    //将低字节在前转为int，高字节在后的byte数组(与IntToByteArray1想对应)
    public static int ByteArrayToInt(byte[] bArr) {
        if (bArr.length != 4) {
            return -1;
        }
        return (int) ((((bArr[3] & 0xff) << 24)
                | ((bArr[2] & 0xff) << 16)
                | ((bArr[1] & 0xff) << 8)
                | ((bArr[0] & 0xff) << 0)));
    }

    /**
     * 将byte[]转为各种进制的字符串
     * @param bytes byte[]
     * @param radix 基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String bytesToString(byte[] bytes, int radix){
//        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
        return new BigInteger( bytes).toString(radix);
    }


}
