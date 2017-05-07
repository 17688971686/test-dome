<#if layer??>package ${layer};</#if>

import cs.common.service.IService;
import ${info.beanPackage}.${info.beanName};
import cs.model.PageModelDto;
<#if info.Dto??>
import ${info.DtoLayer}.${info.Dto};
</#if>
import cs.repo.odata.ODataObj;

/**
 * Description: ${info.comment!''} 业务操作接口
 * author: ${info.author!''}
 * Date: ${.now}
 */
public interface ${fileName!''} extends IService<${info.beanName}, ${info.beanName}Dto> {

    /**
    * 获取数据
    * @param odataObj
    * @return
    */
    PageModelDto<${info.beanName}Dto> getDto(ODataObj odataObj);

}
