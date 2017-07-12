package cs.domain.project;


import cs.domain.DomainBase;
import cs.domain.expert.ExpertSelected;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 设置为动态更新，只更新有修改的字段
 * (对应视图 V_SIGN_DISP_WORK)
 *
 * @author ldm
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "V_SIGN_DISP_WORK")
public class SignDispaWork extends DomainBase {

    /**
     * 收文ID
     */
    @Id
    private String signDwid;    //收文编号*

    @Column
    private String sprojectname;    //项目名称*

    @Column
    private String sbuiltcompanyName;//建设单位*

    @Column
    private String sreviewstage;    //评审阶段*

    @Column
    private String pishaveeia;    //是否有环评*

    @Column(columnDefinition = "INTEGER")
    private Integer sisAssociate;    //是否已关联*

    @Column
    private String pprojecttype;    //项目类型*

    @Column
    private String pmianchargeusername;    //项目负责人*

    @Column
    private String sfilenum;    //归档编号*

    @Column
    private String mOrgNames;    //主办部门*
    @Column

    private String pmaindeptname;    //主管部门(改革前)*

    @Column
    private String surgencydegree;    //缓急程度*

    /*  @Column
      private String mOrgNameAfter;	//主管部门(改革后）
  */
   /* @Column
    private String goodReview;	//优秀评审报告
*/
    @Column
    private String previewtype;    //评审方式*

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date sreceivedate;    //收文日期*

    /*@Column
    private String agreedate;	//批复时间
*/
    @Column
    private String dfilenum;    //文件字号*

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date ddispatchdate;    //发文日期*

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date ffiledate;    //归档日期*

    @Column
    private String ddispatchtype;    //发文类型*

    @Column
    private String pindustrytype;    //行业类型*

    @Column
    private String ssecrectlevel;    //秘密等级*

    @Column
    private String sreviewdays;    //评审天数*

    //@Column
    private String ssurplusdays;    //剩余工作日*

    @Column
    private String sappalyinvestment;    //申报投资*

    @Column
    private String dauthorizevalue;    //审定投资*

    @Column
    private String dextravalue;    //核减（增）投资*

    @Column
    private String dextrarate;    //核减率*

    @Column
    private String dapprovevalue;    //批复金额*

    // @Column
    // private String approveTime;	//批复来文时间

    @Column
    private String sisassistproc;    //是否协审*

    @Column
    private String sdaysafterdispatch;    //发文后工作日*

    @Column
    private String mOrgIds;    //部长所属部门ID*

    @Column
    private String orgdirectorname;    //部长名称*
    @Column
    private String orgMLeaderName;    //主任名称*
    @Column
    private String orgSLeaderName;    //副主任名称*

    public String getOrgdirectorname() {
        return orgdirectorname;
    }

    public void setOrgdirectorname(String orgdirectorname) {
        this.orgdirectorname = orgdirectorname;
    }

    public String getOrgMLeaderName() {
        return orgMLeaderName;
    }

    public void setOrgMLeaderName(String orgMLeaderName) {
        this.orgMLeaderName = orgMLeaderName;
    }

    public String getOrgSLeaderName() {
        return orgSLeaderName;
    }

    public void setOrgSLeaderName(String orgSLeaderName) {
        this.orgSLeaderName = orgSLeaderName;
    }


    public String getSignDwid() {
        return signDwid;
    }

    public void setSignDwid(String signDwid) {
        this.signDwid = signDwid;
    }

    public String getSprojectname() {
        return sprojectname;
    }

    public void setSprojectname(String sprojectname) {
        this.sprojectname = sprojectname;
    }

    public String getSbuiltcompanyName() {
        return sbuiltcompanyName;
    }

    public void setSbuiltcompanyName(String sbuiltcompanyName) {
        this.sbuiltcompanyName = sbuiltcompanyName;
    }

    public String getSreviewstage() {
        return sreviewstage;
    }

    public void setSreviewstage(String sreviewstage) {
        this.sreviewstage = sreviewstage;
    }

    public String getPishaveeia() {
        return pishaveeia;
    }

    public void setPishaveeia(String pishaveeia) {
        this.pishaveeia = pishaveeia;
    }

    public Integer getSisAssociate() {
        return sisAssociate;
    }

    public void setSisAssociate(Integer sisAssociate) {
        this.sisAssociate = sisAssociate;
    }

