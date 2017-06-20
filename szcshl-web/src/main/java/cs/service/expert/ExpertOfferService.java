package cs.service.expert;

import cs.domain.expert.ExpertOffer;
import cs.model.PageModelDto;
import cs.model.expert.ExpertOfferDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 专家聘书 业务操作接口
 * author: ldm
 * Date: 2017-6-19 19:13:35
 */
public interface ExpertOfferService {
    
    PageModelDto<ExpertOfferDto> get(ODataObj odataObj);

	void save(ExpertOfferDto record);

	void update(ExpertOfferDto record);

	ExpertOfferDto findById(String deptId);

	void delete(String id);

}
