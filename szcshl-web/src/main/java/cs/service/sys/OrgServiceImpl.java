package cs.service.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.sys.*;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.utils.BeanCopierUtils;
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
    private CompanyRepo companyRepo;
    @Autowired
    private OrgDeptRepo orgDeptRepo;

    /**
     * 查询部门数据
     * @param odataObj
     * @return
     */
    @Override
    public PageModelDto<OrgDto> get(ODataObj odataObj) {
        List<Org> orgList = orgRepo.findByOdata(odataObj);
        List<OrgDto> orgDtoList = new ArrayList<>();
        orgList.forEach(o -> {
            OrgDto orgDto = new OrgDto();
            BeanCopierUtils.copyProperties(o, orgDto);
            orgDtoList.add(orgDto);
        });

        PageModelDto<OrgDto> pageModelDto = new PageModelDto<>();
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(orgDtoList);
        return pageModelDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg createOrg(OrgDto orgDto) {
        // 判断部门是否已经存在
        Criteria criteria = orgRepo.getExecutableCriteria();
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq(Org_.orgIdentity.getName(), orgDto.getOrgIdentity()));

        int orgCount = Integer.parseInt(criteria.uniqueResult().toString());
        // 部门不存在
        if (orgCount < 1) {
            Org org = new Org();
            BeanCopierUtils.copyProperties(orgDto, org);
            if (Validate.isString(org.getOrgSLeader())) {
                User user = userRepo.findById(User_.id.getName(), org.getOrgSLeader());
                String newMngOrgType = Constant.OrgType.getValue(org.getName());
                //查看其是否还有分管部门
                if (orgRepo.checkMngOrg(user.getId()) > 0 && !user.getMngOrgType().equals(newMngOrgType)) {
                    return ResultMsg.error("操作失败，分管领导不能同时分管概算部又分管评估部！");
                }
                user.setMngOrgType(newMngOrgType);
                userRepo.save(user);
            }

            Date now = new Date();
            org.setId(UUID.randomUUID().toString());
            org.setCreatedBy(SessionUtil.getLoginName());
            org.setCreatedDate(now);
            org.setModifiedBy(SessionUtil.getLoginName());
            org.setModifiedDate(now);

            orgRepo.save(org);
            orgDeptRepo.fleshOrgDeptCache();
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), org.getId(), "操作成功！", null);
            //logger.info(String.format("创建部门,部门名:%s", orgDto.getOrgIdentity()));
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), String.format("部门标识：%s 已经存在,请重新输入！", orgDto.getOrgIdentity()));
            //throw new IllegalArgumentException(String.format("部门标识：%s 已经存在,请重新输入！", orgDto.getOrgIdentity()));
        }
    }

    @Override
    @Transactional
    public ResultMsg updateOrg(OrgDto orgDto) {
        Org org = orgRepo.findById(Org_.id.getName(), orgDto.getId());
        String oldOrgSLeader = org.getOrgSLeader();
        if (Validate.isString(orgDto.getOrgSLeader()) && !oldOrgSLeader.equals(orgDto.getOrgSLeader())) {
            User user = userRepo.findById(User_.id.getName(), orgDto.getOrgSLeader());
            String newMngOrgType = Constant.OrgType.getValue(orgDto.getName());
            //查看其是否还有分管部门
            if (orgRepo.checkMngOrg(user.getId()) > 0 && !newMngOrgType.equals(user.getMngOrgType())) {
                return ResultMsg.error( "操作失败，分管领导不能同时分管概算部又分管评估部！");
            } else {
                user.setMngOrgType(newMngOrgType);
                userRepo.save(user);
            }
        }
        BeanCopierUtils.copyPropertiesIgnoreNull(orgDto, org);
        if (Validate.isString(oldOrgSLeader)) {
            //查看其是否还有分管部门
            if (orgRepo.checkMngOrg(oldOrgSLeader) == 0) {
                User oldUser = userRepo.findById(User_.id.getName(), oldOrgSLeader);
                if (oldUser != null) {
                    oldUser.setMngOrgType(null);
                    userRepo.save(oldUser);
                }
            }
        }
        org.setModifiedBy(SessionUtil.getLoginName());
        org.setModifiedDate(new Date());
        orgRepo.save(org);
        orgDeptRepo.fleshOrgDeptCache();
        return ResultMsg.ok( "操作成功！");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrg(String id) {
        orgRepo.deleteById(Org_.id.getName(), id);
        orgDeptRepo.fleshOrgDeptCache();
        logger.info(String.format("删除部门,部门identity:%s", id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageModelDto<UserDto> getOrgUsers(String id, ODataObj odataObj) {
        PageModelDto<UserDto> pageModelDto = new PageModelDto<>();
        List<UserDto> userDtos = new ArrayList<>();
        Org org = orgRepo.findById(id);
        if (org != null) {
            String ss = "";
            if (odataObj.getFilter() != null) {
                ss = odataObj.getFilter().get(0).getValue().toString();
            }
            List<User> userList = org.getUsers();
            for (int i = 0,l=userList.size(); i < l; i++) {
                User user = userList.get(i);
                if (odataObj.getFilter() != null) {
                    if (user.getLoginName().indexOf(ss) != -1 || user.getDisplayName().indexOf(ss) != -1) {
                        UserDto userDto = new UserDto();
                        BeanCopierUtils.copyProperties(user,userDto);
                        userDtos.add(userDto);
                    }
                } else {
                    UserDto userDto = new UserDto();
                    BeanCopierUtils.copyProperties(user,userDto);
                    userDtos.add(userDto);
                }
            }
            pageModelDto.setValue(userDtos);
            pageModelDto.setCount(userDtos.size());
            logger.info(String.format("查找部门用户，部门%s", org.getOrgIdentity()));
        }
        return pageModelDto;
    }

    /**
     * 查询在职、没有分配部门的非主任员工
     * @param id
     * @param oDataObj
     * @return
     */
    @Override
    public PageModelDto<UserDto> getUsersNotInOrg(String id, ODataObj oDataObj) {
        PageModelDto<UserDto> pageModelDto = new PageModelDto<>();
        Org org = orgRepo.findById(id);
        List<String> userIds = new ArrayList<>();
        if (org != null) {
            List<UserDto> userDtos = new ArrayList<>();
            List<User> userList = userRepo.getUsersNotIn(userIds, oDataObj);
            if(Validate.isList(userList)){
                userList.forEach(x -> {
                    UserDto userDto = new UserDto();
                    userDto.setId(x.getId());
                    userDto.setRemark(x.getRemark());
                    userDto.setLoginName(x.getLoginName());
                    userDto.setDisplayName(x.getDisplayName());
                    userDtos.add(userDto);
                });
            }
            pageModelDto.setValue(userDtos);
            pageModelDto.setCount(oDataObj.getCount());
            logger.info(String.format("查找非部门用户,部门%s", org.getName()));
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
                user.setOrg(org);
            }
            userRepo.save(user);
            logger.info(String.format("添加用户到部门,部门%s,用户:%s", org.getOrgIdentity(), user.getLoginName()));
        }
    }

    @Override
    @Transactional
    public void removeOrgUser(String userId, String orgId) {
        Org org = orgRepo.findById(orgId);
        if (org != null) {
            User user = userRepo.findById(userId);
            if (user != null) {
                user.setOrg(null);
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
                this.removeOrgUser(id, orgId);
            }
            logger.info(String.format("批量删除部门用户,部门%s", org.getOrgIdentity()));
        }
    }

    @Override
    @Transactional
    public List<CompanyDto> getCompany(ODataObj odataObj) {
        List<Company> com = companyRepo.findByOdata(odataObj);
        List<CompanyDto> comDtoList = new ArrayList<>();

        for (Company item : com) {
            CompanyDto comDto = new CompanyDto();

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
		/*List<User> userList = org.getUsers();
		if(userList != null && userList.size() > 0){
			List<UserDto> userDtoList = new ArrayList<UserDto>(userList.size());
			userList.forEach(u ->{
				UserDto userDto = new UserDto();
				BeanCopierUtils.copyProperties(u, userDto);
				userDtoList.add(userDto);
			});
			List<User> mainUser=userRepo.findUserByRoleName("主任");
			if(mainUser != null && mainUser.size() > 0){
				mainUser.forEach(mainu ->{
					orgDto.setOrgMLeader(mainu.getId());;
					orgDto.setOrgMLeaderName(mainu.getLoginName());
				});
			}
			List<User> secondUser=userRepo.findUserByRoleName("副主任");
			if(secondUser != null && secondUser.size() > 0){
				secondUser.forEach(secondu ->{
					UserDto userDto = new UserDto();
					BeanCopierUtils.copyProperties(secondu, userDto);
					userDtoList.add(userDto);
				});
			}
			orgDto.setUserDtos(userDtoList);
		}*/
        return orgDto;
    }

    @Override
    public List<OrgDto> listAll() {
        Criteria criteria = orgRepo.getExecutableCriteria();
        criteria.addOrder(Order.asc(Org_.sort.getName()));
        List<Org> orgList = criteria.list();
        if (orgList != null) {
            List<OrgDto> orgDtoList = new ArrayList<OrgDto>(orgList.size());
            orgList.forEach(o -> {
                OrgDto orgDto = new OrgDto();
                BeanCopierUtils.copyProperties(o, orgDto);
                orgDto.setCharge((SessionUtil.getUserInfo().getId().equals(o.getOrgSLeader())) ? true : false);
                orgDtoList.add(orgDto);
            });
            return orgDtoList;
        }
        return null;
    }

    /**
     * 验证是否是部门用户
     * @param orgId
     * @param userIdStr
     * @return
     */
    @Override
    public boolean checkIsOrgUer(String orgId, String userIdStr) {
        return orgRepo.checkIsOrgUer(orgId,userIdStr);
    }

}
