package com.sn.framework.module.sys.domain;


import com.sn.framework.common.ObjectUtils;

import java.util.Date;

/**
 * Description: 系统变量参数值类型
 *
 * @author: tzg
 * @date: 2018/1/24 10:22
 */
public enum SysVariableType {
    STRING("字符串") {
        @Override
        public String getValue(String varValue) {
            return varValue;
        }
    },
    BOOLEAN("布尔值") {
        @Override
        public Boolean getValue(String varValue) {
            return Boolean.valueOf(varValue);
        }
    },
    INTEGER("整型") {
        @Override
        public Integer getValue(String varValue) {
            return Integer.parseInt(varValue);
        }
    },
    DATETIME("时间") {
        /**
         * 日期格式为 yyyy-MM-dd HH:mm:ss 的字符串
         * @param varValue
         * @return
         */
        @Override
        public Date getValue(String varValue) {
            return ObjectUtils.toDatetime(varValue);
        }
    },
    DATE("日期") {
        /**
         * 日期格式为 yyyy-MM-dd 的字符串
         * @param varValue
         * @return
         */
        @Override
        public Date getValue(String varValue) {
            return ObjectUtils.toDate(varValue);
        }
    },
    ;

    private String typeName;

    SysVariableType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public abstract Object getValue(String varValue);
}
