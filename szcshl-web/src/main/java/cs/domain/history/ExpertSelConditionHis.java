package cs.domain.history;

import cs.domain.expert.ExpertSelCondition;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cs_his_expert_condition")
@DynamicUpdate(true)
public class ExpertSelConditionHis extends ExpertSelCondition{


}
