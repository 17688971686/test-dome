package com.sn.framework.core.boot.config;

import com.sn.framework.odata.impl.jpa.OdataJPAMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.util.List;

/**
 * Description: xss mvc基础配置
 * @Author: tzg
 * @Date: 2017/6/16 15:24
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Value("${sn.multipart.disk-path}")
    private String diskPath;

    /**
     * xss mvc参数解析器
     *
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new OdataJPAMethodArgumentResolver());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/files/**").addResourceLocations("file:" + diskPath + File.separator);
    }

    /**
     * 注册过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean registerOpenEntityManagerInViewFilterBean() {
        FilterRegistrationBean reg = new FilterRegistrationBean(new OpenEntityManagerInViewFilter());
        reg.setName("OpenEntityManagerInViewFilter");
        reg.addUrlPatterns("/*");
        reg.setOrder(3);
        return reg;
    }


}