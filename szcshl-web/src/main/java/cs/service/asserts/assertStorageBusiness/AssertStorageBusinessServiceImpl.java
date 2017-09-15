package cs.service.asserts.assertStorageBusiness;

import cs.common.Constant;
import cs.common.FlowConstant;
import cs.common.ResultMsg;
import cs.common.utils.ActivitiUtil;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.asserts.assertStorageBusiness.AssertStorageBusiness;
import cs.domain.asserts.assertStorageBusiness.AssertStorageBusiness_;
import cs.domain.asserts.goodsDetail.GoodsDetail;
import cs.domain.sys.Org;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.asserts.assertStorageBusiness.AssertStorageBusinessDto;
import cs.model.asserts.goodsDetail.GoodsDetailDto;
import cs.model.flow.FlowDto;
import cs.model.project.ProjectStopDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.asserts.assertStorageBusiness.AssertStorageBusinessRepo;
import cs.repository.repositoryImpl.asserts.goodsDetail.GoodsDetailRepo;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Description: 固定资产申购流程 业务操作实现类
 * author: zsl
 * Date: 2017-9-15 14:45:23
 */
@Service
public class AssertStorageBusinessServiceImpl  implements AssertStorageBusinessService {

	@Autowired
	private AssertStorageBusinessRepo assertStorageBusinessRepo;

	@Autowired
	private GoodsDetailRepo  goodsDetailRepo;

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private TaskService taskService;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private OrgDeptRepo orgDeptRepo;
	
