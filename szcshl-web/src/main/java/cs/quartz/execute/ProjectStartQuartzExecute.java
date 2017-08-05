package cs.quartz.execute;

import cs.common.Constant;
import cs.domain.project.ProjectStop;
import cs.quartz.unit.QuartzUnit;
import cs.service.project.ProjectStopService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.Date;
import java.util.List;

/**
 * Created by MCL
 * 2017/8/3
 * 自动启动暂停的项目
 */
public class ProjectStartQuartzExecute implements Job{

    @Autowired
    private ProjectStopService projectStopService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        List<ProjectStop> projectStopList = projectStopService.findPauseProjectSuccess();
        QuartzUnit quartzUnit = new QuartzUnit();
        for(ProjectStop projectStop : projectStopList){
            int countDay = quartzUnit.countWorkday(projectStop.getPausetime());
            //判断暂定天数是否大于或等于填写的天数，并修改状态，并且记录启动时间
            if(countDay >= projectStop.getPausedays()){
                projectStop.setIsactive(Constant.EnumState.NO.getValue());
                projectStop.setStartTime(new Date());
            }
        }
        projectStopService.updateProjectStopStatus(projectStopList);
    }
}
