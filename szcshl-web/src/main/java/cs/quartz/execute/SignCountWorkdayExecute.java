package cs.quartz.execute;

import java.util.List;

import cs.common.utils.Validate;
import cs.domain.sys.Workday;
import cs.service.sys.WorkdayService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import cs.common.Constant;
import cs.domain.project.ProjectStop;
import cs.domain.project.Sign;
import cs.quartz.unit.QuartzUnit;
import cs.service.project.ProjectStopService;
import cs.service.project.SignService;

/**
 * 计算工作日的执行类
 *
 * @author MCL
 * @date 2017年7月4日 下午4:41:43
 */
@Controller
public class SignCountWorkdayExecute implements Job {

    @Autowired
    private SignService signService;

    @Autowired
    private ProjectStopService projectStopService;

    @Autowired
    private WorkdayService workdayService;

    /*警示灯状态如下：
     * PROCESS("1"),	//在办
     * DISPA("2"),		//已发文
     * ARCHIVE("3"),	//已发送存档
     * PAUSE("4"),		//暂停
     * UNDER3WORKDAY("5"),		//少于三个工作日
     * DISPAOVER("6"),			//发文超期
     * OVER25WORKDAYARCHIVE("7"),	//超过25个工作日未存档
     * ARCHIVEOVER("8");		//存档超期
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        List<Workday> workdayList = null;
        QuartzUnit unit = new QuartzUnit();
        // 1、查询正在办理的项目，通过签收时间计算工作日，通过项目类型判断超过多少个工作日后显示警示灯，不同阶段显示不同的警示灯
        List<Sign> signList = signService.selectSignNotFinish();
        int total = 0;
        if (Validate.isList(signList)) {
            total = signList.size();
            //一次性查出近一年的调休记录，不用每次循环都查
            workdayList = workdayService.findWorkDayByNow();
        }
        for (int i=0;i<total;i++) {
            Sign sign = signList.get(i);
            //1、如果项目未正式签收，不用计算工作日
            if(null == sign.getSigndate() || !Constant.EnumState.YES.getValue().equals(sign.getIssign())){
                continue;
            }
            //2、如果没有亮灯的，则设置为默认亮灯状态
            if (!Validate.isString(sign.getIsLightUp())) {
                sign.setIsLightUp(Constant.signEnumState.PROCESS.getValue());
            }

            //3、通过收文ID查找 项目暂停情况,并计算项目总共暂停了几个工作日
            List<ProjectStop> projectStopList = projectStopService.findProjectStopBySign(sign.getSignid());
            float stopWorkday = 0;
            for (ProjectStop ps : projectStopList) {
                //记录实际暂停的工作日
                stopWorkday += ps.getPausedays();
            }
            //4、判断该项目是否为暂停状态。 未暂停，计算从正式签收到当前时间的工作日，再减掉暂停的工作日，并设置相对应的状态
            if (!Constant.EnumState.STOP.getValue().equals(sign.getSignState())) {
                float usedWorkDay = unit.countWorkday(workdayList,sign.getSigndate()) - stopWorkday;
                //剩余评审天数 = 评审天数-已用评审天数
                sign.setSurplusdays(sign.getReviewdays() - usedWorkDay);
                //默认是在办
                sign.setIsLightUp(Constant.signEnumState.PROCESS.getValue());
                //计算更新亮灯状态
                updateLightUpState(sign, usedWorkDay, new Float(sign.getReviewdays()).intValue());
            }else{
                sign.setIsLightUp(Constant.signEnumState.PAUSE.getValue());
            }
        }
        signService.bathUpdate(signList);
    }

    /**
     * 修改提示灯的状态
     *
     * @param sign         收文
     * @param usedWorkDay  已用工作日
     * @param totalWorkDay 总共工作日
     */
    public void updateLightUpState(Sign sign, float usedWorkDay, int totalWorkDay) {
        /*NOLIGHT("0"),                 //不显示
        PROCESS("1"),                   //在办
        DISPA("2"),                     //已发文
        ARCHIVE("3"),                   //已发送存档
        PAUSE("4"),                     //暂停
        UNDER3WORKDAY("5"),             //少于三个工作日
        DISPAOVER("6"),                 //发文超期
        OVER25WORKDAYARCHIVE("7"),      //超过25个工作日未存档
        ARCHIVEOVER("8");               //存档超期*/

        // 已发文状态
        if (sign.getProcessState() == Constant.SignProcessState.END_DIS.getValue()) {
            sign.setIsLightUp(Constant.signEnumState.DISPA.getValue());
        }

        // 已发送存档状态
        if ((sign.getIsSendFileRecord() == Constant.EnumState.YES.getValue()) ||
                (sign.getProcessState() == Constant.SignProcessState.DO_FILE.getValue())) {
            sign.setIsLightUp(Constant.signEnumState.ARCHIVE.getValue());
        }

        //少于3个工作日
        if ((totalWorkDay - usedWorkDay) <  Constant.WORK_DAY_3) {
            sign.setIsLightUp(Constant.signEnumState.UNDER3WORKDAY.getValue());
        }

        //所用工作日大于总工作日
        if (usedWorkDay > totalWorkDay) {
            //发文超期
            if(sign.getProcessState() < Constant.SignProcessState.END_DIS.getValue()){
                sign.setIsLightUp(Constant.signEnumState.DISPAOVER.getValue());
            }else{
                if ((sign.getProcessState() < Constant.SignProcessState.END_FILE.getValue()) ) {
                    //存档超期
                    sign.setIsLightUp(Constant.signEnumState.ARCHIVEOVER.getValue());
                    if (usedWorkDay > Constant.WORK_DAY_25) {
                        //超过25个工作日未存档
                        sign.setIsLightUp(Constant.signEnumState.OVER25WORKDAYARCHIVE.getValue());
                    }
                }
            }
        }
    }
}
