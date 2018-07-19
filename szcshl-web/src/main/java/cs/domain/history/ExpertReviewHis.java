package cs.domain.history;

import cs.domain.expert.ExpertReviewBase;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 专家评审方案
 *
 * @author Administrator
 */
@Entity
@Table(name = "cs_his_expert_review")
@DynamicUpdate(true)
public class ExpertReviewHis extends ExpertReviewBase {

}
