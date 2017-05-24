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
import cs.domain.expert.Expert;
import cs.domain.project.DispatchDoc;
import cs.domain.project.DispatchDoc_;
import cs.domain.project.MergeDispa;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.domain.sys.Org;
import cs.model.project.DispatchDocDto;
import cs.model.project.SignDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.DispatchDocRepo;
import cs.repository.repositoryImpl.project.MergeDispaRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.sys.OrgRepo;
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
	private OrgRepo orgRepo;
	@Autowired
	private MergeDispaRepo mergeDispaRepo;

	// 初始化页面获取已选项目
	@Override
	public Map<String, Object> getSeleSignBysId(String bussnessId) {
		Map<String, Object> map = new HashMap<>();
		MergeDispa mergeDispa = mergeDispaRepo.findById(bussnessId);
		List<SignDto> signDtoList = null;
		String linkSignId = " ";

		if (mergeDispa != null) {
			linkSignId = mergeDispa.getLinkSignId();
			signDtoList = new ArrayList<>();
			String[] ids = linkSignId.split(",");
			for (String id : ids) {
				if (Validate.isString(id)) {
					SignDto signDto = new SignDto();
					Sign sign = signRepo.findById(id);
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
	public List<SignDto> getSign(SignDto signDto) {
		List<Sign> list = null;
		
		HqlBuilder hqlBuilder = HqlBuilder.create(" from " + Sign.class.getSimpleName()).append(" where ");
		if (StringUtils.isNotBlank(signDto.getSignid())) {
			
			String[] linkSids = signDto.getSignid().split(",");
			hqlBuilder.append(Sign_.signid.getName()).append(" not in( ");
			for (int i = 0, j = 0; i < linkSids.length; i++) {
				if (StringUtils.isNotBlank(linkSids[i])) {
					if (j != 0) {
						hqlBuilder.append(",");
					}
					hqlBuilder.append(":linkSids" + i).setParam("linkSids" + i, linkSids[i]);
					j++;
				}
			}
			hqlBuilder.append(")");
			hqlBuilder.append(" and ");
			
		}
		hqlBuilder.append(Sign_.isDispatchCompleted.getName())
			.append(" not in(:isDispatchCompleted,:isDispatchCompleted1)").setParam("isDispatchCompleted", Constant.EnumState.NO.getValue())
			.setParam("isDispatchCompleted1", "null");
		hqlBuilder.append(" and " + Sign_.folwState.getName()).append(" not in(:folwState,:folwState1,:folwState2)")
			.setParam("folwState", Constant.EnumState.STOP.getValue()).setParam("folwState1", Constant.EnumState.DELETE.getValue()).setParam("folwState2", Constant.EnumState.NO.getValue());
		hqlBuilder.append(" and " + Sign_.signState.getName()).append(" not in(:signState,:signState1,:signState2)")
			.setParam("signState", Constant.EnumState.STOP.getValue()).setParam("signState1", Constant.EnumState.DELETE.getValue()).setParam("signState2", Constant.EnumState.NO.getValue());
		if(StringUtils.isNotBlank(signDto.getProjectname())){
			hqlBuilder.append(" and " + Sign_.projectname.getName()).append("=:projectname").setParam("projectname", signDto.getProjectname());
		}
		if(StringUtils.isNotBlank(signDto.getBuiltcompanyName())){
			hqlBuilder.append(" and " + Sign_.builtcompanyName.getName()).append("=:builtcompanyName").setParam("builtcompanyName", signDto.getBuiltcompanyName());
		}
		if(StringUtils.isNotBlank(signDto.getReviewstage())){
			hqlBuilder.append(" and " + Sign_.reviewstage.getName()).append("=:reviewstage").setParam("reviewstage", signDto.getReviewstage());
		}
		if(signDto.getStartTime()!=null&&signDto.getEndTime()!=null){
			hqlBuilder.append(" and " + Sign_.signdate.getName()).append(" between ").append(":startTime") .append(" and ").append(":endTime").setParam("startTime", signDto.getStartTime()).setParam("endTime", signDto.getEndTime());
		}
		list = signRepo.findByHql(hqlBuilder);
		
		List<SignDto> signDtoList = new ArrayList<>();
		if (list != null) {
			for (Sign sign1 : list) {
				SignDto signDto1 = new SignDto();
				BeanCopierUtils.copyProperties(sign1, signDto1);
				signDtoList.add(signDto1);
			}
		}
		return signDtoList;
	}

	// 获取已选项目
	@Override
	public List<SignDto> getSignbyIds(String[] ids) {
		List<SignDto> signDtoList = new ArrayList<>();
		for (String id : ids) {
			if (Validate.isString(id)) {
				SignDto signDto = new SignDto();
				Sign sign = signRepo.findById(id);
				BeanCopierUtils.copyProperties(sign, signDto);
				signDto.setCreatedDate(sign.getCreatedDate());
				signDto.setModifiedDate(sign.getModifiedDate());
				signDtoList.add(signDto);
			}
		}
		return signDtoList;
	}

	// 生成发文关联
	@Override
	@Transactional
	public void mergeDispa(String signId, String linkSignId) {
		Date now = new Date();
		Sign sign = signRepo.findById(signId);
		MergeDispa mergeDispa = new MergeDispa();
		mergeDispa.setBusinessId(sign.getDispatchDoc().getId());
		mergeDispa.setType(sign.getDispatchDoc().getDispatchType());
		mergeDispa.setSignId(signId);
		mergeDispa.setLinkSignId(linkSignId);
		mergeDispa.setCreatedBy(currentUser.getLoginName());
		mergeDispa.setModifiedBy(currentUser.getLoginName());
		mergeDispa.setCreatedDate(now);
		mergeDispa.setModifiedDate(now);
		mergeDispaRepo.save(mergeDispa);

		// mergeDispaServiceImpl.mergeProject(dispatchDocDto);
	}

	// 生成文件字号
	@Override
	@Transactional
	public String fileNum(String dispaId) {
		String fileNum = NumIncreaseUtils.getFileNo();
		DispatchDoc dispa = dispatchDocRepo.findById(dispaId);
		dispa.setFileNum(fileNum);
		dispatchDocRepo.save(dispa);
		dispa.getSign().setDocnum(fileNum);
		return fileNum;
	}

	// 保存发文拟稿
	@Override
	@Transactional
	public void save(DispatchDocDto dispatchDocDto) throws Exception {
		if (Validate.isString(dispatchDocDto.getSignId())) {
			DispatchDoc dispatchDoc = new DispatchDoc();
			BeanCopierUtils.copyProperties(dispatchDocDto, dispatchDoc);

			Sign sign = signRepo.findById(dispatchDocDto.getSignId());
			dispatchDoc.setSign(sign);
			if (!Validate.isString(dispatchDoc.getId())) {
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
		} else {
			log.info("提交收文信息异常：无法获取收文ID（SignId）信息");
			throw new Exception(Constant.ERROR_MSG);
		}
	}

	// 初始化页面内容
	@Override
	public Map<String, Object> initDispatchData(String signId) {
		Sign sign = signRepo.findById(signId);
		//String dispatype=sign.get
		Map<String, Object> map = new HashMap<String, Object>();
		
		//获取所有部门信息
		List<Org> orgList=orgRepo.findAll();
		List<OrgDto> orgDtoList=new ArrayList<>();
		for (Org org : orgList) {
			OrgDto orgDto=new OrgDto();
			BeanCopierUtils.copyProperties(org, orgDto);
			orgDtoList.add(orgDto);
		}
		
		DispatchDocDto dispatchDto = new DispatchDocDto();
		DispatchDoc dispatch  = sign.getDispatchDoc();
		if (dispatch == null) {
			dispatch = new DispatchDoc();
			dispatch.setSecretLevel(sign.getSecrectlevel());
			// 获取当前用户信息
			dispatch.setUserName(currentUser.getLoginUser().getLoginName());
			dispatch.setUserId(currentUser.getLoginUser().getId());
			// 获取当前部门信息
			dispatch.setOrgName(
					currentUser.getLoginUser().getOrg() == null ? "" : currentUser.getLoginUser().getOrg().getName());
			dispatch.setOrgId(
					currentUser.getLoginUser().getOrg() == null ? "" : currentUser.getLoginUser().getOrg().getId());
		}
		BeanCopierUtils.copyProperties(dispatch, dispatchDto);

		dispatchDto.setSignId(signId);
		
		map.put("dispatch", dispatchDto);

		// 获取主办处联系人
		List<UserDto> userList = userService.findUserByOrgId(sign.getmOrgId());
		map.put("mainUserList", userList);
		map.put("orgList", orgDtoList);
		return map;
	}

	@Override
	public DispatchDocDto initDispatchBySignId(String signId) {
		DispatchDocDto dispatchDocDto = new DispatchDocDto();

		HqlBuilder hqlBuilder = HqlBuilder.create();
		hqlBuilder.append(" from " + DispatchDoc.class.getSimpleName() + " where " + DispatchDoc_.sign.getName() + "."
				+ Sign_.signid.getName() + " = :signId ");
		hqlBuilder.setParam("signId", signId);

		List<DispatchDoc> list = dispatchDocRepo.findByHql(hqlBuilder);
		if (list != null && list.size() > 0) {
			DispatchDoc dispatchDoc = list.get(0);
			BeanCopierUtils.copyProperties(dispatchDoc, dispatchDocDto);
		}
		return dispatchDocDto;
	}

}
