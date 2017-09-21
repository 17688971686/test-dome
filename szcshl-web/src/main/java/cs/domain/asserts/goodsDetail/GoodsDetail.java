package cs.domain.asserts.goodsDetail;

import cs.domain.DomainBase;
import cs.domain.asserts.assertStorageBusiness.AssertStorageBusiness;

import javax.persistence.*;

/**
 * Created by zsl on 2017-09-15.
 */
@Entity
@Table(name = "cs_goods_detail")
public class GoodsDetail extends DomainBase {
    @Id
    private String id;
    //物品编号
    @Column(columnDefinition = "varchar(64) ")
    private String goodsCode;
    //物品名称
    @Column(columnDefinition = "varchar(255) ")
    private String goodsName;
    //规格
    @Column(columnDefinition = "varchar(64) ")
    private String specifications;
    //型号
    @Column(columnDefinition = "varchar(64) ")
    private String models;
    //单位
    @Column(columnDefinition = "varchar(255)")
    private String orgCompany;
    //估价
    @Column(columnDefinition="NUMBER")
    private String evaluate;
    //数量
    @Column(columnDefinition="NUMBER")
    private String goodsNumber;
    //采购入库标志:1.采购流程走完，资产已入库 0:流程没走完，资产未入库
    @Column(columnDefinition="varchar(1)")
    private String storeFlag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "businessId")
    private AssertStorageBusiness assertStorageBusiness;

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

    public String getOrgCompany() {
        return orgCompany;
    }

    public void setOrgCompany(String orgCompany) {
        this.orgCompany = orgCompany;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public AssertStorageBusiness getAssertStorageBusiness() {
        return assertStorageBusiness;
    }

    public void setAssertStorageBusiness(AssertStorageBusiness assertStorageBusiness) {
        this.assertStorageBusiness = assertStorageBusiness;
    }

    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getStoreFlag() {
        return storeFlag;
    }

    public void setStoreFlag(String storeFlag) {
        this.storeFlag = storeFlag;
    }
}
