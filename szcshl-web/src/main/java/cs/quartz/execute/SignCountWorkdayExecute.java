package cs.quartz.execute;

import cs.common.Constant;
import cs.common.utils.Validate;
import cs.domain.project.Sign;
import cs.domain.sys.Log;
import cs.domain.sys.Workday;
import cs.model.project.ProjectStopDto;
import cs.quartz.unit.QuartzUnit;
import cs.service.project.ProjectStopService;
import cs.service.project.SignService;
import cs.service.sys.LogService;
import cs.service.sys.WorkdayService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.Date;
import java.util.List;

import static cs.common.Constant.WORK_DAY_25;
import static cs.common.Constant.WORK_DAY_30;

/**
 * 计算工作日的执行类
 *
 * @author MCL
 * @date 2017年7月4日 下午4:41:43
 */
@Service
public class SignCountWorkdayExecute implements Job {
    private static Logger logger = Logger.getLogger(SignCountWorkdayExecute.class);
    @Autowired
    private SignService signService;
    @Autowired
    private ProjectStopService projectStopService;
    @Autowired
    private WorkdayService workdayService;
    @Autowired
    private LogService logService;

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
        //添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setUserName(Constant.SUPER_USER);
        log.setBuninessId("");
        log.setModule(Constant.LOG_MODULE.QUARTZ.getValue()+"【工作日计算】" );
        //优先级别中等
        log.setLogLevel(Constant.EnumState.STOP.getValue());
        log.setLogger(this.getClass().getName()+".execute");
        try{
            logger.info("------------------ 工作日计算定时器 开始 ------------------");
            List<Workday> workdayList = null;
            // 1、查询正式签收的项目
            List<Sign> signList = signService.selectSignNotFinish();
            int total = 0;
            if (Validate.isList(signList)) {
                total = signList.size();
                //一次性查出近一年的调休记录，不用每次循环都查
                workdayList = workdayService.findWorkDayByNow();
            }
            for (int i=0;i<total;i++) {
                Sign sign = signList.get(i);
                //项目阶段都没有的，直接过滤掉
                if(!Validate.isString(sign.getReviewstage())){
                    continue;
                }
                //1、如果项目未正式签收，不用计算工作日
                if(null == sign.getSigndate() || !Constant.EnumState.YES.getValue().equals(sign.getIssign())){
                    continue;
                }
                //没有评审天数的，重新赋值
                if(sign.getReviewdays() == null){
                    Float reviewsDays = signService.getReviewDays(sign.getReviewstage());
                    if (reviewsDays > 0) {
                        sign.setSurplusdays(reviewsDays-1);
                        sign.setTotalReviewdays(reviewsDays);
                        sign.setReviewdays(1f);
                    }
                }
                //2、如果没有亮灯的，则设置为默认亮灯状态,如果目前是暂停状态，则改为进行中状态
                if (!Validate.isString(sign.getIsLightUp()) || Constant.EnumState.STOP.equals(sign.getSignState())
                        || Constant.signEnumState.PAUSE.getValue().equals(sign.getIsLightUp())) {
                    sign.setIsLightUp(Constant.signEnumState.PROCESS.getValue());
                }
                //默认是在办
                sign.setIsLightUp(Constant.signEnumState.PROCESS.getValue());
                boolean isCountWorkDay = (null == sign.getProcessState() || sign.getProcessState() < Constant.SignProcessState.END_DIS_NUM.getValue());
                float usedWorkDay = 0f,totalDays = 0f;
                //未发文的，才计算剩余工作日
                if(isCountWorkDay){
                    //3、通过收文ID查找 项目暂停情况,并计算项目总共暂停了几个工作日
                    List<ProjectStopDto> projectStopList = projectStopService.findProjectStopBySign(sign.getSignid());
                    float stopWorkday = 0;
                    for (ProjectStopDto ps : projectStopList) {
                        //记录实际暂停的工作日
                        stopWorkday += ps.getPausedays() == null?0:ps.getPausedays();
                    }
                    //4、计算从正式签收到当前时间的工作日，再减掉暂停的工作日，并设置相对应的状态
                    usedWorkDay = QuartzUnit.countWorkday(workdayList,sign.getSigndate()) - stopWorkday;
                    //剩余评审天数 = 总评审天数-已用评审天数
                    sign.setSurplusdays(sign.getTotalReviewdays() - usedWorkDay);
                    //评审天数
                    sign.setReviewdays(usedWorkDay);
                    totalDays = sign.getTotalReviewdays();
                }else{
                    Date disPatchDate = Validate.isObject(sign.getExpectdispatchdate())?sign.getExpectdispatchdate():new Date();
                    usedWorkDay = QuartzUnit.countWorkday(workdayList,disPatchDate);
                    totalDays = WORK_DAY_30;
                }
                //计算更新亮灯状态
                updateLightUpState(sign, usedWorkDay, totalDays,isCountWorkDay);

                sign.setSignState(Constant.signEnumState.PROCESS.getValue());
            }
            signService.bathUpdate(signList);
            log.setMessage("完成项目工作日计算！");
            log.setLogCode(Constant.MsgCode.OK.getValue());
            log.setResult(Constant.EnumState.YES.getValue());
            logger.info("------------------ 工作日计算定时器 结束 ------------------");
        }catch (Exception e){
            log.setMessage("工作日计算异常："+e.getMessage());
            log.setLogCode(Constant.MsgCode.ERROR.getValue());
            log.setResult(Constant.EnumState.NO.getValue());
            logger.info("工作日计算定时器异常："+e.getMessage());
        }
        logService.save(log);

    }

    /**
     * 修改提示灯的状态
     *
     * @param sign         收文
     * @param usedWorkDay  已用工作日
     * @param totalWorkDay 总共工作日
     * @param isCountWorkDay  是否是计算剩余工作日
     */
    public void updateLightUpState(Sign sign, float usedWorkDay, float totalWorkDay,boolean isCountWorkDay) {
        /**
        NOLIGHT("0"),                 //不显示
        PROCESS("1"),                   //在办
        PAUSE("4"),                     //暂停
        UNDER3WORKDAY("5"),             //少于三个工作日
        DISPAOVER("6"),                 //发文超期
        OVER25WORKDAYARCHIVE("7"),      //超过25个工作日未存档
        ARCHIVEOVER("8");               //存档超期
        **/
        //计算剩余工作日
        if(isCountWorkDay){
            //少于3个工作日
            if ((totalWorkDay - usedWorkDay) <=  Constant.WORK_DAY_3) {
                sign.setIsLightUp(Constant.signEnumState.UNDER3WORKDAY.getValue());
            }
            if (usedWorkDay >= totalWorkDay && sign.getProcessState() < Constant.SignProcessState.END_DIS.getValue()){
                sign.setIsLightUp(Constant.signEnumState.DISPAOVER.getValue());
            }
        //计算存档(发文到存档，有30天日期)
        }else{
            sign.setDaysafterdispatch(usedWorkDay);
            if(usedWorkDay > WORK_DAY_30){
                sign.setIsLightUp(Constant.signEnumState.ARCHIVEOVER.getValue());
            }else if(usedWorkDay > WORK_DAY_25){
                sign.setIsLightUp(Constant.signEnumState.OVER25WORKDAYARCHIVE.getValue());
            }
        }
    }
}
