<#if repoPackage??>package ${repoPackage};</#if>

import ${beanPackage}.${beanName};
import cs.repo.IRepository;

/**
 * Description: ${comment!''} 数据操作实现接口
 * author: ${author!''}
 * Date: ${.now}
 */
public interface ${beanName}Repo extends IRepository<${beanName}, String> {
}
