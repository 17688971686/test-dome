package cs.domain.project;

import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

/**
 * 协审单位
 */
@Entity
@Table(name = "cs_as_unit")
@DynamicUpdate(true)
public class AssistUnit extends DomainBase {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 单位名称
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String unitName;

    /**
     * 单位简称
     */
    @Column(columnDefinition = "VARCHAR(30)")
    private String unitShortName;

    /**
     * 电话号码
     */
    @Column(columnDefinition = "VARCHAR(30)")
    private String phoneNum;

    /**
     * 传真
     */
    @Column(columnDefinition = "VARCHAR(30)")
    private String fax;

    /**
     * 抽中次数
     */
    @Column(columnDefinition = "Integer")
    private Integer drawCount;

    /**
     * 企业地址
     */
    @Column(columnDefinition = "VARCHAR(30)")
    private String address;

    /**
     * 序号
     */
    @Column(columnDefinition = "Integer")
    private Integer unitSort;
    
    @Column(columnDefinition="VARCHAR(30)")
    private String contactName;//联系人名称
    
    @Column(columnDefinition="VARCHAR(30)")
    private String contactPhone;//联系人手机号
    
    
    @Column(columnDefinition="VARCHAR(30)")
    private String principalName;//负责人名称

    @Column(columnDefinition="VARCHAR(30)")
    private String principalPhone;//负责人手机号
 
    /**
     * 评审计划
     */
    @ManyToMany(mappedBy = "assistUnitList")
    private List<AssistPlan> assistPlanList;


    /**
     * 是否上轮轮空
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String isLastUnSelected;

   

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitShortName() {
        return unitShortName;
    }

    public void setUnitShortName(String unitShortName) {
        this.unitShortName = unitShortName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getUnitSort() {
        return unitSort;
    }

    public void setUnitSort(Integer unitSort) {
        this.unitSort = unitSort;
    }

    public List<AssistPlan> getAssistPlanList() {
        return assistPlanList;
    }

    public void setAssistPlanList(List<AssistPlan> assistPlanList) {
        this.assistPlanList = assistPlanList;
    }


    public String getIsLastUnSelected() {
        return isLastUnSelected;
    }

    public void setIsLastUnSelected(String isLastUnSelected) {
        this.isLastUnSelected = isLastUnSelected;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Integer getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(Integer drawCount) {
        this.drawCount = drawCount;
    }

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getPrincipalPhone() {
		return principalPhone;
	}

	public void setPrincipalPhone(String principalPhone) {
		this.principalPhone = principalPhone;
	}
    
	
    
}
