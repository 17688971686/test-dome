<#if layer??>package ${layer};</#if>

import ${info.beanPackage}.${info.beanName};
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: ${info.comment!''} 数据操作实现类
 * author: ${info.author!''}
 * Date: ${.now}
 */
@Repository
public class ${info.beanName}RepoImpl extends AbstractRepository<${info.beanName}, String> implements ${info.beanName}Repo {
}