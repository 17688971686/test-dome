package com.sn.framework.core.shiro;

import com.sn.framework.core.common.SNKit;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 凭证匹配器
 *
 * @author: tzg
 * @date: 2017/12/26 16:02
 */
public class RetryLimitCredentialsMatcher extends SimpleCredentialsMatcher {

    private Cache<String, AtomicInteger> passwordRetryCache;
    /**
     * 是否加密
     */
    private boolean encryption = true;
    /**
     * 密码错误限制次数
     */
    private int loginFailCount = 6;

    public boolean isEncryption() {
        return encryption;
    }

    public void setEncryption(boolean encryption) {
        this.encryption = encryption;
    }

    public int getLoginFailCount() {
        return loginFailCount;
    }

    public void setLoginFailCount(int loginFailCount) {
        this.loginFailCount = loginFailCount;
    }

    public RetryLimitCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        //retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        if (retryCount.incrementAndGet() >= loginFailCount) {
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }

        Object tokenCredentials = getCredentials(token);
        Object accountCredentials = getCredentials(info);
        boolean matches = equals(tokenCredentials, accountCredentials);
        if (matches) {
            //clear retry count
            passwordRetryCache.remove(username);
        }
        return matches;
    }

    @Override
    protected Object getCredentials(AuthenticationToken token) {
        SNToken snToken = (SNToken) token;
        String tokenCred = String.valueOf(snToken.getPassword());
        if (encryption && token instanceof SNWebToken) {
            tokenCred = SNKit.decodePwd(snToken.getUsername(), tokenCred, snToken.getUserInfo().getUserSalt());
        }
        return tokenCred;
    }

}