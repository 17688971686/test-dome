package cs.service.project;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.SimpleFormatter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.ICurrentUser;
import cs.common.Constant.EnumState;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.expert.Expert;
import cs.domain.project.DispatchDoc;
import cs.domain.project.Sign;
import cs.model.expert.ExpertDto;
import cs.model.project.DispatchDocDto;
import cs.model.sys.UserDto;
import cs.repository.repositoryImpl.project.DispatchDocRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.service.sys.UserService;

@Service
public class DispatchDocServiceImpl implements DispatchDocService {
	private static Logger log = Logger.getLogger(DispatchDocServiceImpl.class);
	@Autowired
	private DispatchDocRepo dispatchDocRepo;
	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private UserService userService;
	@Autowired
	private SignRepo signRepo;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	@Transactional
	public void save(DispatchDocDto dispatchDocDto) throws Exception{
		if(Validate.isString(dispatchDocDto.getSignId())){
			List<DispatchDoc> dispatchList=dispatchDocRepo.findDispatchBySignId(dispatchDocDto.getSignId());
			DispatchDoc dispatchDoc=null;
				if(dispatchList.size()<1){
					dispatchDoc = new DispatchDoc();
				}else{
					dispatchDoc=dispatchList.get(0);
				}
				dispatchDtoTodispatch(dispatchDocDto,dispatchDoc);
				dispatchDoc.setDraftDate(formatter.parse(dispatchDocDto.getDraftDate()));
				dispatchDoc.setDispatchDate(formatter.parse(dispatchDocDto.getDispatchDate()));
				Sign sign = signRepo.findById(dispatchDocDto.getSignId());
				dispatchDoc.setSign(sign);
				if(!Validate.isString(dispatchDoc.getId())){
					dispatchDoc.setId(UUID.randomUUID().toString());
				}			
				dispatchDocRepo.save(dispatchDoc);
				
				sign.setIsDispatchCompleted(EnumState.YES.getValue());
				sign.setDispatchDoc(dispatchDoc);
				signRepo.save(sign);
		}else{
			log.info("提交收文信息异常：无法获取收文ID（SignId）信息");
			throw new Exception(Constant.ERROR_MSG);
		}
		
	}
	
	private void dispatchDtoTodispatch(DispatchDocDto dispatchDocDto,DispatchDoc dispatchDoc){
		BeanCopierUtils.copyProperties(dispatchDocDto, dispatchDoc);
        Date now = new Date();
        dispatchDoc.setCreatedBy(currentUser.getLoginName());
        dispatchDoc.setCreatedDate(now);
        dispatchDoc.setModifiedBy(currentUser.getLoginName());
        dispatchDoc.setModifiedDate(now);
	}
	
	private void dispatchTodispatchDto(DispatchDoc dispatchDoc,DispatchDocDto dispatchDocDto){
		BeanCopierUtils.copyProperties(dispatchDoc,dispatchDocDto);
		Date now = new Date();
		dispatchDoc.setCreatedBy(currentUser.getLoginName());
		dispatchDoc.setCreatedDate(now);
		dispatchDoc.setModifiedBy(currentUser.getLoginName());
		dispatchDoc.setModifiedDate(now);
	}
	//初始化页面内容
	@Override
	public Map<String, Object> initDispatchData(String signId) {
		Map<String,Object> map = new HashMap<String,Object>();
		
		List<DispatchDoc> dispatchList=dispatchDocRepo.findDispatchBySignId(signId);
		Sign sign = signRepo.findById(signId);
		//获取本部门领导信息
		List<UserDto> userList = userService.findUserByDeptId(sign.getMaindepetid());
		map.put("mainUserList", userList);
		DispatchDoc dispatch=null;
		DispatchDocDto dispatchDto=new DispatchDocDto();
		if(dispatchList.size()==1){
			dispatch=dispatchList.get(0);
			dispatchTodispatchDto(dispatch,dispatchDto);
			dispatchDto.setDraftDate(formatter.format(dispatch.getDraftDate()));
			dispatchDto.setDispatchDate(formatter.format(dispatch.getDispatchDate()));
			dispatchDto.setSignId(signId);
			
		}else{
			dispatch=new DispatchDoc();
			if(Validate.isString(sign.getMaindepetid())){
				dispatch.setSecretLevel(sign.getSecrectlevel());
				dispatch.setSign(sign);
				//获取当前用户信息
				UserDto curUser = userService.findUserByName(currentUser.getLoginName());
				dispatch.setUserName(curUser.getLoginName());
				dispatch.setUserId(curUser.getId());
				dispatch.setOrgName(curUser.getOrgDto().getName());
				dispatch.setOrgId(curUser.getOrgDto().getId());
				dispatchTodispatchDto(dispatch,dispatchDto);
				dispatchDto.setSignId(signId);
				
			}
		}
		map.put("dispatch",dispatchDto);
		return map;
	}

}
