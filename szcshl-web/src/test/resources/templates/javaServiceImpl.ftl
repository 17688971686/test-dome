<#if layer??>package ${layer};</#if>

import cs.common.service.ServiceImpl;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import ${info.beanPackage}.${info.beanName};
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
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
public class ${fileName!''}  implements ${info.beanName}Service {

	@Autowired
	private ${info.beanName}Repo ${info.beanName?uncap_first}Repo;
	
	@Override
	public PageModelDto<${info.beanName}Dto> get(ODataObj odataObj) {
		PageModelDto<${info.beanName}Dto> pageModelDto = new PageModelDto<${info.beanName}Dto>();
		List<${info.beanName}> resultList = ${info.beanName?uncap_first}Repo.findByOdata(odataObj);
		List<${info.beanName}Dto> resultDtoList = new ArrayList<${info.beanName}Dto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				${info.beanName}Dto modelDto = new ${info.beanName}Dto();
				BeanCopierUtils.copyProperties(x, modelDto);
				resultDtoList.add(modelDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(resultDtoList);
		return pageModelDto;
	}

	@Override
	@Transactional
	public void save(${info.beanName}Dto record) {
		${info.beanName} domain = new ${info.beanName}(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setCreatedBy(SessionUtil.getDisplayName());
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		${info.beanName?uncap_first}Repo.save(domain);
	}

	@Override
	@Transactional
	public void update(${info.beanName}Dto record) {
		${info.beanName} domain = ${info.beanName?uncap_first}Repo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setModifiedDate(new Date());
		
		${info.beanName?uncap_first}Repo.save(domain);
	}

	@Override
	public ${info.beanName}Dto findById(String id) {		
		${info.beanName}Dto modelDto = new ${info.beanName}Dto();
		if(Validate.isString(id)){
			${info.beanName} domain = ${info.beanName?uncap_first}Repo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {

	}
	
}