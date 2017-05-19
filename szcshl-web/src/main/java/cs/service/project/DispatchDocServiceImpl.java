package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.NumIncreaseUtils;
import cs.common.utils.Validate;
import cs.domain.project.DispatchDoc;
import cs.domain.project.DispatchDoc_;
import cs.domain.project.MergeDispa;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.model.project.DispatchDocDto;
import cs.model.project.SignDto;
import cs.model.sys.UserDto;
import cs.repository.repositoryImpl.project.DispatchDocRepo;
import cs.repository.repositoryImpl.project.MergeDispaRepo;
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
	@Autowired
	private MergeDispaRepo mergeDispaRepo;
	
	//初始化页面获取已选项目
	@Override
	public Map<String,Object> getSeleSignBysId(String bussnessId){
		Map<String,Object> map=new HashMap<>();
		MergeDispa mergeDispa=mergeDispaRepo.findById(bussnessId);
		List<SignDto> signDtoList=null;
		String linkSignId="";
		if(mergeDispa!=null&&!mergeDispa.equals("")){
			
			linkSignId=mergeDispa.getLinkSignId();
			signDtoList= new ArrayList<>();
			String [] ids=linkSignId.split(",");
			
			for (String id : ids) {
				
				if(Validate.isString(id)){
					
					SignDto signDto=new SignDto();
					Sign sign=signRepo.findById(id);
					BeanCopierUtils.copyProperties(sign, signDto);
					signDto.setCreatedDate(sign.getCreatedDate());
					signDto.setModifiedDate(sign.getModifiedDate());
					signDtoList.add(signDto);
				}
			}
			
		}
		map.put("signDtoList", signDtoList);
		map.put("linkSignId", linkSignId);
		return map;
	}
	
	
	//获取待选项目
	@Override
	public List<SignDto> getSign(String linkSignId) {
		List<SignDto> signDtoList=new ArrayList<>();
		List<Sign> list=null;
		if(linkSignId.equals("")){
			 list=signRepo.findAll();
		}else{
			String[] linkSids=linkSignId.split(",");
			HqlBuilder hqlBuilder= HqlBuilder.create();
			hqlBuilder.append(" from "+Sign.class.getSimpleName()+" where "+Sign_.signid.getName()+" not in(");
			for (int i=0;i<linkSids.length;i++) {
				if(i==0){
					hqlBuilder.append(":linkSids"+i);
					hqlBuilder.setParam("linkSids"+i, linkSids[i]);
				}else{
					hqlBuilder.append(",:linkSids"+i);
					hqlBuilder.setParam("linkSids"+i, linkSids[i]);
				}	
			}
			hqlBuilder.append(")");
			list=signRepo.findByHql(hqlBuilder);
		}
		for (Sign sign : list) {
			SignDto signDto=new SignDto();
			if(!Validate.isString(sign.getIsDispatchCompleted())||sign.getIsDispatchCompleted().equals("0")){
				BeanCopierUtils.copyProperties(sign, signDto);
				signDto.setCreatedDate(sign.getCreatedDate());
				signDto.setModifiedDate(sign.getModifiedDate());
				signDtoList.add(signDto);
			}				
		}
		
		return signDtoList;
	}
	
	//获取已选项目
	@Override
	public List<SignDto> getSignbyIds(String[] ids){
		List<SignDto> signDtoList=new ArrayList<>();
		for (String id : ids) {
			
			if(Validate.isString(id)){
				
				SignDto signDto=new SignDto();
				Sign sign=signRepo.findById(id);
				BeanCopierUtils.copyProperties(sign, signDto);
				signDto.setCreatedDate(sign.getCreatedDate());
				signDto.setModifiedDate(sign.getModifiedDate());
				signDtoList.add(signDto);
				
			}
		}
		return signDtoList;
	}
	
	//生成发文关联
	@Override
	@Transactional
	public void mergeDispa(String signId,String linkSignId){
		Date now = new Date();
		Sign sign=signRepo.findById(signId);
		MergeDispa mergeDispa=new MergeDispa();
		mergeDispa.setBusinessId(sign.getDispatchDoc().getId());
		mergeDispa.setType(sign.getDispatchDoc().getDispatchType());
		mergeDispa.setSignId(signId);
		mergeDispa.setLinkSignId(linkSignId);
		mergeDispa.setCreatedBy(currentUser.getLoginName());
		mergeDispa.setModifiedBy(currentUser.getLoginName());
		mergeDispa.setCreatedDate(now);
		mergeDispa.setModifiedDate(now);
		mergeDispaRepo.save(mergeDispa);
		
		//mergeDispaServiceImpl.mergeProject(dispatchDocDto);
	}
	//生成文件字号
	@Override
	@Transactional
	public String fileNum(String dispaId){
		String fileNum=NumIncreaseUtils.getFileNo();
		DispatchDoc dispa=dispatchDocRepo.findById(dispaId);
		dispa.setFileNum(fileNum);
		dispatchDocRepo.save(dispa);
		dispa.getSign().setDocnum(fileNum);
		return fileNum;
	}
	
	//保存发文拟稿
	@Override
	@Transactional
	public void save(DispatchDocDto dispatchDocDto) throws Exception {
		
		DispatchDoc dispatchDoc=null;
		if(Validate.isString(dispatchDocDto.getSignId())){
			Date now=new Date();
			List<DispatchDoc> dispatchList=dispatchDocRepo.findDispatchBySignId(dispatchDocDto.getSignId());
				if(dispatchList.size()<1){
					dispatchDoc = new DispatchDoc();
				}else{
					dispatchDoc=dispatchList.get(0);
				}
				dispatchDtoTodispatch(dispatchDocDto,dispatchDoc);	
				dispatchDoc.setDraftDate(now);
				dispatchDoc.setDispatchDate(DateUtils.ConverToDate(dispatchDocDto.getDispatchDate()));
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
		Date now=new Date();
		List<DispatchDoc> dispatchList=dispatchDocRepo.findDispatchBySignId(signId);
		Sign sign = signRepo.findById(signId);
		//获取本部门领导信息
		List<UserDto> userList = userService.findUserByOrgId(sign.getMaindepetid());
		map.put("mainUserList", userList);
		DispatchDoc dispatch=null;
		DispatchDocDto dispatchDto=new DispatchDocDto();
		if(dispatchList.size()>0){
			dispatch=dispatchList.get(0);
			dispatchTodispatchDto(dispatch,dispatchDto);
			dispatchDto.setDraftDate(DateUtils.convertDateToString(dispatch.getDraftDate()));
			dispatchDto.setDispatchDate(DateUtils.convertDateToString(dispatch.getDispatchDate()));
			dispatchDto.setCreatedBy(dispatchDto.getUserName());
			
		}else{
			dispatch=new DispatchDoc();
			//if(Validate.isString(sign.getMaindepetid())){
				dispatch.setSecretLevel(sign.getSecrectlevel());
				dispatch.setSign(sign);
				//获取当前用户信息
				UserDto curUser = userService.findUserByName(currentUser.getLoginName());
				dispatch.setUserName(curUser.getLoginName());
				dispatch.setUserId(curUser.getId());
				dispatch.setOrgName(curUser.getOrgDto().getName());
				dispatch.setOrgId(curUser.getOrgDto().getId());
				dispatchTodispatchDto(dispatch,dispatchDto);
				dispatchDto.setDraftDate(DateUtils.convertDateToString(now));
			//}
		}
		dispatchDto.setSignId(signId);
		map.put("dispatch",dispatchDto);
		return map;
	}
		
	@Override
	public DispatchDocDto initDispatchBySignId(String signId) {
		DispatchDocDto dispatchDocDto = new DispatchDocDto();
		
		HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from "+DispatchDoc.class.getSimpleName()+" where "+DispatchDoc_.sign.getName()+"."+Sign_.signid.getName()+" = :signId ");
        hqlBuilder.setParam("signId", signId);               
        
        List<DispatchDoc> list = dispatchDocRepo.findByHql(hqlBuilder);
        if(list != null && list.size() > 0){
        	DispatchDoc  dispatchDoc = list.get(0);
        	BeanCopierUtils.copyProperties(dispatchDoc,dispatchDocDto);
        	//日期要手动转换
        	dispatchDocDto.setCreatedDate(dispatchDoc.getCreatedDate());
        	dispatchDocDto.setDraftDate(DateUtils.convertDateToString(dispatchDoc.getDraftDate()));
        	dispatchDocDto.setDispatchDate(DateUtils.convertDateToString(dispatchDoc.getDispatchDate()));
        	dispatchDocDto.setDirectorDate(DateUtils.convertDateToString(dispatchDoc.getDirectorDate()));
        }
		return dispatchDocDto;
	}

}
