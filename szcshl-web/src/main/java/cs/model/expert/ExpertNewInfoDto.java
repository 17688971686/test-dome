package cs.model.expert;

import cs.model.BaseDto;

import java.util.List;

/**
 * Created by Administrator on 2018/3/18.
 * 新的专家聘请信息
 */
public class ExpertNewInfoDto extends BaseDto {

    private String expertNewInfoId;//id

    private String name;//专家姓名

    private String comPany;//工作单位

    private String job;//现任职位

    private String post;//职称

    private String userPhone;//手机号码

    private String remark;//备注
    /**
     * 业务ID，如项目签收工作方案ID，课题工作方案ID等
     */
    private String businessId;
    /**
     * 是否函评
     */
    private String isLetterRw;

    private List<ExpertNewTypeDto> expertNewTypeDtoList;

    public String getExpertNewInfoId() {
        return expertNewInfoId;
    }

    public void setExpertNewInfoId(String expertNewInfoId) {
        this.expertNewInfoId = expertNewInfoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComPany() {
        return comPany;
    }

    public void setComPany(String comPany) {
        this.comPany = comPany;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getIsLetterRw() {
        return isLetterRw;
    }

    public void setIsLetterRw(String isLetterRw) {
        this.isLetterRw = isLetterRw;
    }

    public List<ExpertNewTypeDto> getExpertNewTypeDtoList() {
        return expertNewTypeDtoList;
    }

    public void setExpertNewTypeDtoList(List<ExpertNewTypeDto> expertNewTypeDtoList) {
        this.expertNewTypeDtoList = expertNewTypeDtoList;
    }
}
