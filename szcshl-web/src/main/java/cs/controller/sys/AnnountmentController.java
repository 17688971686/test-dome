package cs.controller.sys;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.Validate;
import cs.model.PageModelDto;
import cs.model.sys.AnnountmentDto;
import cs.repository.odata.ODataObj;
import cs.service.rtx.RTXService;
import cs.service.sys.AnnountmentService;
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

@Controller
@RequestMapping(name = "通知公告", path = "annountment")
@MudoleAnnotation(name = "公告资料管理", value = "permission#annountment")
public class AnnountmentController {

    private String ctrlName = "annountment";

    @Autowired
    private AnnountmentService annService;

    @Autowired
    private RTXService rtxService;

    //@RequiresPermissions("annountment#fingByCurUser#post")
    @RequiresAuthentication
    @RequestMapping(name = "获取个人发布的通知公告", path = "fingByCurUser", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<AnnountmentDto> fingByCurUser(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<AnnountmentDto> pageModelDto = annService.findByCurUser(odataObj);
        return pageModelDto;
    }

    @RequiresAuthentication
    @RequestMapping(name = "发起流程", path = "startFlow", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg startFlow(@RequestParam String id) {
        ResultMsg resultMsg = annService.startFlow(id);
        if(Validate.isObject(resultMsg)){
            if(resultMsg.isFlag()){
                String procInstName = Validate.isObject(resultMsg.getReObj())?resultMsg.getReObj().toString():"";
                rtxService.dealPoolRTXMsg(null,resultMsg.getIdCode(),resultMsg,procInstName, Constant.MsgType.task_type.name());

                resultMsg.setIdCode(null);
                resultMsg.setReObj(null);
            }
        }else{
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    //@RequiresPermissions("annountment#findByIssue#post")
    @RequiresAuthentication
    @RequestMapping(name = "获取已发布的通知公告", path = "findByIssue", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<AnnountmentDto> findByReception(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<AnnountmentDto> pageModelDto = annService.findByIssue(odataObj);
        return pageModelDto;
    }


    //@RequiresPermissions("annountment##post")
    @RequiresAuthentication
    @RequestMapping(name = "新增通知公告", path = "", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg create(@RequestBody AnnountmentDto annountmentDto) {
        ResultMsg resultMsg = annService.createAnnountment(annountmentDto);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    //@RequiresPermissions("annountment#initAnOrg#get")
    @RequiresAuthentication
    @RequestMapping(name = "初始化发布单位", path = "initAnOrg", method = RequestMethod.GET)
    @ResponseBody
    public String inintAnOrg() {
        String resultStr = annService.findAnOrg();
        return Validate.isString(resultStr)?resultStr:"";
    }

    //@RequiresPermissions("annountment#findAnnountmentById#post")
    @RequiresAuthentication
    @RequestMapping(name = "通过ID获取通知公告", path = "findAnnountmentById", method = RequestMethod.POST)
    @ResponseBody
    public AnnountmentDto findAnnoungmentById(@RequestParam String anId) {
        AnnountmentDto annountmentDto = annService.findAnnountmentById(anId);
        if(!Validate.isObject(annountmentDto)){
            annountmentDto = new AnnountmentDto();
        }
        return annountmentDto;
    }

    //@RequiresPermissions("annountment##put")
    @RequiresAuthentication
    @RequestMapping(name = "更新通知公告", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody AnnountmentDto annountmentDto) {
        annService.updateAnnountment(annountmentDto);
    }

    //@RequiresPermissions("annountment#updateIssueState#post")
    @RequiresAuthentication
    @RequestMapping(name = "更改发布状态", path = "updateIssueState", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg updateIssueState(@RequestParam String ids, @RequestParam String issueState) {
        return annService.updateIssueState(ids, issueState);
    }

    //@RequiresPermissions("annountment#getHomePageAnnountment#get")
    @RequiresAuthentication
    @RequestMapping(name = "获取主页上的通知公告", path = "getHomePageAnnountment", method = RequestMethod.GET)
    @ResponseBody
    public List<AnnountmentDto> getHomePageAnnountment() {
        List<AnnountmentDto> resultList = annService.getHomePageAnnountment();
        if(!Validate.isList(resultList)){
            resultList = new ArrayList<>();
        }
        return resultList;
    }

    //@RequiresPermissions("annountment#postArticle#get")
    @RequiresAuthentication
    @RequestMapping(name = "获取上一篇", path = "postArticle", method = RequestMethod.GET)
    @ResponseBody
    public AnnountmentDto postpostArticle(@RequestParam String anId) {
        AnnountmentDto annountmentDto = annService.postAritle(anId);
        if(!Validate.isObject(annountmentDto)){
            annountmentDto = new AnnountmentDto();
        }
        return annountmentDto;
    }

    //@RequiresPermissions("annountment#nextArticle#get")
    @RequiresAuthentication
    @RequestMapping(name = "获取下一篇", path = "nextArticle", method = RequestMethod.GET)
    @ResponseBody
    public AnnountmentDto nextArticle(@RequestParam String anId) {
        AnnountmentDto annountmentDto = annService.nextArticle(anId);
        if(!Validate.isObject(annountmentDto)){
            annountmentDto = new AnnountmentDto();
        }
        return annountmentDto;
    }

    //@RequiresPermissions("annountment##delete")
    @RequiresAuthentication
    @RequestMapping(name = "删除通知公告", path = "", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg delete(@RequestParam String anId) {
        return annService.deleteAnnountment(anId);
    }

    //begin  html
    @RequiresPermissions("annountment#html/list#get")
    @RequestMapping(name = "通知公告管理", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }

    //@RequiresPermissions("annountment#html/edit#get")
    @RequiresAuthentication
    @RequestMapping(name = "通知公告编辑", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/edit";
    }

    //@RequiresPermissions("admin#html/detail#get")
    @RequiresAuthentication
    @RequestMapping(name = "通知公告详情页", path = "html/detail")
    public String annountment() {
        return ctrlName + "/detail";
    }

    @RequiresPermissions("annountment#html/yetList#get")
    @RequestMapping(name = "通知公告列表", path = "html/yetList", method = RequestMethod.GET)
    public String annList() {
        return ctrlName + "/yetList";
    }
}
