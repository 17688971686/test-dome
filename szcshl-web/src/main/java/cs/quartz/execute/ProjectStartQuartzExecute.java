package cs.quartz.execute;

import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.project.ProjectStop;
import cs.domain.project.Sign;
import cs.domain.sys.Workday;
import cs.quartz.unit.QuartzUnit;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.service.flow.FlowService;
import cs.service.project.ProjectStopService;
import cs.service.sys.WorkdayService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by MCL
 * 2017/8/3
 * 自动启动暂停的项目
 */
@Service
public class ProjectStartQuartzExecute implements Job{

    @Autowired
    private ProjectStopService projectStopService;
    @Autowired
    private WorkdayService workdayService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private SignRepo signRepo;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        List<Workday> workdayList = null;
        List<ProjectStop> projectStopList = projectStopService.findPauseProjectSuccess();

        List<ProjectStop> updateList = new ArrayList<>();
        int total = 0;
        if (Validate.isList(projectStopList)) {
            total = projectStopList.size();
            //一次性查出近一年的调休记录，不用每次循环都查
            workdayList = workdayService.findWorkDayByNow();
        }

        QuartzUnit quartzUnit = new QuartzUnit();
        for(int i=0;i<total;i++){
            ProjectStop projectStop = projectStopList.get(i);
            if(null == projectStop.getPausetime() ){
                projectStop.setPausetime(new Date());
                projectStop.setIsOverTime(Constant.EnumState.PROCESS.getValue());
                updateList.add(projectStop);
                continue;
            }
            //如果还未暂停的，先启动；
            if(Constant.EnumState.NO.getValue().equals(projectStop.getIsOverTime())){
                if(DateUtils.daysBetween(new Date(),projectStop.getPausetime()) <= 0){
                    projectStop.setIsOverTime(Constant.EnumState.PROCESS.getValue());
                    updateList.add(projectStop);
                    Sign sign = projectStop.getSign();
                    ResultMsg stopResult = flowService.stopFlow(projectStop.getSign().getSignid());
                    if(stopResult.isFlag()){
                        //更改项目状态
                        sign.setSignState(Constant.EnumState.STOP.getValue());
                        sign.setIsLightUp(Constant.signEnumState.PAUSE.getValue());
                        signRepo.save(sign);
                    }
                    continue;
                }
            //已经暂停的，则开始计算暂停时长
            }else{
                int countDay = quartzUnit.countWorkday(workdayList,projectStop.getPausetime());
                //判断暂定天数是否大于或等于填写的天数，并修改状态，并且记录启动时间
                if(countDay > projectStop.getExpectpausedays()){
                    projectStop.setIsOverTime(Constant.EnumState.YES.getValue());
                    projectStop.setStartTime(new Date());
                    projectStop.setPausedays(Float.parseFloat(String.valueOf(countDay)));
                    updateList.add(projectStop);

                    //执行激活流程操作,并更新项目状态
                    Sign sign = projectStop.getSign();
                    if(sign != null){
                        ResultMsg result = flowService.restartFlow(sign.getSignid());
                        if(result.isFlag()){
                            sign.setIsLightUp(Constant.signEnumState.PROCESS.getValue());
                            sign.setSignState(Constant.EnumState.PROCESS.getValue());
                            signRepo.save(sign);
                        }
                    }
                }
            }
        }
        if(Validate.isList(updateList)){
            projectStopService.updateProjectStopStatus(updateList);
        }
    }

    /**
     * 清除日期的小时、分、秒和毫秒
     *
     * @param calendar
     */
    private void cleanHMSM(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
