package com.sn.framework.core.boot.config;

import com.sn.framework.core.boot.properties.ShiroProperties;
import com.sn.framework.core.shiro.RetryLimitCredentialsMatcher;
import com.sn.framework.core.shiro.SNFormAuthenticationFilter;
import com.sn.framework.core.shiro.SNShiroRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description: Shiro权限配置类
 * Author: tzg
 * Date: 2017/6/13 17:52
 */
@Configuration
@EnableCaching
@ConditionalOnWebApplication
@EnableConfigurationProperties(
        ShiroProperties.class
)
public class ShiroConfig {

    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

//    @Autowired
    private ShiroProperties shiroProperties;

    /**
     * shiro过滤器工厂类
     *
     * @return
     * @see ShiroFilterFactoryBean
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean createShiroFilter(ShiroProperties shiroProperties) {
        logger.info("init ShiroFilterFactoryBean");
        if (this.shiroProperties == null) {
            this.shiroProperties = shiroProperties;
        }
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager());
        bean.setLoginUrl(shiroProperties.getLoginUrl());
        bean.setSuccessUrl(shiroProperties.getSuccessUrl());
        bean.setUnauthorizedUrl(shiroProperties.getUnauthorizedUrl());

        Map<String, Filter> filterMap = new LinkedHashMap<>(2);
//        filterMap.put(JWTUtil.JWT_PREFIX, new JWTAccessControlFilter());
        filterMap.put(DefaultFilter.authc.name(), newFormAuthenticationFilter());
        bean.setFilters(filterMap);

        // 设置默认过滤链
        Map<String, String> filterChainDefinitionMap = shiroProperties.getFilterChainDefinitions();
        if (filterChainDefinitionMap == null) {
            filterChainDefinitionMap = new LinkedHashMap<>();
        }
        filterChainDefinitionMap.put(shiroProperties.getLoginUrl(), DefaultFilter.anon.name());
        filterChainDefinitionMap.put(shiroProperties.getLogoutUrl(), DefaultFilter.logout.name());
        filterChainDefinitionMap.put(shiroProperties.getSuccessUrl(), DefaultFilter.authc.name());

        filterChainDefinitionMap.put("/captchaImage", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/rsaKey", DefaultFilter.anon.name());

        filterChainDefinitionMap.put("/login/**", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/css/**", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/imgs/**", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/error/**", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/html/**", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/libs/**", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/app-dist/**", DefaultFilter.anon.name());

        filterChainDefinitionMap.put("/diagram-viewer/**", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/editor-app/**", DefaultFilter.anon.name());

//        filterChainDefinitionMap.put("/sys/init", "anon");

        filterChainDefinitionMap.put("/api/getToken", DefaultFilter.anon.name());

        filterChainDefinitionMap.put("/404", DefaultFilter.authc.name());
        filterChainDefinitionMap.put("/403", DefaultFilter.authc.name());

        filterChainDefinitionMap.put("/druid/**", "authc,perms[sys:druid]");

        filterChainDefinitionMap.put("/**", DefaultFilter.authc.name());

        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return bean;
    }

    @Bean(name = "formAuthenticationFilter")
    public SNFormAuthenticationFilter snFormAuthenticationFilter() {
        return newFormAuthenticationFilter();
    }

    protected SNFormAuthenticationFilter newFormAuthenticationFilter() {
        SNFormAuthenticationFilter formAuthenticationFilter = new SNFormAuthenticationFilter();
        formAuthenticationFilter.setLoginUrl(shiroProperties.getLoginUrl());
        formAuthenticationFilter.setSuccessUrl(shiroProperties.getSuccessUrl());
        return formAuthenticationFilter;
    }

    /**
     * @return
     * @see org.apache.shiro.mgt.SecurityManager
     */
    @Bean(name = "securityManager")
    @DependsOn("shiroRealm")
    public org.apache.shiro.mgt.SecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(shiroRealm());
        manager.setCacheManager(shiroCacheManager());
        manager.setSessionManager(defaultWebSessionManager());
        return manager;
    }

    /**
     * @return
     * @see DefaultWebSessionManager
     */
    @Bean(name = "sessionManager")
    public SessionManager defaultWebSessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setCacheManager(shiroCacheManager());
        sessionManager.setGlobalSessionTimeout(shiroProperties.getSessionTimeout() * 1000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionDAO(sessionDAO());
        return sessionManager;
    }

    @Bean
    public EnterpriseCacheSessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        sessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
        return sessionDAO;
    }

    /**
     * @return
     */
    @Bean(name = "shiroRealm")
    @DependsOn("ehCacheManager")
    public Realm shiroRealm() {
        SNShiroRealm shiroRealm = new SNShiroRealm();
        shiroRealm.setCacheManager(shiroCacheManager());
        shiroRealm.setCachingEnabled(true);
//        shiroRealm.setAuthenticationCachingEnabled(true);
//        shiroRealm.setAuthenticationCacheName("authenticationCache");
        shiroRealm.setAuthorizationCachingEnabled(true);
        shiroRealm.setAuthorizationCacheName("authorizationCache");
        shiroRealm.setCredentialsMatcher(credentialsMatcher());
        return shiroRealm;
    }


    @Bean(name = "ehCacheManager")
    @DependsOn("lifecycleBeanPostProcessor")
    public EhCacheManager shiroCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManager(cacheManagerFactory().getObject());
//        cacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return cacheManager;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了，
     * 所以我们需要修改下doGetAuthenticationInfo中的代码）
     *
     * @return
     */
    @Bean(name = "credentialsMatcher")
    @DependsOn("ehCacheManager")
    public CredentialsMatcher credentialsMatcher() {
        RetryLimitCredentialsMatcher cm = new RetryLimitCredentialsMatcher(shiroCacheManager());
        cm.setEncryption(shiroProperties.isEncryption());
        cm.setLoginFailCount(shiroProperties.getLoginFailCount());
        return cm;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager());
        return aasa;
    }

    /**
     * ehcache 主要的管理器
     *
     * @return
     */
    @Bean
    public EhCacheCacheManager cacheManager() {
        System.out.println("CacheConfiguration.ehCacheCacheManager()");
        return new EhCacheCacheManager(cacheManagerFactory().getObject());
    }

    /**
     * 据shared与否的设置，Spring分别通过CacheManager.create()
     * 或new CacheManager()方式来创建一个ehcache基地.
     * <p>
     * 也说是说通过这个来设置cache的基地是这里的Spring独用,还是跟别的(如hibernate的Ehcache共享)
     *
     * @return
     */
    @Bean
    public EhCacheManagerFactoryBean cacheManagerFactory() {
        System.out.println("CacheConfiguration.ehCacheManagerFactoryBean()");
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cacheManagerFactoryBean.setShared(true);
        return cacheManagerFactoryBean;
    }

}