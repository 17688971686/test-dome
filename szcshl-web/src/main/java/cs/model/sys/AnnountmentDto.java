package cs.model.sys;

import com.alibaba.fastjson.annotation.JSONField;
import cs.model.BaseDto;

import java.util.Date;
import java.util.List;

public class AnnountmentDto extends BaseDto {

    private String anId;
    private String anTitle;    //公告标题
    private String anOrg;    //发布部门
    @JSONField(format = "yyyy-MM-dd")
    private Date anDate;    //发布时间
    private String anUser;    //发布人
    /**
     * 是否置顶 （0:不置顶[默认]  9:置顶）
     */
    private Integer isStick;
    /**
     * 是否正式发布（0：否，9:是）
     */
    private String issue;
    private String issueUser;
    private Date issueDate;
    private Integer anSort;        //	排序
    private String anContent;    //内容

    /**
     * 附件列表
     */
    private List<SysFileDto> sysFileList;

    public List<SysFileDto> getSysFileList() {
        return sysFileList;
    }

    public void setSysFileList(List<SysFileDto> sysFileList) {
        this.sysFileList = sysFileList;
    }

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

    public Date getAnDate() {
        return anDate;
    }

    public void setAnDate(Date anDate) {
        this.anDate = anDate;
    }

    public Integer getAnSort() {
        return anSort;
    }

    public void setAnSort(Integer anSort) {
        this.anSort = anSort;
    }

    public String getAnUser() {
        return anUser;
    }

    public void setAnUser(String anUser) {
        this.anUser = anUser;
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
