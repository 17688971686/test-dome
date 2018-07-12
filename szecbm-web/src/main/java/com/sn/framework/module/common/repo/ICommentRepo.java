package com.sn.framework.module.common.repo;

import com.sn.framework.core.repo.IRepository;
import com.sn.framework.module.common.domain.Comment;

import java.util.List;

/**
 * Created by Administrator on 2018/7/12 0012.
 */
public interface ICommentRepo extends IRepository<Comment,String> {

    List<Comment> findByBusinessKey(String businessKey);
}
