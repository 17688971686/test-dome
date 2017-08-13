package cs.service.expert;

import cs.common.ResultMsg;
import cs.domain.expert.ExpertSelected;
import cs.model.PageModelDto;
import cs.model.expert.ExpertSelectedDto;
import cs.repository.odata.ODataObj;

import java.util.List;

/**
 * Description: 抽取专家 业务操作接口
 * author: ldm
 * Date: 2017-6-14 14:26:49
 */
public interface ExpertSelectedService {

	void save(ExpertSelectedDto record);

	void update(ExpertSelectedDto record);

	ExpertSelectedDto findById(String deptId);

	ResultMsg delete(String conditionId, String id, boolean deleteAll);
}
