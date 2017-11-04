package cs.model.manager;

/**
 * Description: 专家评审费发放超期-（项目、课题）信息
 * Author: mcl
 * Date: 2017/11/4 13:09
 */
public class ReviewFee {

    /**
     * 业务ID
     */
    private String businessId ;


    /**
     * 业务类型（SIGN 表示收文，TOPIC表示课题研究）
     */
    private String businessType;

    /**
     * （课题、项目）名称
     */
    private String name ;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}