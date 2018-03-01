package cs.mobile.controller;


import cs.common.utils.SessionUtil;

import cs.controller.flow.FlowController;
import cs.domain.flow.RuProcessTask;

import cs.mobile.service.WorkDynamicService;
import cs.model.PageModelDto;

import cs.model.project.SignDto;

import cs.service.project.SignService;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;


/**
 * Description: 移动端 控制层
 * author: hjm
 * Date: 2018-2-27 15:33:41
 */
@Controller
@RequestMapping(name = "工作状态", path = "api/workDynamic")
public class workDynamicController {
    private static Logger log = Logger.getLogger(FlowController.class);
    @Autowired
    private WorkDynamicService workDynamicService;
    @Autowired
    private SignService signService;



    @RequestMapping(name = "个人待办信息数量查询", path = "myCountInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> tasksCount(String id) throws ParseException {
       /* UserDto userDto=userService.findById(id,false);*/
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("DO_SIGN_COUNT", workDynamicService.findMyDoingSignTask(id));
        resultMap.put("DO_TASK_COUNT", workDynamicService.findMyDoingTask(id));
        //如果是项目签收人员，还要加上项目签收数量
        if(SessionUtil.checkPermissions("sign#html/list#get")){
            resultMap.put("GET_SIGN_COUNT", signService.findSignCount());
        }
        if(SessionUtil.checkPermissions("sign#html/reserveList#get")){
            resultMap.put("GET_RESERVESIGN_COUNT", signService.findReservesSignCount());
        }
        return resultMap;
    }

    @RequestMapping(name = "待办项目", path = "gtasks", method = RequestMethod.GET)
    public @ResponseBody
    PageModelDto<RuProcessTask> tasks(String id) throws ParseException {
        PageModelDto<RuProcessTask> pageModelDto = workDynamicService.queryRunProcessTasks(id,true);
        return pageModelDto;
    }

    @RequestMapping(name = "初始化流程处理页面", path = "initFlowPageData", method = RequestMethod.GET)
    @Transactional
    @ResponseBody
    public SignDto initFlowPageData(@RequestParam(required = true) String signid) {
        return signService.findById(signid, true);
    }



}
