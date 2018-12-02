package cs.controller.topic;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.Validate;
import cs.model.PageModelDto;
import cs.model.topic.ContractDto;
import cs.model.topic.TopicInfoDto;
import cs.model.topic.TopicMaintainDto;
import cs.repository.odata.ODataObj;
import cs.service.rtx.RTXService;
import cs.service.topic.TopicInfoService;
import cs.service.topic.TopicMaintainService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static cs.common.constants.Constant.ERROR_MSG;


/**
 * Description: 课题研究 控制层
 * author: sjy
 * Date: 2017-9-4 15:04:55
 */
@Controller
@RequestMapping(name = "课题研究", path = "topicInfo")
@MudoleAnnotation(name = "课题管理",value = "permission#topicInfo")
public class TopicInfoController {

    String ctrlName = "topicInfo";
    @Autowired
    private TopicInfoService topicInfoService;

    @Autowired
    private TopicMaintainService topicMaintainService;

    @Autowired
    private RTXService rtxService;

    @RequiresAuthentication
    //@RequiresPermissions("topicInfo#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<TopicInfoDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<TopicInfoDto> topicInfoDtos = topicInfoService.get(odataObj);
        return topicInfoDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("topicInfo##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody TopicInfoDto record) {
        return topicInfoService.save(record);
    }

    @RequiresAuthentication
    @RequestMapping(name = "更新记录", path = "updateTopic", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg updateTopic(@RequestBody TopicInfoDto record) {
        ResultMsg resultMsg =topicInfoService.updateTopic(record);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    // @RequiresPermissions("topicInfo#startFlow#post")
    @RequestMapping(name = "发起流程", path = "startFlow", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg startFlow(@RequestBody TopicInfoDto record) {
        ResultMsg resultMsg = topicInfoService.startFlow(record);
        if(resultMsg.isFlag()){
            String procInstName = Validate.isObject(resultMsg.getReObj())?resultMsg.getReObj().toString():"";
            rtxService.dealPoolRTXMsg(resultMsg.getIdCode(),resultMsg,procInstName, Constant.MsgType.task_type.name());
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "主键查询", path = "findById", method = RequestMethod.POST)
    public @ResponseBody
    TopicInfoDto findById(@RequestParam(required = true) String id) {
        return topicInfoService.findById(id);
    }

    @RequiresAuthentication
    @RequestMapping(name = "查询详情", path = "findDetailById", method = RequestMethod.POST)
    public @ResponseBody TopicInfoDto findDetailById(@RequestParam(required = true) String id) {
        return topicInfoService.findDetailById(id);
    }

    @RequiresAuthentication
    @RequestMapping(name = "查询课题维护详情", path = "findTopicDetail", method = RequestMethod.POST)
    @ResponseBody
    public List<TopicMaintainDto> findTopicDetail(@RequestParam String userId) {
        List<TopicMaintainDto> resultList = topicMaintainService.findTopicAll(userId);
        if(!Validate.isList(resultList)){
            resultList = new ArrayList<>();
        }
        return resultList;
    }


    @RequiresAuthentication
    @RequestMapping(name = "合同录入", path = "saveContractDetailList", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveContractDetailList(@RequestBody ContractDto[] contractDtoArrary){
        ResultMsg resultMsg = topicInfoService.saveContractDetailList(contractDtoArrary);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "课题维护", path = "saveTopicDetailList", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveTopicDetailList(@RequestBody TopicMaintainDto[] topicMaintainDtoArray){
        ResultMsg resultMsg = topicInfoService.saveTopicDetailList(topicMaintainDtoArray);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "删除记录", path = "contractDel", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg deleteContract(@RequestParam String ids) {
        ResultMsg resultMsg = topicInfoService.deleteContract(ids);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "删除课题维护记录", path = "topicMaintainDel", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg deleteTopicMaintain(@RequestParam String ids) {
        ResultMsg resultMsg = topicInfoService.deleteTopicMaintain(ids);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    //@RequiresPermissions("topicInfo##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        topicInfoService.delete(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("topicInfo##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody TopicInfoDto record) {
        topicInfoService.update(record);
    }

    // begin#html
    @RequiresAuthentication
    //@RequiresPermissions("topicInfo#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }

    @RequiresPermissions("topicInfo#html/add#get")
    @RequestMapping(name = "创建课题", path = "html/add", method = RequestMethod.GET)
    public String add() {
        return ctrlName + "/add";
    }

    @RequiresAuthentication
    //@RequiresPermissions("topicInfo#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/edit";
    }

    @RequiresPermissions("topicInfo#html/myList#get")
    @RequestMapping(name = "我的课题", path = "html/myList", method = RequestMethod.GET)
    public String myList() {
        return ctrlName + "/myList";
    }

    @RequiresPermissions("topicInfo#html/queryTopic#get")
    @RequestMapping(name = "课题查询", path = "html/queryTopic", method = RequestMethod.GET)
    public String queryTopic() {
        return ctrlName + "/queryTopic";
    }

    @RequiresAuthentication
    @RequestMapping(name = "课题详情页", path = "html/topicDetail", method = RequestMethod.GET)
    public String topicDetail() {
        return ctrlName + "/topicDetail";
    }

    // end#html

}