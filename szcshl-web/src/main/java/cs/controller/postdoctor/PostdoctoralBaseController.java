package cs.controller.postdoctor;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.SessionUtil;
import cs.model.PageModelDto;
import cs.model.postdoctor.PostdoctoralBaseDto;
import cs.repository.odata.ODataObj;
import cs.service.postdoctor.PostdoctoralBaseService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * Created by zsl on 2018-10-23.
 */
@Controller
@RequestMapping(name = "博士后基地", path = "postdoctoralBase")
@MudoleAnnotation(name = "博士后基地管理",value = "permission#postdoctoralManager")
public class PostdoctoralBaseController {

    @Autowired
    private PostdoctoralBaseService postdoctoralBaseService;

    String ctrlName = "postdoctoralBase";

    @RequiresAuthentication
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<PostdoctoralBaseDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<PostdoctoralBaseDto> PostdoctoralBaseDtoList = postdoctoralBaseService.get(odataObj);
        return PostdoctoralBaseDtoList;
    }

    @RequiresAuthentication
    @RequestMapping(name = "创建记录", path = "createPostdoctoralBase", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody PostdoctoralBaseDto record) {
        return postdoctoralBaseService.save(record);
    }

    @RequiresAuthentication
    @RequestMapping(name = "更新记录", path = "updatePostdoctoralBase", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg updatePostdoctoralBase(@RequestBody PostdoctoralBaseDto record) {
        return postdoctoralBaseService.save(record);
    }

    @RequiresAuthentication
    @RequestMapping(name = "主键查询", path = "findById", method = RequestMethod.POST)
    public @ResponseBody
    PostdoctoralBaseDto findById(@RequestParam(required = true) String id) {
        return postdoctoralBaseService.findDetailById(id);
    }

    @RequiresAuthentication
    @RequestMapping(name = "删除记录", path = "deletePostdoctoralBase", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg delete(@RequestParam String id) {
       return postdoctoralBaseService.delete(id);
    }

    @RequestMapping(name = "博士后基地信息录入" , path = "html/postdoctoralBaseAdd" , method = RequestMethod.GET)
    public String edit(){
        return ctrlName + "/postdoctoralBaseAdd";
    }

    @RequiresPermissions("postdoctoralBase#html/postdoctoralBaseList#get")
    @RequestMapping(name = "博士后基地查询" , path = "html/postdoctoralBaseList" , method = RequestMethod.GET)
    public String postdoctoralBaseList(){
        SessionUtil.getSession().setAttribute("POSTDOCTORAL_ADMIN",SessionUtil.hashRole(Constant.EnumPostdoctoralName.POSTDOCTORAL_ADMIN.getValue())==true
                ?"1":"0");
        return ctrlName + "/postdoctoralBaseList";
    }

    @RequestMapping(name = "博士后基地信息" , path = "html/postdoctoralBaseDetail" , method = RequestMethod.GET)
    public String partyList(){
        return ctrlName + "/postdoctoralBaseDetail";
    }

}
