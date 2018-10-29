package cs.domain.postdoctor;

import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 博士后基地管理
 * Created by zsl on 2017/9/4 0004.
 */
@Entity
@Table(name="cs_postdoctoral_base")
@DynamicUpdate(true)
public class PostdoctoralBase extends DomainBase {

    @Id
    private String id;

    /**
     * 基地名称
     */
    @Column(columnDefinition="VARCHAR(128)")
    private String baseName;

    /**
     * 成立时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date foundingTime;

    /**
     * 研究方向及领域
     */
    @Column(columnDefinition="VARCHAR(1000)")
    private String researchArea;

    /**
     * 基地负责人
     */
    @Column(columnDefinition="VARCHAR(100)")
    private String principalBase;


    /**
     * 日常管理人员
     */
    @Column(columnDefinition="VARCHAR(256)")
    private String dailyMananger;


    /**
     * 工作职责
     */
    @Column(columnDefinition="VARCHAR(1000)")
    private String workDuty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public Date getFoundingTime() {
        return foundingTime;
    }

    public void setFoundingTime(Date foundingTime) {
        this.foundingTime = foundingTime;
    }

    public String getResearchArea() {
        return researchArea;
    }

    public void setResearchArea(String researchArea) {
        this.researchArea = researchArea;
    }

    public String getPrincipalBase() {
        return principalBase;
    }

    public void setPrincipalBase(String principalBase) {
        this.principalBase = principalBase;
    }

    public String getDailyMananger() {
        return dailyMananger;
    }

    public void setDailyMananger(String dailyMananger) {
        this.dailyMananger = dailyMananger;
    }

    public String getWorkDuty() {
        return workDuty;
    }

    public void setWorkDuty(String workDuty) {
        this.workDuty = workDuty;
    }
}
