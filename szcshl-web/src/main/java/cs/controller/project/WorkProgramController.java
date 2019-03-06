package cs.controller.project;

import cs.ahelper.IgnoreAnnotation;
import cs.ahelper.projhelper.ProjUtil;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.history.WorkProgramHis;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;
import cs.model.history.WorkProgramHisDto;
import cs.model.project.SignDto;
import cs.model.project.WorkProgramDto;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.service.history.WorkProgramHisService;
import cs.service.project.WorkProgramService;
import cs.service.rtx.RTXSendMsgPool;
import cs.service.rtx.RTXService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cs.common.constants.Constant.ERROR_MSG;

@Controller
@RequestMapping(name = "工作方案", path = "workprogram")
@IgnoreAnnotation
public class WorkProgramController {
    private String ctrlName = "workprogram";
    @Autowired
    private WorkProgramService workProgramService;
    @Autowired
    private RTXService rtxService;
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private WorkProgramHisService workProgramHisService;

    @RequiresAuthentication
    //@RequiresPermissions("workprogram#addWork#post")
    @RequestMapping(name = "保存工作方案", path = "addWork", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody WorkProgramDto workProgramDto, Boolean isNeedWorkProgram) {
        ResultMsg resultMsg = workProgramService.save(workProgramDto, isNeedWorkProgram);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    //@RequiresPermissions("workprogram#saveBaseInfo#post")
    @RequestMapping(name = "保存项目基本信息", path = "saveBaseInfo", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveBaseInfo(@RequestBody WorkProgramDto workProgramDto) {
        ResultMsg resultMsg = workProgramService.saveBaseInfo(workProgramDto);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }
    @RequiresAuthentication
    //@RequiresPermissions("workprogram#initWorkProgram#post")
    @RequestMapping(name = "初始化工作方案", path = "initFlowWP", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> initFlowWP(@RequestParam String signId, String taskId,String branchId) {
        return workProgramService.initWorkProgram(signId,taskId,branchId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("workprogram#initWorkProgram#post")
    @RequestMapping(name = "初始化项目基本信息", path = "initBaseInfo", method = RequestMethod.POST)
    @ResponseBody
    public WorkProgramDto initBaseInfo(@RequestParam String signId) {
        return workProgramService.initBaseInfo(signId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("workprogram#html/workMaintainList#post")
    @RequestMapping(name = "查询工作方案", path = "html/workMaintainList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> workMaintainList(@RequestParam String signId) {
        Map<String,Object> resultMap = workProgramService.workMaintainList(signId);
        if(!Validate.isMap(resultMap)){
            resultMap = new HashMap<>();
        }
        return resultMap;
    }

    @RequiresAuthentication
    //@RequiresPermissions("workprogram#initWorkProgramById#post")
    @RequestMapping(name = "根据ID查找工作方案信息", path = "initWorkProgramById", method = RequestMethod.POST)
    @ResponseBody
    public  WorkProgramDto initWorkProgramById(@RequestParam String workId){
    	WorkProgramDto work = workProgramService.initWorkProgramById(workId);
    	if(!Validate.isObject(work)){
            work = new WorkProgramDto();
        }
    	return work;
    }

    @RequiresAuthentication
    //@RequiresPermissions("workprogram##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        workProgramService.delete(id);
    }

    @RequiresAuthentication
    @RequestMapping(name = "删除工作方案", path = "deleteBySignId", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg deleteBySignId(@RequestParam  String signId) {
        ResultMsg resultMsg = workProgramService.deleteBySignId(signId);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    //@RequiresPermissions("workprogram#findByPrincipalUser#get")
    @RequestMapping(name="通过项目负责人获取项目信息", path="findByPrincipalUser" ,method=RequestMethod.GET)
    @ResponseBody
    public WorkProgramDto findByPrincipalUser(@RequestParam String signId){
        WorkProgramDto work = workProgramService.findByPrincipalUser(signId);
        if(!Validate.isObject(work)){
            work = new WorkProgramDto();
        }
        return work;
    }

    @RequiresAuthentication
    //@RequiresPermissions("workprogram#createMeetingDoc#get")
    @RequestMapping(name = "生成会前准备材料", path = "createMeetingDoc", method = RequestMethod.GET)
    @ResponseBody
    public ResultMsg createMeetingDoc(@RequestParam String signId) {
        ResultMsg resultMsg = workProgramService.createMeetingDoc(signId);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "专家评审会修改", path = "updateReviewType", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg updateReviewType(@RequestBody WorkProgramDto workProgramDto) {
        ResultMsg resultMsg = workProgramService.updateReviewType(workProgramDto.getSignId(),workProgramDto.getId(),workProgramDto.getReviewType());
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    /**
     * 重做或者新增工作方案，
     * @param signId
     * @param reworkType 0是新增，1是重新发起流程
     * @param brandIds
     * @param userId
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(name = "发起重新做工作方案流程", path = "startReWorkFlow", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg startReWorkFlow(@RequestParam String signId,@RequestParam String reworkType,String brandIds,String userId) {
        ResultMsg resultMsg = workProgramService.startReWorkFlow(signId,reworkType,brandIds,userId);
        if(Validate.isObject(resultMsg)){
            if(resultMsg.isFlag()){
                //如果成功，则发送短信通知
                String taskName = resultMsg.getReObj().toString();
                rtxService.dealPoolRTXMsg(null,signId,resultMsg,taskName, Constant.MsgType.task_type.name());
            }
        }else{
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "初始化重做工作方案流程信息", path = "initDealFlow", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> initDealFlow(String wpId){
        Map<String,Object> resultMap = new HashMap<>();
        WorkProgramDto workProgramDto = new WorkProgramDto();
        Sign sign = null;
        String signId = "",brandId = "";
        /**
         * 查询工作方案信息
         * 1、如果通过工作方案ID可以获取得到工作方案信息，
         * 2、如果通过工作方案ID获取不到项目，说明项目已经变为历史记录，得从历史记录中查询
         */
        try {
            WorkProgram workProgram = workProgramRepo.findById(wpId);
            if(Validate.isObject(workProgram) && Validate.isString(workProgram.getId())){
                BeanCopierUtils.copyProperties(workProgram, workProgramDto);
                workProgramRepo.initWPMeetingExp(workProgramDto, workProgram);
                //项目信息
                sign = workProgram.getSign();
            }else{
                WorkProgramHis his = workProgramHisService.findById(wpId);
                if(Validate.isObject(his) && Validate.isString(his.getId())){
                    BeanCopierUtils.copyProperties(his, workProgramDto);
                    workProgramHisService.initWPMeetingExp(workProgramDto, his);
                }

                if(Validate.isString(his.getSignId())){
                    sign = signRepo.findById(Sign_.signid.getName(),his.getSignId());
                }
            }
        }catch (Exception e){
            WorkProgramHis his = workProgramHisService.findById(wpId);
            if(Validate.isObject(his) && Validate.isString(his.getId())){
                BeanCopierUtils.copyProperties(his, workProgramDto);
                workProgramHisService.initWPMeetingExp(workProgramDto, his);
            }

            if(Validate.isString(his.getSignId())){
                sign = signRepo.findById(Sign_.signid.getName(),his.getSignId());
            }
        }

        if(Validate.isObject(sign) && Validate.isString(sign.getSignid())){
            SignDto signDto = new SignDto();
            BeanCopierUtils.copyProperties(sign,signDto);
            resultMap.put("SignDto",signDto);
            signId = sign.getSignid();
        }

        if(Validate.isObject(workProgramDto)){
            brandId = workProgramDto.getBranchId();
        }

        if(Validate.isString(signId)){
            //如果不是主工作方案，还要查询主工作方案信息
            if(!ProjUtil.isMainBranch(brandId)){
                WorkProgram mainWP = workProgramRepo.findBySignIdAndBranchId(signId, FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue(), false);
                if(Validate.isObject(mainWP)){
                    WorkProgramDto mainWPDto = new WorkProgramDto();
                    BeanCopierUtils.copyProperties(mainWP,mainWPDto);
                    workProgramDto.setMainWorkProgramDto(mainWPDto);
                }
            }
            resultMap.put("WorkProgramDto",workProgramDto);

            //历史工作方案记录(重做的时候才有)
            if(Validate.isString(brandId) && !brandId.equals(workProgramDto.getId().substring(0,2))){
                List<WorkProgramHisDto> wpHisDtoList = workProgramHisService.findBySignAndBranch(signId,brandId);
                if(Validate.isList(wpHisDtoList)){
                    resultMap.put("WorkProgramHisDtoList",wpHisDtoList);
                }
            }
        }

        return resultMap;
    }
    @RequiresAuthentication
    @RequestMapping(name = "更新工作方案专家评审费用", path = "updateWPExpertCost", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateWPExpertCost(@RequestParam String wpId) {
        workProgramService.initExpertCost(wpId);
    }


    @RequiresAuthentication
    //@RequiresPermissions("workprogram#html/edit#get")
    @RequestMapping(name = "工作方案编辑", path = "html/edit", method = RequestMethod.GET)
    public String edit() {

        return ctrlName + "/edit";
    }

    @RequiresAuthentication
    //@RequiresPermissions("workprogram#html/baseEdit#get")
    @RequestMapping(name = "项目基本信息", path = "html/baseEdit", method = RequestMethod.GET)
    public String baseEdit() {

        return ctrlName + "/baseEdit";
    }

}
