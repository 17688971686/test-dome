package cs.domain.history;

import cs.domain.expert.ExpertReviewBase;
import cs.domain.expert.ExpertSelCondition;
import cs.domain.expert.ExpertSelected;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

/**
 * 专家评审方案
 *
 * @author Administrator
 */
@Entity
@Table(name = "cs_his_expert_review")
@DynamicUpdate(true)
public class ExpertReviewHis extends ExpertReviewBase {

    /**
     * 抽取条件（一对多）
     */
    @OneToMany(mappedBy = "expertReviewHis", fetch = FetchType.LAZY)
    private List<ExpertSelConditionHis> expertSelConditionHisList;

    /**
     * 抽取的专家信息（一对多）
     */
    @OneToMany(mappedBy = "expertReviewHis", fetch = FetchType.LAZY)
    private List<ExpertSelectedHis> expertSelectedHisList;

    public List<ExpertSelConditionHis> getExpertSelConditionHisList() {
        return expertSelConditionHisList;
    }

    public void setExpertSelConditionHisList(List<ExpertSelConditionHis> expertSelConditionHisList) {
        this.expertSelConditionHisList = expertSelConditionHisList;
    }

    public List<ExpertSelectedHis> getExpertSelectedHisList() {
        return expertSelectedHisList;
    }

    public void setExpertSelectedHisList(List<ExpertSelectedHis> expertSelectedHisList) {
        this.expertSelectedHisList = expertSelectedHisList;
    }
}
