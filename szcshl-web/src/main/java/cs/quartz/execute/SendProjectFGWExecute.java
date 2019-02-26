package cs.quartz.execute;

import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.constants.SysConstants;
import cs.common.utils.DateUtils;
import cs.common.utils.SMSUtils;
import cs.common.utils.Validate;
import cs.domain.project.Sign_;
import cs.domain.sys.Log;
import cs.domain.sys.SMSLog;
import cs.domain.sys.User;
import cs.model.project.CommentDto;
import cs.model.project.SignDto;
import cs.model.project.WorkProgramDto;
import cs.service.flow.FlowService;
import cs.service.project.SignService;
import cs.service.restService.SignRestService;
import cs.service.rtx.RTXService;
import cs.service.sys.LogService;
import cs.service.sys.MsgService;
import cs.service.sys.WorkdayService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static cs.common.constants.Constant.MsgType.sendfgw_type;
import static cs.common.constants.FlowConstant.*;
import static cs.common.constants.SysConstants.SUPER_ACCOUNT;
import static cs.common.constants.SysConstants.SYS_CONFIG_ENUM;

/**
 * @author ldm on 2017/12/18.
 * 发送信息项目信息给委里
 */
@Component
public class SendProjectFGWExecute implements Job {
    private static Logger logger = Logger.getLogger(SendProjectFGWExecute.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        logger.info("---------------------- 项目回传定时器开始执行 ----------------------");
        LogService logService = (LogService) context.getMergedJobDataMap().get("logService");
        SignService signService = (SignService) context.getMergedJobDataMap().get("signService");
        SignRestService signRestService = (SignRestService) context.getMergedJobDataMap().get("signRestService");
        FlowService flowService = (FlowService) context.getMergedJobDataMap().get("flowService");

        //短信需要用到的service
        RTXService rtxService = (RTXService) context.getMergedJobDataMap().get("rtxService");
        MsgService msgService = (MsgService) context.getMergedJobDataMap().get("msgService");
        WorkdayService workdayService = (WorkdayService) context.getMergedJobDataMap().get("workdayService");

        //添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setUserName(SUPER_ACCOUNT);
        log.setBuninessId("");
        log.setModule(Constant.LOG_MODULE.QUARTZ.getValue() + "【回传数据】");
        //优先级别中等
        log.setLogLevel(Constant.EnumState.PROCESS.getValue());
        log.setLogger(this.getClass().getName() + ".execute");

        //获取回传给委里的接口
        String fgwUrl = signRestService.getReturnUrl();
        String localUrl = signRestService.getLocalUrl();

        if (Validate.isString(fgwUrl)) {
            try {
                //1、查询还未发送给发改委的项目信息（在办或者已办结，未发送的项目）
                List<SignDto> unSendList = signService.findUnSendFGWList();
                if (Validate.isList(unSendList)) {
                    //部分参数
                    int sucessCount = 0, errorCount = 0, totalCount = unSendList.size();
                    StringBuffer stringBuffer = new StringBuffer();
                    StringBuffer msgBuffer = new StringBuffer();
                    List<String> sucessIdList = new ArrayList<>();
                    List<CommentDto> commentDtoList = null;
                    Map<String,CommentDto> commentDtoMap = null;
                    List<String> nodeKeyList =  new ArrayList<String>(){{add(FLOW_SIGN_FGLD_FB);add(FLOW_SIGN_BMFB1);add(FLOW_SIGN_BMFB2);add(FLOW_SIGN_BMFB3);add(FLOW_SIGN_BMFB4);}};
                    ResultMsg resultMsg = null;

                    for (int i = 0; i < totalCount; i++) {
                        SignDto signDto = unSendList.get(i);
                        //如果发文时间跟现在时间对比，在5分钟内，则不急回传委里，避免没填写发文编号
                        if (DateUtils.minBetween(signDto.getDispatchdate(),new Date()) < 5) {
                            continue;
                        }
                        if(!Validate.isList(signDto.getSysFileDtoList())){
                            stringBuffer.append(signDto.getProjectname()+"["+signDto.getFilecode()+"]没有评审意见相关附件！" + "\n");
                            msgBuffer.append(signDto.getProjectname()+"["+signDto.getFilecode()+"]没有评审意见相关附件！" + "\n");
                            errorCount++;
                        }else{
                            WorkProgramDto mainWP = null;
                            if (Validate.isList(signDto.getWorkProgramDtoList())) {
                                mainWP = signDto.getWorkProgramDtoList().get(0);
                            }
                            //获取分办部门意见
                            commentDtoList = flowService.findCommentByProcInstId(signDto.getProcessInstanceId(),nodeKeyList );
                            commentDtoMap = flowService.filterComents(commentDtoList,nodeKeyList);
                            resultMsg = signRestService.setToFGW(signDto, mainWP, signDto.getDispatchDocDto(), fgwUrl,localUrl,commentDtoMap,signDto.getSysFileDtoList());
                            if (resultMsg.isFlag()) {
                                sucessIdList.add(signDto.getSignid());
                                sucessCount++;
                                msgBuffer.append(signDto.getProjectname()+"["+signDto.getFilecode()+"]回传成功！" + "\n");
                            } else {
                                errorCount++;
                                stringBuffer.append(resultMsg.getReMsg() + "\r\n");
                                msgBuffer.append(signDto.getProjectname()+"["+signDto.getFilecode()+"]回传失败！" + "\n");
                            }
                        }
                    }
                    if (sucessCount == 0) {
                        resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), stringBuffer.toString());
                    } else if (errorCount == 0) {
                        resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(), "本次回传数据成功，总共回传【" + sucessCount + "】个项目数据！\r\n"+stringBuffer.toString());
                    } else {
                        String msg = "本次总共回传【" + totalCount + "】，成功【" + sucessCount + "】个，失败" + errorCount + "】个！\r\n" + stringBuffer.toString();
                        resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(), msg);
                    }
                    //更改成功发送的项目状态
                    if (Validate.isList(sucessIdList)) {
                        signService.updateSignState(StringUtils.join(sucessIdList, ","), Sign_.isSendFGW.getName(), Constant.EnumState.YES.getValue());
                    }
                    //发送短信
                    if (rtxService.rtxSMSEnabled()  && SMSUtils.isSendTime() && workdayService.isWorkDay(new Date())) {
                        List<User> recvUserList = msgService.getNoticeUserByConfigKey(SYS_CONFIG_ENUM.SMS_SENDFGW_FAIL_USER.toString());
                        if(Validate.isList(recvUserList)){
                            String msgContent = SMSUtils.buildSendMsgContent(null,sendfgw_type.name(),msgBuffer.toString(),resultMsg.isFlag());
                            SMSLog smsLog = new SMSLog();
                            smsLog.setSmsLogType(sendfgw_type.name());
                            smsLog.setBuninessId(StringUtils.join(sucessIdList, SysConstants.SEPARATE_COMMA));
                            //发送短信
                            SMSUtils.sendMsg(msgService,recvUserList,msgContent,smsLog);
                        }
                    }
                    log.setLogCode(resultMsg.getReCode());
                    log.setMessage(resultMsg.getReMsg());
                    log.setResult(resultMsg.isFlag() ? Constant.EnumState.YES.getValue() : Constant.EnumState.NO.getValue());
                } else {
                    log.setLogCode(Constant.MsgCode.OK.getValue());
                    log.setMessage("系统没有检测到要回传的项目信息！");
                    log.setResult(Constant.EnumState.YES.getValue());
                }
            } catch (Exception e) {
                logger.error("异常信息: ", e);
                log.setLogCode(Constant.MsgCode.ERROR.getValue());
                log.setMessage("异常：" + e.getMessage());
                log.setResult(Constant.EnumState.NO.getValue());
            }
        } else {
            log.setLogCode(Constant.MsgCode.ERROR.getValue());
            log.setMessage("系统没有设置回传给委里的接口地址！");
            log.setResult(Constant.EnumState.NO.getValue());
        }
        //添加日记记录
        logService.save(log);
        logger.info("---------------------- 项目回传定时器执行结束 ----------------------");
    }
}
