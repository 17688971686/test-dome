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

    /**
     * 单位类型：主办单位
     */
    public final static String ORGAN_TYPE_GXBSFGW = "GXBSFGW";

    /**
     * 单位类型：主办单位——县（区）发改委
     */
    public final static String ORGAN_TYPE_GXBSXQFGW = "GXBSXQFGW";

    /**
     * 单位类型：协办单位
     */
    public final static String ORGAN_TYPE_GXBSXBDW = "GXBSXBDW";

    /**
     * 单位类型：业主单位
     */
    public final static String ORGAN_TYPE_GXBSYZDW = "GXBSYZDW";

    /**
     * 单位类型：市重大项目管理办公室
     */
    public final static String ORGAN_TYPE_GXBSZDXMBGS = "GXBSZDXMBGS";

    public final static String ORGAN_REL_SEPARATE = "|";

    /**
     * 系统变量：系统初始化
     */
    public final static String SYS_VAR_CODE_IS_INIT = "isInit";

    /**
     * 系统变量：微信同步
     */
    public final static String SYS_VAR_CODE_WECHAT_SYN = "wechatSyn";

    /**
     * 系统变量：发送微信消息
     */
    public final static String SYS_VAR_CODE_WECHAT_MSG = "wechatMsg";

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

}