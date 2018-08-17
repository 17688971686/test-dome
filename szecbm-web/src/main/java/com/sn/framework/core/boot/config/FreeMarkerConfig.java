package com.sn.framework.core.boot.config;

import com.sn.framework.core.shiro.tag.freemarker.ShiroTags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Description: Shiro标签
 * @Author: tzg
 * @Date: 2017/6/16 19:43
 */
@Configuration
public class FreeMarkerConfig {


    @Autowired
    private freemarker.template.Configuration configuration;

    @PostConstruct
    public void setSharedVariable() {
        configuration.setSharedVariable("shiro", new ShiroTags());
    }


}