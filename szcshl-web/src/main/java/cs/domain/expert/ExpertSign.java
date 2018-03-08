package cs.domain.expert;

import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.project.SignBranchID;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ldm on 2018/1/19.
 * 专家评审过的项目视图
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "V_EXPERT_PROJECT")
@IdClass(ExpertSignId.class)
public class ExpertSign {

    @Id
    @Column(name="expertId")
    private String expertId;
    @Id
    @Column(name="signid")
    private String signid;

    @Column
    private String expertName;
    @Column
    private String projectname;
    @Column
    private String reviewstage;
    @Column
    private String builtcompanyname;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date signdate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date reviewDate;

    @Column
    private String processInstanceId;

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public String getExpertName() {
        return expertName;
    }

    public void setExpertName(String expertName) {
        this.expertName = expertName;
    }

    public String getSignid() {
        return signid;
    }

    public void setSignid(String signid) {
        this.signid = signid;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getReviewstage() {
        return reviewstage;
    }

    public void setReviewstage(String reviewstage) {
        this.reviewstage = reviewstage;
    }

    public String getBuiltcompanyname() {
        return builtcompanyname;
    }

    public void setBuiltcompanyname(String builtcompanyname) {
        this.builtcompanyname = builtcompanyname;
    }

    public Date getSigndate() {
        return signdate;
    }

    public void setSigndate(Date signdate) {
        this.signdate = signdate;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }


}
