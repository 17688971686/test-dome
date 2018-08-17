package com.sn.framework.module.sys.service.impl;

import com.sn.framework.common.IdWorker;
import com.sn.framework.common.ObjectUtils;
import com.sn.framework.common.StringUtil;
import com.sn.framework.core.common.SessionUtil;
import com.sn.framework.core.service.impl.SServiceImpl;
import com.sn.framework.module.sys.domain.Organ;
import com.sn.framework.module.sys.domain.Resource;
import com.sn.framework.module.sys.model.OrganDto;
import com.sn.framework.module.sys.repo.IOrganRepo;
import com.sn.framework.module.sys.service.IOrganService;
import com.sn.framework.module.sys.service.ISysService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.sn.framework.core.Constants.CACHE_KEY_SYS_RESOURCE;

/**
 * 机构信息  业务实现类
 * @author qbl
 * @date 2017/9/7
 */
@Service
public class OrganServiceImpl extends SServiceImpl<IOrganRepo, Organ, OrganDto> implements IOrganService {

    @Autowired
    private ISysService sysService;

    @Override
    protected List<OrganDto> convertDtos(List<Organ> domainList, String... ignoreProperties) {
        if (ObjectUtils.isEmpty(ignoreProperties)) {
            ignoreProperties = new String[]{"users", "resources"};
        }
        final String[] _ignoreProperties = ignoreProperties;
        List<OrganDto> dtoList = new ArrayList<>(domainList.size());
        domainList.forEach(x -> dtoList.add(x.convert(modelCls, _ignoreProperties)));
        return dtoList;
    }

    @Override
    public OrganDto getById(String organId, String... ignoreProperties) {
        if (ObjectUtils.isEmpty(ignoreProperties)) {
            ignoreProperties = new String[]{"users"};
        }
        return super.getById(organId, ignoreProperties);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void initOrganData() throws Exception {
        sysService.initData("Organ.data.xml");
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void create(OrganDto dto, String... ignoreProperties) {
        if (ObjectUtils.isEmpty(ignoreProperties)) {
            ignoreProperties = new String[]{"createdDate", "modifiedDate", "organRel", "users", "resources"};
        }
        Organ entity = dto.convert(domainCls, ignoreProperties);
        if (StringUtil.isBlank(entity.getOrganId())) {
            entity.setOrganId(IdWorker.get32UUID());
        }
        String relationChain = "|";
        if (StringUtil.isNotBlank(entity.getParentId())) {
            Organ parent = baseRepo.getById(entity.getParentId());
            if (null != parent && StringUtil.isNotBlank(parent.getOrganRel())) {
                relationChain = parent.getOrganRel();
            }
        }
        relationChain += entity.getOrganId() + "|";
        entity.setOrganRel(relationChain);
        entity.setOrganDataType(1);

        String createdBy = SessionUtil.getUsername();
        if (StringUtil.isBlank(createdBy)) {
            createdBy = "root";
        }
        entity.setCreatedBy(createdBy);
        entity.setModifiedBy(createdBy);
        baseRepo.save(entity);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void update(OrganDto dto, String... ignoreProperties) {
        Organ entity = baseRepo.getById(dto.getOrganId());
        if (null != entity) {
//            if (entity.getOrganDataType() == 0) {
//                throw new IllegalArgumentException("系统基础数据无法更改");
//            }
            if (ObjectUtils.isEmpty(ignoreProperties)) {
                ignoreProperties = new String[]{"createdDate", "modifiedDate", "createdBy", "organRel", "users", "resources", "organDataType"};
            }
            BeanUtils.copyProperties(dto, entity, ignoreProperties);
            if (StringUtil.isBlank(entity.getOrganRel())) {
                String relationChain = "|";
                if (StringUtil.isNotBlank(entity.getParentId())) {
                    Organ parent = baseRepo.getById(entity.getParentId());
                    if (null != parent && StringUtil.isNotBlank(parent.getOrganRel())) {
                        relationChain = parent.getOrganRel();
                    }
                }
                relationChain += entity.getOrganId() + "|";
                entity.setOrganRel(relationChain);
            }

            entity.setModifiedBy(SessionUtil.getUsername());
            entity.setModifiedDate(new Date());
            baseRepo.save(entity);
        } else {
            throw new IllegalArgumentException("数据不存在");
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    @CacheEvict(cacheNames = CACHE_KEY_SYS_RESOURCE, allEntries = true)
    public void deleteById(String organId) {
        Assert.hasText(organId, "缺少参数");
        Organ entity = baseRepo.getById(organId);
        Assert.notNull(entity, "未找到操作的机构");
        if (entity.getOrganDataType() == 0) {
            throw new IllegalArgumentException("系统基础数据无法删除");
        }
        baseRepo.delete(entity);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    @CacheEvict(cacheNames = CACHE_KEY_SYS_RESOURCE, allEntries = true)
    public void authorization(String organId, Set<Resource> resources) {
        Assert.hasText(organId, "缺少参数");
        Organ entity = baseRepo.getById(organId);
        Assert.notNull(entity, "未找到操作的机构");
        entity.getResources().clear();
        entity.setResources(resources);
        entity.setModifiedBy(SessionUtil.getUsername());
        entity.setModifiedDate(new Date());
        baseRepo.save(entity);
    }

    @Override
    public List<OrganDto> findOrganList() {
        List<Organ> organs = baseRepo.findOrganList();
        return convertDtos(organs);
    }

}
