package com.sn.framework.core.common;

import com.sn.framework.common.ObjectUtils;
import com.sn.framework.common.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.PropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description: 读取属性文件
 *
 * @author: tzg
 * @date: 2017/12/20 19:32
 */
public final class AppYmlUtils {

    private final static Logger logger = LoggerFactory.getLogger(AppYmlUtils.class);

    // 主属性文件名称
    private final static String propName = "application.yml";
    // 环境属性文件名称
    private final static String propActName = "application-%s.yml";

    // 主要属性文件
    private static PropertySource defaultProp = null;

    static {
        // 加载配置文件
        if (defaultProp == null) {
            PropertiesUtils yml = PropertiesUtils.INSTANCE;
            defaultProp = yml.load(propName);
            Object profileActive = getProp("xss.profiles.active");
            if (profileActive != null) {
                PropertySource profileProp = yml.load(String.format(propActName, profileActive));
                ((Map) defaultProp.getSource()).putAll((Map) profileProp.getSource());
            }
        }
    }

    /**
     * 获取属性值
     *
     * @param propertyName
     * @return
     */
    public static String getProperty(String propertyName) {
        return ObjectUtils.toString(getProp(propertyName));
    }

    public static Object getProp(String propertyName) {
        if (defaultProp == null) return null;
        if (StringUtil.isBlank(propertyName)) return null;
        //从配置文件取得文件存放位置
        try {
            return defaultProp.getProperty(propertyName);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("未找到【" + propertyName + "】属性");
            }
            return null;
        }
    }

    /**
     * 获取整型属性值
     *
     * @param propertyName
     * @return
     */
    public static Integer getIntegerProperty(String propertyName) {
        return ObjectUtils.toInteger(getProp(propertyName));
    }

    /**
     * 获取列表集合属性值
     *
     * @param propertyName
     * @return
     */
    public static List getListProperty(String propertyName) {
        List list = new ArrayList();
        propertyName += "[%s]";
        Object tmp;
        int i = 0;
        while ((tmp = getProp(String.format(propertyName, i++))) != null) {
            list.add(tmp);
        }
        return list;
    }

}