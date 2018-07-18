package cs.domain.history;

import cs.domain.expert.ExpertSelectedBase;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 抽取的专家信息
 */
@Entity
@Table(name = "cs_his_expert_selected")
@DynamicUpdate(true)
public class ExpertSelectedHis extends ExpertSelectedBase {

    //抽取评审方案（多对一）
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
