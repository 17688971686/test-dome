package cs.model.expert;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

/**
 * 专家抽取历史记录
 * Created by shenning on 2017/10/17.
 */
public class ExpertSelectHis {
    /**
     * 专家ID
     */
    private String epId;
    /**
     * 专家名称
     */
    private String epName;
    /**
     * 专家工作单位
     */
    private String epCompany;
    /**
     * 专家区域类别
     */
    private String epField;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 专业大类
     */
    private String majorBig;
    /**
     * 专业小类
     */
    private String marjorSmall;
    /**
     * 专家类别
     */
    private String expertType;
    /**
     * 抽取类别
     */
    private String selectType;
    /**
     * 是否选定（9：是，其他：否）
     */
    private String isConfirm;
    /**
     * 评审方式
     */
    private String reviewType;
    /**
     * 评审会日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date reviewDate;
    /**
     * 项目负责人
     */
    private String mainChargeUserName;

    //以下字段用于查询用而已
    /**
     * 开始时间
     */
    private String beginTime;
    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 子对象，用于显示布局用
     */
    private List<ExpertSelectHis> childList;

    public String getEpId() {
        return epId;
    }

    public void setEpId(String epId) {
        this.epId = epId;
    }

    public String getEpName() {
        return epName;
    }

    public void setEpName(String epName) {
        this.epName = epName;
    }

    public String getEpCompany() {
        return epCompany;
    }

    public void setEpCompany(String epCompany) {
        this.epCompany = epCompany;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMajorBig() {
        return majorBig;
    }

    public void setMajorBig(String majorBig) {
        this.majorBig = majorBig;
    }

    public String getMarjorSmall() {
        return marjorSmall;
    }

    public void setMarjorSmall(String marjorSmall) {
        this.marjorSmall = marjorSmall;
    }

    public String getExpertType() {
        return expertType;
    }

    public void setExpertType(String expertType) {
        this.expertType = expertType;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(String isConfirm) {
        this.isConfirm = isConfirm;
    }

    public String getReviewType() {
        return reviewType;
    }

    public void setReviewType(String reviewType) {
        this.reviewType = reviewType;
    }

    public String getMainChargeUserName() {
        return mainChargeUserName;
    }

    public void setMainChargeUserName(String mainChargeUserName) {
        this.mainChargeUserName = mainChargeUserName;
    }

    public String getEpField() {
        return epField;
    }

    public void setEpField(String epField) {
        this.epField = epField;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<ExpertSelectHis> getChildList() {
        return childList;
    }

    public void setChildList(List<ExpertSelectHis> childList) {
        this.childList = childList;
    }
}
