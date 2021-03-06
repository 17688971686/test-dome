package cs.domain.project;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 工作方案
 *
 * @author ldm
 */
@Entity
@Table(name = "cs_work_program")
@DynamicUpdate(true)
public class WorkProgram extends WorkBase {

    /**
     * 重做工作方案流程
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String processInstanceId;

    //收文，一对一（只做级联删除）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signId")
    private Sign sign;

    public Sign getSign() {
        return sign;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }


    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
}
