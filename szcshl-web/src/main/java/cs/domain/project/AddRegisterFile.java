package cs.domain.project;

import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
/*
 * 登记补充资料
 */
@Entity
@Table(name = "cs_add_registerfile")
@DynamicUpdate(true)
public class AddRegisterFile extends DomainBase {
	//登记补充资料编号
	@Id
	private String id;
	
   //资料名称
	@Column(columnDefinition="VARCHAR(256)")
	private String fileName;
	
	//份数
	@Column(columnDefinition = "INTEGER")	
	private Integer totalNum;
	
	//是否有原件
	@Column(columnDefinition = "VARCHAR(2)")
	private String isHasOriginfile;
	
	//是否有复印件
	@Column(columnDefinition = "VARCHAR(2)")
	private String isHasCopyfile;

	//补充说明
	@Column(columnDefinition = "VARCHAR(64)")
	private String suppleDeclare;
	
	//补充日期
	@Column(columnDefinition = "Date")
	private Date suppleDate;

	/**
	 * 业务ID
	 */
	@Column(columnDefinition = "VARCHAR(64)")
	private String businessId;

	/**
	 * 是否拟补充资料函
	 */
	@Column(columnDefinition = "VARCHAR(2)")
	private String isSupplement;

	/**
	 * 业务类型：图纸：DRAWING_FILE  ,  其它：ORTHER_FILE ,
	 */
	@Column(columnDefinition = "VARCHAR(64)")
	private String businessType;

	public String getIsSupplement() {
		return isSupplement;
	}

	public void setIsSupplement(String isSupplement) {
		this.isSupplement = isSupplement;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
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

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}
