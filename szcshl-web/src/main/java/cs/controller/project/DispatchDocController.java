package cs.controller.project;

import cs.common.ResultMsg;
import cs.model.project.DispatchDocDto;
import cs.model.project.SignDto;
import cs.service.project.DispatchDocService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(name = "发文", path = "dispatch")
public class DispatchDocController {

    private String ctrlName = "dispatch";

    @Autowired
    private DispatchDocService dispatchDocService;

    @RequiresPermissions("dispatch##post")
    @RequestMapping(name = "保存发文", path = "", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody DispatchDocDto dispatchDocDto) throws Exception {
       return dispatchDocService.save(dispatchDocDto);
    }

    @RequiresPermissions("dispatch#mergeDispa#get")
    @RequestMapping(name = "生成关联信息", path = "mergeDispa", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void mergeDispa(@RequestParam String signId, @RequestParam String mainBusinessId, @RequestParam String linkSignId) throws Exception {
        dispatchDocService.mergeDispa(signId, mainBusinessId, linkSignId);
    }

    @RequiresPermissions("dispatch#deleteMerge#post")
    @RequestMapping(name = "删除关联信息", path = "deleteMerge", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void deleteMerge(@RequestParam String mainBusinessId,String removeSignIds) {
        dispatchDocService.deleteMergeDispa(mainBusinessId, removeSignIds);
    }

    @RequiresPermissions("dispatch#createFileNum#post")
    @RequestMapping(name = "生成文件字号", path = "createFileNum", method = RequestMethod.POST)
    public @ResponseBody ResultMsg createFileNum(@RequestParam String dispatchId) throws Exception {
        ResultMsg returnMsg = dispatchDocService.fileNum(dispatchId);
        return returnMsg;
    }

    @RequiresPermissions("dispatch#getSignForMerge#get")
    @RequestMapping(name = "获取待选项目", path = "getSignForMerge", method = RequestMethod.POST)
    public @ResponseBody
    List<SignDto> getSignForMerge(@RequestBody SignDto signDto, @RequestParam String dispatchId) throws Exception {
        List<SignDto> sList = dispatchDocService.getSignForMerge(signDto, dispatchId);
        return sList;
    }

    @RequiresPermissions("dispatch#getSignByBusinessId#get")
    @RequestMapping(name = "获取已选合并发文项目", path = "getSignByBusinessId", method = RequestMethod.GET)
    public @ResponseBody
    List<SignDto> getSignByBusinessId(@RequestParam String mainBussnessId) throws Exception {
        return dispatchDocService.getSeleSignByMainBusiId(mainBussnessId);
    }

    @RequiresPermissions("dispatch#initData#get")
    @RequestMapping(name = "初始化发文页面", path = "initData", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> initDispatch(@RequestParam String signid) throws Exception {
        Map<String, Object> map = dispatchDocService.initDispatchData(signid);
        return map;
    }

    @RequiresPermissions("dispatch#html/initDispatchBySignId#get")
    @RequestMapping(name = "查询流程发文信息", path = "html/initDispatchBySignId", method = RequestMethod.GET)
    public @ResponseBody
    DispatchDocDto initDispatchBySignId(@RequestParam String signId) throws Exception {
        return dispatchDocService.initDispatchBySignId(signId);
    }

    @RequiresPermissions("dispatch#html/edit#get")
    @RequestMapping(name = "发文编辑", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/edit";
    }
}
