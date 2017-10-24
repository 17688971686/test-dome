package cs.service.project;

import cs.common.ResultMsg;
import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;

import java.util.List;

public interface SignDispaWorkService {

    /**
     * 查询个人办理项目
     * @param odataObj
     * @param isMianUser
     * @return
     */
    PageModelDto<SignDispaWork> findMyDoProject(ODataObj odataObj,boolean isMianUser);

    PageModelDto<SignDispaWork> getCommQurySign(ODataObj odataObj);

    List<SignDispaWork> unMergeWPSign(String signId);

    List<SignDispaWork> getMergeWPSignBySignId(String signId);

    List<SignDispaWork> unMergeDISSign(String signId);

    List<SignDispaWork> getMergeDISSignBySignId(String signId);

    ResultMsg mergeSign(String signId, String mergeIds, String mergeType);

    ResultMsg cancelMergeSign(String signId, String cancelIds, String mergeType);

    ResultMsg deleteAllMerge(String signId,String mergeType);

    List<SignDispaWork> getSignDispaWork(String filters);

    //查询评审费发放超期的项目信息
    PageModelDto<SignDispaWork> findOverSignDispaWork();

    /**
     * 通过专家id 获取专家评审过的项目信息
     * @param expertId
     * @return
     */
    PageModelDto<SignDispaWork> reviewProject(String expertId);
}
