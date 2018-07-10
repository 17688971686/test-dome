package com.sn.framework.core.service.impl;

import com.sn.framework.common.ObjectUtils;
import com.sn.framework.core.SNEntity;
import com.sn.framework.core.repo.IQRepository;
import com.sn.framework.core.service.IQueryService;
import com.sn.framework.core.web.PageModelDto;
import com.sn.framework.odata.impl.jpa.OdataJPA;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 基础业务 查询实现类
 * @Author: tzg
 * @Date: 2017/9/19 11:13
 */
public abstract class QueryServiceImpl<R extends IQRepository<T, ID>, T extends SNEntity, M extends T, ID extends Serializable> implements IQueryService<M, ID> {

    @Autowired
    protected R baseRepo;

    /**
     * 数据映射实体类
     */
    protected Class<T> domainCls;
    /**
     * 数据展示映射实体
     */
    protected Class<M> modelCls;

    public QueryServiceImpl() {
        Type[] types = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        this.domainCls = (Class<T>) types[1];
        this.modelCls = (Class<M>) types[2];
    }

    @Override
    public M getById(ID id, String... ignoreProperties) {
        T domain = baseRepo.getById(id);
        return domain != null ? domain.convert(modelCls, ignoreProperties) : null;
    }

    @Override
    public List<M> findAll(String... ignoreProperties) {
        return convertDtos(baseRepo.findAll(), ignoreProperties);
    }

    /**
     * 转换实体
     * @param domainList
     * @param ignoreProperties
     * @return
     */
    protected List<M> convertDtos(List<T> domainList, String... ignoreProperties) {
        if (ObjectUtils.isEmpty(domainList)) {
            return new ArrayList<>();
        }
        List<M> dtoList = new ArrayList<>(domainList.size());
        domainList.forEach(x -> dtoList.add(x.convert(modelCls, ignoreProperties)));
        return dtoList;
    }

    @Override
    public List<M> findByOdata(OdataJPA odata, String... ignoreProperties) {
        return convertDtos(baseRepo.findByOdata(odata), ignoreProperties);
    }

    @Override
    public PageModelDto<M> findPageByOdata(OdataJPA odata, String... ignoreProperties) {
        List<M> list = findByOdata(odata, ignoreProperties);
        return new PageModelDto<>(list, odata.getCount() == 0 ? list.size() : odata.getCount());
    }

}