package cs.service.expert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertType;
import cs.domain.expert.ExpertType_;
import cs.model.expert.ExpertTypeDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ExpertTypeRepo;

@Service
public class ExpertTypeServiceImpl implements ExpertTypeService{
	
	@Autowired
	private ICurrentUser currentUser;
	
	@Autowired
	private ExpertTypeRepo expertTypeRepo;
	
	@Autowired
	private ExpertRepo expertRepo;

	@Override
	@Transactional
	public void saveExpertType(ExpertTypeDto expertTypeDto) {
		boolean isExpertTypeExist=expertTypeRepo.isExpertTypeExist(expertTypeDto.getExpertType());
		if(!isExpertTypeExist){
			
			ExpertType expertType=new ExpertType();
			BeanCopierUtils.copyProperties(expertTypeDto, expertType);
			expertType.setId(UUID.randomUUID().toString());
			expertType.setCreatedBy(currentUser.getDisplayName());
			expertType.setCreatedDate(new Date());
			expertType.setModifiedBy(currentUser.getDisplayName());
			expertType.setModifiedDate(new Date());
			
			Expert expert=expertRepo.findById(expertTypeDto.getExpertID());
			
			expertType.setExpert(expert);
			expertTypeRepo.save(expertType);
		}else{
			throw new IllegalArgumentException(String.format("专家类型：%s 已经存在,请重新输入！",expertTypeDto.getExpertType()));
		}
	}

	@Override
	@Transactional
	public List<ExpertTypeDto> getExpertType(ODataObj odataObj) {
		List<ExpertType> expertTypeList=expertTypeRepo.findByOdata(odataObj);
		List<ExpertTypeDto> expertTypeDtoList=new ArrayList<>();
		for(ExpertType expertType:expertTypeList){
			ExpertTypeDto expertTypeDto=new ExpertTypeDto();
			BeanCopierUtils.copyProperties(expertTypeDto, expertType);
			expertTypeDtoList.add(expertTypeDto);
		}
		return expertTypeDtoList;
	}

	@Override
	@Transactional
	public ExpertTypeDto getExpertTypeById(String expertTypeId) {
		ExpertType expertType=expertTypeRepo.findById(expertTypeId);
		ExpertTypeDto expertTypeDto =new ExpertTypeDto();
		if(expertType!=null){
			BeanCopierUtils.copyProperties(expertType, expertTypeDto);
			
			expertTypeDto.setExpertID(expertType.getExpert().getExpertID());
		}
		return expertTypeDto;
	}

	@Override
	@Transactional
	public void updateExpertType(ExpertTypeDto expertTypeDto) {
		ExpertType expertType=new ExpertType();
		BeanCopierUtils.copyProperties(expertTypeDto, expertType);
		expertType.setModifiedBy(currentUser.getDisplayName());
		expertType.setExpert(expertRepo.findById(expertTypeDto.getExpertID()));
		expertType.setModifiedDate(new Date());
		expertTypeRepo.save(expertType);
		
	}

	@Override
	@Transactional
	public void deleteExpertType(String ids) {
		String[] idss=ids.split(",");
		for(String id:idss){
			
			ExpertType expertType=expertTypeRepo.findById(id);
			expertTypeRepo.delete(expertType);
		}
		
		expertTypeRepo.deleteById(ExpertType_.id.getName(), ids);
		
	}

	
}
