package cs.service.expert;

import cs.model.PageModelDto;
import cs.model.expert.ExpertSelConditionDto;
import cs.repository.odata.ODataObj;

import java.util.List;
import java.util.Map;


/**
 * Description: 专家抽取条件 业务操作接口
 * author: ldm
 * Date: 2017-5-23 19:49:58
 */
public interface ExpertSelConditionService {
    
    PageModelDto<ExpertSelConditionDto> get(ODataObj odataObj);

	void save(ExpertSelConditionDto record);

	void update(ExpertSelConditionDto record);

	ExpertSelConditionDto findById(String deptId);

	void delete(String id, String workProId, boolean deleteEP);

	List<ExpertSelConditionDto> saveConditionList(ExpertSelConditionDto[] recordList) throws Exception;

	Map<String,Object> findByWorkProId(String workProId) ;
}
