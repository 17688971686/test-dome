package cs.domain.sharing;


import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "cs_sharing_platform")
@DynamicUpdate(true)
public class SharingPlatlform extends DomainBase {

    /**
     * 主键
     */
    @Id
    private String sharId;

    /**
     * 主题
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String theme;

    /**
     * 是否全员可看（9：是，0：否）
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String isNoPermission;

    /**
     * 是否正式发布（ 0:否 ,9:是）
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String isPublish;

    /**
     * 发布人
     */
    @Column(columnDefinition = "VARCHAR(32)")
    private String publishUsername;

    /**
     * 发布时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date publishDate;

    /**
     * 内容
     */
    @Column(columnDefinition = "VARCHAR(2000)")
    private String content;

    @OneToMany(mappedBy="sharingPlatlform",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SharingPrivilege> privilegeList;

    public String getSharId() {
        return sharId;
    }

    public void setSharId(String sharId) {
        this.sharId = sharId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsNoPermission() {
        return isNoPermission;
    }

    public void setIsNoPermission(String isNoPermission) {
        this.isNoPermission = isNoPermission;
    }

    public String getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(String string) {
        this.isPublish = string;
    }

    public String getPublishUsername() {
        return publishUsername;
    }

    public void setPublishUsername(String publishUsername) {
        this.publishUsername = publishUsername;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public List<SharingPrivilege> getPrivilegeList() {
        return privilegeList;
    }

    public void setPrivilegeList(List<SharingPrivilege> privilegeList) {
        this.privilegeList = privilegeList;
    }
}
