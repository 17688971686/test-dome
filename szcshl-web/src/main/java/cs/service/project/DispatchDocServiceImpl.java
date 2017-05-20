package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
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
	public Map<String, Object> getSeleSignBysId(String bussnessId) {
		Map<String, Object> map = new HashMap<>();
		MergeDispa mergeDispa = mergeDispaRepo.findById(bussnessId);
		List<SignDto> signDtoList = null;
		String linkSignId = "";
		
		if (mergeDispa != null && !mergeDispa.getSignId().equals("")) {
			linkSignId = mergeDispa.getLinkSignId();
			signDtoList = new ArrayList<>();
			String[] ids = linkSignId.split(",");
			
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

	// 获取待选项目
	@Override
	public List<SignDto> getSign(String linkSignId) {
		List<SignDto> signDtoList = new ArrayList<>();
		List<Sign> list = null;
		if (StringUtils.isBlank(linkSignId)) {
			list = signRepo.findAll();
		} else {
			String[] linkSids = linkSignId.split(",");
			if (StringUtils.isNoneBlank(linkSids)) {
				HqlBuilder hqlBuilder = HqlBuilder.create(" from "+Sign.class.getSimpleName())
						.append(" where " +Sign_.signid.getName()).append(" not in( ");
				for (int i = 0; i < linkSids.length; i++) {
					if (i != 0) {
						hqlBuilder.append(",");
					}
					hqlBuilder.append(":linkSids" + i).setParam("linkSids" + i, linkSids[i]);
				}
				hqlBuilder.append(")");
				list = signRepo.findByHql(hqlBuilder);
			}
		}
		if(list != null){
			for (Sign sign : list) {
				SignDto signDto=new SignDto();
				if(!Validate.isString(sign.getIsDispatchCompleted()) || sign.getIsDispatchCompleted().equals("0")){
					BeanCopierUtils.copyProperties(sign, signDto);				
					signDtoList.add(signDto);
				}				
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
		if(Validate.isString(dispatchDocDto.getSignId())){
			DispatchDoc dispatchDoc = new DispatchDoc();
			BeanCopierUtils.copyProperties(dispatchDocDto, dispatchDoc);	
						
			Sign sign = signRepo.findById(dispatchDocDto.getSignId());
			dispatchDoc.setSign(sign);
			if(!Validate.isString(dispatchDoc.getId())){
				Date now = new Date();
				dispatchDoc.setId(UUID.randomUUID().toString());
				dispatchDoc.setDraftDate(now);
				dispatchDoc.setCreatedDate(now);
				dispatchDoc.setModifiedDate(now);
				dispatchDoc.setCreatedBy(currentUser.getLoginName());
				dispatchDoc.setModifiedBy(currentUser.getLoginName());
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
	
	//初始化页面内容
	@Override
	public Map<String, Object> initDispatchData(String signId) {
		Sign sign = signRepo.findById(signId);		
		Map<String,Object> map = new HashMap<String,Object>();
		DispatchDocDto dispatchDto=new DispatchDocDto();

		DispatchDoc dispatch = null;		
		if(sign.getDispatchDoc() != null && Validate.isString(sign.getDispatchDoc().getId())){
			dispatch = sign.getDispatchDoc();
		}						
		if(dispatch == null){
			dispatch = new DispatchDoc();
			dispatch.setSecretLevel(sign.getSecrectlevel());
			//获取当前用户信息
			dispatch.setUserName(currentUser.getLoginUser().getLoginName());
			dispatch.setUserId(currentUser.getLoginUser().getId());
			dispatch.setOrgName(currentUser.getLoginUser().getOrg()==null?"":currentUser.getLoginUser().getOrg().getName());
			dispatch.setOrgId(currentUser.getLoginUser().getOrg()==null?"":currentUser.getLoginUser().getOrg().getId());					
		}
		BeanCopierUtils.copyProperties(dispatch,dispatchDto);	
		
		dispatchDto.setSignId(signId);
		map.put("dispatch",dispatchDto);
		
		//获取本部门领导信息
		List<UserDto> userList = userService.findUserByOrgId(sign.getMaindepetid());
		map.put("mainUserList", userList);
				
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
        }
		return dispatchDocDto;
	}

}
