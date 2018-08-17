package com.sn.framework.module.sys.service.impl;

import com.sn.framework.common.IdWorker;
import com.sn.framework.common.ObjectUtils;
import com.sn.framework.common.StringUtil;
import com.sn.framework.core.common.SessionUtil;
import com.sn.framework.core.service.impl.SServiceImpl;
import com.sn.framework.module.sys.domain.Resource;
import com.sn.framework.module.sys.domain.Role;
import com.sn.framework.module.sys.model.RoleDto;
import com.sn.framework.module.sys.repo.IRoleRepo;
import com.sn.framework.module.sys.service.IRoleService;
import com.sn.framework.odata.impl.jpa.OdataJPA;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.sn.framework.core.Constants.CACHE_KEY_SYS_RESOURCE;

/**
 * Description: 角色信息  业务处理实现类
 * @Author tzg
 * @Date 2017/8/25 10:50
 */
@Service
public class RoleServiceImpl extends SServiceImpl<IRoleRepo, Role, RoleDto> implements IRoleService {


    @Override
    public List<RoleDto> findAll(String... ignoreProperties) {
        if (ObjectUtils.isEmpty(ignoreProperties)) {
            ignoreProperties = new String[]{"resources"};
        }
        return super.findAll(ignoreProperties);
    }

    @Override
    public List<RoleDto> findByOdata(OdataJPA odata, String... ignoreProperties) {
        if (ObjectUtils.isEmpty(ignoreProperties)) {
            ignoreProperties = new String[]{"resources"};
        }
        return super.findByOdata(odata, ignoreProperties);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(String roleId) {
        Role role = baseRepo.getById(roleId);
        Assert.notNull(role, "未找到操作的角色");
        role.setRoleState("1");
        role.setModifiedBy(SessionUtil.getUsername());
        role.setModifiedDate(new Date());
        baseRepo.save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(String roleId) {
        Role role = baseRepo.getById(roleId);
        Assert.notNull(role, "未找到操作的角色");
        role.setRoleState("0");
        role.setModifiedBy(SessionUtil.getUsername());
        role.setModifiedDate(new Date());
        baseRepo.save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = CACHE_KEY_SYS_RESOURCE, allEntries = true)
    public void authorization(String roleId, Set<Resource> resources) {
        Role role = baseRepo.getById(roleId);
        Assert.notNull(role, "未找到操作的角色");
        role.getResources().clear();
        role.setResources(resources);
        role.setModifiedBy(SessionUtil.getUsername());
        role.setModifiedDate(new Date());
        baseRepo.save(role);
    }

    @Override
    public List<RoleDto> findUserRoles(String userId) {
        return baseRepo.findUserRoles(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(RoleDto dto, String... ignoreProperties) {
        if (ObjectUtils.isEmpty(ignoreProperties)) {
            ignoreProperties = new String[]{"createdDate", "modifiedDate", "resources"};
        }
        Role role = baseRepo.findByRoleName(dto.getRoleName());
        if(null == role) {
            Role entity = dto.convert(domainCls, ignoreProperties);
            if (StringUtil.isBlank(entity.getRoleId())) {
                entity.setRoleId(IdWorker.get32UUID());
            }

            String createdBy = SessionUtil.getUsername();
            if (StringUtil.isBlank(createdBy)) {
                createdBy = "root";
            }
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(createdBy);
            entity.setRoleDataType(1);
            baseRepo.save(entity);
        }else {
            throw new IllegalArgumentException(String.format("角色名称：【%s】 已经存在，请重新输入！",dto.getRoleName()));
        }


    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleDto dto, String... ignoreProperties) {
        Role entity = baseRepo.getById(dto.getRoleId());
        Assert.notNull(entity, "数据不存在");
        if (ObjectUtils.isEmpty(ignoreProperties)) {
            ignoreProperties = new String[]{"createdDate", "modifiedDate", "createdBy", "resources", "roleDataType"};
        }
        BeanUtils.copyProperties(dto, entity, ignoreProperties);
        entity.setModifiedBy(SessionUtil.getUsername());
        entity.setModifiedDate(new Date());
        baseRepo.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = CACHE_KEY_SYS_RESOURCE, allEntries = true)
    public void deleteById(String roleId) {
        Role entity = baseRepo.getById(roleId);
        Assert.notNull(entity, "数据不存在");
        if (entity.getRoleDataType() == 0) {
            throw new IllegalArgumentException("系统基础数据，无法删除");
        }
        baseRepo.delete(entity);
    }

}