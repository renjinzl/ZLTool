package com.renjinzl.tool.net;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Md5 {


    /**
     * Md5加密
     *
     * return 加密后的字符串
     */
    public static String encryptionByMd5(String str_temp) {
        if (!TextUtils.isEmpty(str_temp)) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
                byte[] md5Byte = md5.digest(str_temp.getBytes("UTF8"));
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < md5Byte.length; i++) {
                    sb.append(HEX[(md5Byte[i] & 0xff) / 16]);
                    sb.append(HEX[(md5Byte[i] & 0xff) % 16]);
                }
                str_temp = sb.toString();
            } catch (Exception e) {
                return str_temp;
            }
        }
        return str_temp;
    }
}
