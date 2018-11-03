package cs.model.postdoctor;

import com.alibaba.fastjson.annotation.JSONField;
import cs.model.BaseDto;

import java.util.Date;

/**
 * Description: 博士后基地课题
 * Author: mcl
 * Date: 2018/10/31 9:51
 */
public class PostdoctorSubjectDto extends BaseDto{

    /**
     * 课题Id
     */
    private String id;

    /**
     * 课题名称
     */
    private String subjectName ;


    /**
     * 课题负责人
     */
    private String pricipalId;

    /**
     * 课题负责人名
     */
    private String pricipalName;

    /**
     * 课题创建时间
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date subjectCreatedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getPricipalId() {
        return pricipalId;
    }

    public void setPricipalId(String pricipalId) {
        this.pricipalId = pricipalId;
    }

    public String getPricipalName() {
        return pricipalName;
    }

    public void setPricipalName(String pricipalName) {
        this.pricipalName = pricipalName;
    }

    public Date getSubjectCreatedDate() {
        return subjectCreatedDate;
    }

    public void setSubjectCreatedDate(Date subjectCreatedDate) {
        this.subjectCreatedDate = subjectCreatedDate;
    }
}