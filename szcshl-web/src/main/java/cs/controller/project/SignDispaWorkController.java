package cs.controller.project;

import cs.common.ResultMsg;
import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.model.project.SignDto;
import cs.repository.odata.ODataObj;
import cs.service.project.SignDispaWorkService;
import cs.service.project.SignService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * 项目详情信息视图控制器
 * Created by ldm on 2017/9/17 0017.
 */
@Controller
@RequestMapping(name = "收文", path = "signView")
public class SignDispaWorkController {

    @Autowired
    private SignDispaWorkService signDispaWorkService;


    @RequiresPermissions("signView#getSignList#post")
    @RequestMapping(name = "项目查询统计", path = "getSignList", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<SignDispaWork> getSignList(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SignDispaWork> pageModelDto = signDispaWorkService.getCommQurySign(odataObj);
        return pageModelDto;
    }

    @RequiresPermissions("signView#unMergeWPSign#post")
    @RequestMapping(name = "待选合并评审项目", path = "unMergeWPSign", method = RequestMethod.POST)
    @ResponseBody
    public List<SignDispaWork> unMergeWPSign(@RequestParam(required = true) String signId) {
        return signDispaWorkService.unMergeWPSign(signId);
    }

    @RequiresPermissions("signView#getMergeSignBySignId#post")
    @RequestMapping(name = "获取已选合并评审项目", path = "getMergeSignBySignId", method = RequestMethod.POST)
    public @ResponseBody
    List<SignDispaWork> getMergeSignBySignId(@RequestParam(required = true) String signId){
        return signDispaWorkService.getMergeWPSignBySignId(signId);
    }

    @RequiresPermissions("signView#unMergeDISSign#post")
    @RequestMapping(name = "待选合并发文项目", path = "unMergeDISSign", method = RequestMethod.POST)
    @ResponseBody
    public List<SignDispaWork> unMergeDISSign(@RequestParam(required = true) String signId) {
        return signDispaWorkService.unMergeDISSign(signId);
    }

    @RequiresPermissions("signView#getMergeDISSign#post")
    @RequestMapping(name = "获取已选合并发文项目", path = "getMergeDISSign", method = RequestMethod.POST)
    @ResponseBody
    public List<SignDispaWork> getMergeDISSign(@RequestParam(required = true) String signId){
        return signDispaWorkService.getMergeDISSignBySignId(signId);
    }

    @RequiresPermissions("signView#mergeSign#post")
    @RequestMapping(name = "保存合并评审发文", path = "mergeSign", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg mergeSign(@RequestParam(required = true) String signId,
        @RequestParam(required = true) String mergeIds, @RequestParam(required = true) String mergeType) {
        return signDispaWorkService.mergeSign(signId,mergeIds,mergeType);
    }

    @RequiresPermissions("signView#cancelMergeSign#post")
    @RequestMapping(name = "解除合并评审发文", path = "cancelMergeSign", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg cancelMergeSign(@RequestParam(required = true) String signId, String cancelIds,
                                     @RequestParam(required = true) String mergeType){
        return signDispaWorkService.cancelMergeSign(signId,cancelIds,mergeType);
    }


    @RequiresPermissions("signView#html/signList#get")
    @RequestMapping(name = "项目查询统计", path = "html/signList", method = RequestMethod.GET)
    public String getsignList() {
        return "sign/signList";
    }
}
