package cs.repository.repositoryImpl.expert;

import cs.common.Constant;
import cs.common.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertReview_;
import cs.domain.expert.ExpertSelected;
import cs.domain.expert.ExpertSelected_;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.domain.project.WorkProgram_;
import cs.domain.topic.TopicInfo;
import cs.domain.topic.TopicInfo_;
import cs.domain.topic.WorkPlan_;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertReviewDto;
import cs.model.expert.ExpertSelectedDto;
import cs.repository.AbstractRepository;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.topic.TopicInfoRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description: 专家评审 数据操作实现类
 * author: ldm
 * Date: 2017-5-17 14:02:25
 */
@Repository
public class ExpertReviewRepoImpl extends AbstractRepository<ExpertReview, String> implements ExpertReviewRepo {
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private TopicInfoRepo topicInfoRepo;

    /**
     * 根据业务类型，更新评审会时间
     *
     * @param businessId
     * @param businessType
     * @param rbDate
     */
    @Override
    public void updateReviewDate(String businessId, String businessType, Date rbDate) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update cs_expert_review erv set erv." + ExpertReview_.reviewDate.getName() + " = to_date(:rbDate,'yyyy-mm-dd') ");
        hqlBuilder.setParam("rbDate", DateUtils.converToString(rbDate, "yyyy-MM-dd"));
        hqlBuilder.append(" where erv." + ExpertReview_.reviewDate.getName() + " is null ");
        hqlBuilder.append(" and erv." + ExpertReview_.businessId.getName() + " = (");
        //如果是项目签收工作方案
        if (Constant.BusinessType.SIGN_WP.getValue().equals(businessType)) {
            hqlBuilder.append(" select s." + Sign_.signid.getName() + " from cs_sign s where s." + Sign_.signid.getName() + " = (");
            hqlBuilder.append(" select w.signid from cs_work_program w where w." + WorkProgram_.id.getName() + " =:wpID ) ");
            hqlBuilder.setParam("wpID", businessId);
        } else if (Constant.BusinessType.TOPIC_WP.getValue().equals(businessType)) {
            hqlBuilder.append(" select s." + TopicInfo_.id.getName() + " from cs_topic_info s where s." + TopicInfo_.id.getName() + " = (");
            hqlBuilder.append(" select w.topid from cs_topic_workplan w where w." + WorkPlan_.id.getName() + " =:wpID ) ");
            hqlBuilder.setParam("wpID", businessId);
        }
        hqlBuilder.append(" ) ");