	@Override
	public PageModelDto<AssertStorageBusinessDto> get(ODataObj odataObj) {
		PageModelDto<AssertStorageBusinessDto> pageModelDto = new PageModelDto<AssertStorageBusinessDto>();
		List<AssertStorageBusiness> resultList = assertStorageBusinessRepo.findByOdata(odataObj);
		List<AssertStorageBusinessDto> resultDtoList = new ArrayList<AssertStorageBusinessDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				AssertStorageBusinessDto modelDto = new AssertStorageBusinessDto();
				BeanCopierUtils.copyProperties(x, modelDto);
				resultDtoList.add(modelDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(resultDtoList);
		return pageModelDto;
	}

	@Override
	@Transactional
	public void save(AssertStorageBusinessDto record) {
		AssertStorageBusiness domain = new AssertStorageBusiness(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setCreatedBy(SessionUtil.getDisplayName());
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		assertStorageBusinessRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(AssertStorageBusinessDto record) {
		AssertStorageBusiness domain = assertStorageBusinessRepo.findById(record.getBusinessId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setModifiedDate(new Date());
		
		assertStorageBusinessRepo.save(domain);
	}

	@Override
	public AssertStorageBusinessDto findById(String id) {		
		AssertStorageBusinessDto modelDto = new AssertStorageBusinessDto();
		if(Validate.isString(id)){
			AssertStorageBusiness domain = assertStorageBusinessRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {

	}

	@Override
	public ResultMsg saveGoodsDetailList(GoodsDetailDto[] goodsDetailDtoList, AssertStorageBusiness assertStorageBus) {
		if(goodsDetailDtoList != null && goodsDetailDtoList.length > 0){
			Date now = new Date();
			AssertStorageBusiness assertStorageBusiness = new AssertStorageBusiness();
			//第一次保存/发起流程
			if (!Validate.isString(goodsDetailDtoList[0].getBusinessId())) {
				if( null!= assertStorageBus && Validate.isString(assertStorageBus.getProcessInstanceId())){
					assertStorageBusiness.setBusinessId(assertStorageBus.getBusinessId());
					assertStorageBusiness.setProcessInstanceId(assertStorageBus.getProcessInstanceId());
					assertStorageBusiness.setState(assertStorageBus.getState());
					assertStorageBusiness.setBusinessName(assertStorageBus.getBusinessName());
				}else{
					assertStorageBusiness.setBusinessId(UUID.randomUUID().toString());
				}
				assertStorageBusiness.setCreatedBy(SessionUtil.getDisplayName());
				assertStorageBusiness.setModifiedBy(SessionUtil.getDisplayName());
				assertStorageBusiness.setCreatedDate(now);
				assertStorageBusiness.setModifiedDate(now);
				assertStorageBusiness.setApplyDept(goodsDetailDtoList[0].getApplyDept());
				assertStorageBusiness.setOperator(goodsDetailDtoList[0].getOperator());
			}else{
				//多次保存或者保存后在发起流程
				assertStorageBusiness = assertStorageBusinessRepo.findById(AssertStorageBusiness_.businessId.getName(),goodsDetailDtoList[0].getBusinessId());
				assertStorageBusiness.setCreatedBy(SessionUtil.getDisplayName());
				assertStorageBusiness.setModifiedBy(SessionUtil.getDisplayName());
				assertStorageBusiness.setCreatedDate(now);
				assertStorageBusiness.setModifiedDate(now);
				assertStorageBusiness.setApplyDept(goodsDetailDtoList[0].getApplyDept());
				assertStorageBusiness.setOperator(goodsDetailDtoList[0].getOperator());
				if( null!= assertStorageBus && Validate.isString(assertStorageBus.getProcessInstanceId())){
					assertStorageBusiness.setProcessInstanceId(assertStorageBus.getProcessInstanceId());
					assertStorageBusiness.setState(assertStorageBus.getState());
					assertStorageBusiness.setBusinessName(assertStorageBus.getBusinessName());
				}

			}
			List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
			for(int i=0,l=goodsDetailDtoList.length;i<l;i++){
				GoodsDetail goodsDetail = new GoodsDetail();
				GoodsDetailDto goodsDetailDto = goodsDetailDtoList[i];
				BeanCopierUtils.copyProperties(goodsDetailDto, goodsDetail);
				goodsDetail.setAssertStorageBusiness(assertStorageBusiness);
				goodsDetail.setId(UUID.randomUUID().toString());
				goodsDetail.setCreatedBy(SessionUtil.getDisplayName());
				goodsDetail.setModifiedBy(SessionUtil.getDisplayName());
				goodsDetail.setCreatedDate(now);
				goodsDetail.setModifiedDate(now);
				goodsDetailList.add(goodsDetail);
			}
			if (goodsDetailList.size()>0){
				if (null != assertStorageBusiness.getGoodsDetailList()){
					assertStorageBusiness.getGoodsDetailList().clear();
					assertStorageBusiness.getGoodsDetailList().addAll(goodsDetailList);
				}else{
					assertStorageBusiness.setGoodsDetailList(goodsDetailList);
				}
			}
			assertStorageBusinessRepo.save(assertStorageBusiness);
			return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"保存成功！",assertStorageBusiness);
		}else{
			return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"请添加物品信息后，再进行保存！");
		}
	}

	@Override
	public ResultMsg startFlow(GoodsDetailDto[] bookList,AssertStorageBusiness goodsDetailBus) {
		if(null==bookList || bookList.length==0){
			return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"请添加物品信息后，再发起流程！");
		}
		//2、判断是否已经发起流程，如果未发起，则发起流程
		if (!Validate.isString(goodsDetailBus.getProcessInstanceId())) {
			if(!Validate.isString(bookList[0].getBusinessId())){
				goodsDetailBus.setBusinessId(UUID.randomUUID().toString());
			}else{
				goodsDetailBus.setBusinessId(bookList[0].getBusinessId());
			}
			goodsDetailBus.setBusinessName("固定资产申购流程"+(assertStorageBusinessRepo.findAll().size()+1));
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(FlowConstant.BOOKS_BUY_FLOW, goodsDetailBus.getBusinessId(),
					ActivitiUtil.setAssigneeValue(FlowConstant.BooksBuyFlowParams.USER_APPLY.getValue(), SessionUtil.getUserId()));
			processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), goodsDetailBus.getBusinessName());
			goodsDetailBus.setProcessInstanceId(processInstance.getId());
			goodsDetailBus.setState(Constant.EnumState.PROCESS.getValue());
			//跳过第一环节
			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
			taskService.addComment(task.getId(), processInstance.getId(), "");
			taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.BooksBuyFlowParams.USER_BZ.getValue(), SessionUtil.getUserInfo().getOrg().getOrgDirector()));
		}

