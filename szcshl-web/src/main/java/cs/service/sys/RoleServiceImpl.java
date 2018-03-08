package cs.service.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.domain.sys.Resource;
import cs.domain.sys.Role;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.sys.ResourceDto;
import cs.model.sys.RoleDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.RoleRepo;

@Service
public class RoleServiceImpl implements RoleService {
    private static Logger logger = Logger.getLogger(RoleServiceImpl.class);
    @Autowired
    private RoleRepo roleRepository;
    @Autowired
    private IdentityService identityService;

    /*
     * (non-Javadoc)
     *
     * @see cs.service.RoleService#get(cs.common.ODataObj)
     */
    @Override
    @Transactional
    public PageModelDto<RoleDto> get(ODataObj odataObj) {

        List<Role> listRole = roleRepository.findByOdata(odataObj);
        List<RoleDto> roleDtoList = new ArrayList<RoleDto>();
        for (Role item : listRole) {
            RoleDto roleDto = new RoleDto();
            roleDto.setId(item.getId());
            roleDto.setRoleName(item.getRoleName());
            roleDto.setCreatedDate(item.getCreatedDate());
            roleDto.setRemark(item.getRemark());

			/*List<ResourceDto> resourceDtoList = new ArrayList<>();
            for (Resource resource : item.getResources()) {
				ResourceDto resourceDto = new ResourceDto();
				resourceDto.setMethod(resource.getMethod());
				resourceDto.setName(resource.getName());
				resourceDto.setPath(resource.getPath());
				resourceDtoList.add(resourceDto);
			}
			roleDto.setResources(resourceDtoList);*/
            roleDtoList.add(roleDto);
        }
        PageModelDto<RoleDto> pageModelDto = new PageModelDto<>();
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(roleDtoList);

        logger.info("查询角色数据");
        return pageModelDto;
    }

    @Override
    @Transactional
    public void createRole(RoleDto roleDto) {
        boolean isRoleExist = roleRepository.isRoleExist(roleDto.getRoleName());
        if (!isRoleExist) {//角色不存在
            Role role = new Role();
            role.setRemark(roleDto.getRemark());
            role.setRoleName(roleDto.getRoleName());
            role.setId(UUID.randomUUID().toString());
            role.setCreatedBy(SessionUtil.getLoginName());
            role.setModifiedBy(SessionUtil.getLoginName());

            // resource
            for (ResourceDto resourceDto : roleDto.getResources()) {
                Resource resource = new Resource();
                resource.setMethod(resourceDto.getMethod());
                resource.setPath(resourceDto.getPath());
                resource.setName(resourceDto.getName());
                role.getResources().add(resource);

            }

            roleRepository.save(role);
            //this.createActivitiGroup(role.getRoleName());
            logger.info(String.format("创建角色,角色名:%s", roleDto.getRoleName()));
        } else {
            throw new IllegalArgumentException(String.format("角色名：%s 已经存在,请重新输入！", roleDto.getRoleName()));
        }
    }

    @Override
    @Transactional
    public void updateRole(RoleDto roleDto) {
        Role role = roleRepository.findById(roleDto.getId());
        String oldRoleName = role.getRoleName();
        role.setRemark(roleDto.getRemark());
        role.setModifiedBy(SessionUtil.getLoginName());
        //清除已有resource
        role.getResources().clear();

        // resource
        for (ResourceDto resourceDto : roleDto.getResources()) {
            Resource resource = new Resource();
            resource.setMethod(resourceDto.getMethod());
            resource.setPath(resourceDto.getPath());
            resource.setName(resourceDto.getName());
            role.getResources().add(resource);
        }
        //this.updateActivitiGroup(oldRoleName, roleDto.getRoleName());
        roleRepository.save(role);
        logger.info(String.format("更新角色,角色名:%s", roleDto.getRoleName()));
    }

    //删除角色
    @Override
    @Transactional
    public void deleteRole(String id) {
        Role role = roleRepository.findById(id);
        if (role != null) {
            //this.deleteActivitiGroup(role.getRoleName());
            List<User> users = role.getUsers();
            //把用户里的角色移出才能删除
            for (User user : users) {
                user.getRoles().remove(role);
            }
            if (!role.getRoleName().equals(Constant.SUPER_ROLE)) {
                roleRepository.delete(role);
                logger.info(String.format("删除角色,角色名:%s", role.getRoleName()));
            }
        }
    }

    @Override
    @Transactional
    public void deleteRoles(String[] ids) {
        for (String id : ids) {
            Role role = roleRepository.findById(id);
            //this.deleteActivitiGroup(role.getRoleName());
            this.deleteRole(id);
        }
        logger.info("批量删除角色");
    }


    protected void createActivitiGroup(String roleName) {
        Group group = identityService.newGroup(roleName);
        group.setName(roleName);
        identityService.saveGroup(group);
    }

    protected void updateActivitiGroup(String oldRoleName, String newRoleName) {
        if (identityService.createGroupQuery().groupId(oldRoleName).count() != 0) {
            identityService.deleteGroup(oldRoleName);
            Group group = identityService.newGroup(newRoleName);
            identityService.saveGroup(group);
            List<org.activiti.engine.identity.User> groupUsers = identityService.createUserQuery().memberOfGroup(oldRoleName).list();
            for (org.activiti.engine.identity.User user : groupUsers) {
                identityService.deleteMembership(user.getId(), oldRoleName);
                identityService.createMembership(user.getId(), newRoleName);
            }

        }
    }

    protected void deleteActivitiGroup(String roleName) {
        if (identityService.createGroupQuery().groupId(roleName).count() != 0) {
            List<org.activiti.engine.identity.User> groupUsers = identityService.createUserQuery().memberOfGroup(roleName).list();
            for (org.activiti.engine.identity.User user : groupUsers) {
                identityService.deleteMembership(user.getId(), roleName);
            }
            identityService.deleteGroup(roleName);
        }
    }
		/*public Role findByRoleId(String id) {
			String hql = " from Role where roleId = ?";
			return RoleRepo.;
		}*/

    @Override
    public RoleDto findById(String roleId) {
        Role role = roleRepository.findById(roleId);
        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setRoleName(role.getRoleName());
        roleDto.setCreatedDate(role.getCreatedDate());
        roleDto.setRemark(role.getRemark());

        List<ResourceDto> resourceDtoList = new ArrayList<>();
        for (Resource resource : role.getResources()) {
            ResourceDto resourceDto = new ResourceDto();
            resourceDto.setMethod(resource.getMethod());
            resourceDto.setName(resource.getName());
            resourceDto.setPath(resource.getPath());
            resourceDtoList.add(resourceDto);
        }
        roleDto.setResources(resourceDtoList);
        return roleDto;
    }

    /**
     * 查询所有角色信息
     *
     * @return
     */
    @Override
    public List<RoleDto> findAllRoles() {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from "+Role.class.getSimpleName()+" ");
        List<Role> roleList = roleRepository.findByHql(hqlBuilder);

        List<RoleDto> resultList = new ArrayList<>(roleList == null?0:roleList.size());
        if(roleList != null && roleList.size() > 0){
            roleList.forEach(rl -> {
                RoleDto roleDto = new RoleDto();
                BeanCopierUtils.copyProperties(rl,roleDto);
                resultList.add(roleDto);
            });
        }
        return resultList;
    }
}
