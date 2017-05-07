<#if layer??>package ${layer};</#if>

import cs.common.service.ServiceImpl;
import ${info.beanPackage}.${info.beanName};
import cs.model.PageModelDto;
<#if info.dto??>
import ${info.DtoLayer}.${info.Dto};
</#if>
import cs.repository.odata.ODataObj;
<#if info.dto??>
import ${info.RepoLayer}.${info.Repo};
</#if>

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: ${info.comment!''} 业务操作实现类
 * author: ${info.author!''}
 * Date: ${.now}
 */
@Service
public class ${fileName!''} extends ServiceImpl<${info.beanName}, ${info.beanName}Dto, ${info.beanName}Repo> implements ${info.beanName}Service {

    @Override
    public PageModelDto<${info.beanName}Dto> getDto(ODataObj odataObj) {
        PageModelDto<${info.beanName}Dto> page = new PageModelDto<${info.beanName}Dto>();
        List<${info.beanName}> dmList = baseRepo.findByOdata(odataObj);
        List<${info.beanName}Dto> tdoList = new ArrayList<${info.beanName}Dto>(dmList.size());
            ${info.beanName}Dto target = null;
        for (MyTest d : dmList) {
            target = new ${info.beanName}Dto(d);
            tdoList.add(target);
        }
        page.setValue(tdoList);
        page.setCount(odataObj.getCount());

        logger.info("查询数据");
        return page;
    }

}