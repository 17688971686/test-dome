package com.sn.framework.core.service.impl;

import com.sn.framework.core.DomainBase;
import com.sn.framework.common.IdWorker;
import com.sn.framework.common.ObjectUtils;
import com.sn.framework.common.StringUtil;
import com.sn.framework.core.common.SessionUtil;
import com.sn.framework.core.repo.ICommonRepo;
import com.sn.framework.core.repo.IRepository;
import com.sn.framework.core.service.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.GeneratedValue;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * 基础服务层抽象类
 *
 * @author tzg
 * @date 2017/9/1
 */
public abstract class ServiceImpl<R extends IRepository<T, ID>, T extends DomainBase, M extends T, ID extends Serializable> extends QueryServiceImpl<R, T, M, ID> implements IService<M, ID> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected ICommonRepo commonRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(M dto, String... ignoreProperties) {
        if (ObjectUtils.isEmpty(ignoreProperties)) {
            ignoreProperties = new String[]{"createdDate", "modifiedDate"};
        }
        T entity = dto.convert(domainCls, ignoreProperties);
        Field idField = ObjectUtils.getIdField(domainCls);
        if (!idField.isAnnotationPresent(GeneratedValue.class)) {
            idField.setAccessible(true);
            try {
                if (ObjectUtils.isEmpty(idField.get(entity))) {
                    idField.set(entity, idField.getType() == Long.class ? IdWorker.getId() : IdWorker.getUUID());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        String createdBy = SessionUtil.getUsername();
        if (StringUtil.isBlank(createdBy)) {
            createdBy = "root";
        }
        entity.setCreatedBy(createdBy);
        entity.setModifiedBy(createdBy);
        baseRepo.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(M dto, String... ignoreProperties) {
        ID id = (ID) ObjectUtils.getIdFieldValue(dto.convert(domainCls));
        T entity = baseRepo.getById(id);
        Assert.notNull(entity, "数据不存在");
        if (ObjectUtils.isEmpty(ignoreProperties)) {
            ignoreProperties = new String[]{"createdDate", "modifiedDate", "createdBy"};
        }
        BeanUtils.copyProperties(dto, entity, ignoreProperties);
        entity.setModifiedBy(SessionUtil.getUsername());
        entity.setModifiedDate(new Date());
        baseRepo.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(ID id) {
        T entity = baseRepo.getById(id);
        Assert.notNull(entity, "数据不存在");
        baseRepo.delete(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(ID[] ids) {
        for (ID id : ids) {
            deleteById(id);
        }
    }

}
