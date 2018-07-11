package com.sn.framework.core.syslog;

import com.sn.framework.core.service.IQueryService;

import java.lang.annotation.*;

/**
 * 操作日志标识
 * Created by ldm on 2018/7/11 0011.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SysLog {

    /**
     * 操作类型("新增"，"修改"，"删除"，"保存")
     * @return
     */
    String operatorType() default "";

    /**
     * 获取编辑信息的解析类，目前为使用id获取，复杂的解析需要自己实现，默认不填写
     * 则使用默认解析类
     * @return
     */
    Class parseclass() default DefaultContentParse.class;

    /**
     * 查询数据库所调用的class文件
     * @return
     */
    Class serviceclass() default IQueryService.class;

    /**
     * 业务类型
     */
    String businessType() default "";

    /**
     * 对象id值
     * @return
     */
    String idName() default "id";

    /**
     * 忽略的属性
     */
    String[] ingoreProps() default {"createdDate","createdBy","modifiedDate","modifiedBy"};
}
