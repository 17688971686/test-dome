package cs.model.sys;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cs.model.BaseDto;

public class AnnountmentDto extends BaseDto{

	private String anId;
	private String anTitle;	//公告标题
	private String anOrg;	//发布部门
	@JSONField(format = "yyyy-MM-dd")
	private Date anDate;	//发布时间
	private String anUser;	//发布人
	private String isStick;	//是否置顶      0:置顶      1:不置顶（默认）
	private Integer anSort;		//	排序
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
