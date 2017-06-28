package cs.domain.expert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cs.domain.DomainBase;

/**
 * 专家类型
 * @author MCL
 *@date 2017年6月17日 下午3:04:03 
 */
@Entity
@Table(name="cs_expert_type")
public class ExpertType extends DomainBase{
	
	@Id
	private String id;

	@Column(columnDefinition="INTEGER")
	private  Integer seqNum;	//序号（序号，一个专家有多个专业）
	
	@Column(columnDefinition="VARCHAR(300)")
	private String expertType;//专家类型
	
	@Column(columnDefinition="VARCHAR(200)")
	private String maJorBig;//突出专业（大类）
	
	@Column(columnDefinition="VARCHAR(200)")
	private String maJorSmall;//突出专业（小类）
	
	 //抽取专家关系（多对一）
    @ManyToOne
    @JoinColumn(name = "expertID")
    private Expert expert;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExpertType() {
		return expertType;
	}

	public void setExpertType(String expertType) {
		this.expertType = expertType;
	}

	public String getMaJorBig() {
		return maJorBig;
	}

	public void setMaJorBig(String maJorBig) {
		this.maJorBig = maJorBig;
	}

	public String getMaJorSmall() {
		return maJorSmall;
	}

	public void setMaJorSmall(String maJorSmall) {
		this.maJorSmall = maJorSmall;
	}

	public Expert getExpert() {
		return expert;
	}

	public void setExpert(Expert expert) {
		this.expert = expert;
	}

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }
}
