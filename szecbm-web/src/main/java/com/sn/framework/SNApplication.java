package com.sn.framework;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sn.framework.xss.StringXssDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.servlet.MultipartConfigElement;

/**
 * Description: 系统入口
 * Author: tzg
 * Date: 2017/9/1 10:30
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class,
})
public class SNApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SNApplication.class);
    }
    /**
     * 对@RequestBody 对象做xss处理
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter htmlEscapingConverter = new MappingJackson2HttpMessageConverter();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(String.class, new StringXssDeserializer());
        htmlEscapingConverter.getObjectMapper().registerModule(simpleModule);
        return htmlEscapingConverter;
    }

    /**
     * 文件上传配置(Spring Boot默认文件上传大小为2M)
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //KB,MB
        factory.setMaxFileSize("102400MB");
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("1024000MB");
        return factory.createMultipartConfig();
    }

    public static void main(String[] args) {
        SpringApplication.run(SNApplication.class, args);
    }

}