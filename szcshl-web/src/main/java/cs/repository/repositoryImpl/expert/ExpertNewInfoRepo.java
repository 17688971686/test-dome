package cs.repository.repositoryImpl.expert;

import cs.domain.expert.Expert;
import cs.domain.expert.ExpertNewInfo;
import cs.domain.expert.ExpertOffer;
import cs.repository.IRepository;

import java.util.List;

/**
 * Description:
 * author: ldm
 * Date: 2017-6-19 19:13:35
 */
public interface ExpertNewInfoRepo extends IRepository<ExpertNewInfo, String> {

    /**
     * 根据业务ID获取选取的专家信息(确认并参加会议的专家)
     * @param businessId
     * @return
     */
    List<ExpertNewInfo> findByBusinessId(String businessId);

}
