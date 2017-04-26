package cs.model;



import java.math.BigDecimal;
import java.util.Date;

public class SignDto extends BaseDto {
	
	private String signid;
	
	private String filecode;
	
	private String projectcode;
	
	private String reviewstage;
	
	private String ispresign;
	
	private String projectname;
	
	private String isregisteredcompleted;
	
	private String maindepetid;
	
	private String maindepetcontactuserid;
	
	private String assistdeptid;
	
	private String assistdeptcontactuserid;
	
	private String designcompanyid;
	
	private String builtcompanyid;
	
	private String urgencydegree;
	
	private String yearplantype;
	
	private String secrectlevel;
	
	private String comprehensivehandlesugid;
	
	private String leaderhandlesugid;
	
	private String ministerhandlesugid;
	
	private String sendusersign;
	
	private String issign;
	
	private String ishasreviewcost;
	
	private Date signdate;
	
	private BigDecimal surplusdays;
	
	private Date expectdispatchdate;
	
	private Date receivedate;
	
	private BigDecimal daysafterdispatch;
	
	private BigDecimal reviewdays;
	
	private String isassistproc;
	
	private String mainchargeuserid;
	
	private String reviewdeptid;
	
	private String filenum;
	
	private String docnum;
	
	private String ispause;
	
	private BigDecimal pausedays;
	
	private Date pausetime;
	
	private String pasedescription;	

	public String getSignid(){
		return signid;
	}
	
	public void setSignid(String signid){
		this.signid = signid;
	}
	
	public String getFilecode(){
		return filecode;
	}
	
	public void setFilecode(String filecode){
		this.filecode = filecode;
	}
	
	public String getProjectcode(){
		return projectcode;
	}
	
	public void setProjectcode(String projectcode){
		this.projectcode = projectcode;
	}
	
	public String getReviewstage(){
		return reviewstage;
	}
	
	public void setReviewstage(String reviewstage){
		this.reviewstage = reviewstage;
	}
	
	public String getIspresign(){
		return ispresign;
	}
	
	public void setIspresign(String ispresign){
		this.ispresign = ispresign;
	}
	
	public String getProjectname(){
		return projectname;
	}
	
	public void setProjectname(String projectname){
		this.projectname = projectname;
	}
	
	public String getIsregisteredcompleted(){
		return isregisteredcompleted;
	}
	
	public void setIsregisteredcompleted(String isregisteredcompleted){
		this.isregisteredcompleted = isregisteredcompleted;
	}
	
	public String getMaindepetid(){
		return maindepetid;
	}
	
	public void setMaindepetid(String maindepetid){
		this.maindepetid = maindepetid;
	}
	
	public String getMaindepetcontactuserid(){
		return maindepetcontactuserid;
	}
	
	public void setMaindepetcontactuserid(String maindepetcontactuserid){
		this.maindepetcontactuserid = maindepetcontactuserid;
	}
	
	public String getAssistdeptid(){
		return assistdeptid;
	}
	
	public void setAssistdeptid(String assistdeptid){
		this.assistdeptid = assistdeptid;
	}
	
	public String getAssistdeptcontactuserid(){
		return assistdeptcontactuserid;
	}
	
	public void setAssistdeptcontactuserid(String assistdeptcontactuserid){
		this.assistdeptcontactuserid = assistdeptcontactuserid;
	}
	
	public String getDesigncompanyid(){
		return designcompanyid;
	}
	
	public void setDesigncompanyid(String designcompanyid){
		this.designcompanyid = designcompanyid;
	}
	
	public String getBuiltcompanyid(){
		return builtcompanyid;
	}
	
	public void setBuiltcompanyid(String builtcompanyid){
		this.builtcompanyid = builtcompanyid;
	}
	
	public String getUrgencydegree(){
		return urgencydegree;
	}
	
	public void setUrgencydegree(String urgencydegree){
		this.urgencydegree = urgencydegree;
	}
	
	public String getYearplantype(){
		return yearplantype;
	}
	
	public void setYearplantype(String yearplantype){
		this.yearplantype = yearplantype;
	}
	
