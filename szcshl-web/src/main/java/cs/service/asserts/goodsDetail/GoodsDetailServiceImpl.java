package cs.service.asserts.goodsDetail;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.asserts.goodsDetail.GoodsDetail;
import cs.model.PageModelDto;
import cs.model.asserts.goodsDetail.GoodsDetailDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.asserts.goodsDetail.GoodsDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 物品明细 业务操作实现类
 * author: zsl
 * Date: 2017-9-15 14:15:55
 */
@Service
public class GoodsDetailServiceImpl  implements GoodsDetailService {

	@Autowired
	private GoodsDetailRepo goodsDetailRepo;
	
	@Override
	public PageModelDto<GoodsDetailDto> get(ODataObj odataObj) {
		PageModelDto<GoodsDetailDto> pageModelDto = new PageModelDto<GoodsDetailDto>();
		List<GoodsDetail> resultList = goodsDetailRepo.findByOdata(odataObj);
		List<GoodsDetailDto> resultDtoList = new ArrayList<GoodsDetailDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				GoodsDetailDto modelDto = new GoodsDetailDto();
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
	public void save(GoodsDetailDto record) {
		GoodsDetail domain = new GoodsDetail(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setCreatedBy(SessionUtil.getDisplayName());
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		goodsDetailRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(GoodsDetailDto record) {
		GoodsDetail domain = goodsDetailRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setModifiedDate(new Date());
		
		goodsDetailRepo.save(domain);
	}

	@Override
	public GoodsDetailDto findById(String id) {		
		GoodsDetailDto modelDto = new GoodsDetailDto();
		if(Validate.isString(id)){
			GoodsDetail domain = goodsDetailRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {

	}
	
}