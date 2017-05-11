package cs.model.external;

import cs.model.BaseDto;
    
/**
 * Description: 部门 页面数据模型
 * User: ldm
 * Date: 2017-5-9 18:12:44
 */
public class DeptDto extends BaseDto {

    private String deptId;
    private String deptName;
    private String address;
    private String deptType;
    private String status;

    public DeptDto() {
    }
  
    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}