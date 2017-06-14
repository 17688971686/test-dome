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
}