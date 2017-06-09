<#if layer??>package ${layer};</#if>

import ${info.beanPackage}.${info.beanName};
import cs.model.PageModelDto;
<#if info.Dto??>
import ${info.DtoLayer}.${info.Dto};
</#if>
import cs.repository.odata.ODataObj;

/**
 * Description: ${info.comment!''} 业务操作接口
 * author: ${info.author!''}
 * Date: ${.now}
 */
public interface ${fileName!''} {
    
    PageModelDto<${info.beanName}Dto> get(ODataObj odataObj);

	void save(${info.beanName}Dto record);

	void update(${info.beanName}Dto record);

	${info.beanName}Dto findById(String deptId);

	void delete(String id);

}
