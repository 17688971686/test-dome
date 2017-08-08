package cs.model.project;

import cs.domain.project.AddRegisterFile;
import cs.model.BaseDto;
    
    
    
    
    
    
    import java.util.Date;


/**
 * Description: 登记补充资料 页面数据模型
 * author: ldm
 * Date: 2017-8-3 15:26:51
 */
public class AddRegisterFileDto extends BaseDto {

    private String id;
    private String fileName;
    private Integer totalNum;
    private String isHasOriginfile;
    private String isHasCopyfile;
    private String suppleDeclare;
    private Date suppleDate;
    private String signid;
    
    public String getSignid() {
		return signid;
	}

	public void setSignid(String signid) {
		this.signid = signid;
	}

	public AddRegisterFileDto() {
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