package com.sn.framework.module.sys.service.impl;

import com.sn.framework.common.ObjectUtils;
import com.sn.framework.core.common.SessionUtil;
import com.sn.framework.odata.impl.jpa.OdataJPA;
import com.sn.framework.core.service.impl.SServiceImpl;
import com.sn.framework.module.sys.domain.Organ;
import com.sn.framework.module.sys.domain.Resource;
import com.sn.framework.module.sys.domain.Role;
import com.sn.framework.module.sys.repo.IOrganRepo;
import com.sn.framework.module.sys.repo.IResourceRepo;
import com.sn.framework.module.sys.repo.IRoleRepo;
import com.sn.framework.module.sys.service.IResourceService;
import com.sn.framework.module.sys.service.ISysService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static com.sn.framework.core.Constants.CACHE_KEY_SYS_RESOURCE;
import static com.sn.framework.core.Constants.ROLE_KEY_ADMIN;

/**
 * Description: 系统资源  业务处理实现类
 * @Author: tzg
 * @Date: 2017/9/14 9:58
 */
@Service
public class ResourceServiceImpl extends SServiceImpl<IResourceRepo, Resource, Resource> implements IResourceService {

    @Autowired
    private ISysService sysService;
    @Autowired
    private IRoleRepo roleRepo;
    @Autowired
    private IOrganRepo organRepo;

    @Override
    @Cacheable(value = CACHE_KEY_SYS_RESOURCE)
    public List<Resource> findByOdata(OdataJPA odata, String... ignoreProperties) {
        if (ObjectUtils.isEmpty(ignoreProperties)) {
            ignoreProperties = new String[]{"organs", "roles"};
        }
        return super.findByOdata(odata, ignoreProperties);
    }

    @Override
    public List<Resource> findAll(String... ignoreProperties) {
        if (ObjectUtils.isEmpty(ignoreProperties)) {
            ignoreProperties = new String[]{"organs", "roles"};
        }
        return super.findAll(ignoreProperties);
    }

    @Override
    public Resource getById(String s, String... ignoreProperties) {
        if (ObjectUtils.isEmpty(ignoreProperties)) {
            ignoreProperties = new String[]{"organs", "roles"};
        }
        return super.getById(s, ignoreProperties);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_KEY_SYS_RESOURCE, allEntries = true)
    public void initResourceData() throws Exception {
        sysService.initData("Resource.data.xml");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_KEY_SYS_RESOURCE, allEntries = true)
    public void resetResource() throws Exception {
        List<Role> roles = roleRepo.findAll(), tmpRoles = new ArrayList<>();
        roles.forEach(x -> {
            if (!ROLE_KEY_ADMIN.equals(x.getRoleName()) && ObjectUtils.isNotEmpty(x.getResources())) {
                tmpRoles.add(x);
            }
        });
        List<Organ> organs = organRepo.findAll(), tmpOrgans = new ArrayList<>();
        organs.forEach(x -> {
            if (ObjectUtils.isNotEmpty(x.getResources())) {
                tmpOrgans.add(x);
            }
        });

        initResourceData();

        roleRepo.saveList(tmpRoles);
        organRepo.saveList(tmpOrgans);

        Role role = roleRepo.findByRoleName(ROLE_KEY_ADMIN);
        if (null != role) {
            List<Resource> resources = baseRepo.findAll();
            role.getResources().clear();
            role.setResources(new HashSet<>(resources));
            roleRepo.save(role);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_KEY_SYS_RESOURCE, allEntries = true)
    public void create(Resource dto, String... ignoreProperties) {
        dto.setDataType(1);
        super.create(dto, ignoreProperties);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_KEY_SYS_RESOURCE, allEntries = true)
    public void update(Resource dto, String... ignoreProperties) {
        Resource entity = baseRepo.getById(dto.getResId());
        Assert.notNull(entity, "数据不存在");
        if (ObjectUtils.isEmpty(ignoreProperties)) {
            ignoreProperties = new String[]{"createdDate", "modifiedDate", "createdBy", "organs", "roles", "dataType"};
        }
        BeanUtils.copyProperties(dto, entity, ignoreProperties);
        entity.setModifiedBy(SessionUtil.getUsername());
        entity.setModifiedDate(new Date());
        baseRepo.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_KEY_SYS_RESOURCE, allEntries = true)
    public void deleteById(String id) {
        Resource entity = baseRepo.getById(id);
        Assert.notNull(entity, "数据不存在");
        if (entity.getDataType() == 0) {
            throw new IllegalArgumentException("系统基础数据，无法删除");
        }
        // 删除机构权限关联数据
        entity.getOrgans().forEach(x -> {
            x.getResources().remove(entity);
            organRepo.save(x);
        });
        // 删除角色权限关联数据
        entity.getRoles().forEach(x -> {
            x.getResources().remove(entity);
            roleRepo.save(x);
        });
        baseRepo.delete(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_KEY_SYS_RESOURCE, allEntries = true)
    public void deleteByIds(String[] strings) {
        super.deleteByIds(strings);
    }

}