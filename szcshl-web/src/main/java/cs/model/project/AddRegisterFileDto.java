package cs.model.project;

import cs.domain.project.AddRegisterFile;
import cs.model.BaseDto;
    
    
    
    
    
    
    import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;


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
    @JSONField(format = "yyyy-MM-dd")
    private Date suppleDate;
    /**
     * 业务ID
     */
    private String businessId;

    /**
     * 是否拟补充资料函
     */
//    private String isSupplement;

    /**
     * 业务类型：
     * 1为报审文件，2为归档图纸，3为补充材料，4其他资料，
     * 5为归档中的报审登记表中的其它资料，
     * 6为归档项目审核中的补充资料，
     * 7为归档其它特殊文件
     */
    private Integer businessType;

//    public String getIsSupplement() {
//        return isSupplement;
//    }
//
//    public void setIsSupplement(String isSupplement) {
//        this.isSupplement = isSupplement;
//    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
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

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}