package cs.quartz.execute;

import cs.common.constants.Constant;
import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertReview;
import cs.domain.sys.Log;
import cs.service.expert.ExpertReviewService;
import cs.service.project.ProjectStopService;
import cs.service.sys.LogService;
import cs.service.sys.WorkdayService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.Date;
import java.util.List;

import static cs.common.constants.SysConstants.SUPER_ACCOUNT;

/**
 * 系统自动计算发文环节，项目未发放专家评审费定时器
 * Created by ldm on 2017/12/4.
 */
@Component
public class CountExpertExecute implements Job {
    private static Logger logger = Logger.getLogger(CountExpertExecute.class);

    /*
    @Autowired
    private LogService logService;
    @Autowired
    private ExpertReviewService expertReviewService;*/
    /*
     以下语句是查询超期未办理专家评审费发放的语句，只限制于项目发文环节
     * SELECT *
     FROM CS_EXPERT_REVIEW er
     WHERE     ER.PAYDATE IS NULL
     AND (ER.REVIEWDATE IS NOT NULL AND (SYSDATE - ER.REVIEWDATE) > 1)
     AND (CASE WHEN ER.BUSINESSTYPE = 'SIGN'
     THEN (select count(pt.taskid) from V_RU_PROCESS_TASK pt where pt.NODEDEFINEKEY = 'SIGN_FW' and pt.BUSINESSKEY =er.businessId )
     WHEN ER.BUSINESSTYPE = 'TOPIC'
     THEN (select count(rt.taskid) from V_RU_TASK rt where rt.NODEKEY = 'TOPIC_ZLGD' and rt.BUSINESSKEY =er.businessId )
     ELSE 0
     END) > 0
     */

     /**
     * @param context
     * @throws JobExecutionException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        logger.info("---------------------- 计算评审费的定时器开始执行 ----------------------");
        LogService logService = (LogService) context.getMergedJobDataMap().get("logService");
        ExpertReviewService expertReviewService = (ExpertReviewService) context.getMergedJobDataMap().get("expertReviewService");

        List<ExpertReview> expertReviewList = null;
        //添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setUserName(SUPER_ACCOUNT);
        log.setBuninessId("");
        log.setModule(Constant.LOG_MODULE.QUARTZ.getValue()+"【专家评审费】" );
        //优先级别中等
        log.setLogLevel(Constant.EnumState.STOP.getValue());
        log.setLogger(this.getClass().getName()+".execute");
        try{
            expertReviewList = expertReviewService.queryUndealReview();
            if(Validate.isList(expertReviewList)){
                logger.info("开始处理专家评审费发放："+ DateUtils.converToString(new Date(),"yyyy-MM-dd HH:mm:ss"));
                for(int i=0,l=expertReviewList.size();i<l;i++){
                    ExpertReview expertReview = expertReviewList.get(i);
                    //如果没有评审会日期，则不予处理
                    if(null == expertReview.getReviewDate() || !Validate.isList(expertReview.getExpertSelectedList())){
                        continue;
                    }
                    expertReviewService.countReviewExpense(expertReview);
                    //付款日期
                    expertReview.setPayDate(DateUtils.converToDate(DateUtils.converToString(new Date(),null),null));
                    expertReview.setState(Constant.EnumState.YES.getValue());
                    //保存
                    expertReviewService.save(expertReview);
                }
                logger.info("完成处理专家评审费发放："+ DateUtils.converToString(new Date(),"yyyy-MM-dd HH:mm:ss"));
                log.setMessage("完成专家评审费发放");
            }else{
                log.setMessage("没有需要处理的评审费信息！");
            }
            log.setLogCode(Constant.MsgCode.OK.getValue());
            log.setResult(Constant.EnumState.YES.getValue());
        }catch (Exception e){
            log.setLogCode(Constant.MsgCode.ERROR.getValue());
            log.setResult(Constant.EnumState.NO.getValue());
            log.setMessage(e.getMessage());
            logger.info("专家评审费统计异常："+ e.getMessage());
        }
        logService.save(log);
        logger.info("---------------------- 计算评审费的定时器执行结束 ----------------------");
    }
}
