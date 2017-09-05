package cs.service.project;

import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;

import cs.common.ResultMsg;
import cs.domain.project.Sign;
import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.project.ProjectStopDto;
import cs.model.project.SignDto;
import cs.model.sys.OrgDto;
import cs.repository.odata.ODataObj;

public interface SignService {

    ResultMsg createSign(SignDto signDto);

    PageModelDto<SignDto> get(ODataObj odataObj);

    void updateSign(SignDto signDto);

    ResultMsg initFillPageData(String signId);

    List<OrgDto> selectSign(ODataObj odataObj);

    ResultMsg deleteSign(String signid);

    SignDto findById(String signid, boolean queryAll);

    //流程处理begin
    ResultMsg startNewFlow(String signid);

    ResultMsg stopFlow(String signid, ProjectStopDto projectStopDto);

    ResultMsg restartFlow(String signid);

    ResultMsg endFlow(String signid);

    ResultMsg dealFlow(ProcessInstance processInstance, FlowDto flowDto);
    //流程处理end

    List<SignDto> findAssistSign();

    List<SignDto> findByPlanId(String planId);

    void updateAssistState(String signIds, String status);

    void associate(String signId, String associateId);

    List<SignDto> getAssociateDtos(String signId);

    List<Sign> getAssociates(String signId);

    List<Sign> selectSignNotFinish();//查询正在办理的项目

    void bathUpdate(List<Sign> signList);

    PageModelDto<SignDispaWork> getCommQurySign(ODataObj odataObj);

    ResultMsg initSignList();

    ResultMsg realSign(String signId);

	void reserveAddSign(SignDto signDto);

	PageModelDto<SignDto> findAllReserve(ODataObj odataObj);

	void deleteReserveSign(String signid);

    /******************   以下是项目关联操作  *******************/
    List<SignDto> unMergeWPSign(String signId);

    List<SignDto> getMergeWPSignBySignId(String signId);

    List<SignDto> unMergeDISSign(String signId);

    List<SignDto> getMergeDISSignBySignId(String signId);

    ResultMsg mergeSign(String signId, String mergeIds, String mergeType);

    ResultMsg cancelMergeSign(String signId, String cancelIds, String mergeType);

    /***********************   更改项目状态  ****************************/
    boolean updateSignState(String signId,String state);

    boolean updateSignProcessState(String signId,Integer processState);

    PageModelDto<SignDto> findBySignUser(ODataObj odataObj);

    List<SignDispaWork> findAssociateSign(SignDispaWork signDispaWork);

    /***********************   以下是对接接口部分  ****************************/
    ResultMsg pushProject(SignDto signDto);



}
