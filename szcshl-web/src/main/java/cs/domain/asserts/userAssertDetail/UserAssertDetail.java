package cs.domain.asserts.userAssertDetail;

import cs.domain.DomainBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zsl on 2017-09-20.
 */
@Entity
@Table(name = "cs_user_assert_detail")
public class UserAssertDetail extends DomainBase{
    @Id
    @Column(columnDefinition = "varchar(64)")
    private String id;
    //申请用户id
    @Column(columnDefinition = "varchar(64)")
    private String userId;
    //申请用户名字
    @Column(columnDefinition = "varchar(64)")
    private String userName;
    //申请设备id
    @Column(columnDefinition = "varchar(64)")
    private String goodId;
    //申请物品名称
    @Column(columnDefinition = "varchar(255) ")
    private String goodsName;
    //申请数量
    @Column(columnDefinition="NUMBER")
    private String goodsNumber;
    //申请部门
    @Column(columnDefinition = "varchar(64)")
    private String applyDept;

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
