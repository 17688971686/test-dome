package cs.model.sys;

import java.util.List;

/**
 * Created by shenning on 2017/8/18.
 */
public class SysDeptDto {

    private String id;

    /**
     * 组别名称
     */
    private String name;

    /**
     * 部长名称
     */
    private String ministerId;

    /**
     * 部长名称
     */
    private String ministerName;

    /**
     * 备注信息
     */
    private String remark;
    /**
     * 用户列表
     */
    private List<UserDto> userDtoList;

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

    public List<UserDto> getUserDtoList() {
        return userDtoList;
    }

    public void setUserDtoList(List<UserDto> userDtoList) {
        this.userDtoList = userDtoList;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
