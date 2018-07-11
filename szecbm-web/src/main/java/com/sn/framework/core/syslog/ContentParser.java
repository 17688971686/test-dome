package com.sn.framework.core.syslog;

import java.util.Map;


/**
 * 解析接口
 *
 * @author lw
 * @date 2018-03-02
 */

public interface ContentParser {

    /**
     * 获取信息返回查询出的对象
     * @param idValue Id值
     * @return
     */
    Object getResult(Object idValue,SysLog sysLog);
}

