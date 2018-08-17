package com.sn.framework.core;

/**
 * Description: 系统常量
 * Author: tzg
 * Date: 2017/9/1 11:11
 */
public final class Constants {

    /**
     * 系统当前用户key
     */
    public final static String USER_SESSION_KEY = "currentUser";

    /**
     * 系统当前配置key
     */
    public final static String CONFIG_SESSION_KEY = "configParam";

    /**
     * 缓存键：系统缓存
     */
    public final static String CACHE_KEY_SYS_CACHE = "sysCache";

    /**
     * 缓存键：系统资源
     */
    public final static String CACHE_KEY_SYS_RESOURCE = "sysResourceCache";

    /**
     * 用户名：系统管理员
     */
    public final static String USER_KEY_ADMIN = "admin";

    /**
     * 角色名称：系统管理员
     */
    public final static String ROLE_KEY_ADMIN = "ADMIN";

    /**
     * 单位类型：市领导
     */
    public final static String ORGAN_TYPE_GXBSSLD = "GXBSSLD";


    public final static String ORGAN_REL_SEPARATE = "|";

    /**
     * 系统变量：系统初始化
     */
    public final static String SYS_VAR_CODE_IS_INIT = "isInit";


    public enum FlowUserGroupType {
        Organ,             // 机构
        Role               // 角色
    }

    /**
     * 系统返回码
     */
    public enum ReCode{
        ERROR,
        OK
    }

    /**
     * 处理意见环节
     * comment nodekey
     */

    public enum NodeKey{
        XM_FGLD,            //项目分管领导
        XM_BZ,              //项目部长
        XM_QSR
    }


    /**
     * 模板枚举
     */
    public static enum Template {
        WORD_SUFFIX(".doc", "work文档"),
        PROJECT_DETAIL("project/projectInfo","工程信息统计表");
        private String key;
        private String value;

        Template(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getKey() {
            return key;
        }
    }
}