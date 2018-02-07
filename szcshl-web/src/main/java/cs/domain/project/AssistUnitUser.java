package cs.domain.project;


import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 协审单位联系人
 */

@Entity
@Table(name = "cs_as_unituser")
@DynamicUpdate(true)
public class AssistUnitUser extends DomainBase {

    @Id
    private String id;

    /**
     * 联系人名称
     */
    @Column(columnDefinition="VARCHAR(64)")
    private String userName;

    /**
     * 联系人电话
     */
    @Column(columnDefinition="VARCHAR(16)")
    private String phoneNum;

    /**
     * 联系人地址
     */
    @Column(columnDefinition="VARCHAR(64)")
    private String position;

    @ManyToOne
    @JoinColumn(name = "assistUnitId")
    private AssistUnit assistUnit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public AssistUnit getAssistUnit() {
        return assistUnit;
    }

    public void setAssistUnit(AssistUnit assistUnit) {
        this.assistUnit = assistUnit;
    }
}
