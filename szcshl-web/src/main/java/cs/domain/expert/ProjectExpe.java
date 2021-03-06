package cs.domain.expert;

import cs.domain.DomainBase;

import javax.persistence.*;

/**
 * 项目经验
 *
 * @author Administrator
 */

@Entity
@Table(name = "cs_project_expe")
public class ProjectExpe extends DomainBase {

    @Id
    private String peID; //Id

    @Column(name = "projectName", nullable = false, length = 128)
    private String projectName; //项目名称

    @Column(name = "projectType", nullable = false, length = 100)
    private String projectType; //项目类型

    @Column(columnDefinition = "INTEGER")
    private Integer seqNum;    //序号

    @ManyToOne
    @JoinColumn(name = "expertID")
    private Expert expert;

    public Expert getExpert() {
        return expert;
    }

    public void setExpert(Expert expert) {
        this.expert = expert;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPeID() {
        return peID;
    }

    public void setPeID(String peID) {
        this.peID = peID;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }
}
