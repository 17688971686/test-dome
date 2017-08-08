package cs.model.sharing;

import cs.model.BaseDto;

import javax.persistence.Entity;
import javax.persistence.Table;

public class SharingPrivilegeDto extends BaseDto {

    private String id;

    /**
     * 共享ID
     */
    private String sharId;

    /**
     * 业务ID（用户ID或者部门ID）
     */
    private String businessId;

    /**
     * 业务类型（用户或者部门）
     */
    private String businessType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSharId() {
        return sharId;
    }

    public void setSharId(String sharId) {
        this.sharId = sharId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
