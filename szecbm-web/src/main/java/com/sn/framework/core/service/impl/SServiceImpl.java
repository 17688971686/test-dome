package com.sn.framework.core.service.impl;

import com.sn.framework.core.DomainBase;
import com.sn.framework.core.repo.IRepository;
import com.sn.framework.core.service.ISService;

/**
 * Description: 基础业务实现类（针对id为String的实体）
 * @Author: tzg
 * @Date: 2017/9/11 10:24
 */
public class SServiceImpl<R extends IRepository<T, String>, T extends DomainBase, M extends T> extends ServiceImpl<R, T, M, String> implements ISService<M> {

}