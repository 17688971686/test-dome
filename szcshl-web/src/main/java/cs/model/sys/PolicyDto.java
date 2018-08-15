package cs.model.sys;
import cs.model.BaseDto;
import java.util.Date;

/**
 * Description: 政策标准库
 * Author: zsl
 * Date: 2018/7/27 10:43
 */
public class PolicyDto extends BaseDto{

    private String id ;

    /**
     * 标准号
     */
    private String standardNo;

    /**
     * 标准名称
     */
    private String standardName;

    /**
     * 标准组织
     */
    private String standardOrg;

    /**
     * 英文名称
     */
    private String standardEnglishName;

    /**
     * 语种
     */
    private String languageType;

    /**
     * CCS分类
     */
    private String cCSType;

    /**
     * ICS分类
     */
    private String iCSType;

    /**
     * 标准摘要
     */
    private String standardSummary;

    /**
     * 发布单位
     */
    private String publishUnit;

    /**
     * 归口单位
     */
    private String belongUnit;

    /**
     * 起草单位
     */
    private String draftUnit;

    /**
     * 起草人
     */
    private String draftPerson;

    /**
     * 出处
     */
    private String rootFrom;

    /**
     * 备案号
     */
    private String fillingNo;

    /**
     * 简评
     */
    private String briefComment;

    /**
     * 备注
     */

    private String remark;

    /**
     * 发布日期
     */
    private Date publishDate;

    /**
     * 实施日期
     */
    private Date implementDate;

    /**
     * 作废日期
     */
    private Date cancelDate;

    /**
     * 状态 1：生效，9: 删除
     */
    private String state;

    /**
     * 政策库标准类型
     */
    private String stardandType;

    /**
     * 政策指标库的父ID
     */
    private String standardPId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStandardNo() {
        return standardNo;
    }

    public void setStandardNo(String standardNo) {
        this.standardNo = standardNo;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public String getStandardOrg() {
        return standardOrg;
    }

    public void setStandardOrg(String standardOrg) {
        this.standardOrg = standardOrg;
    }

    public String getStandardEnglishName() {
        return standardEnglishName;
    }

    public void setStandardEnglishName(String standardEnglishName) {
        this.standardEnglishName = standardEnglishName;
    }

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    public String getcCSType() {
        return cCSType;
    }

    public void setcCSType(String cCSType) {
        this.cCSType = cCSType;
    }

    public String getiCSType() {
        return iCSType;
    }

    public void setiCSType(String iCSType) {
        this.iCSType = iCSType;
    }

    public String getStandardSummary() {
        return standardSummary;
    }

    public void setStandardSummary(String standardSummary) {
        this.standardSummary = standardSummary;
    }

    public String getPublishUnit() {
        return publishUnit;
    }

    public void setPublishUnit(String publishUnit) {
        this.publishUnit = publishUnit;
    }

    public String getBelongUnit() {
        return belongUnit;
    }

    public void setBelongUnit(String belongUnit) {
        this.belongUnit = belongUnit;
    }

    public String getDraftUnit() {
        return draftUnit;
    }

    public void setDraftUnit(String draftUnit) {
        this.draftUnit = draftUnit;
    }

    public String getDraftPerson() {
        return draftPerson;
    }

    public void setDraftPerson(String draftPerson) {
        this.draftPerson = draftPerson;
    }

    public String getRootFrom() {
        return rootFrom;
    }

    public void setRootFrom(String rootFrom) {
        this.rootFrom = rootFrom;
    }

    public String getFillingNo() {
        return fillingNo;
    }

    public void setFillingNo(String fillingNo) {
        this.fillingNo = fillingNo;
    }

    public String getBriefComment() {
        return briefComment;
    }

    public void setBriefComment(String briefComment) {
        this.briefComment = briefComment;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getImplementDate() {
        return implementDate;
    }

    public void setImplementDate(Date implementDate) {
        this.implementDate = implementDate;
    }

    public Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStardandType() {
        return stardandType;
    }

    public void setStardandType(String stardandType) {
        this.stardandType = stardandType;
    }

    public String getStandardPId() {
        return standardPId;
    }

    public void setStandardPId(String standardPId) {
        this.standardPId = standardPId;
    }
}