package com.sn.framework.core.common;

import com.sn.framework.common.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

/**
 * Description: 属性文件读取工具（YAML属性文件使用spring boot的YamlPropertySourceLoader加载，
 * properties属性文件使用spring boot的PropertiesPropertySourceLoader加载）
 *
 * @author: tzg
 * @date: 2017/12/20 19:33
 */
public enum PropertiesUtils {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    private static PropertySourceLoader loader = null;

    /**
     * @param propertyFile 属性文件路径
     * @return
     */
    public static PropertySource load(String propertyFile) {
        if (StringUtil.isBlank(propertyFile)) return null;
        String suffix = null;
        if (propertyFile.lastIndexOf(".") > 0) {
            suffix = propertyFile.substring(propertyFile.lastIndexOf(".") + 1);
        } else {
            logger.warn("【" + propertyFile + "】缺少后缀名");
        }
        return load(propertyFile, suffix);
    }

    /**
     * @param propertyFile 属性文件路径
     * @param suffix       文件扩展名
     * @return
     */
    public static PropertySource load(String propertyFile, String suffix) {
        if (StringUtil.isAnyBlank(propertyFile, suffix)) return null;
        try {
            if (loader == null && StringUtil.isNotBlank(suffix)) {
                if ("properties".equalsIgnoreCase(suffix)) {
                    loader = new PropertiesPropertySourceLoader();
                } else //if ("yml".equalsIgnoreCase(suffix) || "yaml".equalsIgnoreCase(suffix))
                {
                    loader = new YamlPropertySourceLoader();
                }
            }
            return loader.load(propertyFile, new ClassPathResource(propertyFile), null);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取属性文件的某个属性
     *
     * @param propertyFile 属性文件路径
     * @param propertyName 属性名
     * @return
     */
    public static Object getProperty(String propertyFile, String propertyName) {
        PropertySource ps = load(propertyFile, null);
        return ps == null ? null : ps.getProperty(propertyName);
    }

}