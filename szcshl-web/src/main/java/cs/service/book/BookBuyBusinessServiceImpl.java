package cs.service.book;

import cs.common.Constant;
import cs.common.FlowConstant;
import cs.common.ResultMsg;
import cs.common.utils.ActivitiUtil;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
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
	@Autowired
	private OrgDeptRepo orgDeptRepo;
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
	public void delete(String id) {

	}

	@Override
	public ResultMsg saveBooksDetailList(BookBuyDto[] bookList,BookBuyBusiness bookBuyBus) {
		if(bookList != null && bookList.length > 0){
			Date now = new Date();
			BookBuyBusiness bookBuyBusiness = new BookBuyBusiness();
			if (!Validate.isString(bookBuyBus.getBusinessId())) {
				bookBuyBusiness.setBusinessId(UUID.randomUUID().toString());
				bookBuyBusiness.setCreatedBy(SessionUtil.getDisplayName());
				bookBuyBusiness.setModifiedBy(SessionUtil.getDisplayName());
				bookBuyBusiness.setCreatedDate(now);
				bookBuyBusiness.setModifiedDate(now);
				bookBuyBusiness.setApplyDept(bookList[0].getApplyDept());
				bookBuyBusiness.setOperator(bookList[0].getOperator());
				bookBuyBusiness.setBuyChannel(bookList[0].getBuyChannel());
				bookBuyBusinessRepo.save(bookBuyBusiness);
			}else{
				//bookBuyBusiness = bookBuyBusinessRepo.findById(bookBuyBus.getBusinessId());
				bookBuyBusiness = bookBuyBusinessRepo.getById(bookBuyBus.getBusinessId());
				if(null == bookBuyBusiness){//直接发起流程
					bookBuyBusiness = new BookBuyBusiness();
					BeanCopierUtils.copyProperties(bookBuyBus,bookBuyBusiness);
					bookBuyBusiness.setCreatedBy(SessionUtil.getDisplayName());
					bookBuyBusiness.setModifiedBy(SessionUtil.getDisplayName());
					bookBuyBusiness.setCreatedDate(now);
					bookBuyBusiness.setModifiedDate(now);
					bookBuyBusiness.setApplyDept(bookList[0].getApplyDept());
					bookBuyBusiness.setOperator(bookList[0].getOperator());
					bookBuyBusiness.setBuyChannel(bookList[0].getBuyChannel());
					bookBuyBusinessRepo.save(bookBuyBusiness);
				}else{   //保存后发起流程
					bookBuyRepoRepo.deleteById(BookBuy_.bookBuyBusiness.getName(),bookBuyBus.getBusinessId());
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
			if (bookBuyList.size()>0)
			bookBuyRepoRepo.bathUpdate(bookBuyList);
			return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"保存成功！");
		}else{
			return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"请添加图书信息后，再进行保存！");
		}
	}

	@Override
	public ResultMsg startFlow(BookBuyDto[] bookList,BookBuyBusiness bookBuyBus) {
		if(null==bookList || bookList.length==0){
			return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"请添加图书信息后，再发起流程！");
		}
		//2、判断是否已经发起流程，如果未发起，则发起流程
		if (!Validate.isString(bookBuyBus.getProcessInstanceId())) {
			if (!Validate.isString(bookBuyBus.getBusinessId())) {
				bookBuyBus.setBusinessId(UUID.randomUUID().toString());
			}
			bookBuyBus.setBusinessName("图书采购流程"+bookBuyBusinessRepo.findAll().size()+1);
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
		return saveBooksDetailList(bookList,bookBuyBus);
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
		BookBuyBusiness bookBuyBusiness = null;
		List<User> dealUserList = null;
		User dealUser = null;
		Org org = null;
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
				variables = ActivitiUtil.setAssigneeValue(FlowConstant.BooksBuyFlowParams.USER_FGLD.getValue(), SessionUtil.getUserInfo().getOrg().getOrgSLeader());
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
				variables = ActivitiUtil.setAssigneeValue(FlowConstant.BooksBuyFlowParams.USER_ZR.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
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
				break;
			case FlowConstant.BOOK_YSRK:
				bookBuyBusiness = bookBuyBusinessRepo.findById(BookBuyBusiness_.businessId.getName(), businessKey);
				bookBuyBusiness.setFilerId(SessionUtil.getUserInfo().getId());
				bookBuyBusiness.setFiler(SessionUtil.getUserInfo().getLoginName());
				bookBuyBusiness.setFilerDate(new Date());
				bookBuyBusiness.setFilerHandlesug(flowDto.getDealOption());
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
		}
		return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
	}

}