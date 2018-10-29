package cs.domain.postdoctor;

import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 博士后人员管理
 * Created by zsl on 2017/9/4 0004.
 */
@Entity
@Table(name="cs_postdoctoral_staff")
@DynamicUpdate(true)
public class PostdoctoralStaff extends DomainBase {

    @Id
    private String id;

    /**
     * 姓名
     */
    @Column(columnDefinition="VARCHAR(128)")
    private String name;

    /**
     * 性别
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String sex;

    /**
     * 出生日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date birthdayDate;

    /**
     * 学习经历
     */
    @Column(columnDefinition="VARCHAR(1000)")
    private String learningExperience ;

    /**
     * 工作履历
     */
    @Column(columnDefinition="VARCHAR(1000)")
    private String workExperience ;

    /**
     * 进入基地时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date enterBaseDate;

    /**
     * 进站批准时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date enterStackApproveDate;


    /**
     * 出站时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date pooStackDate;



    /**
     * 培养单位
     */
    @Column(columnDefinition="VARCHAR(128)")
    private String trainUnit ;

    /**
     * 合作导师
     */
    @Column(columnDefinition="VARCHAR(50)")
    private String cooperativeMentor  ;

    /**
     * 课题研究简介
     */
    @Column(columnDefinition="VARCHAR(1000)")
    private String topicIntroduce  ;

    /**
     * 0.申请进站 1.表示进站，2.申请出站 3.出站
     */
    @Column(columnDefinition="VARCHAR(1)")
    private String status  ;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(Date birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public String getLearningExperience() {
        return learningExperience;
    }

    public void setLearningExperience(String learningExperience) {
        this.learningExperience = learningExperience;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }

    public Date getEnterBaseDate() {
        return enterBaseDate;
    }

    public void setEnterBaseDate(Date enterBaseDate) {
        this.enterBaseDate = enterBaseDate;
    }

    public Date getEnterStackApproveDate() {
        return enterStackApproveDate;
    }

    public void setEnterStackApproveDate(Date enterStackApproveDate) {
        this.enterStackApproveDate = enterStackApproveDate;
    }

    public Date getPooStackDate() {
        return pooStackDate;
    }

    public void setPooStackDate(Date pooStackDate) {
        this.pooStackDate = pooStackDate;
    }

    public String getTrainUnit() {
        return trainUnit;
    }

    public void setTrainUnit(String trainUnit) {
        this.trainUnit = trainUnit;
    }

    public String getCooperativeMentor() {
        return cooperativeMentor;
    }

    public void setCooperativeMentor(String cooperativeMentor) {
        this.cooperativeMentor = cooperativeMentor;
    }

    public String getTopicIntroduce() {
        return topicIntroduce;
    }

    public void setTopicIntroduce(String topicIntroduce) {
        this.topicIntroduce = topicIntroduce;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
