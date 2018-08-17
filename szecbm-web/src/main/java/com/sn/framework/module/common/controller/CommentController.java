package com.sn.framework.module.common.controller;

import com.sn.framework.core.syslog.OperatorType;
import com.sn.framework.core.syslog.SysLog;
import com.sn.framework.module.common.model.CommentDto;
import com.sn.framework.module.common.service.ICommentService;
import com.sn.framework.module.sys.model.DictDto;
import com.sn.framework.module.sys.service.IDictService;
import com.sn.framework.module.sys.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by ldm on 2018/7/12 0012.
 */
@Controller
@RequestMapping(name = "评审意见", path = "comment")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @RequiresPermissions("sys:dict:post")
    @RequestMapping(name = "创建意见", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @SysLog(businessType = "评审意见",operatorType = OperatorType.ADD,serviceclass = ICommentService.class,idName = "commentId")
    public void post(@RequestBody CommentDto commentDto) {
        commentService.create(commentDto);
    }
}
