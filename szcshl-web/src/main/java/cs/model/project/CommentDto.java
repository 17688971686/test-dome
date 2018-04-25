package cs.model.project;

import java.util.Date;

/**
 * 办理意见DTO
 * Created by ldm on 2018/4/25.
 */
public class CommentDto {

    /**
     * 流程实例ID
     */
    private String procInstId;

    /**
     * 环节名称
     */
    private String nodeKeyValue;

    /**
     * 办理人
     */
    private String userName;

    /**
     * 处理日期
     */
    private Date commentDate;

    /**
     * 意见信息
     */
    private String comments;


    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getNodeKeyValue() {
        return nodeKeyValue;
    }

    public void setNodeKeyValue(String nodeKeyValue) {
        this.nodeKeyValue = nodeKeyValue;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
