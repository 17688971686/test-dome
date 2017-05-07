<#if repoImplPackage??>package ${repoImplPackage};</#if>

import ${beanPackage}.${beanName};
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
* Description: ${comment!''} 数据操作实现类
* author: ${author!''}
* Date: ${.now}
*/
@Repository
public class ${beanName}RepoImpl extends AbstractRepository<${beanName}, String> implements ${beanName}Repo {
}