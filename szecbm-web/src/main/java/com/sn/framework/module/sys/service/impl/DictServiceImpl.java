package com.sn.framework.module.sys.service.impl;

import com.sn.framework.common.IdWorker;
import com.sn.framework.common.ObjectUtils;
import com.sn.framework.common.StringUtil;
import com.sn.framework.core.common.SessionUtil;
import com.sn.framework.core.service.impl.SServiceImpl;
import com.sn.framework.module.sys.domain.Dict;
import com.sn.framework.module.sys.domain.Dict_;
import com.sn.framework.module.sys.model.DictDto;
import com.sn.framework.module.sys.repo.IDictRepo;
import com.sn.framework.module.sys.service.IDictService;
import com.sn.framework.module.sys.service.ISysService;
import com.sn.framework.odata.impl.jpa.OdataJPA;
import org.apache.shiro.util.Assert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 数据字典  业务操作实现类
 * @Author: qbl
 * @Date: 2017/9/12 16:40
 */
@Service
public class DictServiceImpl extends SServiceImpl<IDictRepo, Dict, DictDto> implements IDictService {

    @Autowired
    private ISysService sysService;

    private final static String DICT_ITEMS = "DICT_ITEMS";
    private final static String DICT_NAME_DATA = "DICT_NAME_DATA";

    @Override
    @Cacheable(value = DICT_ITEMS, key = "'all_'")
    public List<DictDto> findAll(String... ignoreProperties) {
        OdataJPA odata = new OdataJPA();
        odata.addNEFilter(Dict_.dictState, 0);
        odata.addOrderby(Dict_.itemOrder.getName(), false);
        return findByOdata(odata, ignoreProperties);
    }

    @Override
//    @Cacheable(value = DICT_ITEMS)
    public List<DictDto> findByOdata(OdataJPA odata, String... ignoreProperties) {
        return super.findByOdata(odata, ignoreProperties);
    }

    @Override
    @Cacheable(value = DICT_ITEMS, key = "'dictId_' + #dictId")
    public List<DictDto> findChildrenById(String dictId) {
        List<Dict> dicts = baseRepo.findChildrenById(dictId);
        return convertDtos(dicts);
    }

//    @Override
//    @Cacheable(value = DICT_NAME_DATA, key = "'parentId_' + #parentId")
//    public List<DictDto> getParentDictItemById(String parentId) {
//        List<Dict> list = baseRepo.findByParentIdList(parentId);
//        return convertDtos(list);
//    }

    @Override
    @Cacheable(value = DICT_NAME_DATA, key = "'dictId_' + #dictId")
    public Map<String, String> getDictNameById(String dictId) {
        Map<String, String> dictNameDataMap = new HashMap<>();

        // 查询数据库
        List<DictDto> dictItems = findChildrenById(dictId);
        if (ObjectUtils.isNotEmpty(dictItems)) {
            for (DictDto dictDto : dictItems) {
                dictNameDataMap.put(dictDto.getDictKey(), dictDto.getDictName());
            }
        }

        return dictNameDataMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {DICT_ITEMS, DICT_NAME_DATA}, allEntries = true)
    public void create(DictDto dto, String... ignoreProperties) {
        checkDictExists(dto);
        if (ObjectUtils.isEmpty(ignoreProperties)) {
            ignoreProperties = new String[]{"createdDate", "modifiedDate", "dictType"};
        }
        Dict entity = dto.convert(domainCls, ignoreProperties);
        if (StringUtil.isBlank(entity.getDictId())) {
            entity.setDictId(IdWorker.get32UUID());
        }
        entity.setDictType(1);
        entity.setCreatedBy(SessionUtil.getUsername());
        entity.setModifiedBy(SessionUtil.getUsername());
        baseRepo.save(entity);
    }

