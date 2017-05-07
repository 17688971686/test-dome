<#if servicePackage??>package ${servicePackage};</#if>

import cs.common.service.IService;
import ${beanPackage}.${beanName};
import cs.model.PageModelDto;
import ${dtoPackage}.${beanName}Dto;
import cs.repo.odata.ODataObj;

/**
 * Description: ${comment!''} 业务操作接口
 * author: ${author!''}
 * Date: ${.now}
 */
public interface ${beanName}Service extends IService<${beanName}, ${beanName}Dto> {

    /**
    * 获取数据
    * @param odataObj
    * @return
    */
    PageModelDto<${beanName}Dto> getDto(ODataObj odataObj);

}
