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
					bookBuyBusiness.setApplyDept(SessionUtil.getUserInfo().getOrg().getName());
					bookBuyBusiness.setOperator(SessionUtil.getUserInfo().getDisplayName());
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
		List<User> userList = null;
		User dealUser = null;
		Org org = null;
		Map<String, Object> variables = processInstance.getProcessVariables();
		boolean saveFlag = false;

		//以下是流程环节处理
		switch (task.getTaskDefinitionKey()) {
			//各项目负责人/部门提出购买图书请求
			case FlowConstant.BOOK_LEADER_CGQQ:
				bookBuyBusiness = bookBuyBusinessRepo.findById(BookBuyBusiness_.businessId.getName(), businessKey);
				task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
				taskService.addComment(task.getId(), processInstance.getId(), flowDto.getDealOption());    //添加处理信息
				taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.BooksBuyFlowParams.USER_BZ.getValue(), SessionUtil.getUserId()));
				task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
				//todo:设置购买渠道后saveFlag置为true
				break;
			case FlowConstant.BOOK_BZSP:

				break;
			case FlowConstant.BOOK_FGFZRSP:

				break;

			case FlowConstant.BOOK_ZXZRSP:

				break;
			case FlowConstant.BOOK_YSRK:
				break;
				default:
					;
		}
		if (bookBuyBusiness != null && saveFlag) {
			//todo:设置购买渠道购买渠道
			//bookBuyBusiness.setBuyChannel();
			bookBuyBusinessRepo.save(bookBuyBusiness);
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