package cs.service.project;

import cs.common.ResultMsg;
import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;

import java.util.List;

public interface SignDispaWorkService {
    PageModelDto<SignDispaWork> getCommQurySign(ODataObj odataObj);

    List<SignDispaWork> unMergeWPSign(String signId);

    List<SignDispaWork> getMergeWPSignBySignId(String signId);

    List<SignDispaWork> unMergeDISSign(String signId);

    List<SignDispaWork> getMergeDISSignBySignId(String signId);

    ResultMsg mergeSign(String signId, String mergeIds, String mergeType);

    ResultMsg cancelMergeSign(String signId, String cancelIds, String mergeType);

}
