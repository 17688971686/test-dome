package cs.domain.history;

import cs.domain.expert.ExpertSelConditionBase;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "cs_his_expert_condition")
@DynamicUpdate(true)
public class ExpertSelConditionHis extends ExpertSelConditionBase {

    @Column(columnDefinition = "VARCHAR(64)")
    private String expertReviewId;

    public String getExpertReviewId() {
        return expertReviewId;
    }

    public void setExpertReviewId(String expertReviewId) {
        this.expertReviewId = expertReviewId;
    }
}
