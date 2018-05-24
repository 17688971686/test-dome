package cs.quartz.execute;

import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.project.ProjectStop;
import cs.domain.project.Sign;
import cs.domain.sys.Log;
import cs.domain.sys.Workday;
import cs.model.project.ProjectStopDto;
import cs.quartz.unit.QuartzUnit;
import cs.service.flow.FlowService;
import cs.service.project.ProjectStopService;
import cs.service.project.SignService;
import cs.service.sys.LogService;
import cs.service.sys.WorkdayService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cs.common.constants.SysConstants.SUPER_ACCOUNT;

/**
 * Created by MCL
 * 2017/8/3
 * 自动启动暂停的项目
 */
@Component
public class ProjectStopExecute implements Job{
    private static Logger logger = Logger.getLogger(ProjectStopExecute.class);
   /* @Autowired
    private ProjectStopService projectStopService;
    @Autowired
    private WorkdayService workdayService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private SignService signService;
    @Autowired
    private LogService logService;
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        logger.info("---------------------- 项目暂停的定时器开始执行 ----------------------");

        ProjectStopService projectStopService = (ProjectStopService) context.getMergedJobDataMap().get("projectStopService");
        WorkdayService workdayService = (WorkdayService) context.getMergedJobDataMap().get("workdayService");
        FlowService flowService = (FlowService) context.getMergedJobDataMap().get("flowService");
        SignService signService = (SignService) context.getMergedJobDataMap().get("signService");
        LogService logService = (LogService) context.getMergedJobDataMap().get("logService");
        RuntimeService runtimeService = (RuntimeService) context.getMergedJobDataMap().get("runtimeService");
        //添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setUserName(SUPER_ACCOUNT);
        log.setBuninessId("");
        log.setModule(Constant.LOG_MODULE.QUARTZ.getValue()+"【项目暂停】" );
        //优先级别中等
        log.setLogLevel(Constant.EnumState.STOP.getValue());
        log.setLogger(this.getClass().getName()+".execute");

        Date now = new Date();
        try{
            List<Workday> workdayList = null;
            List<ProjectStop> projectList = projectStopService.selectPauseProject();
            List<ProjectStop> updateList = new ArrayList<>();
            int total = 0;
            if (Validate.isList(projectList)) {
                total = projectList.size();
                //一次性查出近一年的调休记录，不用每次循环都查
                workdayList = workdayService.findWorkDayByNow();
            }

            for(int i=0;i<total;i++){
                ProjectStop projectStop = projectList.get(i);
                if(!Validate.isObject(projectStop.getPausetime())){
                    projectStop.setPausetime(now);
                    projectStop.setIsOverTime(Constant.EnumState.PROCESS.getValue());
                    updateList.add(projectStop);
                    continue;
                }
                //如果还未到暂停日期的，先判断是否要启动暂停；
                if(Constant.EnumState.NO.getValue().equals(projectStop.getIsOverTime())){
                    if(now.after(projectStop.getPausetime())){
                        projectStop.setIsOverTime(Constant.EnumState.PROCESS.getValue());
                        updateList.add(projectStop);
                        Sign sign = projectStopService.findSignByStopId(projectStop.getStopid());
                        if(sign != null){
                            /*ResultMsg stopResult = flowService.stopFlow(sign.getSignid());
                            if(stopResult.isFlag()){
                                //更改项目状态
                                sign.setSignState(Constant.EnumState.STOP.getValue());
                                sign.setIsLightUp(Constant.signEnumState.PAUSE.getValue());
                                signService.save(sign);
                            }*/
                            //只更改项目状态，不暂停流程
                            sign.setSignState(Constant.EnumState.STOP.getValue());
                            sign.setIsLightUp(Constant.signEnumState.PAUSE.getValue());
                            signService.save(sign);
                        }
                    }
                //已经暂停的，则开始计算暂停时长
                }else{
                    int countDay = QuartzUnit.countWorkday(workdayList,(projectStop.getPausetime()==null?projectStop.getCreatedDate():projectStop.getPausetime()));
                    //判断暂定天数是否大于或等于填写的天数，并修改状态，并且记录启动时间
                    if(Validate.isObject(projectStop.getExpectpausedays()) && countDay > projectStop.getExpectpausedays()){
                        projectStop.setIsOverTime(Constant.EnumState.YES.getValue());
                        //实际启动日期
                        projectStop.setStartTime(new Date());
                        //实际暂停天数
                        projectStop.setPausedays(Float.parseFloat(String.valueOf(countDay)));
                        updateList.add(projectStop);

                        //执行激活流程操作,并更新项目状态
                        Sign sign = projectStopService.findSignByStopId(projectStop.getStopid());
                        if(sign != null){
                            ProcessInstance processInstance = flowService.findProcessInstanceByBusinessKey(sign.getSignid());
                            //如果是暂停，则重新启动
                            if (processInstance.isSuspended()) {
                                runtimeService.activateProcessInstanceById(processInstance.getId());
                            }
                            sign.setIsLightUp(Constant.signEnumState.PROCESS.getValue());
                            sign.setSignState(Constant.EnumState.PROCESS.getValue());
                            signService.save(sign);
                        }
                    }
                }
            }
            if(Validate.isList(updateList)){
                projectStopService.updateProjectStopStatus(updateList);
            }
            log.setMessage("完成项目暂停定时器处理！");
            log.setLogCode(Constant.MsgCode.OK.getValue());
            log.setResult(Constant.EnumState.YES.getValue());
            logger.info("---------------------- 自动启动暂停的项目定时器结束 ----------------------");
        }catch (Exception e){
            log.setMessage("处理异常："+e.getMessage());
            log.setLogCode(Constant.MsgCode.ERROR.getValue());
            log.setResult(Constant.EnumState.NO.getValue());
            logger.info("自动启动暂停的项目定时器异常："+e.getMessage());
        }
        logService.save(log);
    }

}
