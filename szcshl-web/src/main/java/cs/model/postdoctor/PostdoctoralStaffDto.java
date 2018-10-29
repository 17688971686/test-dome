package cs.model.postdoctor;

import com.alibaba.fastjson.annotation.JSONField;
import cs.model.BaseDto;
import java.util.Date;

/**
 * 博士后基地管理
 * Created by zsl on 2018/10/23 0004.
 */

public class PostdoctoralStaffDto extends BaseDto {

    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 出生日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date birthdayDate;

    /**
     * 学习经历
     */
    private String learningExperience ;

    /**
     * 工作履历
     */
    private String workExperience ;

    /**
     * 进入基地时间
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date enterBaseDate;

    /**
     * 进站批准时间
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date enterStackApproveDate;


    /**
     * 出站时间
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date pooStackDate;

    /**
     * 培养单位
     */
    private String trainUnit ;

    /**
     * 合作导师
     */
    private String cooperativeMentor  ;

    /**
     * 课题研究简介
     */
    private String topicIntroduce  ;

    /**
     * 1.申请进站 2.表示进站，3.申请出站 4.出站
     */
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
