package cs.controller.postdoctor;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.SessionUtil;
import cs.model.PageModelDto;
import cs.model.postdoctor.PostdoctoralStaffDto;
import cs.repository.odata.ODataObj;
import cs.service.postdoctor.PostdoctoralStaffService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by zsl on 2018-10-23.
 */
@Controller
@RequestMapping(name = "博士后人员", path = "postdoctoralStaff")
@MudoleAnnotation(name = "博士后人员管理",value = "permission#postdoctoralManager")
public class PostdoctoralStaffController {

    @Autowired
    private PostdoctoralStaffService postdoctoralStaffService;


    String ctrlName = "postdoctoralBase";

    @RequiresAuthentication
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<PostdoctoralStaffDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        //odataObj.buildFilterToCriteria().
        PageModelDto<PostdoctoralStaffDto> PostdoctoralBaseDtoList = postdoctoralStaffService.get(odataObj);
        return PostdoctoralBaseDtoList;
    }

    @RequiresAuthentication
    @RequestMapping(name = "创建记录", path = "createPostdoctoralStaff", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody PostdoctoralStaffDto record) {
        return postdoctoralStaffService.save(record);
    }

    @RequiresAuthentication
    @RequestMapping(name = "更新记录", path = "updatePostdoctoralStaff", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg updatePostdoctoralBase(@RequestBody PostdoctoralStaffDto record) {
        return postdoctoralStaffService.save(record);
    }

    @RequiresAuthentication
    @RequestMapping(name = "主键查询", path = "findById", method = RequestMethod.POST)
    public @ResponseBody
    PostdoctoralStaffDto findById(@RequestParam(required = true) String id) {
        return postdoctoralStaffService.findDetailById(id);
    }

    @RequiresAuthentication
    @RequestMapping(name = "博士后人员审核", path = "approvePostdoctoralStaff", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg approvePostdoctoralStaff(@RequestParam String id ,@RequestParam String status) {
        return postdoctoralStaffService.approvePostdoctoralStaff(id,status);
    }

    @RequiresAuthentication
    @RequestMapping(name = "博士后人员回退", path = "backPostdoctoralStaff", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg backPostdoctoralStaff(@RequestParam String id ,@RequestParam String status) {
        return postdoctoralStaffService.backPostdoctoralStaff(id,status);
    }

    @RequiresAuthentication
    @RequestMapping(name = "删除记录", path = "deletePostdoctoralStaff", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg delete(@RequestParam String id) {
      return   postdoctoralStaffService.delete(id);
    }


    @RequestMapping(name = "博士后进站人员信息录入" , path = "html/postdoctoralStaffAdd" , method = RequestMethod.GET)
    public String edit(){
        return ctrlName + "/postdoctoralStaffAdd";
    }

    @RequestMapping(name = "博士后出站人员信息录入" , path = "html/postdoctoralPopStaffAdd.html" , method = RequestMethod.GET)
    public String postdoctoralPopStaffAdd(){
        return ctrlName + "/postdoctoralPopStaffAdd";
    }

    @RequestMapping(name = "博士后在站人员列表" , path = "html/postdoctoralStaffList" , method = RequestMethod.GET)
    public String postdoctoralStaffList(){
        SessionUtil.getSession().setAttribute("POSTDOCTORAL_ADMIN",SessionUtil.hashRole(Constant.EnumPostdoctoralName.POSTDOCTORAL_ADMIN.getValue())==true
                ?"1":"0");
        SessionUtil.getSession().setAttribute("POSTDOCTORAL_PERSON",SessionUtil.hashRole(Constant.EnumPostdoctoralName.POSTDOCTORAL_PERSON.getValue())==true
                ?"1":"0");
        return ctrlName + "/postdoctoralStaffList";
    }


    @RequestMapping(name = "博士后在站人员信息" , path = "html/postdoctoralStaffDetail" , method = RequestMethod.GET)
    public String postdoctoralStaffDetail(){
        return ctrlName + "/postdoctoralStaffDetail";
    }


    @RequestMapping(name = "博士后出站人员列表" , path = "html/postdoctoralPopStaffList" , method = RequestMethod.GET)
    public String postdoctoralPopStaffList(){
        SessionUtil.getSession().setAttribute("POSTDOCTORAL_ADMIN",SessionUtil.hashRole("博士后基地管理员")==true
                ?"1":"0");
        SessionUtil.getSession().setAttribute("POSTDOCTORAL_PERSON",SessionUtil.hashRole(Constant.EnumPostdoctoralName.POSTDOCTORAL_PERSON.getValue())==true
                ?"1":"0");
        return ctrlName + "/postdoctoralPopStaffList";
    }

    @RequestMapping(name = "博士后出站申请信息" , path = "html/postdoctoralPopStaff" , method = RequestMethod.GET)
    public String postdoctoralPopStaff(){
        return ctrlName + "/postdoctoralPopStaff";
    }


    @RequestMapping(name = "博士后出站人员信息" , path = "html/postdoctoralPopStaffDetail" , method = RequestMethod.GET)
    public String postdoctoralPopStaffDetail(){
        return ctrlName + "/postdoctoralPopStaffDetail";
    }
}
