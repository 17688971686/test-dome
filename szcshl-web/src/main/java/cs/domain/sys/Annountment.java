package cs.domain.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import cs.domain.DomainBase;

/**
 * 通知公告
 * @author MCL
 *@date 2017年7月5日 下午3:14:13 
 */
@Entity
@Table(name="cs_annountment")
public class Annountment extends DomainBase{
	
	@Id
	private String anId;	
	
	@Column(columnDefinition="VARCHAR(255)")
	private String anTitle;	//公告标题
	
	@Column(columnDefinition="VARCHAR(126)")
	private String anOrg;	//发布部门
	
	
	@Column(columnDefinition="DATE")
	private Date anDate;		//发布时间
	
	@Column(columnDefinition="VARCHAR(50)")
	private String anUser;	//	发布人
	
	@Column(columnDefinition="VARCHAR(2)")
	private String isStick;	//是否置顶      0:置顶      1:不置顶（默认）
	
	
	@Column(columnDefinition="INTEGER")
	private Integer anSort;		//	排序
	
	@Column(columnDefinition="VARCHAR(5000)")
	private String anContent;	//内容


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


	public String getIsStick() {
		return isStick;
	}


	public void setIsStick(String isStick) {
		this.isStick = isStick;
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
	
	
}
