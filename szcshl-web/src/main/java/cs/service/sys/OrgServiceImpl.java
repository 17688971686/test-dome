package cs.service.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.domain.sys.Company;
import cs.domain.sys.Org;
import cs.domain.sys.Org_;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.sys.CompanyDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.CompanyRepo;
import cs.repository.repositoryImpl.sys.OrgRepo;
import cs.repository.repositoryImpl.sys.UserRepo;

@Service
public class OrgServiceImpl implements OrgService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private OrgRepo orgRepo;
	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private CompanyRepo companyRepo;
	
	@Override
	public PageModelDto<OrgDto> get(ODataObj odataObj) {
		List<Org> orgList = orgRepo.findByOdata(odataObj);
		List<OrgDto> orgDtoList = new ArrayList<>();
		orgList.forEach(o ->{
			OrgDto orgDto = new OrgDto();	
			BeanCopierUtils.copyProperties(o, orgDto);
			List<User> userList = o.getUsers();
			if(userList != null && userList.size() > 0){
				List<UserDto> userDtoList = new ArrayList<UserDto>(userList.size());
				userList.forEach(u ->{
					UserDto userDto = new UserDto();
					BeanCopierUtils.copyProperties(u, userDto);
					userDtoList.add(userDto);
				});
				orgDto.setUserDtos(userDtoList);
			}
			orgDtoList.add(orgDto);
		});
		
		PageModelDto<OrgDto> pageModelDto = new PageModelDto<>();
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(orgDtoList);

		logger.info("查询部门数据");		
		return pageModelDto;
	}

	@Override
	@Transactional
	public void createOrg(OrgDto orgDto) {
		// 判断部门是否已经存在
		Criteria criteria = orgRepo.getExecutableCriteria();
		criteria.add(Restrictions.eq(Org_.orgIdentity.getName(), orgDto.getOrgIdentity()));
		List<Org> orgs = criteria.list();
		// 部门不存在
		if (orgs.isEmpty()) {
			Org org = new Org();						
			BeanCopierUtils.copyProperties(orgDto, org);
			Date now = new Date();
			org.setCreatedBy(currentUser.getLoginName());
			org.setCreatedDate(now);
			org.setModifiedBy(currentUser.getLoginName());
			org.setModifiedDate(now);
			
			orgRepo.save(org);
			logger.info(String.format("创建部门,部门名:%s", orgDto.getOrgIdentity()));
		} else{
			throw new IllegalArgumentException(String.format("部门标识：%s 已经存在,请重新输入！", orgDto.getOrgIdentity()));
		}
	}

	@Override
	@Transactional
	public void updateOrg(OrgDto orgDto) {
		Org org = orgRepo.findById(orgDto.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(orgDto, org);						
		org.setModifiedBy(currentUser.getLoginName());
		org.setModifiedDate(new Date());
		orgRepo.save(org);
		logger.info(String.format("更新部门,部门名:%s", orgDto.getName()));
	}

	@Override
	@Transactional
	public void deleteOrg(String id) {
		Org org = orgRepo.findById(id);
		if (org != null) {
			orgRepo.delete(org);
			logger.info(String.format("删除部门,部门identity:%s", org.getOrgIdentity()));
		}
	}

	@Override
	@Transactional
	public void deleteOrgs(String[] ids) {
		for (String id : ids) {
			this.deleteOrg(id);
		}
		logger.info("批量删除部门");
	}

	@Override
	@Transactional
	public PageModelDto<UserDto> getOrgUsers(String id) {
		PageModelDto<UserDto> pageModelDto = new PageModelDto<>();
		List<UserDto> userDtos = new ArrayList<>();
		Org org = orgRepo.findById(id);
		if (org != null) {
//			org.getUsers().forEach(x -> {
//				UserDto userDto = new UserDto();
//				userDto.setId(x.getId());
//				userDto.setRemark(x.getRemark());
//				userDto.setLoginName(x.getLoginName());
//				userDto.setDisplayName(x.getDisplayName());
//				userDtos.add(userDto);

//			});
			pageModelDto.setValue(userDtos);
			pageModelDto.setCount(userDtos.size());
			logger.info(String.format("查找部门用户，部门%s", org.getOrgIdentity()));
		}

		return pageModelDto;
	}

	@Override
	@Transactional
	public PageModelDto<UserDto> getUsersNotInOrg(String id, ODataObj oDataObj) {
		PageModelDto<UserDto> pageModelDto = new PageModelDto<>();
		List<UserDto> userDtos = new ArrayList<>();
		Org org = orgRepo.findById(id);
		List<String> userIds = new ArrayList<>();
		if (org != null) {

//			org.getUsers().forEach(x -> {
//				userIds.add(x.getId());
//			});

			List<User> users = userRepo.getUsersNotIn(userIds, oDataObj);
			users.forEach(x -> {
				UserDto userDto = new UserDto();
				userDto.setId(x.getId());
				userDto.setRemark(x.getRemark());
				userDto.setLoginName(x.getLoginName());
				userDto.setDisplayName(x.getDisplayName());
				userDtos.add(userDto);

			});
			pageModelDto.setValue(userDtos);
			pageModelDto.setCount(userDtos.size());

			logger.info(String.format("查找非部门用户,部门%s", org.getOrgIdentity()));
		}

		return pageModelDto;
	}

	@Override
	@Transactional
	public void addUserToOrg(String userId, String orgId) {
		Org org = orgRepo.findById(orgId);
		if (org != null) {
			User user = userRepo.findById(userId);
			if (user != null) {
				//user.getOrgs().add(org);
			}
			userRepo.save(user);
			logger.info(String.format("添加用户到部门,部门%s,用户:%s", org.getOrgIdentity(), user.getLoginName()
					));
			
		}

	}

	@Override
	@Transactional
	public void removeOrgUser(String userId, String orgId) {
		Org org = orgRepo.findById(orgId);
		if (org != null) {
			User user = userRepo.findById(userId);
			if (user != null) {
			//	user.getOrgs().remove(org);
			}
			userRepo.save(user);
			logger.info(String.format("从部门移除用户,部门%s,用户:%s", org.getOrgIdentity(), user.getLoginName()));
		}

	}

	@Override
	@Transactional
	public void removeOrgUsers(String[] userIds, String orgId) {
		Org org = orgRepo.findById(orgId);
		if (org != null) {
			for (String id : userIds) {
				this.removeOrgUser(id,orgId);
			}
			logger.info(String.format("批量删除部门用户,部门%s", org.getOrgIdentity()));
		}
	}

	@Override
	@Transactional
	public List<UserDto> getUser(ODataObj odataObj) {
		List<User> listUser = userRepo.findByOdata(odataObj);
		List<UserDto> userDtoList = new ArrayList<>();
		for (User item : listUser) {
			UserDto userDto = new UserDto();
			userDto.setId(item.getId());
			userDto.setLoginName(item.getLoginName());
			userDto.setDisplayName(item.getDisplayName());
			userDto.setPassword(item.getPassword());
			userDto.setRemark(item.getRemark());
			userDto.setCreatedDate(item.getCreatedDate());

			userDtoList.add(userDto);
		}
	
		return userDtoList;
	}

	@Override
	@Transactional
	public List<CompanyDto> getCompany(ODataObj odataObj) {
	
		List<Company> com	=companyRepo.findByOdata(odataObj);
	
		List<CompanyDto> comDtoList= new ArrayList<>();
		for(Company item : com){
		
			CompanyDto comDto =new CompanyDto();
			
			comDto.setId(item.getId());
			comDto.setCoAddress(item.getCoAddress());
			comDto.setCoDept(item.getCoDept());
			comDto.setCoDeptName(item.getCoDeptName());
			comDto.setCoFax(item.getCoFax());
			comDto.setCoName(item.getCoName());
			comDto.setCoPC(item.getCoPC());
			comDto.setCoPhone(item.getCoPhone());
			comDto.setCoSite(item.getCoSite());
			comDto.setCoSynopsis(item.getCoSynopsis());
	
			comDtoList.add(comDto);
		
		}
		return comDtoList;
	}

	@Override
	public OrgDto findById(String id) {
		Org org = orgRepo.findById(id);
		OrgDto orgDto = new OrgDto();	
		BeanCopierUtils.copyProperties(org, orgDto);
		List<User> userList = org.getUsers();
		if(userList != null && userList.size() > 0){
			List<UserDto> userDtoList = new ArrayList<UserDto>(userList.size());
			userList.forEach(u ->{
				UserDto userDto = new UserDto();
				BeanCopierUtils.copyProperties(u, userDto);
				userDtoList.add(userDto);
			});
			orgDto.setUserDtos(userDtoList);
		}
		return orgDto;
	}

}
