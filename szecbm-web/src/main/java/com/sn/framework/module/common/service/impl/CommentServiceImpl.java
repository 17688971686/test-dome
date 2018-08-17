package com.sn.framework.module.common.service.impl;

import com.sn.framework.core.service.impl.SServiceImpl;
import com.sn.framework.module.common.domain.Comment;
import com.sn.framework.module.common.helper.CommentHelper;
import com.sn.framework.module.common.model.CommentDto;
import com.sn.framework.module.common.repo.ICommentRepo;
import com.sn.framework.module.common.service.ICommentService;
import org.apache.shiro.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 评审意见处理类
 * @Author: ldm
 * @Date: 2017/9/12 16:40
 */
@Service
public class CommentServiceImpl extends SServiceImpl<ICommentRepo, Comment, CommentDto> implements ICommentService {

    @Autowired
    private ICommentRepo commentRepo;

    @Override
    public List<CommentDto> findByBusinessKey(String businessKey,boolean filterNew) {
        Assert.notNull(businessKey, "获取意见参数不正确！");
        List<Comment> cmList = commentRepo.findByBusinessKey(businessKey);
        if(filterNew){
            return CommentHelper.create(cmList).filterNew().listTransToDto();
        }
        return CommentHelper.create(cmList).listTransToDto();
    }
}