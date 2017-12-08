package cs.service.sys;

import java.util.*;

import cs.common.FlowConstant;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.model.flow.FlowDto;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.rtx.RTXSendMsgPool;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.domain.sys.Annountment;
import cs.domain.sys.Annountment_;
import cs.domain.sys.Org;
import cs.domain.sys.Org_;
import cs.domain.sys.User;
import cs.domain.sys.User_;
import cs.model.PageModelDto;
import cs.model.sys.AnnountmentDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.AnnountmentRepo;
import cs.repository.repositoryImpl.sys.OrgRepo;

@Service
public class AnnountmentServiceImpl implements AnnountmentService {

    @Autowired
    private AnnountmentRepo annountmentRepo;
    @Autowired
    private OrgRepo orgRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private TaskService taskService;

    /* 
     * 获取个人发布的通知公告
     */
    @Override
    public PageModelDto<AnnountmentDto> findByCurUser(ODataObj odataobj) {
    	PageModelDto<AnnountmentDto> pageModelDto = new PageModelDto<>();
    	Criteria criteria=annountmentRepo.getExecutableCriteria();
    	criteria=odataobj.buildFilterToCriteria(criteria);
    	//创建人为当前用户,
    	criteria.add(Restrictions.eq(Annountment_.createdBy.getName(), SessionUtil.getUserId()));
    	//统计总数
    	Integer totalResult=((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
       pageModelDto.setCount(totalResult);
       //处理分页
       criteria.setProjection(null);
       if(odataobj.getSkip() > 0){
    	   criteria.setFirstResult(odataobj.getSkip());
       }
       if(odataobj.getTop() > 0){
    	   criteria.setMaxResults(odataobj.getTop());
       }
    	
       //处理orderby
       if(Validate.isString(odataobj.getOrderby())){
    	   if(odataobj.isOrderbyDesc()){
    		   criteria.addOrder(Property.forName(odataobj.getOrderby()).desc());
    	   }else{
    		   criteria.addOrder(Property.forName(odataobj.getOrderby()).asc());
    	   }
       }
       
    	List<Annountment> annList =criteria.list();
        List<AnnountmentDto> annountmentDtoList = new ArrayList<>();
        for (Annountment annountment : annList) {
            AnnountmentDto annDto = new AnnountmentDto();
            BeanCopierUtils.copyProperties(annountment, annDto);
            annountmentDtoList.add(annDto);
        }

        pageModelDto.setValue(annountmentDtoList);

        return pageModelDto;
    }



	/* 
	 * 获取所有已经发布的通知公告
	 */
	@Override
	public PageModelDto<AnnountmentDto> findByIssue(ODataObj odataobj) {
		PageModelDto<AnnountmentDto> pageModelDto=new PageModelDto<>();
		Criteria criteria = annountmentRepo.getExecutableCriteria();
		criteria.add(Restrictions.like(Annountment_.issue.getName(), Constant.EnumState.YES.getValue()));
		criteria=odataobj.buildFilterToCriteria(criteria);
		
		//统计总数
    	Integer totalResult=((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
       pageModelDto.setCount(totalResult);
       //处理分页
       criteria.setProjection(null);
       if(odataobj.getSkip() > 0){
    	   criteria.setFirstResult(odataobj.getSkip());
       }
       if(odataobj.getTop() > 0){
    	   criteria.setMaxResults(odataobj.getTop());
       }
    	
       //处理orderby
       if(Validate.isString(odataobj.getOrderby())){
    	   if(odataobj.isOrderbyDesc()){
    		   criteria.addOrder(Property.forName(odataobj.getOrderby()).desc());
    	   }else{
    		   criteria.addOrder(Property.forName(odataobj.getOrderby()).asc());
    	   }
       }
		List<Annountment> annountmentList=criteria.list();
		
		List<AnnountmentDto> annountmentDtoList=new ArrayList<>();
		for(Annountment annountment : annountmentList){
			AnnountmentDto annountmentDto = new AnnountmentDto();
			BeanCopierUtils.copyProperties(annountment, annountmentDto);
			annountmentDtoList.add(annountmentDto);
		}
		
		pageModelDto.setValue(annountmentDtoList);
		return pageModelDto;
	}


    
    @Override
    @Transactional
    public void createAnnountment(AnnountmentDto annountmentDto) {
        Annountment annountment = new Annountment();
        BeanCopierUtils.copyProperties(annountmentDto, annountment);
        Date now = new Date();
        //默认不排序和不置顶
        if(!Validate.isString(annountment.getIssue())){
            annountment.setIssue(Constant.EnumState.NO.getValue());
        }
        if(!Validate.isObject(annountment.getIsStick())){
            annountment.setIsStick(Integer.valueOf(Constant.EnumState.NO.getValue()));
        }else{
            if(Constant.EnumState.YES.getValue().equals(annountment.getIsStick())){
                annountment.setIssueDate(now);
            }
        }
        if(!Validate.isString(annountment.getAnId())){
            annountment.setAnId(null);
        }
        //已发布的要加上发布人和发布时间
        if(Constant.EnumState.YES.getValue().equals(annountment.getIssue())){
            annountment.setIssueDate(now);
            annountment.setIssueUser(SessionUtil.getLoginName());
        }
        annountment.setCreatedBy(SessionUtil.getUserId());
        annountment.setCreatedDate(now);
        annountment.setModifiedBy(SessionUtil.getUserId());
        annountment.setModifiedDate(now);
        annountmentRepo.save(annountment);
        annountmentDto.setAnId(annountment.getAnId());
    }

    @Override
    @Transactional
    public String findAnOrg() {
        String loginName = SessionUtil.getLoginName();
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select o from " + Org.class.getSimpleName() + " o where o." + Org_.id.getName() + "=(");
        hqlBuilder.append("select u." + User_.org.getName() + "." + Org_.id.getName() + " from " + User.class.getSimpleName() + " u where u." + User_.loginName.getName() + "=:loginName)");
        hqlBuilder.setParam("loginName", loginName);
        List<Org> orgList = orgRepo.findByHql(hqlBuilder);
        if (orgList != null && orgList.size() > 0) {
            return orgList.get(0).getName();
        } else {
            return null;
        }

    }

    @Override
    @Transactional
    public AnnountmentDto findAnnountmentById(String anId) {
        Annountment annountment = annountmentRepo.findById(Annountment_.anId.getName(),anId);
        AnnountmentDto annountmentDto = new AnnountmentDto();
        BeanCopierUtils.copyProperties(annountment, annountmentDto);
        return annountmentDto;
    }

    @Override
    @Transactional
    public void updateAnnountment(AnnountmentDto annountmentDto) {
        Annountment annountment = annountmentRepo.findById(Annountment_.anId.getName(),annountmentDto.getAnId());
        if (annountment != null) {
            BeanCopierUtils.copyPropertiesIgnoreNull(annountmentDto, annountment);
            annountment.setModifiedBy(SessionUtil.getLoginName());
            annountment.setModifiedDate(new Date());
        }
        annountmentRepo.save(annountment);
    }

    /**
     * 经过审批的通知公告，不能修改和删除！不用审批的通知公告，若已发布，不能删除
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ResultMsg deleteAnnountment(String id) {
        ResultMsg resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(),"删除成功！");
        List<Annountment> deleteList = annountmentRepo.findByIds(Annountment_.anId.getName(),id,null);
        if(Validate.isList(deleteList)){
            for(int i=0,l=deleteList.size();i<l;i++){
                Annountment dl = deleteList.get(i);
                if(Validate.isString(dl.getProcessInstanceId()) || Constant.EnumState.YES.getValue().equals(dl.getIssue())){
                    resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"操作失败，审批的通知公告和已发布的通知公告不能删除！");
                    break;
                }
            }
        }
        if(resultMsg.isFlag()){
            annountmentRepo.deleteById(Annountment_.anId.getName(),id);
        }
	   return resultMsg;
    }

    /**
     * 获取主页上的通知公告数据，最多6条数据
     * @return
     */
    @Override
    @Transactional
    public List<AnnountmentDto> getHomePageAnnountment() {
       /* HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select a from " + Annountment.class.getSimpleName() + " a order by " + Annountment_.isStick.getName() + " desc," + Annountment_.issueDate.getName() + " desc");
        List<Annountment> annList = annountmentRepo.findByHql(hqlBuilder);*/

        //通过Criteria查询
        Criteria criteria = annountmentRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(Annountment_.issue.getName(), Constant.EnumState.YES.getValue()));
        criteria.addOrder(Order.desc(Annountment_.isStick.getName())).addOrder(Order.desc(Annountment_.issueDate.getName()));
        criteria.setMaxResults(6);
        List<Annountment> annList = criteria.list();

        List<AnnountmentDto> annDtoList = new ArrayList<>();
        for (Annountment ann : annList) {
            AnnountmentDto annDto = new AnnountmentDto();
            BeanCopierUtils.copyProperties(ann, annDto);
            annDtoList.add(annDto);
        }
        return annDtoList;
    }

    /**
     * 上一篇
     * @param id
     * @return
     */
    @Override
    @Transactional
    public AnnountmentDto postAritle(String id) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("with res as(select cs_annountment.*,row_number() over(order by isstick desc,issueDate desc)t from cs_annountment)");
        hqlBuilder.append(" select res.* from res where t=(");
        hqlBuilder.append("select res.t-1 from res where res.anid=:id)");
        hqlBuilder.append(" and res.issue=:issue");
        hqlBuilder.setParam("id", id);
        hqlBuilder.setParam("issue" , Constant.EnumState.YES.getValue());
        List<Annountment> annList = annountmentRepo.findBySql(hqlBuilder);
        AnnountmentDto annDto = new AnnountmentDto();
        if (annList.size() > 0) {
            BeanCopierUtils.copyProperties(annList.get(0), annDto);
        }
        return annDto;
    }

    /**
     * 下一篇
     * @param id
     * @return
     */
    @Override
    @Transactional
    public AnnountmentDto nextArticle(String id) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("with res as(select cs_annountment.*,row_number() over(order by isstick desc,issueDate desc)t from cs_annountment)");
        hqlBuilder.append(" select res.* from res where t=(");
        hqlBuilder.append("select res.t+1 from res where res.anid=:id)");
        hqlBuilder.append(" and res.issue=:issue");
        hqlBuilder.setParam("id", id);
        hqlBuilder.setParam("issue" , Constant.EnumState.YES.getValue());
        List<Annountment> annList = annountmentRepo.findBySql(hqlBuilder);
        AnnountmentDto annDto = new AnnountmentDto();
        if (annList.size() > 0) {
            BeanCopierUtils.copyProperties(annList.get(0), annDto);
        }
        return annDto;
    }

    /**
     * 更改通知公告的发布状态（如果是已审批的，不能取消发布）
     * @param ids
     * @param issueState
     */
    @Override
    @Transactional
    public ResultMsg updateIssueState(String ids, String issueState) {
        ResultMsg resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(),"操作成功！");
        List<Annountment> bathAnnountment = new ArrayList<>();
        //是否取消发布
        boolean isCancelPublish = (Constant.EnumState.NO.getValue().equals(issueState))?true:false;
        List<Annountment> updateList = annountmentRepo.findByIds(Annountment_.anId.getName(),ids,null);
        if(Validate.isList(updateList)){
            Date now = new Date();
            for(int i = 0,l=updateList.size(); i < l; i++){
                Annountment annountment = updateList.get(i);
                if(Validate.isString(annountment.getProcessInstanceId())){
                    resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"操作失败，审批的通知公告，不能对其进行修改和删除操作！");
                    break;
                }
                //如果是发布
                if(!isCancelPublish){
                    if(!Validate.isString(annountment.getIssueUser())){
                        annountment.setIssueUser(SessionUtil.getDisplayName());
                    }
                    if(!Validate.isObject(annountment.getIssueDate())){
                        annountment.setIssueDate(now);
                    }
                }
                annountment.setModifiedBy(SessionUtil.getUserId());
                annountment.setModifiedDate(now);
                annountment.setIssue(issueState);

                bathAnnountment.add(annountment);
            }
        }
        if(resultMsg.isFlag() && Validate.isList(bathAnnountment)){
            annountmentRepo.bathUpdate(bathAnnountment);
        }

        return resultMsg;
    }


