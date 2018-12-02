package cs.controller.project;

import cs.ahelper.IgnoreAnnotation;
import cs.common.ResultMsg;
import cs.common.utils.Validate;
import cs.domain.project.DispatchDoc;
import cs.model.project.DispatchDocDto;
import cs.model.project.SignDto;
import cs.service.project.DispatchDocService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(name = "发文", path = "dispatch")
@IgnoreAnnotation
public class DispatchDocController {

    private String ctrlName = "dispatch";

    @Autowired
    private DispatchDocService dispatchDocService;

    @RequiresAuthentication
    //@RequiresPermissions("dispatch##post")
    @RequestMapping(name = "保存发文", path = "", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody DispatchDocDto dispatchDocDto) throws Exception {
       return dispatchDocService.save(dispatchDocDto);
    }

    @RequiresAuthentication
    //@RequiresPermissions("dispatch#createFileNum#post")
    @RequestMapping(name = "生成文件字号", path = "createFileNum", method = RequestMethod.POST)
    public @ResponseBody ResultMsg createFileNum(@RequestParam String signId, @RequestParam String dispatchId) throws Exception {
        ResultMsg returnMsg = dispatchDocService.fileNum(signId,dispatchId);
        return returnMsg;
    }

    @RequiresAuthentication
    // @RequiresPermissions("dispatch#initData#get")
    @RequestMapping(name = "初始化发文页面", path = "initData", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> initDispatch(@RequestParam String signid){
        Map<String, Object> map = dispatchDocService.initDispatchData(signid);
        if(!Validate.isMap(map)){
            map = new HashMap<>();
        }
        return map;
    }

    @RequiresAuthentication
    //@RequiresPermissions("dispatch#html/initDispatchBySignId#get")
    @RequestMapping(name = "查询流程发文信息", path = "html/initDispatchBySignId", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public DispatchDocDto initDispatchBySignId(@RequestParam String signId){
        return dispatchDocService.initDispatchBySignId(signId);
    }

    @RequiresAuthentication
    @RequestMapping(name = "查看合并发文次项目信息", path = "findMergeDisInfo", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public List<DispatchDocDto> findMergeDisInfo(@RequestParam String mainSignId){
        return dispatchDocService.findMergeDisInfo(mainSignId);
    }


    @RequiresAuthentication
    @RequestMapping(name = "根据ID查询对象", path = "findById", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public DispatchDocDto findById(@RequestParam String dictId) {
        return dispatchDocService.findById(dictId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("dispatch#createDispatchTemplate#post")
    @RequestMapping(name="生成评审报告模板" , path="createDispatchTemplate" , method = RequestMethod.POST )
    @ResponseBody
    public ResultMsg createDispatchTemplate(@RequestParam  String signId){

      return   dispatchDocService.createDisPatchTemplate(signId );
    }

    @RequiresAuthentication
    //@RequiresPermissions("dispatch#html/edit#get")
    @RequestMapping(name = "发文编辑", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/edit";
    }
}
