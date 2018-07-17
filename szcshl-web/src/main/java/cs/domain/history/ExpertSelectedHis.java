package cs.domain.history;

import cs.domain.expert.ExpertSelected;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 抽取的专家信息
 */
@Entity
@Table(name = "cs_his_expert_selected")
@DynamicUpdate(true)
public class ExpertSelectedHis extends ExpertSelected{

}