		return  saveGoodsDetailList(bookList,goodsDetailBus);
	}

	@Override
	public ResultMsg startNewFlow(String signid) {
		return null;
	}

	@Override
	public ResultMsg stopFlow(String signid, ProjectStopDto projectStopDto) {
		return null;
	}

	@Override
	public ResultMsg restartFlow(String signid) {
		return null;
	}

	@Override
	public ResultMsg endFlow(String signid) {
		return null;
	}

	@Override
	/**
	 * 流程处理
	 * @param processInstance
	 * @param flowDto
	 * @return ResultMsg
	 */
	public ResultMsg dealFlow(ProcessInstance processInstance,Task task, FlowDto flowDto) {
		String businessKey = processInstance.getBusinessKey(), businessId = "", assigneeValue = "", branchIndex = "";
		AssertStorageBusiness goodsDetailBusiness = null;
		List<User> dealUserList = null;
		User dealUser = null;
		Org org = null;
		Map<String, Object> variables = processInstance.getProcessVariables();
		boolean saveFlag = false;
		//以下是流程环节处理
		/*switch (task.getTaskDefinitionKey()) {
			case FlowConstant.BOOK_BZSP:
				bookBuyBusiness = bookBuyBusinessRepo.findById(AssertStorageBusiness_.businessId.getName(), businessKey);
				bookBuyBusiness.setOrgDirectorId(SessionUtil.getUserInfo().getOrg().getOrgDirector());
				bookBuyBusiness.setOrgDirector(SessionUtil.getUserInfo().getOrg().getOrgDirectorName());
				bookBuyBusiness.setOrgDirectorDate(new Date());
				bookBuyBusiness.setApplyReason(flowDto.getDealOption());
				bookBuyBusinessRepo.save(bookBuyBusiness);
				task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
				variables = ActivitiUtil.setAssigneeValue(FlowConstant.BooksBuyFlowParams.USER_FGLD.getValue(), SessionUtil.getUserInfo().getOrg().getOrgSLeader());
				break;
			case FlowConstant.BOOK_FGFZRSP:
				bookBuyBusiness = bookBuyBusinessRepo.findById(AssertStorageBusiness_.businessId.getName(), businessKey);
				bookBuyBusiness.setOrgSLeaderId(SessionUtil.getUserInfo().getId());
				bookBuyBusiness.setOrgSLeader(SessionUtil.getLoginName());
				bookBuyBusiness.setOrgSLeaderDate(new Date());
				bookBuyBusiness.setOrgSLeaderHandlesug(flowDto.getDealOption());
				bookBuyBusinessRepo.save(bookBuyBusiness);
				dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue());
				if (!Validate.isList(dealUserList)) {
					return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
				}
				dealUser = dealUserList.get(0);
				task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
				variables = ActivitiUtil.setAssigneeValue(FlowConstant.BooksBuyFlowParams.USER_ZR.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
				break;
			case FlowConstant.BOOK_ZXZRSP:
				bookBuyBusiness = bookBuyBusinessRepo.findById(AssertStorageBusiness_.businessId.getName(), businessKey);
				bookBuyBusiness.setOrgMLeader(SessionUtil.getUserInfo().getLoginName());
				bookBuyBusiness.setOrgMLeaderId(SessionUtil.getUserInfo().getId());
				bookBuyBusiness.setOrgMLeaderDate(new Date());
				bookBuyBusiness.setOrgMLeaderHandlesug(flowDto.getDealOption());
				bookBuyBusinessRepo.save(bookBuyBusiness);
				task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
				dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.FILER.getValue());
				if (!Validate.isList(dealUserList)) {
					return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
				}
				dealUser = dealUserList.get(0);
				assigneeValue = Validate.isString(dealUser.getTakeUserId())?dealUser.getTakeUserId():dealUser.getId();
				variables = ActivitiUtil.setAssigneeValue(FlowConstant.BooksBuyFlowParams.USER_DAY.getValue(), assigneeValue);
				break;
			case FlowConstant.BOOK_YSRK:
				bookBuyBusiness = bookBuyBusinessRepo.findById(AssertStorageBusiness_.businessId.getName(), businessKey);
				bookBuyBusiness.setFilerId(SessionUtil.getUserInfo().getId());
				bookBuyBusiness.setFiler(SessionUtil.getUserInfo().getLoginName());
				bookBuyBusiness.setFilerDate(new Date());
				bookBuyBusiness.setFilerHandlesug(flowDto.getDealOption());
				bookBuyBusinessRepo.save(bookBuyBusiness);
				task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
				break;
			default:;
		}*/

		taskService.addComment(task.getId(), processInstance.getId(), flowDto.getDealOption());    //添加处理信息
		if (flowDto.isEnd()) {
			taskService.complete(task.getId());
		} else {
			taskService.complete(task.getId(), variables);
		}
		return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
	}
	
}