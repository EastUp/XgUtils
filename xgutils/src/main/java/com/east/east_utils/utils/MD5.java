package com.east.east_utils.utils;

import com.east.east_utils.utils.log.LogUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {  
	
	private static final String TAG = "MD5";
	
    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',  
'A', 'B', 'C', 'D', 'E', 'F' };  
  
    public static String toHexString(byte[] b) {  
        StringBuilder sb = new StringBuilder(b.length * 2);  
        for (int i = 0; i < b.length; i++) {  
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);  
            sb.append(HEX_DIGITS[b[i] & 0x0f]);  
        }  
        return sb.toString();  
    }  
  
    /**  
     * ???????????md5��???  
     *   
     * @param s  
     * @return  
     */   
    public static String getMD5String(byte[] buffer) throws NoSuchAlgorithmException {  
        MessageDigest md5;  
        md5 = MessageDigest.getInstance("MD5");  
        md5.update(buffer); 
        return toHexString(md5.digest());     
    }  
    
    /**  
     * ?��????????md5��?????????????????md5???????  
     *   
     * @param buffer    ?��???byte????
     * @param md5PwdStr ?????md5��????  
     * @return  
     */   
    public static boolean checkMD5(byte[] buffer, String md5PwdStr) {  
        String s = null;
		try {
			s = getMD5String(buffer);
			LogUtils.i(TAG,s+"  ");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}  
        return  s.equals(md5PwdStr);  
    }

    // MD5加码。32位
    public static String MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];

        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString().toLowerCase();
    }
}  