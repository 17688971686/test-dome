package cs.service.book;

import cs.common.Constant;
import cs.common.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.book.BookBuy;
import cs.domain.book.BookBuyBusiness;
import cs.domain.book.BookBuyBusiness_;
import cs.domain.book.BookBuy_;
import cs.domain.sys.Org;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.book.BookBuyBusinessDto;
import cs.model.book.BookBuyDto;
import cs.model.flow.FlowDto;
import cs.model.project.ProjectStopDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.book.BookBuyBusinessRepo;
import cs.repository.repositoryImpl.book.BookBuyRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.rtx.RTXSendMsgPool;
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
 * Description: 图书采购申请业务信息 业务操作实现类
 * author: zsl
 * Date: 2017-9-8 10:26:20
 */
@Service
public class BookBuyBusinessServiceImpl  implements BookBuyBusinessService {

	@Autowired
	private BookBuyBusinessRepo bookBuyBusinessRepo;
	@Autowired
	private BookBuyRepo bookBuyRepoRepo;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private TaskService taskService;
	@Autowired
	private UserRepo userRepo;

	@Override
	public PageModelDto<BookBuyBusinessDto> get(ODataObj odataObj) {
		PageModelDto<BookBuyBusinessDto> pageModelDto = new PageModelDto<BookBuyBusinessDto>();
		List<BookBuyBusiness> resultList = bookBuyBusinessRepo.findByOdata(odataObj);
		List<BookBuyBusinessDto> resultDtoList = new ArrayList<BookBuyBusinessDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				BookBuyBusinessDto modelDto = new BookBuyBusinessDto();
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
	public void save(BookBuyBusinessDto record) {
		BookBuyBusiness domain = new BookBuyBusiness(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setCreatedBy(SessionUtil.getDisplayName());
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		bookBuyBusinessRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(BookBuyBusinessDto record) {
		BookBuyBusiness domain = bookBuyBusinessRepo.findById(record.getBusinessId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setModifiedDate(new Date());
		
		bookBuyBusinessRepo.save(domain);
	}

	@Override
	public BookBuyBusinessDto findById(String id) {		
		BookBuyBusinessDto modelDto = new BookBuyBusinessDto();
		if(Validate.isString(id)){
			BookBuyBusiness domain = bookBuyBusinessRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public ResultMsg delete(String ids) {
		try{
			if(Validate.isString(ids)){
				if(ids.contains(",")){
					String idsArr[] = ids.split(",");
					for(String id : idsArr){
						delExistBookInfo(id);
					}
				}else{
					delExistBookInfo(ids);
				}
			}

		}catch (Exception e){
			return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"保存失败，数据异常");
		}
		return new ResultMsg(false, Constant.MsgCode.OK.getValue(),"操作成功！");
	}

	private boolean delExistBookInfo(String id){
		boolean flag = false;
		BookBuy bookBuy = bookBuyRepoRepo.findById(BookBuy_.id.getName(), id);
		if (null != bookBuy){
			bookBuyRepoRepo.delete(bookBuy);
			flag = true;
		}
		return  flag;
	}

	@Override
	@Transactional
	public void deleteByBusinessId(String businessId) {
		HqlBuilder sqlBuilder = HqlBuilder.create();
		sqlBuilder.append(" delete from cs_books_buy where businessid = '"+businessId+"'");
		bookBuyBusinessRepo.executeSql(sqlBuilder);
	}

	@Override
	public ResultMsg saveBooksDetailList(BookBuyDto[] bookList,BookBuyBusiness bookBuyBus) {
		if(bookList != null && bookList.length > 0){
			Date now = new Date();
			BookBuyBusiness bookBuyBusiness = new BookBuyBusiness();
			//第一次保存/发起流程
			if (!Validate.isString(bookList[0].getBusinessId())) {
				if( null!= bookBuyBus && Validate.isString(bookBuyBus.getProcessInstanceId())){
					bookBuyBusiness.setBusinessId(bookBuyBus.getBusinessId());
					bookBuyBusiness.setProcessInstanceId(bookBuyBus.getProcessInstanceId());
					bookBuyBusiness.setState(bookBuyBus.getState());
					bookBuyBusiness.setBusinessName(bookBuyBus.getBusinessName());
				}else{
					bookBuyBusiness.setBusinessId(UUID.randomUUID().toString());
				}
				bookBuyBusiness.setCreatedBy(SessionUtil.getDisplayName());
				bookBuyBusiness.setModifiedBy(SessionUtil.getDisplayName());
				bookBuyBusiness.setCreatedDate(now);
				bookBuyBusiness.setModifiedDate(now);
				bookBuyBusiness.setApplyDate(now);
				bookBuyBusiness.setApplyDept(bookList[0].getApplyDept());
				bookBuyBusiness.setOperator(bookList[0].getOperator());
				bookBuyBusiness.setBuyChannel(bookList[0].getBuyChannel());
				bookBuyBusiness.setBusinessName(bookList[0].getBusinessName());
				bookBuyBusiness.setApplyReason(bookList[0].getApplyReason());
				//bookBuyBusinessRepo.save(bookBuyBusiness);
			}else{
				//多次保存或者保存后在发起流程
				bookBuyBusiness = bookBuyBusinessRepo.findById(BookBuyBusiness_.businessId.getName(),bookList[0].getBusinessId());
				bookBuyBusiness.setCreatedBy(SessionUtil.getDisplayName());
				bookBuyBusiness.setModifiedBy(SessionUtil.getDisplayName());
				bookBuyBusiness.setCreatedDate(now);
				bookBuyBusiness.setModifiedDate(now);
				bookBuyBusiness.setApplyDate(now);
				bookBuyBusiness.setApplyDept(bookList[0].getApplyDept());
				bookBuyBusiness.setOperator(bookList[0].getOperator());
				bookBuyBusiness.setBuyChannel(bookList[0].getBuyChannel());
				bookBuyBusiness.setBusinessName(bookList[0].getBusinessName());
				bookBuyBusiness.setApplyReason(bookList[0].getApplyReason());
				if( null!= bookBuyBus && Validate.isString(bookBuyBus.getProcessInstanceId())){
					bookBuyBusiness.setProcessInstanceId(bookBuyBus.getProcessInstanceId());
					bookBuyBusiness.setState(bookBuyBus.getState());
					bookBuyBusiness.setBusinessName(bookBuyBus.getBusinessName());
				}

			}
			List<BookBuy> bookBuyList = new ArrayList<BookBuy>();
			for(int i=0,l=bookList.length;i<l;i++){
				BookBuy bookBuy = new BookBuy();
				BookBuyDto bookBuyDto = bookList[i];
				BeanCopierUtils.copyProperties(bookBuyDto, bookBuy);
				bookBuy.setBookBuyBusiness(bookBuyBusiness);
				bookBuy.setId(UUID.randomUUID().toString());
				bookBuy.setCreatedBy(SessionUtil.getDisplayName());
				bookBuy.setModifiedBy(SessionUtil.getDisplayName());
				bookBuy.setCreatedDate(now);
				bookBuy.setModifiedDate(now);
				bookBuyList.add(bookBuy);
			}
			if (bookBuyList.size()>0){
				if (null != bookBuyBusiness.getBookBuyList()){
					bookBuyBusiness.getBookBuyList().clear();
					bookBuyBusiness.getBookBuyList().addAll(bookBuyList);
				}else{
					bookBuyBusiness.setBookBuyList(bookBuyList);
				}
			}
			bookBuyBusinessRepo.save(bookBuyBusiness);
			return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"保存成功！",bookBuyBusiness);
		}else{
			return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"没有分录数据，无法保存！");
		}
	}

	@Override
	public ResultMsg startFlow(BookBuyDto[] bookList,BookBuyBusiness bookBuyBus) {
		if(null==bookList || bookList.length==0){
			return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"请添加图书信息后，再发起流程！");
		}
		//2、判断是否已经发起流程，如果未发起，则发起流程
		if (!Validate.isString(bookBuyBus.getProcessInstanceId())) {
			if(!Validate.isString(bookList[0].getBusinessId())){
				bookBuyBus.setBusinessId(UUID.randomUUID().toString());
			}else{
				bookBuyBus.setBusinessId(bookList[0].getBusinessId());
			}
			if(!Validate.isString(bookList[0].getBusinessName())){
				bookBuyBus.setBusinessName("图书采购流程");
			}else{
				bookBuyBus.setBusinessName(bookList[0].getBusinessName());
			}
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(FlowConstant.BOOKS_BUY_FLOW, bookBuyBus.getBusinessId(),
					ActivitiUtil.setAssigneeValue(FlowConstant.BooksBuyFlowParams.USER_APPLY.getValue(), SessionUtil.getUserId()));
			processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), bookBuyBus.getBusinessName());
			bookBuyBus.setProcessInstanceId(processInstance.getId());
			bookBuyBus.setState(Constant.EnumState.PROCESS.getValue());
			//跳过第一环节
			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
			taskService.addComment(task.getId(), processInstance.getId(), "");
			taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.BooksBuyFlowParams.USER_BZ.getValue(), SessionUtil.getUserInfo().getOrg().getOrgDirector()));
		}

		return  saveBooksDetailList(bookList,bookBuyBus);
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
		BookBuyBusiness bookBuyBusiness = null;
		List<User> dealUserList = null;
		User dealUser = null;
		Org org = null;
		boolean isNextUser = false;                 //是否是下一环节处理人（主要是处理领导审批，部长审批）
		String nextNodeKey = "";                    //下一环节名称
		Map<String, Object> variables = processInstance.getProcessVariables();
		boolean saveFlag = false;
		//以下是流程环节处理
		switch (task.getTaskDefinitionKey()) {
			case FlowConstant.BOOK_BZSP:
				bookBuyBusiness = bookBuyBusinessRepo.findById(BookBuyBusiness_.businessId.getName(), businessKey);
				bookBuyBusiness.setOrgDirectorId(SessionUtil.getUserInfo().getOrg().getOrgDirector());
				bookBuyBusiness.setOrgDirector(SessionUtil.getUserInfo().getOrg().getOrgDirectorName());
				bookBuyBusiness.setOrgDirectorDate(new Date());
				bookBuyBusiness.setApplyReason(flowDto.getDealOption());
				bookBuyBusinessRepo.save(bookBuyBusiness);
				task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
				assigneeValue=SessionUtil.getUserInfo().getOrg().getOrgSLeader();
				variables = ActivitiUtil.setAssigneeValue(FlowConstant.BooksBuyFlowParams.USER_FGLD.getValue(),assigneeValue);
				//下一环节还是自己处理
				if(assigneeValue.equals(SessionUtil.getUserId())){
					isNextUser = true;
					nextNodeKey = FlowConstant.BOOK_FGFZRSP;
				}
				break;
			case FlowConstant.BOOK_FGFZRSP:
				bookBuyBusiness = bookBuyBusinessRepo.findById(BookBuyBusiness_.businessId.getName(), businessKey);
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
				assigneeValue=Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
				variables = ActivitiUtil.setAssigneeValue(FlowConstant.BooksBuyFlowParams.USER_ZR.getValue(),assigneeValue );
				//下一环节还是自己处理
				if(assigneeValue.equals(SessionUtil.getUserId())){
					isNextUser = true;
					nextNodeKey = FlowConstant.BOOK_ZXZRSP;
				}
				break;
			case FlowConstant.BOOK_ZXZRSP:
				bookBuyBusiness = bookBuyBusinessRepo.findById(BookBuyBusiness_.businessId.getName(), businessKey);
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
				//下一环节还是自己处理
				if(assigneeValue.equals(SessionUtil.getUserId())){
					isNextUser = true;
					nextNodeKey = FlowConstant.BOOK_YSRK;
				}
				break;
			case FlowConstant.BOOK_YSRK:
				bookBuyBusiness = bookBuyBusinessRepo.findById(BookBuyBusiness_.businessId.getName(), businessKey);
				bookBuyBusiness.setFilerId(SessionUtil.getUserInfo().getId());
				bookBuyBusiness.setFiler(SessionUtil.getUserInfo().getLoginName());
				bookBuyBusiness.setFilerDate(new Date());
				bookBuyBusiness.setFilerHandlesug(flowDto.getDealOption());
				List<BookBuy> booksList = bookBuyBusiness.getBookBuyList();
				flowEndUpdateBooks(booksList);
				//bookBuyBusiness.getBookBuyList().clear();
				bookBuyBusiness.setBookBuyList(booksList);
				bookBuyBusinessRepo.save(bookBuyBusiness);
				task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
				break;
				default:;
		}

		taskService.addComment(task.getId(), processInstance.getId(), flowDto.getDealOption());    //添加处理信息
		if (flowDto.isEnd()) {
			taskService.complete(task.getId());
		} else {
			taskService.complete(task.getId(), variables);
			//如果下一环节还是自己
			if(isNextUser){
				List<Task> nextTaskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee(assigneeValue).list();
				for(Task t:nextTaskList){
					if(nextNodeKey.equals(t.getTaskDefinitionKey())){
						ResultMsg returnMsg = dealFlow(processInstance,t,flowDto);
						if(returnMsg.isFlag() == false){
							return returnMsg;
						}
						break;
					}
				}
			}
		}
		//放入腾讯通消息缓冲池
		RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(),assigneeValue);
		return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
	}

	/**
	 * 流程结束后更新图书库存数量、入库时间
	 * @param booksList
	 */
	private  void flowEndUpdateBooks(List<BookBuy> booksList){
		for(int i=0;i< booksList.size();i++){
			if(null !=booksList.get(i).getBookNumber()){
				booksList.get(i).setStoreConfirm(booksList.get(i).getBookNumber());
			}
			booksList.get(i).setStoreTime(new Date());
		}
	}

}