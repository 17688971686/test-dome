package com.sn.framework.core.syslog;

import com.sn.framework.core.common.Validate;
import com.sn.framework.core.service.IQueryService;
import com.sn.framework.core.util.SpringUtil;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 基础解析类
 * 单表编辑时可以直接使用id来查询
 * 如果为多表复杂逻辑，请自行编写具体实现类
 *
 * @author lw
 * @date 2018-03-02
 */
public class DefaultContentParse implements ContentParser {

    @Override
    public Object getResult(Object idValue, SysLog sysLog) {
        if(Validate.isObject(idValue)){
            IQueryService service = null;
            Class cls = sysLog.serviceclass();
            service = (IQueryService) SpringUtil.getBean(cls);

            return service.getById(idValue);
        }else{
            return null;
        }
    }
}
