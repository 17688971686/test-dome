package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.project.AssistUnit;
import cs.domain.project.AssistUnitUser;
import cs.domain.project.AssistUnitUser_;
import cs.model.PageModelDto;
import cs.model.project.AssistUnitDto;
import cs.model.project.AssistUnitUserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.AssistUnitRepo;
import cs.repository.repositoryImpl.project.AssistUnitUserRepo;

/**
 * Description: 协审单位用户 业务操作实现类
 * author: ldm
 * Date: 2017-6-9 9:37:54
 */
@Service
public class AssistUnitUserServiceImpl  implements AssistUnitUserService {

	@Autowired
	private AssistUnitUserRepo assistUnitUserRepo;
	
	@Autowired
	private AssistUnitRepo assistUnitRepo;
	
	@Autowired
	private ICurrentUser currentUser;
	
	@Override
	public PageModelDto<AssistUnitUserDto> get(ODataObj odataObj) {
		PageModelDto<AssistUnitUserDto> pageModelDto = new PageModelDto<AssistUnitUserDto>();
		List<AssistUnitUser> resultList = assistUnitUserRepo.findByOdata(odataObj);
		List<AssistUnitUserDto> resultDtoList = new ArrayList<>(resultList==null?0:resultList.size());
		
		if(resultList != null && resultList.size() > 0){
			resultList.forEach(x->{
				AssistUnitUserDto modelDto = new AssistUnitUserDto();
				BeanCopierUtils.copyProperties(x, modelDto);
				//cannot copy 
//				modelDto.setCreatedDate(x.getCreatedDate());
//				modelDto.setModifiedDate(x.getModifiedDate());
				
				AssistUnitDto assistUnit=new AssistUnitDto();
				if(x.getAssistUnit()!=null){
					assistUnit.setId(x.getAssistUnit().getId());
					assistUnit.setUnitName(x.getAssistUnit().getUnitName());
				}
				modelDto.setAssistUnit(assistUnit);
				
				resultDtoList.add(modelDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(resultDtoList);
		return pageModelDto;
	}

	@Override
	@Transactional
	public void save(AssistUnitUserDto record) {
		
		boolean isUserExist=assistUnitUserRepo.isUserExist(record.getUserName());
		if(!isUserExist){
			AssistUnitUser domain = new AssistUnitUser(); 
			BeanCopierUtils.copyProperties(record, domain); 
			domain.setId(UUID.randomUUID().toString());
			if(Validate.isString(record.getAssistUnitID())){
				AssistUnit assistUnit=assistUnitRepo.findById(record.getAssistUnitID());
				domain.setAssistUnit(assistUnit);
			}
			Date now = new Date();
			domain.setCreatedBy(currentUser.getLoginName());
			domain.setModifiedBy(currentUser.getLoginName());
			domain.setCreatedDate(now);
			domain.setModifiedDate(now);
			assistUnitUserRepo.save(domain);
		}else{
			
			throw new IllegalArgumentException(String.format("单位人员：%s 已经存在，请重新输入！", record.getUserName()));
		}
	}

	@Override
	@Transactional
	public void update(AssistUnitUserDto record) {
		AssistUnitUser domain = assistUnitUserRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setModifiedDate(new Date());
		
		assistUnitUserRepo.save(domain);
	}

	@Override
	public AssistUnitUserDto findById(String id) {		
		AssistUnitUserDto modelDto = new AssistUnitUserDto();
		if(Validate.isString(id)){
			AssistUnitUser domain = assistUnitUserRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
			if(domain.getAssistUnit()!=null){
				AssistUnitDto assistUnitDto=new AssistUnitDto();
				BeanCopierUtils.copyProperties(domain.getAssistUnit(), assistUnitDto);
				modelDto.setAssistUnit(assistUnitDto);
			}
		}	
		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {
		
		String[] ids=id.split(",");
		for(String assistUnitUserID:ids){
			
			assistUnitUserRepo.deleteById(AssistUnitUser_.id.getName(), assistUnitUserID);
		}

	}

	/* 
	 * 获取协审单位
	 */
	@Override
	@Transactional
	public List<AssistUnitDto> getAssistUnit(ODataObj odataObj) {
		
		List<AssistUnit> assistUnitList=assistUnitRepo.findByOdata(odataObj);
		List<AssistUnitDto> assistUnitDtoList=new ArrayList<>();
		for(AssistUnit assistUnit:assistUnitList){
			AssistUnitDto assistUnitDto=new AssistUnitDto();getClass();
			BeanCopierUtils.copyProperties(assistUnit,assistUnitDto);
			assistUnitDtoList.add(assistUnitDto);
			
		}
		return assistUnitDtoList;
	}

	
}