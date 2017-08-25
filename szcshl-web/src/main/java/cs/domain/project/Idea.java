package cs.domain.project;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

import cs.domain.DomainBase;
import org.hibernate.annotations.GenericGenerator;


/**
 * 审批意见表
 * @author MCL
 * @date 2017年6月6日 上午9:46:59 
 */

@Entity
@Table(name="cs_idea")
@DynamicUpdate(true)
public class Idea extends DomainBase{

	@Id
	@GeneratedValue(generator= "ideaGenerator")
	@GenericGenerator(name= "ideaGenerator",strategy = "uuid")
	private String ideaID;  //审批意见id
	
	@Column(columnDefinition="VARCHAR(2000)")
	private String ideaContent;  //意见内容
	
	@Column(columnDefinition="VARCHAR(2)")
	private String ideaType;  //意见类型（1：表示个人常用意见）

	public String getIdeaID() {
		return ideaID;
	}

	public void setIdeaID(String ideaID) {
		this.ideaID = ideaID;
	}

	public String getIdeaContent() {
		return ideaContent;
	}

	public void setIdeaContent(String ideaContent) {
		this.ideaContent = ideaContent;
	}

	public String getIdeaType() {
		return ideaType;
	}

	public void setIdeaType(String ideaType) {
		this.ideaType = ideaType;
	}
	
	
	

}
