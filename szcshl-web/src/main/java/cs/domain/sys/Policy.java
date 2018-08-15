package cs.domain.sys;

import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.DomainBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Description: 政策指标库
 * Author: zsl
 * Date: 2017/8/21 11:46
 */
@Entity
@Table(name="cs_policy")
public class Policy extends DomainBase{

    @Id
    private String id ;

    /**
     * 标准号
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String standardNo;

    /**
     * 标准名称
     */
    @Column(columnDefinition = "VARCHAR(200)")
    private String standardName;

    /**
     * 标准组织
     */
    @Column(columnDefinition = "VARCHAR(100)")
    private String standardOrg;

    /**
     * 英文名称
     */
    @Column(columnDefinition = "VARCHAR(300)")
    private String standardEnglishName;

    /**
     * 语种
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String languageType;

    /**
     * CCS分类
     */
    @Column(columnDefinition = "VARCHAR(150)")
    private String cCSType;


    /**
     * ICS分类
     */
    @Column(columnDefinition = "VARCHAR(150)")
    private String iCSType;

    /**
     * 标准摘要
     */
    @Column(columnDefinition = "VARCHAR(800)")
    private String standardSummary;

    /**
     * 发布单位
     */
    @Column(columnDefinition = "VARCHAR(80)")
    private String publishUnit;

    /**
     * 归口单位
     */
    @Column(columnDefinition = "VARCHAR(80)")
    private String belongUnit;

    /**
     * 起草单位
     */
    @Column(columnDefinition = "VARCHAR(300)")
    private String draftUnit;

    /**
     * 起草人
     */
    @Column(columnDefinition = "VARCHAR(120)")
    private String draftPerson;


    /**
     * 出处
     */
    @Column(columnDefinition = "VARCHAR(120)")
    private String rootFrom;


    /**
     * 备案号
     */
    @Column(columnDefinition = "VARCHAR(50)")
    private String fillingNo;


    /**
     * 简评
     */
    @Column(columnDefinition = "VARCHAR(50)")
    private String briefComment;


    /**
     * 备注
     */
    @Column(columnDefinition = "VARCHAR(500)")
    private String remark;


    /**
     * 发布日期
     */
    @Column(columnDefinition = "DATE")
    @JSONField(format = "yyyy-MM-dd")
    private Date publishDate;

    /**
     * 实施日期
     */
    @Column(columnDefinition = "DATE")
    @JSONField(format = "yyyy-MM-dd")
    private Date implementDate;

    /**
     * 作废日期
     */
    @Column(columnDefinition = "DATE")
    @JSONField(format = "yyyy-MM-dd")
    private Date cancelDate;

    /**
     * 状态 1：生效，9: 删除
     */
    @Column(columnDefinition = "VARCHAR(1)")
    private String state;


    /**
     * 政策库标准类型
     */
    @Column(columnDefinition = "VARCHAR(64)")
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