package com.sn.framework.core.boot.properties;

import com.sn.framework.core.shiro.SNShiroRealm;
import org.apache.shiro.realm.Realm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Map;

/**
 * Description: Shiro权限配置属性类
 * @Author: tzg
 * @Date: 2017/6/13 17:54
 */
@ConfigurationProperties(prefix = "sn.shiro")
public class ShiroProperties {

    /**
     * 默认时间（单位：秒）
     */
    public final static long DEFAULT_TIME = 1800L;

    /**
     * Custom Realm
     */
    private Class<? extends Realm> realmClass = SNShiroRealm.class;

    /**
     * 登录路径
     */
    private String loginUrl = "/login";
    /**
     * 登出路径
     */
    private String logoutUrl = "/logout";
    /**
     * 系统主页路径
     */
    private String successUrl = "/index";
    /**
     * 微信端主页路径
     */
    private String wechatUrl;
    /**
     * 系统异常路径
     */
    private String unauthorizedUrl = "/403";

    /**
     * 用户登录是否需要验证码
     */
    private boolean loginCaptcha = true;
    /**
     * 是否加密
     */
    private boolean encryption = true;
    /**
     * 登录失败限制次数
     */
    private int loginFailCount = 6;
    // 加密算法名称，如：MD2/SHA-1/SHA-256/SHA-384/SHA-512
//    private String hashAlgorithmName = "MD5";
    // 加密迭代次数
//    private int hashIterations = 2;

    /**
     * session超时时间，单位秒
     */
    private long sessionTimeout = DEFAULT_TIME;

    @NestedConfigurationProperty
    private final JWT jwt = new JWT();

    public static class JWT {
        /**
         * 是否启用jwt，默认为禁用
         */
        private boolean enabled = false;
        /**
         * jwt http头部属性名
         */
        private String header = "TOKEN";
        /**
         * 秘钥
         */
        private String secret = "tangguo";
        /**
         * 有效时间
         */
        private long expiration = DEFAULT_TIME;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public long getExpiration() {
            return expiration;
        }

        public void setExpiration(long expiration) {
            this.expiration = expiration;
        }
    }


    /**
     * Filter chain
     */
    private Map<String, String> filterChainDefinitions;

    public Class<? extends Realm> getRealmClass() {
        return realmClass;
    }

    public void setRealmClass(Class<? extends Realm> realmClass) {
        this.realmClass = realmClass;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getWechatUrl() {
        return wechatUrl;
    }

    public void setWechatUrl(String wechatUrl) {
        this.wechatUrl = wechatUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public int getLoginFailCount() {
        return loginFailCount;
    }

    public void setLoginFailCount(int loginFailCount) {
        this.loginFailCount = loginFailCount;
    }

//    public String getHashAlgorithmName() {
//        return hashAlgorithmName;
//    }
//
//    public void setHashAlgorithmName(String hashAlgorithmName) {
//        this.hashAlgorithmName = hashAlgorithmName;
//    }
//
//    public int getHashIterations() {
//        return hashIterations;
//    }
//
//    public void setHashIterations(int hashIterations) {
//        this.hashIterations = hashIterations;
//    }

    public long getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public Map<String, String> getFilterChainDefinitions() {
        return filterChainDefinitions;
    }

    public void setFilterChainDefinitions(Map<String, String> filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    public boolean isLoginCaptcha() {
        return loginCaptcha;
    }

    public void setLoginCaptcha(boolean loginCaptcha) {
        this.loginCaptcha = loginCaptcha;
    }

    public boolean isEncryption() {
        return encryption;
    }

    public void setEncryption(boolean encryption) {
        this.encryption = encryption;
    }

    public JWT getJwt() {
        return jwt;
    }
}