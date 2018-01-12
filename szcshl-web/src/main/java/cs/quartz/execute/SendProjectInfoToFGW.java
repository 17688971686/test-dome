package cs.quartz.execute;

import cs.common.Constant;
import cs.common.IFResultCode;
import cs.common.ResultMsg;
import cs.common.utils.PropertyUtil;
import cs.common.utils.Validate;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.domain.sys.Log;
import cs.service.project.SignService;
import cs.service.restService.SignRestService;
import cs.service.sys.LogService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setUserName(Constant.SUPER_USER);
        log.setBuninessId("");
        log.setModule(Constant.LOG_MODULE.QUARTZ.getValue()+"【回传数据】" );
        //优先级别中等
        log.setLogLevel(Constant.EnumState.PROCESS.getValue());
        log.setLogger(this.getClass().getName()+".execute");
        try{
            //1、查询还未发送给发改委的项目信息（在办或者已办结，未发送的项目）
            List<Sign> unSendList = signService.findUnSendFGWList();
            if (Validate.isList(unSendList)) {
                //部分参数
                int sucessCount = 0, errorCount = 0, totalCount = unSendList.size();
                StringBuffer errorBuffer = new StringBuffer();
                List<String> sucessIdList = new ArrayList<>();
                ResultMsg resultMsg = null;
                // 接口地址
                PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
                String endpoint = propertyUtil.readProperty(IFResultCode.FGW_PROJECT_IFS),
                        loaclUrl = propertyUtil.readProperty(IFResultCode.LOCAL_URL);
                if (Validate.isString(endpoint)) {
                    for (int i = 0, l = unSendList.size(); i < l; i++) {
                        resultMsg = signRestService.setToFGW(unSendList.get(i), endpoint, loaclUrl);
                        if (resultMsg.isFlag()) {
                            sucessCount++;
                        } else {
                            errorCount++;
                            errorBuffer.append(resultMsg.getReMsg() + "\r\n");
                        }
                    }
                } else {
                    errorBuffer.append(IFResultCode.IFMsgCode.SZEC_SFGW_01.getValue());
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
            }else{
                log.setLogCode(Constant.MsgCode.OK.getValue());
                log.setMessage("没有回传数据");
                log.setResult(Constant.EnumState.YES.getValue());
            }
        }catch(Exception e){
            log.setLogCode(Constant.MsgCode.ERROR.getValue());
            log.setMessage("异常："+e.getMessage());
            log.setResult(Constant.EnumState.NO.getValue());
        }
        //添加日记记录
        logService.save(log);
    }
}
