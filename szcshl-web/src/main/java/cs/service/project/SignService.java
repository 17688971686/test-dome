package cs.service.project;

import cs.common.ResultMsg;
import cs.domain.flow.RuProcessTask;
import cs.domain.project.Sign;
import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.project.ProjectStopDto;
import cs.model.project.SignDto;
import cs.model.sys.OrgDto;
import cs.repository.odata.ODataObj;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Map;

public interface SignService {

    ResultMsg createSign(SignDto signDto);

    PageModelDto<SignDto> get(ODataObj odataObj);

    void updateSign(SignDto signDto);

    ResultMsg initFillPageData(String signId);

    List<OrgDto> selectSign(ODataObj odataObj);

    ResultMsg deleteSign(String signid);

    SignDto findById(String signid, boolean queryAll);

    /**
     * 发起流程
     * @param signid
     * @return
     */
    ResultMsg startNewFlow(String signid);

    /**
     * 终止流程
     * @param signid
     * @return
     */
    ResultMsg endFlow(String signid);

    /**
     * 流程处理
     * @param processInstance
     * @param task
     * @param flowDto
     * @return
     */
    ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto);
    //流程处理end

    /**
     * 查询协审项目（调概项目）
     * @return
     */
    List<SignDto> findAssistSign();

    /**
     * 根据协审计划ID，查询对应的项目信息
     * @param planId
     * @return
     */
    Map<String,Object> findByPlanId(String planId);

    void updateAssistState(String signIds, String status);

    ResultMsg associate(String signId, String associateId);

    List<SignDto> getAssociateDtos(String signId);

    List<Sign> getAssociates(String signId);

    /**
     * 查询正在办理的项目，用于计算剩余工作日
     * @return
     */
    List<Sign> selectSignNotFinish();

    void bathUpdate(List<Sign> signList);

    ResultMsg initSignList();

    ResultMsg realSign(String signId);

    ResultMsg reserveAddSign(SignDto signDto);

	PageModelDto<SignDto> findAllReserve(ODataObj odataObj);

	void deleteReserveSign(String signid);

    List<SignDto> findReviewSign(String signid);
    /***********************   更改项目状态  ****************************/
    boolean updateSignState(String signId,String state);

    boolean updateSignProcessState(String signId,Integer processState);

    PageModelDto<SignDto> findBySignUser(ODataObj odataObj);

    List<SignDispaWork> findAssociateSign(SignDispaWork signDispaWork);

    /***********************   以下是对接接口部分  ****************************/
    ResultMsg pushProject(SignDto signDto);


    List<SignDispaWork> getStastitacalData(ODataObj oDataObj);

    void updateSignTemplate(String signId);

    /**
     * 可取回项目列表
     * @param odataObj
     * @param isOrgLeader 是否部长
     * @return
     */
    PageModelDto<RuProcessTask> getBackList(ODataObj odataObj, boolean isOrgLeader);

    /**
     * 删除相应的分支信息（没有分支ID，则删除全部分支，包括工作方案，会议室预定，专家抽取等）
     * @param signId
     * @param branchIndex
     */
    void deleteBranchInfo(String signId,String branchIndex);
}
