package cs.domain.expert;

import cs.domain.DomainBase;

import javax.persistence.*;
import java.util.List;

/**
 * 新的拟聘请专家名单
 * Created by Administrator on 2018/3/18.
 */
@Entity
@Table(name = "cs_expert_new_info")
public class ExpertNewInfo extends DomainBase {
    @Id
    private String expertNewInfoId;//id

    @Column(columnDefinition = "varchar(65)")
    private String expertID;//专家ID

    @Column(columnDefinition = "varchar(32) NOT NULL")
    private String name;//专家姓名

    @Column(columnDefinition = "varchar(128) ")
    private String comPany;//工作单位

    @Column(columnDefinition = "varchar(100) ")
    private String job;//现任职位

    @Column(columnDefinition = "varchar(255) ")
    private String post;//职称

    @Column(columnDefinition = "varchar(50) ")
    private String userPhone;//手机号码

    @Column(columnDefinition = "varchar(255) ")
    private String remark;//备注
    /**
     * 业务ID，如项目签收工作方案ID，课题工作方案ID等
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String businessId;
    /**
     * 是否函评
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String isLetterRw;
    @OneToMany(targetEntity=ExpertNewType.class ,mappedBy = "expertNewInfo", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ExpertNewType> expertType;//专家类型

    public String getExpertNewInfoId() {
        return expertNewInfoId;
    }

    public void setExpertNewInfoId(String expertNewInfoId) {
        this.expertNewInfoId = expertNewInfoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComPany() {
        return comPany;
    }

    public void setComPany(String comPany) {
        this.comPany = comPany;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getIsLetterRw() {
        return isLetterRw;
    }

    public void setIsLetterRw(String isLetterRw) {
        this.isLetterRw = isLetterRw;
    }

    public List<ExpertNewType> getExpertType() {
        return expertType;
    }

    public void setExpertType(List<ExpertNewType> expertType) {
        this.expertType = expertType;
    }

    public String getExpertID() {
        return expertID;
    }

    public void setExpertID(String expertID) {
        this.expertID = expertID;
    }
}