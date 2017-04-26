package cs.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cs.common.Constant;
import cs.common.ICurrentUser;
import cs.common.utils.ActivitiUtil;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.Role;
import cs.domain.Sign;
import cs.domain.User;
import cs.model.PageModelDto;
import cs.model.SignDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.SignRepo;

@Service
public class SignServiceImpl implements SignService {
	private static Logger log = Logger.getLogger(SignServiceImpl.class);
	
	@Autowired
	private SignRepo signRepo;
	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private UserService userService;
	
	//flow service
	@Autowired
	private TaskService taskService;
	@Autowired
	private RuntimeService runtimeService;
	
	@Override
	@Transactional
	public void createSign(SignDto signDto) { 
		Sign sign = new Sign(); 
		SignDtoToSign(signDto,sign);       
        signRepo.save(sign);
	}			
	
	@Override
	public PageModelDto<SignDto> get(ODataObj odataObj) {
		PageModelDto<SignDto> pageModelDto = new PageModelDto<SignDto>();
		List<Sign> signs = signRepo.findByOdata(odataObj);
		List<SignDto> signDtos = new ArrayList<SignDto>();
		
		if(signs != null && signs.size() > 0){
			signs.forEach(x->{
				SignDto signDto = new SignDto();
				BeanCopierUtils.copyProperties(x, signDto);
				//cannot copy 
				signDto.setCreatedDate(x.getCreatedDate());
				signDto.setModifiedDate(x.getModifiedDate());
				
				signDtos.add(signDto);
			});						
		}		
		pageModelDto.setCount(signDtos.size());
		pageModelDto.setValue(signDtos);		
		return pageModelDto;
	}

	@Override
	@Transactional
	public void completeFillSign(SignDto signDto) throws Exception{		
		if(!Validate.isString(signDto.getSignid())){
			throw new Exception( "操作异常，错误信息已记录，请联系管理员尽快处理！");
		}
		Sign sign = signRepo.findById(signDto.getSignid());
		BeanCopierUtils.copyPropertiesIgnoreNull(signDto, sign);
		
		sign.setModifiedBy(currentUser.getLoginName());
	    sign.setModifiedDate(new Date());
		signRepo.save(sign);
		
		try{
			//创建流程 并 跳过第一第二环节
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constant.EnumFlow.SIGN.getValue(),signDto.getSignid());
			//设置第三环节参数，为综合部部长
			Map<String,Object> flowParamMap = ActivitiUtil.flowArguments(null,currentUser.getLoginName(),currentUser.getLoginName(),false);
			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();		
			taskService.complete(task.getId(),flowParamMap);
			
			//设置第三环节参数，为综合部部长
			ActivitiUtil.flowArguments(flowParamMap,null,Constant.EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue(),true);		
			task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
			taskService.complete(task.getId(),flowParamMap);
			
			log.info("项目签收流程创建成功,流程实例ID为"+processInstance.getId()+"，并成功跳过前2个环节！");
		}catch(Exception e){
			log.info("项目签收流程创建失败：" + e.getMessage());
			throw new Exception( "保存失败，错误信息已记录，请联系管理员尽快处理！");
		}				
	}

	@Override
	public void updateSign(SignDto signDto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> initFillPageData(String signId) {
		Map<String,Object> map = new HashMap<String,Object>();
		
		//1收文对象
		Sign sign = signRepo.findById(signId);
		SignDto signDto = new SignDto();
		SignToSignDto(sign,signDto);		
		map.put("sign", signDto);
		
		//2以下初始化待完成
		//map.put("sign", signRepo.findById(signId));
		//System.out.println(JSON.toJSONString(map));
		return map;
	}

	@Override
	public PageModelDto<SignDto> getFlow(ODataObj odataObj) {
		//获取当前用户信息
		User curUser = userService.findUserByName(currentUser.getLoginName());
		TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey(Constant.EnumFlow.SIGN.getValue());
		taskQuery.or().taskCandidateUser(curUser.getLoginName()).taskAssignee(curUser.getLoginName());
		
		List<Role> roles = curUser.getRoles();
		if(roles != null){
			List<String> roleList = new ArrayList<String>(roles.size());
			roles.forEach(role ->{
				roleList.add(role.getRoleName());
			});
			if(roleList != null && roleList.size() > 0){
				taskQuery.taskCandidateGroupIn(roleList);
			}
		}	
		
		
		long total = taskQuery.count();		
		List<Task> tasks = taskQuery.orderByTaskCreateTime().desc().listPage(odataObj.getSkip(), odataObj.getTop());
		
		PageModelDto<SignDto> pageModelDto = new PageModelDto<SignDto>();
		List<SignDto> signDtos = new ArrayList<SignDto>();
		if(tasks != null){
			tasks.forEach(t->{
				// 通过任务对象获取流程实例
				ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(t.getProcessInstanceId()).singleResult();
				// 通过流程实例获取“业务键”
				String signId = pi.getBusinessKey();
				
				SignDto signDto = new SignDto();
				Sign sign = signRepo.findById(signId);				
				SignToSignDto(sign,signDto);
				// 添加流程参数
				signDto.setTaskId(t.getId());
				signDto.setProcessInstanceId(t.getProcessInstanceId());
				
				signDtos.add(signDto);
			});		
		}				
		pageModelDto.setCount(Integer.valueOf(String.valueOf(total)));
		pageModelDto.setValue(signDtos);		
		return pageModelDto;
	}

	private void SignToSignDto(Sign sign,SignDto signDto){
		BeanCopierUtils.copyProperties(sign, signDto);
		signDto.setCreatedDate(sign.getCreatedDate());
		signDto.setModifiedDate(sign.getModifiedDate());
	}
	
	private void SignDtoToSign(SignDto signDto,Sign sign){
		BeanCopierUtils.copyProperties(signDto, sign);
        Date now = new Date();
        sign.setCreatedBy(currentUser.getLoginName());
        sign.setCreatedDate(now);
        sign.setModifiedBy(currentUser.getLoginName());
        sign.setModifiedDate(now);
	}

	@Override
	public void claimSignFlow(String taskId) {
		 taskService.claim(taskId, currentUser.getLoginName());
	}
}
