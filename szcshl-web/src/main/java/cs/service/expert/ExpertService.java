package cs.service.expert;

import java.util.List;
import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertSelConditionDto;
import cs.model.expert.ExpertSelectHis;
import cs.repository.odata.ODataObj;

public interface ExpertService {

    PageModelDto<ExpertDto> get(ODataObj odataObj);

    ResultMsg saveExpert(ExpertDto expertDto);

    /**
     * 逻辑删除专家
     * @param id
     * @return
     */
    ResultMsg deleteExpert(String id);

    /**
     * 物理删除专家
     * @param id
     * @return
     */
    ResultMsg deleteExpertData(String id);

    void updateAudit(String ids, String flag);

    ExpertDto findById(String id);

    List<ExpertDto> findAllRepeat();

    List<ExpertDto> findExpert(String minBusinessId, String reviewId, ExpertSelConditionDto[] epSelCondition);

    List<ExpertDto> countExpert(String minBusinessId, String reviewId, ExpertSelConditionDto epSelCondition);

    void savePhone(byte[] bytes, String expertId);

    byte[] findExpertPhoto(String expertId);

    int findMaxNumber();

    /**
     * 专家 抽取
     * @param isAllExtract 是否整体专家抽取
     * @param minBusinessId
     * @param reviewId
     * @param paramArrary
     * @return
     */
    ResultMsg autoExpertReview(boolean isAllExtract,String minBusinessId, String reviewId, ExpertSelConditionDto[] paramArrary);

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

    /**
     * 获取室内境外专家
     * @param odataObj
     * @return
     */
    PageModelDto<ExpertDto> getExpertField(ODataObj odataObj);
}
