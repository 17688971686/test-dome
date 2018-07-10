package com.sn.framework.module.sys.controller;

import com.sn.framework.core.common.SessionUtil;
import com.sn.framework.odata.impl.jpa.OdataJPA;
import com.sn.framework.core.web.PageModelDto;
import com.sn.framework.module.sys.domain.OperatorLog_;
import com.sn.framework.module.sys.model.OperatorLogDto;
import com.sn.framework.module.sys.service.IOperatorLogService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.sn.framework.core.Constants.ROLE_KEY_ADMIN;

/**
 * Description: 系统操作日志   控制器
 * @Author: tzg
 * @Date: 2017/9/11 17:02
 */
@Controller
@RequestMapping(name = "系统操作日志", path = "sys/operatorLog")
public class OperatorLogController {

    @Autowired
    private IOperatorLogService operatorLogService;

    @RequiresAuthentication
    @RequestMapping(name = "获取操作日志数据", path = "", method = RequestMethod.GET)
    @ResponseBody
    public PageModelDto<OperatorLogDto> get(OdataJPA odata) {
        if (!SecurityUtils.getSubject().hasRole(ROLE_KEY_ADMIN)) {
            // 当前用户的操作日志
            odata.addEQFilter(OperatorLog_.createdBy, SessionUtil.getUsername());
        }
        return operatorLogService.findPageByOdata(odata);
    }

    @RequiresAuthentication
    @RequestMapping(name = "删除操作日志", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam Integer days) {
        operatorLogService.deleteByDays(days);
    }

    @RequestMapping(name = "操作日志列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return "sys/operatorLog/operatorLogList";
    }

}