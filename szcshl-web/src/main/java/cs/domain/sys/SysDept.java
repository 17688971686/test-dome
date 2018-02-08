package cs.domain.sys;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 组别表
 * Created by ldm on 2017/8/18.
 */
@Entity
@Table(name = "cs_dept")
@DynamicUpdate(true)
public class SysDept {
    /**
     * id
     */
    @Id
    @GeneratedValue(generator= "deptGenerator")
    @GenericGenerator(name= "deptGenerator",strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    /**
     * 组别名称
     */
    @Column(columnDefinition = "varchar(63)")
    private String name;

    /**
     * 部长ID
     */
    @Column(columnDefinition = "varchar(64)")
    private String ministerId;

    /**
     * 部长名称
     */
    @Column(columnDefinition = "varchar(32)")
    private String ministerName;

    /**
     * 备注信息
     */
    @Column(columnDefinition = "varchar(512)")
    private String remark;
    /**
     * 用户列表
     */
    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> userList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinisterId() {
        return ministerId;
    }

    public void setMinisterId(String ministerId) {
        this.ministerId = ministerId;
    }

    public String getMinisterName() {
        return ministerName;
    }

    public void setMinisterName(String ministerName) {
        this.ministerName = ministerName;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
