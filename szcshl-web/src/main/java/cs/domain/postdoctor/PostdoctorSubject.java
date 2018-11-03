package cs.domain.postdoctor;

import cs.domain.DomainBase;
import cs.model.BaseDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Description: 博士后基本课题
 * Author: mcl
 * Date: 2018/10/31 9:41
 */
@Entity
@Table(name="cs_postdoctor_subject")
public class PostdoctorSubject extends DomainBase {

    /**
     * 课题Id
     */
    @Id
    private String id;

    /**
     * 课题名称
     */
    @Column(columnDefinition = "VARCHAR(500)")
    private String subjectName ;


    /**
     * 课题负责人
     */
    @Column(columnDefinition = "VARCHAR(255)")
    private String pricipalId;

    /**
     * 课题创建时间
     */
    @Column(columnDefinition = "date")
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

    public Date getSubjectCreatedDate() {
        return subjectCreatedDate;
    }

    public void setSubjectCreatedDate(Date subjectCreatedDate) {
        this.subjectCreatedDate = subjectCreatedDate;
    }
}