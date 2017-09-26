package cs.service.reviewProjectAppraising;

import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;

/**
 * Created by MCL
 * 2017/9/23
 */
public interface AppraisingService {

    PageModelDto<SignDispaWork> findEndProject(ODataObj oDataObj);

    PageModelDto<SignDispaWork> findAppraisingProject(ODataObj oDataObj);

    void updateIsAppraising(String signId);
}
