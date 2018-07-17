package cs.domain.expert;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "cs_expert_condition")
@DynamicUpdate(true)
public class ExpertSelCondition extends ExpertSelConditionBase{

    /**
     * 专家评审方案
     */
    @ManyToOne
    @JoinColumn(name = "expertReviewId")
    private ExpertReview expertReview;

    public ExpertReview getExpertReview() {
        return expertReview;
    }

    public void setExpertReview(ExpertReview expertReview) {
        this.expertReview = expertReview;
    }
}
