package cs.domain.expert;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 专家评审方案
 *
 * @author Administrator
 */
@Entity
@Table(name = "cs_expert_review")
@DynamicUpdate(true)
public class ExpertReview extends ExpertReviewBase {
    /**
     * 抽取条件（一对多）
     */
    @OneToMany(mappedBy = "expertReview", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ExpertSelCondition> expertSelConditionList;

    /**
     * 抽取的专家信息（一对多）
     */
    @OneToMany(mappedBy = "expertReview", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ExpertSelected> expertSelectedList;

    public List<ExpertSelCondition> getExpertSelConditionList() {
        return expertSelConditionList;
    }

    public void setExpertSelConditionList(List<ExpertSelCondition> expertSelConditionList) {
        this.expertSelConditionList = expertSelConditionList;
    }

    public List<ExpertSelected> getExpertSelectedList() {
        return expertSelectedList;
    }

    public void setExpertSelectedList(List<ExpertSelected> expertSelectedList) {
        this.expertSelectedList = expertSelectedList;
    }

    public ExpertReview() {

    }
}
