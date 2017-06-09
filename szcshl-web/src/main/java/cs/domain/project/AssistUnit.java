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
    @Column(columnDefinition="VARCHAR(64)")
    private String unitName;

    /**
     * 单位简称
     */
    @Column(columnDefinition="VARCHAR(30)")
    private String unitShortName;

    /**
     * 电话号码
     */
    @Column(columnDefinition="VARCHAR(30)")
    private String phoneNum;

    /**
     * 企业地址
     */
    @Column(columnDefinition="VARCHAR(30)")
    private String address;

    /**
     * 序号
     */
    @Column(columnDefinition="Integer")
    private String unitSort;

    /**
     * 评审计划
     */
    @ManyToMany(fetch = FetchType.LAZY,mappedBy="assistUnitList")
    private List<AssistPlan> assistPlanList;

    /**
     * 协审单位人员
     */
    @OneToMany(fetch = FetchType.LAZY,mappedBy="assistUnit")
    private List<AssistUnitUser> assistUnitUserList;

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

    public String getUnitSort() {
        return unitSort;
    }

    public void setUnitSort(String unitSort) {
        this.unitSort = unitSort;
    }

    public List<AssistPlan> getAssistPlanList() {
        return assistPlanList;
    }

    public void setAssistPlanList(List<AssistPlan> assistPlanList) {
        this.assistPlanList = assistPlanList;
    }

    public List<AssistUnitUser> getAssistUnitUserList() {
        return assistUnitUserList;
    }

    public void setAssistUnitUserList(List<AssistUnitUser> assistUnitUserList) {
        this.assistUnitUserList = assistUnitUserList;
    }
}
