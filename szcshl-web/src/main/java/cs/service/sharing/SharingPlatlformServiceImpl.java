package cs.service.sharing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.Constant.OrgName;
import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.sharing.SharingPlatlform;
import cs.domain.sharing.SharingPrivilege;
import cs.domain.sys.Org;
import cs.domain.sys.Org_;
import cs.domain.sys.SysFile;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.sharing.SharingPlatlformDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sharing.SharingPlatlformRepo;
import cs.repository.repositoryImpl.sharing.SharingPrivilegeRepo;
import cs.repository.repositoryImpl.sys.OrgRepo;
import cs.repository.repositoryImpl.sys.UserRepo;

/**
 * Description: 共享平台 业务操作实现类
 * author: sjy
 * Date: 2017-7-11 10:40:17
 */
@Service
public class SharingPlatlformServiceImpl  implements SharingPlatlformService {

	private static Logger logger = Logger.getLogger(SharingPlatlformServiceImpl.class);
	@Autowired
	private SharingPlatlformRepo sharingPlatlformRepo;
	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private SharingPrivilegeRepo sharingPrivilegeRepo;
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private OrgRepo orgRepo;
	
	@Override
	public PageModelDto<SharingPlatlformDto> get(ODataObj odataObj) {
		PageModelDto<SharingPlatlformDto> pageModelDto = new PageModelDto<SharingPlatlformDto>();
		List<SharingPlatlform> resultList = sharingPlatlformRepo.findByOdata(odataObj);
		List<SharingPlatlformDto> resultDtoList = new ArrayList<SharingPlatlformDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				SharingPlatlformDto modelDto = new SharingPlatlformDto();
				BeanCopierUtils.copyProperties(x, modelDto);
				modelDto.setCreatedDate(x.getCreatedDate());
				modelDto.setModifiedDate(x.getModifiedDate());
				
				resultDtoList.add(modelDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(resultDtoList);		
		return pageModelDto;
	}

	@Override
	@Transactional
	public void save(SharingPlatlformDto record) {
		Org  org= orgRepo.findById(record.getOrgId());
		
		SharingPlatlform domain = new SharingPlatlform(); 
		
		SharingPrivilege shared = new SharingPrivilege();
		Date now = new Date();
		
		
		BeanCopierUtils.copyProperties(record, domain); 
		domain.setSharId(UUID.randomUUID().toString());
		domain.setPubDept(org.getName());
		domain.setIsPublish(record.getIsPublish());
		domain.setCreatedBy(currentUser.getLoginName());
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setPublishDate(now);
		domain.setPublishUsername(currentUser.getLoginName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		sharingPlatlformRepo.save(domain);
		record.setSharId(domain.getSharId());
		//for(Org o : record.getOrgs()){
		
		shared.setId(UUID.randomUUID().toString());
		shared.setBusinessId(domain.getSharId());
		shared.setBusinessType(org.getName());
		shared.setCreatedBy(currentUser.getLoginName());
		shared.setModifiedBy(currentUser.getLoginName());
		shared.setCreatedDate(now);
		shared.setModifiedDate(now);
		sharingPrivilegeRepo.save(shared);
	//}
	}

	@Override
	@Transactional
	public void update(SharingPlatlformDto record) {
		SharingPlatlform domain = sharingPlatlformRepo.findById(record.getSharId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		Date now = new Date();
		domain.setCreatedBy(currentUser.getLoginName());
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setPublishDate(now);
		domain.setPublishUsername(currentUser.getLoginName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		sharingPlatlformRepo.save(domain);
	}

	@Override
	public SharingPlatlformDto findById(String id) {		
		SharingPlatlformDto modelDto = new SharingPlatlformDto();
		if(Validate.isString(id)){
			SharingPlatlform domain = sharingPlatlformRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {
		
	SharingPlatlform sharing = sharingPlatlformRepo.findById(id);
	if(sharing !=null){
		if(sharing.getCreatedBy().equals(currentUser.getLoginName())){
			sharingPlatlformRepo.delete(sharing);
			logger.info(String.format("删除发现信息，发布主题:%s", sharing.getTheme()));
		}
		else if(currentUser.getLoginName().equals("admin")){
			sharingPlatlformRepo.delete(sharing);
			logger.info(String.format("删除发现信息，发布主题:%s", sharing.getTheme()));
		}else{
			  throw new IllegalArgumentException("您不是发布人员，不能对其进行删除操作！");
		}
	}
		
	}

	@Override
	public void deletes(String[] ids) {
		for(String id :ids){
			this.delete(id);
		}
		logger.info("批量删除发布信息");
		
	}

	@Override
	public SharingPlatlformDto postSharingAritle(String id) {
		HqlBuilder hqlBuilder = HqlBuilder.create();
		hqlBuilder.append("with res as(select cs_sharing_Platform.*,row_number() over(order by isPublish desc,publishDate desc)t from  cs_sharing_Platform)");
		hqlBuilder.append(" select res.* from res where t=(");
		hqlBuilder.append(" select res.t-1 from res where res.sharId=:id)");
		hqlBuilder.setParam("id", id);
		List<SharingPlatlform> sharinglist = sharingPlatlformRepo.findBySql(hqlBuilder);
		SharingPlatlformDto sharingDto = new SharingPlatlformDto();
		if(sharinglist.size() > 0 ){
			BeanCopierUtils.copyProperties(sharinglist.get(0), sharingDto);
		}
		return sharingDto;
	}

	@Override
	public SharingPlatlformDto nextSharingArticle(String id) {
		HqlBuilder hqlBuilder = HqlBuilder.create();
		hqlBuilder.append("with res as(select cs_sharing_Platform.*,row_number() over(order by isPublish desc,publishDate desc)t from  cs_sharing_Platform)");
		hqlBuilder.append(" select res.* from res where t=(");
		hqlBuilder.append(" select res.t+1 from res where res.sharId=:id)");
		hqlBuilder.setParam("id", id);
		List<SharingPlatlform> sharinglist = sharingPlatlformRepo.findBySql(hqlBuilder);
		SharingPlatlformDto sharingDto = new SharingPlatlformDto();
		if(sharinglist.size() > 0 ){
			BeanCopierUtils.copyProperties(sharinglist.get(0), sharingDto);
		}
		return sharingDto;
	}


	@Override
	@Transactional
	public void updatePublishStatus(SharingPlatlformDto sharingDto) {
		SharingPlatlform domain = sharingPlatlformRepo.findById(sharingDto.getSharId());
		if(domain !=null){
		
			if(domain.getIsPublish().equals("0") && domain.getPublishUsername().equals(currentUser.getLoginName())){
				domain.setIsPublish(EnumState.YES.getValue());
				BeanCopierUtils.copyPropertiesIgnoreNull(sharingDto, domain);
				Date now = new Date();
				domain.setCreatedBy(currentUser.getLoginName());
				domain.setModifiedBy(currentUser.getLoginName());
				domain.setPublishDate(now);
				domain.setPublishUsername(currentUser.getLoginName());
				domain.setCreatedDate(now);
				domain.setModifiedDate(now);
			}else if(domain.getIsPublish().equals("9") && domain.getPublishUsername().equals(currentUser.getLoginName())){
				domain.setIsPublish(EnumState.NO.getValue());
				BeanCopierUtils.copyPropertiesIgnoreNull(sharingDto, domain);
				Date now = new Date();
				domain.setCreatedBy(currentUser.getLoginName());
				domain.setModifiedBy(currentUser.getLoginName());
				domain.setPublishDate(now);
				domain.setPublishUsername(currentUser.getLoginName());
				domain.setCreatedDate(now);
				domain.setModifiedDate(now);
			}else{
				 throw new IllegalArgumentException("您不是发布人员，不能对该记录操作！");
			}	
		}
		
		sharingPlatlformRepo.save(domain);
		
	}

	@Override
	public UserDto findUser(String loginName) {
		User user =   userRepo.findUserByName(loginName);
		UserDto userDto = new UserDto();
		if(user.getLoginName().equals(currentUser.getLoginName())){
			BeanCopierUtils.copyProperties(user, userDto);
			return userDto;
		}else{
			throw new IllegalArgumentException("只能选择登录本人！");
		}
		
	}

	@Override
	public Map<String, Object> getOrg() {
		Map<String, Object> map = new HashMap<String, Object>();
		HqlBuilder hql =  HqlBuilder.create();
		hql.append(" select o from " +Org.class.getSimpleName()+" o where o."+Org_.name.getName()+ "=:name");
		hql.setParam("name", "综合部");
		List<Org> o =   orgRepo.findByHql(hql);
		String id ="";
		for(Org org : o){
			id=org.getId();
		}
		 List<User> userList = userRepo.findUserByOrgId(id);
	        List<UserDto> userDtolist = new ArrayList<>();
	        if (userList != null && userList.size() > 0) {
	            userList.forEach(x -> {
	                UserDto userDto = new UserDto();
	                BeanCopierUtils.copyProperties(x, userDto);
	                userDtolist.add(userDto);
	            });
	        }
	        if(userDtolist!=null){
				map.put("userDtolist", userDtolist);
			}
	        HqlBuilder hqlpgyib =  HqlBuilder.create();
	        hqlpgyib.append(" select o from " +Org.class.getSimpleName()+" o where o."+Org_.name.getName()+ "=:name");
	        hqlpgyib.setParam("name", "评估一部");
			List<Org> orgpgyb =   orgRepo.findByHql(hqlpgyib);
			String pgybId ="";
			for(Org pgyb : orgpgyb){
				pgybId=pgyb.getId();
			}         
			 List<User> userpgyb = userRepo.findUserByOrgId(pgybId);
		        List<UserDto> userDtopgyb = new ArrayList<>();
		        if (userpgyb != null && userpgyb.size() > 0) {
		        	userpgyb.forEach(x -> {
		                UserDto userpgybD= new UserDto();
		                BeanCopierUtils.copyProperties(x, userpgybD);
		                userDtopgyb.add(userpgybD);
		            });
		        }
		
		if(userDtopgyb!=null){
			map.put("userDtopgyb", userDtopgyb);
		}
		return map;
	}
	
}