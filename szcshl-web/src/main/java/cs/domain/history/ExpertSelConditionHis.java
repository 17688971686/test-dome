package cs.domain.history;

import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertSelConditionBase;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cs_his_expert_condition")
@DynamicUpdate(true)
public class ExpertSelConditionHis extends ExpertSelConditionBase {

    @ManyToOne
    @JoinColumn(name = "expertReviewId")
    private ExpertReviewHis expertReviewHis;

    public ExpertReviewHis getExpertReviewHis() {
        return expertReviewHis;
    }

    public void setExpertReviewHis(ExpertReviewHis expertReviewHis) {
        this.expertReviewHis = expertReviewHis;
    }
}