    /**
     * 通知公告发起流程
     *
     * @param id
     * @return
     */
    @Override
    public ResultMsg startFlow(String id) {
        Annountment annountment = annountmentRepo.findById(Annountment_.anId.getName(), id);
        String assigneeValue;
        Map<String, Object> variables = new HashMap<>();       //流程参数
        if (annountment == null) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "发起流程失败，该项目已不存在！");
        }
        if (Validate.isString(annountment.getProcessInstanceId())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "该项目已发起流程！");
        }
        if (SessionUtil.getUserInfo().getOrg() == null || !Validate.isString(SessionUtil.getUserInfo().getOrg().getOrgDirector())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您所在部门还没设置部长，请联系管理员进行设置！");
        }

        //查询部门领导
        User user = userRepo.findOrgDirector(annountment.getCreatedBy());//查询用户的所在部门领导
        if (!Validate.isString(user.getDisplayName())) {//判断是否有部门领导
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "【" + user.getOrg().getName() + "】的部长未设置，请先设置！");
        }
        //1、启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(FlowConstant.ANNOUNT_MENT_FLOW, id,
                ActivitiUtil.setAssigneeValue(FlowConstant.AnnountMentFLOWParams.USER.getValue(), SessionUtil.getUserId()));

        //2、设置流程实例名称
        processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), annountment.getAnTitle());

        //3、更改项目状态
        annountment.setProcessInstanceId(processInstance.getId());
        annountment.setAppoveStatus(Constant.EnumState.NO.getValue());
        annountmentRepo.save(annountment);

        //4、跳过第一环节（填报）
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        taskService.addComment(task.getId(), processInstance.getId(), "");    //添加处理信息

        if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
            assigneeValue=SessionUtil.getUserInfo().getOrg().getOrgSLeader();
            variables=ActivitiUtil.setAssigneeValue(FlowConstant.AnnountMentFLOWParams.USER_FZ.getValue(), assigneeValue);
            variables.put(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue(), true);
            taskService.complete(task.getId(), variables);
        }else{
            assigneeValue = Validate.isString(user.getTakeUserId()) ? user.getTakeUserId() : user.getId();
            variables=ActivitiUtil.setAssigneeValue(FlowConstant.AnnountMentFLOWParams.USER_BZ.getValue(), assigneeValue);
            variables.put(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue(), false);
            taskService.complete(task.getId(), variables);
        }

        //放入腾讯通消息缓冲池
        RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 通知公告流程处理
     *
     * @param processInstance
     * @param task
     * @param flowDto
     * @return
     */
    @Override
    @Transactional
    public ResultMsg dealSignSupperFlow(ProcessInstance processInstance, Task task, FlowDto flowDto) {
        String businessId = processInstance.getBusinessKey(),
        assigneeValue = "";                            //流程处理人
        List<User> userList = null;                 //用户列表
        User dealUser = null;
        Map<String, Object> variables = new HashMap<>();       //流程参数
        boolean isNextUser = false;                 //是否是下一环节处理人（主要是处理领导审批，部长审批）
        String nextNodeKey = "";                    //下一环节名称
        Annountment annountment = annountmentRepo.findById(Annountment_.anId.getName(),businessId);
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        //环节处理人设定
        switch (task.getTaskDefinitionKey()) {
            //项目负责人填报
            case FlowConstant.ANNOUNT_TZ:
                flowDto.setDealOption("");//默认意见为空
                if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){//判断是否是部长
                    dealUser = userRepo.getCacheUserById(SessionUtil.getUserInfo().getOrg().getOrgSLeader());
                    assigneeValue= Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                    variables=ActivitiUtil.setAssigneeValue(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue(), assigneeValue);
                    variables.put(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue(), true);
                }else{
                    String userId = SessionUtil.getUserInfo().getOrg().getOrgDirector() == null?SessionUtil.getUserId():SessionUtil.getUserInfo().getOrg().getOrgDirector();
                    User leadUser = userRepo.getCacheUserById(userId);
                    assigneeValue = Validate.isString(leadUser.getTakeUserId()) ? leadUser.getTakeUserId() : leadUser.getId();
                    variables=ActivitiUtil.setAssigneeValue(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue(), assigneeValue);
                    variables.put(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue(), false);
                }
                break;
            //部长审批
            case FlowConstant.ANNOUNT_BZ:
                annountment.setDeptMinisterId(SessionUtil.getUserId());
                annountment.setDeptMinisterName(SessionUtil.getDisplayName());
                annountment.setDeptMinisterDate(new Date());
                annountment.setDeptMinisterIdeaContent(flowDto.getDealOption());
                annountment.setAppoveStatus(Constant.EnumState.PROCESS.getValue());
                annountmentRepo.save(annountment);
                assigneeValue=SessionUtil.getUserInfo().getOrg().getOrgSLeader();//下一环节的处理人
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.AnnountMentFLOWParams.USER_FZ.getValue(), assigneeValue);
                //下一环节还是自己处理
                if(assigneeValue.equals(SessionUtil.getUserId())){
                    isNextUser = true;
                    nextNodeKey = FlowConstant.ANNOUNT_FZ;
                }
                break;
            //副主任审批
            case FlowConstant.ANNOUNT_FZ:
                annountment.setDeptSLeaderId(SessionUtil.getUserId());
                annountment.setDeptSLeaderName(SessionUtil.getDisplayName());
                annountment.setDeptSleaderDate(new Date());
                annountment.setDeptSLeaderIdeaContent(flowDto.getDealOption());
                annountment.setAppoveStatus(Constant.EnumState.STOP.getValue());
                annountmentRepo.save(annountment);
                //查询部门主任领导

                userList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(userList)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                assigneeValue=userList.get(0).getId();//下一环节处理人
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.AnnountMentFLOWParams.USER_ZR.getValue(), assigneeValue);
                //下一环节还是自己处理
                if(assigneeValue.equals(SessionUtil.getUserId())){
                    isNextUser = true;
                    nextNodeKey = FlowConstant.ANNOUNT_ZR;
                }
                break;

            //主任审批
            case FlowConstant.ANNOUNT_ZR:
                annountment.setDeptDirectorId(SessionUtil.getUserId());
                annountment.setDeptDirectorName(SessionUtil.getDisplayName());
                annountment.setDeptDirectorDate(new Date());
                annountment.setDeptDirectorIdeaContent(flowDto.getDealOption());
                annountment.setAppoveStatus(Constant.EnumState.YES.getValue());
                annountmentRepo.save(annountment);
                if(flowDto.getBusinessMap().get("AGREE")!=null){
                    if(flowDto.getBusinessMap().get("AGREE").equals("9")){//当主任同意时就发布通知公告
                        updateIssueState(annountment.getAnId(),"9");
                    }
                }else{
                    updateIssueState(annountment.getAnId(),"9");
                }

                break;
            default:
                break;
        }
        taskService.addComment(task.getId(), processInstance.getId(), (flowDto == null) ? "" : flowDto.getDealOption());    //添加处理信息
        if (flowDto.isEnd()) {
            taskService.complete(task.getId());
        } else {
            taskService.complete(task.getId(), variables);
            //如果下一环节还是自己
            if(isNextUser){
                List<Task> nextTaskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee(assigneeValue).list();
                for(Task t:nextTaskList){
                    if(nextNodeKey.equals(t.getTaskDefinitionKey())){
                        ResultMsg returnMsg = dealSignSupperFlow(processInstance,t,flowDto);
                        if(returnMsg.isFlag() == false){
                            return returnMsg;
                        }
                        break;
                    }
                }
            }
        }
        //放入腾讯通消息缓冲池
        RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }



}
