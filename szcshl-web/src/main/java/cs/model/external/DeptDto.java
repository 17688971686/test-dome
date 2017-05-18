package cs.model.external;

import java.util.List;

import cs.model.BaseDto;
    
/**
 * Description: 部门 页面数据模型
 * User: ldm
 * Date: 2017-5-9 18:12:44
 */
public class DeptDto extends BaseDto {

    private String deptId;
    private String deptName;
    private String deptUserName;
    private String deptOfficeId;
    private String address;
    private String deptType;
    private String status;
    private List<OfficeUserDto> offices;
    
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

	public String getDeptUserName() {
		return deptUserName;
	}

	public void setDeptUserName(String deptUserName) {
		this.deptUserName = deptUserName;
	}

	public List<OfficeUserDto> getOffices() {
		return offices;
	}

	public void setOffices(List<OfficeUserDto> offices) {
		this.offices = offices;
	}

	public String getDeptOfficeId() {
		return deptOfficeId;
	}

	public void setDeptOfficeId(String deptOfficeId) {
		this.deptOfficeId = deptOfficeId;
	}

	
	
    

}