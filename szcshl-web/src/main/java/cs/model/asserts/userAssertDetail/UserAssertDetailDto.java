package cs.model.asserts.userAssertDetail;

import cs.model.BaseDto;

/**
 * Description: 用户资产明细 页面数据模型
 * author: zsl
 * Date: 2017-9-20 15:35:02
 */
public class UserAssertDetailDto extends BaseDto {

    private String id;
    private String userId;
    private String userName;
    private String goodId;
    private String goodsName;
    private String goodsNumber;
    private String applyDept;

    public UserAssertDetailDto() {
    }
   
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getApplyDept() {
        return applyDept;
    }

    public void setApplyDept(String applyDept) {
        this.applyDept = applyDept;
    }
}