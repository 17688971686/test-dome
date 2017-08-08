package cs.domain.project;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.alibaba.fastjson.annotation.JSONField;

import cs.domain.DomainBase;
/*
 * 登记补充资料
 */
@Entity
@Table(name = "cs_add_Registerfile")
@DynamicUpdate(true)
public class AddRegisterFile extends DomainBase {
	//登记补充资料编号
	@Id
	private String id;
	
   //资料名称
	@Column(columnDefinition="VARCHAR(64)")
	private String fileName;
	
	//份数
	@Column(columnDefinition = "INTEGER")	
	private Integer totalNum;
	
	//是否有原件
	@Column(columnDefinition = "VARCHAR(10)")
	private String isHasOriginfile;
	
	//是否有复印件
	@Column(columnDefinition = "VARCHAR(10)")
	private String isHasCopyfile;
	
	//补充说明
	@Column(columnDefinition = "VARCHAR(64)")
	private String suppleDeclare;
	
	//补充日期
	@JSONField(format = "yyyy-MM-dd")
	@Column(columnDefinition = "Date")
	private Date suppleDate;
	
	//收文编号
	@Column(columnDefinition = "VARCHAR(64)")
	private String signid;
	

	public String getSignid() {
		return signid;
	}

	public void setSignid(String signid) {
		this.signid = signid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public String getIsHasOriginfile() {
		return isHasOriginfile;
	}

	public void setIsHasOriginfile(String isHasOriginfile) {
		this.isHasOriginfile = isHasOriginfile;
	}

	public String getIsHasCopyfile() {
		return isHasCopyfile;
	}

	public void setIsHasCopyfile(String isHasCopyfile) {
		this.isHasCopyfile = isHasCopyfile;
	}

	public String getSuppleDeclare() {
		return suppleDeclare;
	}

	public void setSuppleDeclare(String suppleDeclare) {
		this.suppleDeclare = suppleDeclare;
	}

	public Date getSuppleDate() {
		return suppleDate;
	}

	public void setSuppleDate(Date suppleDate) {
		this.suppleDate = suppleDate;
	}
	
	

	
}
