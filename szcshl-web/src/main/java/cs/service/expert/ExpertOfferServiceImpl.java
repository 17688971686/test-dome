package cs.service.expert;

import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertOffer;
import cs.domain.expert.ExpertOffer_;
import cs.model.PageModelDto;
import cs.model.expert.ExpertOfferDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertOfferRepo;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * Description: 专家聘书 业务操作实现类
 * author: ldm
 * Date: 2017-6-19 19:13:35
 */
@Service
public class ExpertOfferServiceImpl  implements ExpertOfferService {

    @Autowired
	private ExpertOfferRepo expertOfferRepo;
    @Autowired
    private ExpertRepo expertRepo;

	@Autowired
	private ICurrentUser currentUser;

	@Override
	public PageModelDto<ExpertOfferDto> get(ODataObj odataObj) {
		PageModelDto<ExpertOfferDto> pageModelDto = new PageModelDto<ExpertOfferDto>();
		List<ExpertOffer> resultList = expertOfferRepo.findByOdata(odataObj);
		List<ExpertOfferDto> resultDtoList = new ArrayList<ExpertOfferDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				ExpertOfferDto modelDto = new ExpertOfferDto();
				BeanCopierUtils.copyProperties(x, modelDto);
				//cannot copy 
				modelDto.setCreatedDate(x.getCreatedDate());
				modelDto.setModifiedDate(x.getModifiedDate());
				
				resultDtoList.add(modelDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(resultDtoList);
		return pageModelDto;
	}

	@Override
	@Transactional
	public void save(ExpertOfferDto record) {
	    if(Validate.isString(record.getExpertId())){
            ExpertOffer domain = new ExpertOffer();
            BeanCopierUtils.copyProperties(record, domain);
            Date now = new Date();
            domain.setCreatedBy(currentUser.getLoginName());
            domain.setModifiedBy(currentUser.getLoginName());
            domain.setCreatedDate(now);
            domain.setModifiedDate(now);

            domain.setExpert(expertRepo.findById(record.getExpertId()));
            expertOfferRepo.save(domain);
        }else{
	        throw new IllegalArgumentException("操作失败，获取不到专家信息！");
        }
	}

	@Override
	@Transactional
	public void update(ExpertOfferDto record) {
		ExpertOffer domain = expertOfferRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setModifiedDate(new Date());
		
		expertOfferRepo.save(domain);
	}

	@Override
	public ExpertOfferDto findById(String id) {		
		ExpertOfferDto modelDto = new ExpertOfferDto();
		if(Validate.isString(id)){
			ExpertOffer domain = expertOfferRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {
        expertOfferRepo.deleteById(ExpertOffer_.id.getName(),id);
	}
	
}