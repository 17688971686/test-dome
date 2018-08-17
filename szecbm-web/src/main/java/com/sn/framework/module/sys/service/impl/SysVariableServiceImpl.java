package com.sn.framework.module.sys.service.impl;

import com.sn.framework.common.ObjectUtils;
import com.sn.framework.core.common.SessionUtil;
import com.sn.framework.core.service.impl.SServiceImpl;
import com.sn.framework.module.sys.domain.SysVariable;
import com.sn.framework.module.sys.model.SysVariableDto;
import com.sn.framework.module.sys.repo.ISysVariableRepo;
import com.sn.framework.module.sys.service.ISysVariableService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;

import static com.sn.framework.core.Constants.CACHE_KEY_SYS_CACHE;


/**
 * Description: 系统变量信息  业务操作实现类
 *
 * @author: tzg
 * @date: 2018/1/23 20:27
 */
@Service
public class SysVariableServiceImpl extends SServiceImpl<ISysVariableRepo, SysVariable, SysVariableDto> implements ISysVariableService {

    @Override
    @Cacheable(value = CACHE_KEY_SYS_CACHE, key = "#varCode")
    public SysVariableDto findByCode(String varCode) {
        SysVariable domain = baseRepo.findByCode(varCode);
        return domain != null ? domain.convert(modelCls) : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(SysVariableDto dto, String... ignoreProperties) {
        dto.setVarCategory("sys");
        dto.setVarDataType(1);
        super.create(dto, ignoreProperties);
    }

    @Override
    @CachePut(value = CACHE_KEY_SYS_CACHE, key = "#dto.varCode")
    @Transactional(rollbackFor = Exception.class)
    public void update(SysVariableDto dto, String... ignoreProperties) {
        SysVariable entity = baseRepo.getById(dto.getVarId());
        Assert.notNull(entity, "数据不存在");
        Assert.isTrue(entity.getVarDataType() == 0, "系统基础数据，无法更改");
        if (ObjectUtils.isEmpty(ignoreProperties)) {
            ignoreProperties = new String[]{"createdDate", "modifiedDate", "createdBy", "varDataType", "varCategory", "varCode"};
        }
        BeanUtils.copyProperties(dto, entity, ignoreProperties);
        entity.setModifiedBy(SessionUtil.getUsername());
        entity.setModifiedDate(new Date());
        baseRepo.save(entity);
    }

    @Override
    @CacheEvict(value = CACHE_KEY_SYS_CACHE, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String varId) {
        SysVariable entity = baseRepo.getById(varId);
        Assert.notNull(entity, "数据不存在");
        Assert.isTrue(entity.getVarDataType() == 0, "系统基础数据，无法删除");
        baseRepo.delete(entity);
    }

}