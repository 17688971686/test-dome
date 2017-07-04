package cs.model.project;

import cs.model.BaseDto;

/**
 * 合并发文/评审
 *
 * @author ldm
 */
public class MergeOptionDto extends BaseDto {

    /**
     * id
     */
	private String id;

	/**
	 * 业务ID
	 */
	private String businessId;

    /**
     * 主业务ID
     */
    private String mainBusinessId;

	/**
	 * 合并类型（1：工作方案，2：收文）
	 */
	private String businessType;    //业务类型


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMainBusinessId() {
        return mainBusinessId;
    }

    public void setMainBusinessId(String mainBusinessId) {
        this.mainBusinessId = mainBusinessId;
    }
}
