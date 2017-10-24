package cs.service.expert;

import java.util.List;

import cs.common.ResultMsg;
import cs.domain.expert.Expert;
import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertSelConditionDto;
import cs.model.expert.ExpertSelectHis;
import cs.repository.odata.ODataObj;

public interface ExpertService {

    PageModelDto<ExpertDto> get(ODataObj odataObj);

    ResultMsg saveExpert(ExpertDto expertDto);

    void deleteExpert(String id);

    void deleteExpert(String[] ids);

    void updateAudit(String ids, String flag);

    ExpertDto findById(String id);

    List<ExpertDto> findAllRepeat();

    List<ExpertDto> findExpert(String minBusinessId, String reviewId, ExpertSelConditionDto[] epSelCondition);

    List<ExpertDto> countExpert(String minBusinessId, String reviewId, ExpertSelConditionDto epSelCondition);

    void savePhone(byte[] bytes, String expertId);

    byte[] findExpertPhoto(String expertId);

    int findMaxNumber();

    ResultMsg autoExpertReview(String minBusinessId, String reviewId, ExpertSelConditionDto[] paramArrary);

    /**
     * 专家统计信息
     * @param expertSelectHis
     * @return
     */
    List<ExpertSelectHis> expertSelectHis(ExpertSelectHis expertSelectHis,boolean isScore);

    /**
     * 通过查询条件，查询数据 -用于专家信息导出
     * @param filters
     * @return
     */
    List<ExpertDto> exportData(String filters);
}
