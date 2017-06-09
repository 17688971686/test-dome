<#if layer??>package ${layer};</#if>

import ${info.beanPackage}.${info.beanName};
import cs.repository.IRepository;

/**
 * Description: ${info.comment!''} 数据操作实现接口
 * author: ${info.author!''}
 * Date: ${.now}
 */
public interface ${info.beanName}Repo extends IRepository<${info.beanName}, String> {
}