	public String getSecrectlevel(){
		return secrectlevel;
	}
	
	public void setSecrectlevel(String secrectlevel){
		this.secrectlevel = secrectlevel;
	}
	
	public String getComprehensivehandlesugid(){
		return comprehensivehandlesugid;
	}
	
	public void setComprehensivehandlesugid(String comprehensivehandlesugid){
		this.comprehensivehandlesugid = comprehensivehandlesugid;
	}
	
	public String getLeaderhandlesugid(){
		return leaderhandlesugid;
	}
	
	public void setLeaderhandlesugid(String leaderhandlesugid){
		this.leaderhandlesugid = leaderhandlesugid;
	}
	
	public String getMinisterhandlesugid(){
		return ministerhandlesugid;
	}
	
	public void setMinisterhandlesugid(String ministerhandlesugid){
		this.ministerhandlesugid = ministerhandlesugid;
	}
	
	public String getSendusersign(){
		return sendusersign;
	}
	
	public void setSendusersign(String sendusersign){
		this.sendusersign = sendusersign;
	}
	
	public String getIssign(){
		return issign;
	}
	
	public void setIssign(String issign){
		this.issign = issign;
	}
	
	public String getIshasreviewcost(){
		return ishasreviewcost;
	}
	
	public void setIshasreviewcost(String ishasreviewcost){
		this.ishasreviewcost = ishasreviewcost;
	}
	
	public Date getSigndate(){
		return signdate;
	}
	
	public void setSigndate(Date signdate){
		this.signdate = signdate;
	}
	
	public BigDecimal getSurplusdays(){
		return surplusdays;
	}
	
	public void setSurplusdays(BigDecimal surplusdays){
		this.surplusdays = surplusdays;
	}
	
	public Date getExpectdispatchdate(){
		return expectdispatchdate;
	}
	
	public void setExpectdispatchdate(Date expectdispatchdate){
		this.expectdispatchdate = expectdispatchdate;
	}
	
	public Date getReceivedate(){
		return receivedate;
	}
	
	public void setReceivedate(Date receivedate){
		this.receivedate = receivedate;
	}
	
	public BigDecimal getDaysafterdispatch(){
		return daysafterdispatch;
	}
	
	public void setDaysafterdispatch(BigDecimal daysafterdispatch){
		this.daysafterdispatch = daysafterdispatch;
	}
	
	public BigDecimal getReviewdays(){
		return reviewdays;
	}
	
	public void setReviewdays(BigDecimal reviewdays){
		this.reviewdays = reviewdays;
	}
	
	public String getIsassistproc(){
		return isassistproc;
	}
	
	public void setIsassistproc(String isassistproc){
		this.isassistproc = isassistproc;
	}
	
	public String getMainchargeuserid(){
		return mainchargeuserid;
	}
	
	public void setMainchargeuserid(String mainchargeuserid){
		this.mainchargeuserid = mainchargeuserid;
	}
	
	public String getReviewdeptid(){
		return reviewdeptid;
	}
	
	public void setReviewdeptid(String reviewdeptid){
		this.reviewdeptid = reviewdeptid;
	}
	
	public String getFilenum(){
		return filenum;
	}
	
	public void setFilenum(String filenum){
		this.filenum = filenum;
	}
	
	public String getDocnum(){
		return docnum;
	}
	
	public void setDocnum(String docnum){
		this.docnum = docnum;
	}
	
	public String getIspause(){
		return ispause;
	}
	
	public void setIspause(String ispause){
		this.ispause = ispause;
	}
	
	public BigDecimal getPausedays(){
		return pausedays;
	}
	
	public void setPausedays(BigDecimal pausedays){
		this.pausedays = pausedays;
	}
	
	public Date getPausetime(){
		return pausetime;
	}
	
	public void setPausetime(Date pausetime){
		this.pausetime = pausetime;
	}
	
	public String getPasedescription(){
		return pasedescription;
	}
	
	public void setPasedescription(String pasedescription){
		this.pasedescription = pasedescription;
	}
}