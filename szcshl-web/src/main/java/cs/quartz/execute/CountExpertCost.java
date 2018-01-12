package cs.quartz.execute;

import cs.common.Constant;
import cs.common.FlowConstant;
import cs.common.utils.Arith;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertReview_;
import cs.domain.expert.ExpertSelected;
import cs.domain.sys.Log;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.service.expert.ExpertReviewService;
import cs.service.sys.LogService;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统自动计算发文环节，项目未发放专家评审费定时器
 * Created by ldm on 2017/12/4.
 */
@Service
public class CountExpertCost implements Job {
    private static Logger logger = Logger.getLogger(CountExpertCost.class);

    @Autowired
    private LogService logService;
    @Autowired
    private ExpertReviewService expertReviewService;
    /*
     以下语句是查询超期未办理专家评审费发放的语句，只限制于项目发文环节和课题归档环节
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
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        List<ExpertReview> expertReviewList = null;
        //添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setUserName(Constant.SUPER_USER);
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
                    BigDecimal totalExpense = BigDecimal.ZERO;
                    BigDecimal totalTaxes = BigDecimal.ZERO;
                    List<Object[]> expCostList = expertReviewService.countExpertReviewCost(expertReview.getId(),DateUtils.converToString(expertReview.getReviewDate(),"yy-MM"));
                    int totalCost = Validate.isList(expCostList)?expCostList.size():0;
                    for(int n=0,m=expertReview.getExpertSelectedList().size();n<m;n++){
                        ExpertSelected sl = expertReview.getExpertSelectedList().get(n);
                        if(!Constant.EnumState.YES.getValue().equals(sl.getIsConfrim()) || !Constant.EnumState.YES.getValue().equals(sl.getIsJoin())){
                            continue;
                        }
                        if(totalCost > 0){
                            boolean isHaveCount = false;
                            for(int index=0;index<totalCost;index++){
                                Object[] costObj = expCostList.get(index);
                                if(sl.getExpert().getExpertID().equals(costObj[1].toString())){
                                    isHaveCount = true;
                                    BigDecimal cost = new BigDecimal(costObj[0]==null?"0":costObj[0].toString());
                                    sl.setReviewTaxes(Arith.countCost(Arith.safeAdd(sl.getReviewCost(),cost)));
                                }
                            }
                            //如果没有，计税还是当前金额计算
                            if(!isHaveCount){
                                sl.setReviewTaxes(Arith.countCost(sl.getReviewCost()));
                            }
                        }else{
                            sl.setReviewTaxes(Arith.countCost(sl.getReviewCost()));
                        }
                        //评审费
                        totalExpense = Arith.safeAdd(totalExpense,sl.getReviewCost());
                        //税
                        totalTaxes = Arith.safeAdd(totalTaxes,sl.getReviewTaxes());
                        //总费用
                        sl.setTotalCost(Arith.safeAdd(sl.getReviewCost(),sl.getReviewTaxes()));
                    }
                    //评审费
                    expertReview.setReviewCost(totalExpense);
                    //税
                    expertReview.setReviewTaxes(totalTaxes);
                    //总费用
                    expertReview.setTotalCost(Arith.safeAdd(totalExpense,totalTaxes));
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
    }
}