    /**
     * 检查数据字典是否已存在
     * @param dictDto
     */
    private void checkDictExists(DictDto dictDto) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {DICT_ITEMS, DICT_NAME_DATA}, allEntries = true)
    public void update(DictDto dto, String... ignoreProperties) {
        Dict entity = baseRepo.getById(dto.getDictId());
        if (null != entity) {
            if (entity.getDictType() == 0) {
                throw new IllegalArgumentException("系统基础数据无法操作");
            }
            if (ObjectUtils.isEmpty(ignoreProperties)) {
                ignoreProperties = new String[]{"createdDate", "modifiedDate", "createdBy", "dictType"};
            }
            BeanUtils.copyProperties(dto, entity, ignoreProperties);
            entity.setModifiedBy(SessionUtil.getUsername());
            entity.setModifiedDate(new Date());
            baseRepo.save(entity);
        } else {
            throw new IllegalArgumentException("数据不存在");
        }
    }

    @Override
    @CacheEvict(value = {DICT_ITEMS, DICT_NAME_DATA}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String dictId) {
        Dict entity = baseRepo.getById(dictId);
        if (null != entity) {
            if (entity.getDictType() == 0) {
                throw new IllegalArgumentException("系统基础数据无法操作");
            }
            baseRepo.delete(entity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {DICT_ITEMS, DICT_NAME_DATA}, allEntries = true)
    public void deleteByIds(String[] strings) {
        super.deleteByIds(strings);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {DICT_ITEMS, DICT_NAME_DATA}, allEntries = true)
    public void sortTop(String dictId) {
        Dict dict = baseRepo.getById(dictId);
        Assert.notNull(dict, "数据不存在");
//        Integer itemOrder1 = dict.getItemOrder();
        List<Dict> beforeDicts = baseRepo.findBeforeDicts(dict.getParentId(), dict.getItemOrder());
        if (beforeDicts != null && !beforeDicts.isEmpty()) {
            int i = 0;
            dict.setItemOrder(i);
            baseRepo.save(dict);
            for (Dict b : beforeDicts) {
                b.setItemOrder(i + 1);
                baseRepo.save(b);
                i++;
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {DICT_ITEMS, DICT_NAME_DATA}, allEntries = true)
    public void sortUp(String dictId) { // TODO 如果存在两条数据排序号一样的情况排序存在会有问题
        Dict dict = baseRepo.getById(dictId);
        Assert.notNull(dict, "数据不存在");
        Integer itemOrder1 = dict.getItemOrder();
        List<Dict> beforeDicts = baseRepo.findBeforeDicts(dict.getParentId(), itemOrder1);
        if (beforeDicts != null && !beforeDicts.isEmpty()) {
            Dict bd = beforeDicts.get(beforeDicts.size() - 1);
            dict.setItemOrder(bd.getItemOrder());
            baseRepo.save(dict);
            bd.setItemOrder(itemOrder1);
            baseRepo.save(bd);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {DICT_ITEMS, DICT_NAME_DATA}, allEntries = true)
    public void sortDown(String dictId) {
        Dict dict = baseRepo.getById(dictId);
        Assert.notNull(dict, "数据不存在");
        Integer itemOrder1 = dict.getItemOrder();
        List<Dict> afterDicts = baseRepo.findAfterDicts(dict.getParentId(), itemOrder1);
        if (afterDicts != null && !afterDicts.isEmpty()) {
            Dict bd = afterDicts.get(1);
            dict.setItemOrder(bd.getItemOrder());
            baseRepo.save(dict);
            bd.setItemOrder(itemOrder1);
            baseRepo.save(bd);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {DICT_ITEMS, DICT_NAME_DATA}, allEntries = true)
    public void sortBottom(String dictId) {
        Dict dict = baseRepo.getById(dictId);
        Assert.notNull(dict, "数据不存在");
        List<Dict> afterDicts = baseRepo.findAfterDicts(dict.getParentId(), dict.getItemOrder());
        if (afterDicts != null && !afterDicts.isEmpty()) {
            int i = 0;
            for (Dict b : afterDicts) {
                b.setItemOrder(i);
                baseRepo.save(b);
                i++;
            }
            dict.setItemOrder(i);
            baseRepo.save(dict);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {DICT_ITEMS, DICT_NAME_DATA}, allEntries = true)
    public void initDictData() {
        sysService.initData("Dict.data.xml");
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String getDictKeyByDatabaseKey(String organId) {
//        Query query = baseRepo.getQuery("SELECT d.dictKey FROM Dict d where d.databaseKey =:databaseKey");
//        query.setParameter(Dict_.databaseKey.getName(), organId);
//        List<String> dictKeys = query.getResultList();
//        Assert.notNull(dictKeys, "查询的字典集合为空，请联系管理员");
//        return dictKeys.get(0);
//    }

}