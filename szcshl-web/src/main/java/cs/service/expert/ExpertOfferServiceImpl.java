package cs.service.expert;

import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
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

	/**
	 * 保存专家评书信息
	 * @param record
	 * @return
	 */
	@Override
	@Transactional
	public ResultMsg save(ExpertOfferDto record) {
        ExpertOffer domain =null;
        Date now = new Date();
	    if(Validate.isString(record.getId())){
            domain = expertOfferRepo.findById(record.getId());
            BeanCopierUtils.copyPropertiesIgnoreNull(record,domain);
        }else{
            domain = new ExpertOffer();
            BeanCopierUtils.copyProperties(record, domain);
            domain.setCreatedBy(SessionUtil.getDisplayName());
            domain.setCreatedDate(now);
        }
        domain.setModifiedBy(SessionUtil.getLoginName());
        domain.setModifiedDate(now);

        domain.setExpert(expertRepo.findById(record.getExpertId()));
        expertOfferRepo.save(domain);

        BeanCopierUtils.copyProperties(domain, record);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"操作成功！",record);
	}

	@Override
	@Transactional
	public void update(ExpertOfferDto record) {
		ExpertOffer domain = expertOfferRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(SessionUtil.getLoginName());
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