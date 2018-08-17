package com.sn.framework.core.common;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * shiro加密解密
 */
public class Cryptography {

    /**
     * md5加密
     * @param str
     * @param salt
     * @return
     */
    public static String md5(String str,String salt){
        return new Md5Hash(str, salt).toString();
    }

    /**
     * md5加密(散列次数)
     * @param str
     * @param salt
     * @return
     */
    public static String md5(String str,String salt, int hashIterations){
        return new Md5Hash(str, salt, hashIterations).toString();
    }

}
