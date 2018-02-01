package cs.controller.project;

import cs.ahelper.MudoleAnnotation;
import cs.model.PageModelDto;
import cs.model.project.SignDto;
import cs.model.project.SignWorkDto;
import cs.repository.odata.ODataObj;
import cs.service.project.SignService;
import cs.service.project.SignWorkService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * Created by shenning on 2018/1/20.
 */
@Controller
@RequestMapping(name = "项目专家抽取", path = "signwork")
@MudoleAnnotation(name = "系统管理员" , value="permission#manager")
public class SignWorkController {
    String ctrlName = "signwork";
    @Autowired
    private SignWorkService signWorkService;

    @RequiresPermissions("signwork#html/list#get")
    @RequestMapping(name = "项目专家抽取", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }


    @RequiresAuthentication
    @RequestMapping(name = "根据ID获取项目信息", path = "fingSignWorkById", method = RequestMethod.POST)
    @ResponseBody
    public SignWorkDto fingSignWorkById(@RequestParam(required = true) String signId) {
        SignWorkDto result = signWorkService.fingSignWorkById(signId);
        return result;
    }

    @RequiresAuthentication
    @RequestMapping(name = "获取收文数据", path = "fingSignWorkList", method = RequestMethod.POST)
    @ResponseBody
    public List<SignWorkDto> fingSignWorkList(HttpServletRequest request) throws ParseException{
        ODataObj odataObj = new ODataObj(request);
        return signWorkService.fingSignWorkList(odataObj);
    }
}
