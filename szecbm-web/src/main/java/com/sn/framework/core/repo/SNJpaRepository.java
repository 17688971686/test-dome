package com.sn.framework.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Description: 自定义 JPA接口
 * @Author: tzg
 * @Date: 2017/6/13 11:41
 */
@NoRepositoryBean
public interface SNJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {




}