        executeSql(hqlBuilder);

    }

    /**
     * 根据业务ID查询评审方案信息
     *
     * @param businessId
     * @return
     */
    @Override
    public ExpertReview findByBusinessId(String businessId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(ExpertReview_.businessId.getName(), businessId));
        List<ExpertReview> list = criteria.list();
        if (Validate.isList(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public ExpertReviewDto formatReview(ExpertReview expertReview) {
        if (expertReview != null) {
            ExpertReviewDto expertReviewDto = new ExpertReviewDto();
            BeanCopierUtils.copyProperties(expertReview, expertReviewDto);
            List<ExpertSelectedDto> dtoList = new ArrayList<>();
            //选取的专家
            if (Validate.isList(expertReview.getExpertSelectedList())) {
                expertReview.getExpertSelectedList().forEach(el -> {
                    ExpertSelectedDto dto = new ExpertSelectedDto();
                    BeanCopierUtils.copyProperties(el, dto);
                    //专家信息
                    if (el.getExpert() != null) {
                        ExpertDto expertDto = new ExpertDto();
                        BeanCopierUtils.copyProperties(el.getExpert(), expertDto);
                        dto.setExpertDto(expertDto);
                    }
                    dtoList.add(dto);
                });
                expertReviewDto.setExpertSelectedDtoList(dtoList);
            }
            return expertReviewDto;
        }
        return null;
    }

    /**
     * 根据业务ID判断是否有评审专家
     * 是否有专家评审费
     *
     * @param businessId
     * @return
     */
    @Override
    public boolean isHaveEPReviewCost(String businessId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("SELECT count(eps.id) FROM cs_expert_selected eps WHERE eps." + ExpertSelected_.isConfrim.getName() + " =:isConfrim ");
        sqlBuilder.setParam("isConfrim", Constant.EnumState.YES.getValue());
        sqlBuilder.append(" and eps." + ExpertSelected_.isJoin.getName() + " =:isJoin ");
        sqlBuilder.setParam("isJoin", Constant.EnumState.YES.getValue());
        sqlBuilder.append(" and eps.expertReviewId = ( SELECT epr.id FROM cs_expert_review epr WHERE ");
        sqlBuilder.append(" epr." + ExpertReview_.businessId.getName() + " =:businessId )");
        sqlBuilder.setParam("businessId", businessId);
        int resultInt = returnIntBySql(sqlBuilder);
        return (resultInt > 0) ? true : false;
    }

    /**
     * 根据业务ID判断是否完成专家评分
     *
     * @param businessId
     * @return
     */
    @Override
    public boolean isFinishEPGrade(String businessId) {
        //有评审专家，才要判断
        if (isHaveEPReviewCost(businessId)) {
            HqlBuilder sqlBuilder = HqlBuilder.create();
            sqlBuilder.append("SELECT count(eps.id) FROM cs_expert_selected eps WHERE eps." + ExpertSelected_.isConfrim.getName() + " =:isConfrim ");
            sqlBuilder.setParam("isConfrim", Constant.EnumState.YES.getValue());
            sqlBuilder.append(" and eps." + ExpertSelected_.isJoin.getName() + " =:isJoin ");
            sqlBuilder.setParam("isJoin", Constant.EnumState.YES.getValue());
            sqlBuilder.append(" and (eps." + ExpertSelected_.score.getName() + " is null or eps." + ExpertSelected_.score.getName() + " = 0 ) ");
            sqlBuilder.append(" and eps.expertReviewId = ( SELECT epr.id FROM cs_expert_review epr WHERE ");
            sqlBuilder.append(" epr." + ExpertReview_.businessId.getName() + " =:businessId )");
            sqlBuilder.setParam("businessId", businessId);

            int resultInt = returnIntBySql(sqlBuilder);
            return (resultInt > 0) ? false : true;
        }
        return true;
    }

    /**
     * 初始化评审方案标题
     *
     * @param expertReview
     * @param businessId
     * @param businessType
     */
    @Override
    public void initReviewTitle(ExpertReview expertReview, String businessId, String businessType) {
        if (Constant.BusinessType.SIGN.getValue().equals(businessType)) {
            Sign sign = signRepo.findById(Sign_.signid.getName(), businessId);
            expertReview.setReviewTitle("《" + sign.getProjectname() + sign.getReviewstage()+"》专家评审费发放表");//专家评审费
        } else if (Constant.BusinessType.TOPIC.getValue().equals(businessType)) {
            TopicInfo topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(), businessId);
            expertReview.setReviewTitle("《" + topicInfo.getTopicName() + "》专家评审费发放表");
        }
    }


    /**
     * 查询专家评审费超期发放的信息（停用）
     * @param businessType
     * @return
     */
    @Override
    @Deprecated
    public List<ExpertReview> findReviewOverTime(String businessType) {

        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + ExpertReview.class.getSimpleName() + " where " + ExpertReview_.state.getName() + "<>:state");

       if(Validate.isString(businessType)){
           hqlBuilder.append(" and " + ExpertReview_.businessType.getName() + "=:businessType").setParam("businessType" , businessType);
       }
        hqlBuilder.append(" or " + ExpertReview_.state.getName() + " is null");
        hqlBuilder.setParam("state" , Constant.EnumState.YES.getValue());
        List<ExpertReview> expertReviewList = this.findByHql(hqlBuilder);

        List<ExpertReview> resultList = new ArrayList<>();
        if(expertReviewList!=null && expertReviewList.size()>0){
            for(ExpertReview e : expertReviewList){
                if(DateUtils.daysBetween(e.getReviewDate(),new Date())>1){
                    resultList.add(e);
                }
            }
        }
        return resultList;
    }

    /**
     * 判断评审方式是否为空（即没有专家抽取条件，也没有抽取专家）
     * @param businessId
     * @return
     */
    @Override
    public boolean isReviewIsEmpty(String businessId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(ExpertReview_.businessId.getName(),businessId));
        ExpertReview expertReview = (ExpertReview) criteria.uniqueResult();
        if(expertReview == null || (!Validate.isList(expertReview.getExpertSelectedList())
        && !Validate.isList(expertReview.getExpertSelConditionList()))){
            return true;
        }
        return false;
    }

    /*@Override
    public List<Object[]> countExpertReviewCost(String expertReviewId, String month) {
        List<Object[]> experReviewCosts = null;
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("SELECT SUM (veph.REVIEWCOST) ecost, veph.EXPERTID FROM V_EXPERT_PAY_HIS veph ");
        hqlBuilder.append(" WHERE veph.PAYDATE = :month ").setParam("month",month);
        hqlBuilder.append(" AND veph.EXPERTID IN (SELECT EXPERTID FROM CS_EXPERT_SELECTED es ");
        hqlBuilder.append(" WHERE es.EXPERTREVIEWID = :expertReviewId ").setParam("expertReviewId",expertReviewId);
        hqlBuilder.append(" AND es.ISCONFRIM =:isconfirm AND es.isJoin =:isjoin ) ");
        hqlBuilder.setParam("isconfirm", Constant.EnumState.YES.getValue());
        hqlBuilder.setParam("isjoin", Constant.EnumState.YES.getValue());
        hqlBuilder.append(" GROUP BY veph.EXPERTID ");
        experReviewCosts = getObjectArray(hqlBuilder);
        return experReviewCosts;
    }*/

    /**
     * 查询在发文环节，或者课题归档环节，未处理的专家评审费信息
     * @return
     */
    @Override
    public List<ExpertReview> queryUndealReview() {
        Object[] values = new Object[4];
        Type[] types = new Type[4];
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" ({alias}.REVIEWDATE IS NOT NULL AND (SYSDATE - {alias}.REVIEWDATE) > 1) ");
        sqlBuffer.append(" AND (CASE WHEN {alias}.BUSINESSTYPE = ? ");
        values[0] = Constant.BusinessType.SIGN.getValue();
        types[0] = StringType.INSTANCE;
        sqlBuffer.append(" THEN (select count(pt.taskid) from V_RU_PROCESS_TASK pt where pt.NODEDEFINEKEY = ? and pt.BUSINESSKEY ={alias}.businessId and pt.TASKSTATE = '1') ");
        values[1] = FlowConstant.FLOW_SIGN_FW;
        types[1] = StringType.INSTANCE;
        sqlBuffer.append(" WHEN {alias}.BUSINESSTYPE = ? ");
        values[2] = Constant.BusinessType.TOPIC.getValue();
        types[2] = StringType.INSTANCE;
        sqlBuffer.append(" THEN (select count(rt.taskid) from V_RU_TASK rt where rt.NODEKEY = ? and rt.BUSINESSKEY ={alias}.businessId and rt.TASKSTATE = '1' ) ");
        sqlBuffer.append(" ELSE 0  END) > 0 ");
        values[3] = FlowConstant.TOPIC_ZLGD;
        types[3] = StringType.INSTANCE;

        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.isNull(ExpertReview_.payDate.getName()));
        criteria.add(Restrictions.sqlRestriction(sqlBuffer.toString(),values,types));
        List<ExpertReview> expertReviewList = criteria.list();
        expertReviewList.forEach(er->{
            List<ExpertSelected> unconfirmList = new ArrayList<>();
            //只筛选出确认过的
            er.getExpertSelectedList().forEach(sl ->{
                if(!Constant.EnumState.YES.getValue().equals(sl.getIsConfrim()) || !Constant.EnumState.YES.getValue().equals(sl.getIsJoin())){
                    unconfirmList.add(sl);
                }
            });
            er.getExpertSelectedList().removeAll(unconfirmList);
        });
        return criteria.list();
    }

    /**
     * 保存专家评审费发放打印方案
     * @param expertSelectedDto
     * @return
     */
    @Override
    @Transactional
    public void saveSplit(ExpertSelectedDto expertSelectedDto) {

        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("update " + ExpertSelected.class.getSimpleName() + " set " + ExpertSelected_.isSplit.getName() + "=:isSplit ");
        hqlBuilder.append(" , " + ExpertSelected_.oneCost.getName() + "=:oneCost " );
        hqlBuilder.append(" where " + ExpertSelected_.id.getName() + "=:id");
        hqlBuilder.setParam("isSplit" , expertSelectedDto.getIsSplit());
        hqlBuilder.setParam("oneCost" , expertSelectedDto.getOneCost());
        hqlBuilder.setParam("id" , expertSelectedDto.getId());

        executeHql(hqlBuilder);

    }
}