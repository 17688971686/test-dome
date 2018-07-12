package com.sn.framework.module.common.service;

import com.sn.framework.core.service.ISService;
import com.sn.framework.module.common.model.CommentDto;

import java.util.List;

/**
 * Description: 数据字典  业务操作接口
 * @Author: qbl
 * @Date: 2017/9/12 16:36
 */
public interface ICommentService extends ISService<CommentDto> {

    /**
     * 根据业务ID查询评审意见信息
     * @param businessKey   意见ID
     * @param filterNew     是否只要最新意见
     * @return
     */
    List<CommentDto> findByBusinessKey(String businessKey,boolean filterNew);
}
