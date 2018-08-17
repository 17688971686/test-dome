package cs.domain.history;

import cs.domain.expert.Expert;
import cs.domain.expert.ExpertSelectedBase;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 抽取的专家信息
 */
@Entity
@Table(name = "cs_his_expert_selected")
@DynamicUpdate(true)
public class ExpertSelectedHis extends ExpertSelectedBase {

    @Column(columnDefinition = "VARCHAR(64)")
    private String expertReviewId;

    //抽取专家关系（多对一）
    @ManyToOne
    @JoinColumn(name = "expertId")
    private Expert expert;


    public String getExpertReviewId() {
        return expertReviewId;
    }

    public void setExpertReviewId(String expertReviewId) {
        this.expertReviewId = expertReviewId;
    }

    public Expert getExpert() {
        return expert;
    }

    public void setExpert(Expert expert) {
        this.expert = expert;
    }
}
