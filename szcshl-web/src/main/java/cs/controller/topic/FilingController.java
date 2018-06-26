package cs.controller.topic;

import cs.ahelper.IgnoreAnnotation;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.model.PageModelDto;
import cs.model.topic.FilingDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.UserService;
import cs.service.topic.FilingService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 课题归档 控制层
 * author: ldm
 * Date: 2017-9-4 15:40:48
 */
@Controller
@RequestMapping(name = "课题归档", path = "filing")
@IgnoreAnnotation
public class FilingController {

    String ctrlName = "filing";
    @Autowired
    private FilingService filingService;
    @Autowired
    private UserService userService;

    @RequiresAuthentication
    //@RequiresPermissions("filing#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<FilingDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<FilingDto> filingDtos = filingService.get(odataObj);
        return filingDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("filing##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody FilingDto record) {

        return filingService.save(record);
    }

    @RequiresAuthentication
    @RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
    public @ResponseBody FilingDto findById(@RequestParam(required = true)String id){
        return filingService.findById(id);
    }

    @RequiresAuthentication
    @RequestMapping(name = "根据课题ID查询", path = "initByTopicId",method=RequestMethod.POST)
    public @ResponseBody
    Map<String,Object>  initByTopicId(@RequestParam(required = true)String topicId){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("file_record", filingService.initByTopicId(topicId));
        resultMap.put("topic_user_List", userService.findUserByRoleName(Constant.EnumFlowNodeGroupName.FILER.getValue()));
        return resultMap;
    }

    @RequiresAuthentication
    //@RequiresPermissions("filing##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        filingService.delete(id);
    }

    @RequiresAuthentication
    //RequiresPermissions("filing##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody FilingDto record) {
        filingService.update(record);
    }

    // begin#html
    @RequiresAuthentication
    //@RequiresPermissions("filing#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list";
    }

    @RequiresAuthentication
    //@RequiresPermissions("filing#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return "topicInfo/filingEdit";
    }
    // end#html

}