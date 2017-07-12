package cs.domain.sys;

import cs.domain.DomainBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 通知公告
 *
 * @author MCL
 * @date 2017年7月5日 下午3:14:13
 */
@Entity
@Table(name = "cs_annountment")
public class Annountment extends DomainBase {

    @Id
//    @GeneratedValue(generator = "noticeGenerator")
//    @GenericGenerator(name = "noticeGenerator", strategy = "uuid")
    private String anId;

    /**
     * 公告标题
     */
    @Column(columnDefinition = "VARCHAR(255)")
    private String anTitle;

    /**
     * 发布部门
     */
    @Column(columnDefinition = "VARCHAR(126)")
    private String anOrg;

    /**
     * 是否置顶 （0:不置顶[默认]  9:置顶）
     */
    @Column(columnDefinition = "INTEGER")
    private Integer isStick;

    /**
     * 是否正式发布（0：否，9:是）
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String issue;

    /**
     * 发布人
     */
    @Column(columnDefinition = "VARCHAR(32)")
    private String issueUser;

    /**
     * 发布日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date issueDate;
    /**
     * 排序
     */
    @Column(columnDefinition = "INTEGER")
    private Integer anSort;

    /**
     * 内容
     */
    @Column(columnDefinition = "CLOB")
    private String anContent;


    public String getAnId() {
        return anId;
    }


    public void setAnId(String anId) {
        this.anId = anId;
    }


    public String getAnTitle() {
        return anTitle;
    }


    public void setAnTitle(String anTitle) {
        this.anTitle = anTitle;
    }


    public String getAnOrg() {
        return anOrg;
    }


    public void setAnOrg(String anOrg) {
        this.anOrg = anOrg;
    }


    public Integer getAnSort() {
        return anSort;
    }


    public void setAnSort(Integer anSort) {
        this.anSort = anSort;
    }


    public String getAnContent() {
        return anContent;
    }


    public void setAnContent(String anContent) {
        this.anContent = anContent;
    }


    public Integer getIsStick() {
        return isStick;
    }

    public void setIsStick(Integer isStick) {
        this.isStick = isStick;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueUser() {
        return issueUser;
    }

    public void setIssueUser(String issueUser) {
        this.issueUser = issueUser;
    }
}