    public String getPprojecttype() {
        return pprojecttype;
    }

    public void setPprojecttype(String pprojecttype) {
        this.pprojecttype = pprojecttype;
    }

    public String getPmianchargeusername() {
        return pmianchargeusername;
    }

    public void setPmianchargeusername(String pmianchargeusername) {
        this.pmianchargeusername = pmianchargeusername;
    }

    public String getSfilenum() {
        return sfilenum;
    }

    public void setSfilenum(String sfilenum) {
        this.sfilenum = sfilenum;
    }

    public String getmOrgNames() {
        return mOrgNames;
    }

    public void setmOrgNames(String mOrgNames) {
        this.mOrgNames = mOrgNames;
    }

    public String getPmaindeptname() {
        return pmaindeptname;
    }

    public void setPmaindeptname(String pmaindeptname) {
        this.pmaindeptname = pmaindeptname;
    }

    public String getSurgencydegree() {
        return surgencydegree;
    }

    public void setSurgencydegree(String surgencydegree) {
        this.surgencydegree = surgencydegree;
    }

    public String getPreviewtype() {
        return previewtype;
    }

    public void setPreviewtype(String previewtype) {
        this.previewtype = previewtype;
    }

    public Date getSreceivedate() {
        return sreceivedate;
    }

    public void setSreceivedate(Date sreceivedate) {
        this.sreceivedate = sreceivedate;
    }

    public String getDfilenum() {
        return dfilenum;
    }

    public void setDfilenum(String dfilenum) {
        this.dfilenum = dfilenum;
    }

    public Date getDdispatchdate() {
        return ddispatchdate;
    }

    public void setDdispatchdate(Date ddispatchdate) {
        this.ddispatchdate = ddispatchdate;
    }

    public Date getFfiledate() {
        return ffiledate;
    }

    public void setFfiledate(Date ffiledate) {
        this.ffiledate = ffiledate;
    }

    public String getDdispatchtype() {
        return ddispatchtype;
    }

    public void setDdispatchtype(String ddispatchtype) {
        this.ddispatchtype = ddispatchtype;
    }

    public String getPindustrytype() {
        return pindustrytype;
    }

    public void setPindustrytype(String pindustrytype) {
        this.pindustrytype = pindustrytype;
    }

    public String getSsecrectlevel() {
        return ssecrectlevel;
    }

    public void setSsecrectlevel(String ssecrectlevel) {
        this.ssecrectlevel = ssecrectlevel;
    }

    public String getSreviewdays() {
        return sreviewdays;
    }

    public void setSreviewdays(String sreviewdays) {
        this.sreviewdays = sreviewdays;
    }

    public String getSsurplusdays() {
        return ssurplusdays;
    }


    public void setSsurplusdays(String ssurplusdays) {
        this.ssurplusdays = ssurplusdays;
    }

    public String getSappalyinvestment() {
        return sappalyinvestment;
    }

    public void setSappalyinvestment(String sappalyinvestment) {
        this.sappalyinvestment = sappalyinvestment;
    }

    public String getDauthorizevalue() {
        return dauthorizevalue;
    }

    public void setDauthorizevalue(String dauthorizevalue) {
        this.dauthorizevalue = dauthorizevalue;
    }

    public String getDextravalue() {
        return dextravalue;
    }

    public void setDextravalue(String dextravalue) {
        this.dextravalue = dextravalue;
    }

    public String getDextrarate() {
        return dextrarate;
    }

    public void setDextrarate(String dextrarate) {
        this.dextrarate = dextrarate;
    }

    public String getDapprovevalue() {
        return dapprovevalue;
    }

    public void setDapprovevalue(String dapprovevalue) {
        this.dapprovevalue = dapprovevalue;
    }

    public String getSisassistproc() {
        return sisassistproc;
    }

    public void setSisassistproc(String sisassistproc) {
        this.sisassistproc = sisassistproc;
    }

    public String getSdaysafterdispatch() {
        return sdaysafterdispatch;
    }

    public void setSdaysafterdispatch(String sdaysafterdispatch) {
        this.sdaysafterdispatch = sdaysafterdispatch;
    }

    public String getmOrgIds() {
        return mOrgIds;
    }

    public void setmOrgIds(String mOrgIds) {
        this.mOrgIds = mOrgIds;
    }


}