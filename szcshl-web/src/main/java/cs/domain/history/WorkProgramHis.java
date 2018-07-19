package cs.domain.history;

import cs.domain.project.WorkBase;
import cs.domain.project.WorkProgram;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 工作方案留痕
 * Created by ldm on 2018/7/16 0016.
 */
@Entity
@Table(name = "cs_his_work_program")
@DynamicUpdate(true)
public class WorkProgramHis extends WorkBase {

    @Column(columnDefinition = "VARCHAR(64)")
    private String signId;

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }
}
