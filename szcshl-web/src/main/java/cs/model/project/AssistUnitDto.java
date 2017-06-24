package cs.model.project;

import cs.model.BaseDto;

import javax.persistence.Column;
import java.util.List;


/**
 * Description: 协审单位 页面数据模型
 * author: ldm
 * Date: 2017-6-6 14:49:58
 */
public class AssistUnitDto extends BaseDto {

    private String id;
    private String unitName;
    private String unitShortName;
    private String phoneNum;
    private String address;
    private Integer unitSort;
    private String isLastUnSelected;
    private String fax;
    private Integer drawCount;
    private String contactTell;//联系人手机
    private String contactFax;//联系人传真
    
    private String isUse;//是否在用
    
    private String contactName;//联系人名称
    private String contactPhone;//联系人手机号
    private String principalName;//负责人名称
    private String principalPhone;//负责人手机号

    private List assistPlanList;

    public AssistUnitDto() {
    }

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

    public List getAssistPlanList() {
        return assistPlanList;
    }

    public void setAssistPlanList(List assistPlanList) {
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

	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	public String getContactTell() {
		return contactTell;
	}

	public void setContactTell(String contactTell) {
		this.contactTell = contactTell;
	}

	public String getContactFax() {
		return contactFax;
	}

	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}
    
	
}