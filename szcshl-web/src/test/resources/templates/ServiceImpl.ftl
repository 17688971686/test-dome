<#if serviceImplPackage??>package ${serviceImplPackage};</#if>

import cs.common.service.ServiceImpl;
import ${beanPackage}.${beanName};
import cs.model.PageModelDto;
import ${dtoPackage}.${beanName}Dto;
import cs.repository.odata.ODataObj;
import ${repoPackage}.${beanName}Repo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: ${comment!''} 业务操作实现类
 * author: ${author!''}
 * Date: ${.now}
 */
@Service
public class ${beanName}ServiceImpl extends ServiceImpl<${beanName}, ${beanName}Dto, ${beanName}Repo> implements ${beanName}Service {

    @Override
    public PageModelDto<${beanName}Dto> getDto(ODataObj odataObj) {
        PageModelDto<${beanName}Dto> page = new PageModelDto<${beanName}Dto>();
        List<${beanName}> dmList = baseRepo.findByOdata(odataObj);
        List<${beanName}Dto> tdoList = new ArrayList<${beanName}Dto>(dmList.size());
            ${beanName}Dto target = null;
        for (MyTest d : dmList) {
            target = new ${beanName}Dto(d);
            tdoList.add(target);
        }
        page.setValue(tdoList);
        page.setCount(odataObj.getCount());

        logger.info("查询用户数据");
        return page;
    }

}