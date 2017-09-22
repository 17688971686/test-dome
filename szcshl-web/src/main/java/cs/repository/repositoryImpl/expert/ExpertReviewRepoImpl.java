package cs.repository.repositoryImpl.expert;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertReview_;
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
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            expertReview.setReviewTitle("《" + sign.getProjectname() + sign.getReviewstage() + "》专家评审费");
        } else if (Constant.BusinessType.TOPIC.getValue().equals(businessType)) {
            TopicInfo topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(), businessId);
            expertReview.setReviewTitle("《" + topicInfo.getTopicName() + "》专家评审费");
        }
    }
}