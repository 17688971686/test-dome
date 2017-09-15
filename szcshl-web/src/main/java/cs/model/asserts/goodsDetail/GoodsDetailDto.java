package cs.model.asserts.goodsDetail;

import cs.domain.asserts.assertStorageBusiness.AssertStorageBusiness;
import cs.model.BaseDto;
    
    
    
    
    
    
    
    
    


/**
 * Description: 物品明细 页面数据模型
 * author: zsl
 * Date: 2017-9-15 14:15:55
 */
public class GoodsDetailDto extends BaseDto {

    private String id;
    private String goodsCode;
    private String goodsName;
    private String specifications;
    private String models;
    private String goodsPrice;
    private String evaluate;
    private String goodsNumber;
    private String businessId;
    private String applyDept;
    private String operator;
    private AssertStorageBusiness assertStorageBusiness;

    public GoodsDetailDto() {
    }
   
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }
    public String getModels() {
        return models;
    }

    public void setModels(String models) {
        this.models = models;
    }
    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }
    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }
    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }
    public AssertStorageBusiness getAssertStorageBusiness() {
        return assertStorageBusiness;
    }

    public void setAssertStorageBusiness(AssertStorageBusiness assertStorageBusiness) {
        this.assertStorageBusiness = assertStorageBusiness;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getApplyDept() {
        return applyDept;
    }

    public void setApplyDept(String applyDept) {
        this.applyDept = applyDept;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}