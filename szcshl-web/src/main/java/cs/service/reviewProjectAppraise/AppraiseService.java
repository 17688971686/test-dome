package cs.service.reviewProjectAppraise;

import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.model.project.AppraiseReportDto;
import cs.repository.odata.ODataObj;

import java.util.List;

/**
 * Created by MCL
 * 2017/9/23
 */
public interface AppraiseService {

    PageModelDto<SignDispaWork> findEndProject(ODataObj oDataObj);

    PageModelDto<SignDispaWork> findAppraisingProject(ODataObj oDataObj);

    void updateIsAppraising(String signId);

    AppraiseReportDto initProposer();

    void saveApply(AppraiseReportDto appraiseReportDto);

    PageModelDto<AppraiseReportDto> getAppraiseReport(ODataObj oDataObj);

    AppraiseReportDto getAppraiseById (String id);
    void saveApprove(AppraiseReportDto appraiseReportDto);

    int  countApprove();

    /**
     * 查询主页的评审报告评审审批项目信息
     * @return
     */
    List<AppraiseReportDto> findHomeAppraise();
}
