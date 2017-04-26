package cs.utils;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * shiro加密解密
 * @author wyl
 * @date 2016/10/26 0026
 */
public class Cryptography {

    /**
     * base64加密
     * @param str
     * @return
     */
    public static String encBase64(String str){
        return Base64.encodeToString(str.getBytes());
    }

    /**
     * base64解密
     * @param str
     * @return
     */
    public static String decBase64(String str){
        return Base64.decodeToString(str);
    }

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
