package cs.repository.repositoryImpl.expert;

import java.util.List;

import cs.domain.expert.Expert;
import cs.repository.IRepository;
import cs.repository.odata.ODataObj;

public interface ExpertRepo extends IRepository<Expert, String> {

    boolean checkIsHaveIdCard(String idCard,String expertId);

    List<Expert> findExpertByIdCard(String idCard);

    List<Expert> findAllRepeat();

    /**
     * 根据业务ID获取选取的专家信息(确认并参加会议的专家)
     * @param businessId
     * @return
     */
    List<Expert> findByBusinessId(String businessId);

    List<Expert> get(ODataObj odataObj);

    /**
     * 通过查询条件，查询数据-用于导出功能
     * @param filters
     * @return
     */
    List<Expert> exportData(String  filters);

    /**
     * 更新专家的综合评分
     * @param expertID
     */
    void updateExpertCompositeScore(String expertID);
}
