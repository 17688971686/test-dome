package cs.controller.sys;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.sys.PolicyDto;
import cs.service.sys.PolicyService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * Description: 政策指标库
 * Author: mcl
 * Date: 2018/8/14 10:27
 */
@Controller
@RequestMapping(name="政策指标库",path="policy")
@MudoleAnnotation(name = "公告资料管理",value = "permission#annountment")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    @RequiresAuthentication
    @RequestMapping(name="初始化-文件夹",path="initFileFolder",method = RequestMethod.POST)
    @ResponseBody
    public List<PolicyDto> initFileFolder() throws ParseException {

        return policyService.initFileFolder();
    }

    @RequiresAuthentication
    @RequestMapping(name = "创建政策指标库", path = "", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody PolicyDto record) {
        return policyService.save(record);
    }

    @RequestMapping(name = "通过ID查询文件" , path = "findFileById" , method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<PolicyDto> findFileById(String fileId ,  String skip, String size ){
     /*   ODataObj oDataObj = null;
        PageModelDto<PolicyDto> pageModelDto = new PageModelDto<>();
        try {
            oDataObj = new ODataObj(request);
            pageModelDto = policyService.findFileById(fileId , skip , size);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        return policyService.findFileById(fileId , skip , size);

    }

    @RequestMapping(name = "删除政策指标库" , path = "deletePolicy" , method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
     public void deletePolicy(@RequestParam  String idStr){
        policyService.deletePolicy(idStr);
     }
}