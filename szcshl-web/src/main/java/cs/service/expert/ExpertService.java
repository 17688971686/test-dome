package cs.service.expert;

import java.util.List;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertSelConditionDto;
import cs.repository.odata.ODataObj;

public interface ExpertService {

    PageModelDto<ExpertDto> get(ODataObj odataObj);

    String createExpert(ExpertDto expertDto);

    void deleteExpert(String id);

    void deleteExpert(String[] ids);

    void updateExpert(ExpertDto expertDto);

    void updateAudit(String ids, String flag);

    ExpertDto findById(String id);

    List<ExpertDto> findAllRepeat();

    List<ExpertDto> findExpert(String minBusinessId, String reviewId, ExpertSelConditionDto[] epSelCondition);

    List<ExpertDto> countExpert(String minBusinessId, String reviewId, ExpertSelConditionDto epSelCondition);

    void savePhone(byte[] bytes, String expertId);

    byte[] findExpertPhoto(String expertId);

    int findMaxNumber();

    ResultMsg autoExpertReview(String minBusinessId, String reviewId, ExpertSelConditionDto[] paramArrary);
}
