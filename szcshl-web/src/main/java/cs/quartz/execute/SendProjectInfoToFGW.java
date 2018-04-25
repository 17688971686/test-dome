package cs.quartz.execute;

import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.project.Sign_;
import cs.domain.sys.Log;
import cs.model.project.CommentDto;
import cs.model.project.SignDto;
import cs.model.project.WorkProgramDto;
import cs.service.flow.FlowService;
import cs.service.project.SignService;
import cs.service.restService.SignRestService;
import cs.service.sys.LogService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.ibm.icu.util.LocalePriorityList.add;
import static cs.common.FlowConstant.*;

/**
 * 发送信息项目信息给委里
 * Created by ldm on 2017/12/18.
 */
public class SendProjectInfoToFGW implements Job {
    @Autowired
    private SignService signService;
    @Autowired
    private LogService logService;
    @Autowired
    private SignRestService signRestService;
    @Autowired
    private FlowService flowService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        //添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setUserName(Constant.SUPER_USER);
        log.setBuninessId("");
        log.setModule(Constant.LOG_MODULE.QUARTZ.getValue() + "【回传数据】");
        //优先级别中等
        log.setLogLevel(Constant.EnumState.PROCESS.getValue());
        log.setLogger(this.getClass().getName() + ".execute");
        //获取回传给委里的接口
        String fgwUrl = signRestService.getReturnUrl();
        if (Validate.isString(fgwUrl)) {
            try {
                //1、查询还未发送给发改委的项目信息（在办或者已办结，未发送的项目）
                List<SignDto> unSendList = signService.findUnSendFGWList();
                if (Validate.isList(unSendList)) {
                    //部分参数
                    int sucessCount = 0, errorCount = 0, totalCount = unSendList.size();
                    StringBuffer errorBuffer = new StringBuffer();
                    List<String> sucessIdList = new ArrayList<>();
                    List<CommentDto> commentDtoList = null;
                    Map<String,CommentDto> commentDtoMap = null;
                    List<String> nodeKeyList =  new ArrayList<String>(){{add(FLOW_SIGN_FGLD_FB);add(FLOW_SIGN_BMFB1);add(FLOW_SIGN_BMFB2);add(FLOW_SIGN_BMFB3);add(FLOW_SIGN_BMFB4);}};
                    ResultMsg resultMsg = null;

                    for (int i = 0, l = unSendList.size(); i < l; i++) {
                        SignDto signDto = unSendList.get(i);
                        //如果发文时间跟现在时间对比，在8分钟内，则不急回传委里，避免没填写发文编号
                        if (DateUtils.minBetween(signDto.getExpectdispatchdate(),new Date()) < 8) {
                            continue;
                        }
                        WorkProgramDto mainWP = null;
                        if (Validate.isList(signDto.getWorkProgramDtoList())) {
                            for (WorkProgramDto wpd : signDto.getWorkProgramDtoList()) {
                                if (SignFlowParams.BRANCH_INDEX1.getValue().equals(wpd.getBranchId())) {
                                    mainWP = wpd;
                                }
                            }
                        }
                        //获取分办部门意见
                        commentDtoList = flowService.findCommentByProcInstId(signDto.getProcessInstanceId(),nodeKeyList );
                        commentDtoMap = flowService.filterComents(commentDtoList,nodeKeyList);
                        resultMsg = signRestService.setToFGW(signDto, mainWP, signDto.getDispatchDocDto(), fgwUrl,commentDtoMap);
                        if (resultMsg.isFlag()) {
                            sucessIdList.add(signDto.getSignid());
                            sucessCount++;
                        } else {
                            errorCount++;
                            errorBuffer.append(resultMsg.getReMsg() + "\r\n");
                        }
                    }
                    if (sucessCount == 0) {
                        resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), errorBuffer.toString());
                    } else if (errorCount == 0) {
                        resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(), "本次回传数据成功，总共回传【" + sucessCount + "】个项目数据！");
                    } else {
                        String msg = "本次总共回传【" + totalCount + "】，成功【" + sucessCount + "】个，失败" + errorCount + "】个；" + errorBuffer.toString();
                        resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(), msg);
                    }
                    //更改成功发送的项目状态
                    if (Validate.isList(sucessIdList)) {
                        signService.updateSignState(StringUtils.join(sucessIdList, ","), Sign_.isSendFGW.getName(), Constant.EnumState.YES.getValue());
                    }
                    log.setLogCode(resultMsg.getReCode());
                    log.setMessage(resultMsg.getReMsg());
                    log.setResult(resultMsg.isFlag() ? Constant.EnumState.YES.getValue() : Constant.EnumState.NO.getValue());
                } else {
                    log.setLogCode(Constant.MsgCode.OK.getValue());
                    log.setMessage("没有回传数据");
                    log.setResult(Constant.EnumState.YES.getValue());
                }
            } catch (Exception e) {
                log.setLogCode(Constant.MsgCode.ERROR.getValue());
                log.setMessage("异常：" + e.getMessage());
                log.setResult(Constant.EnumState.NO.getValue());
            }
        } else {
            log.setLogCode(Constant.MsgCode.ERROR.getValue());
            log.setMessage("没有设置回传给委里的接口地址！");
            log.setResult(Constant.EnumState.NO.getValue());
        }

        //添加日记记录
        logService.save(log);
    }
}
