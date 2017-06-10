package cs.service.expert;

import java.util.List;
import java.util.Map;

import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertReviewDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 专家评审 业务操作接口
 * author: ldm
 * Date: 2017-5-17 14:02:25
 */
public interface ExpertReviewService {
    
    PageModelDto<ExpertReviewDto> get(ODataObj odataObj);

	void save(ExpertReviewDto record) throws Exception;

	void update(ExpertReviewDto record);

	ExpertReviewDto findById(String deptId);

	void delete(String id);

	List<ExpertReviewDto> initByWorkProgramId(String workProgramId);

	void save(String workProgramId, String expertIds, String selectType);

	List<ExpertDto> refleshExpert(String workProgramId, String selectType);

	void updateExpertState(String workProgramId, String expertIds, String state);

	void deleteExpert(String workProgramId, String expertIds,String seleType,String expertSelConditionId);
	
	Map<String,Object> getReviewList(String orgName,String year,String quarter);
	
	List<ExpertDto> getSelectExpert();
	
	void expertMark(String expertId,String expertMark,String expertDecride );
	
	void savePayment(ExpertReviewDto expertReviewDto)throws Exception;
	
	ExpertReviewDto getSelectExpertById(String expertId);
}
