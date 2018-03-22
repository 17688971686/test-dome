package cs.model.expert;

import cs.model.BaseDto;

import java.util.List;


/**
 * Created by Administrator on 2018/3/18.
 * 新的专家聘请信息
 */
public class ExpertNewInfoDto extends BaseDto {

    private String expertNewInfoId;//id

    private String expertID;//专家ID

    private String name;//专家姓名

    private String comPany;//工作单位

    private String job;//现任职位

    private String post;//职称

    private String phone;//办公电话

    private String addRess;//通讯地址

    private String email;//电子邮箱

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

    private String expeRttype;

    private String maJorBig;//突出专业（大类）

    private String maJorSmall;//突出专业（小类）

    private String isJoin;

    private List<ExpertNewTypeDto> expertTypeDtoList;

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

    public List<ExpertNewTypeDto> getExpertTypeDtoList() {
        return expertTypeDtoList;
    }

    public void setExpertTypeDtoList(List<ExpertNewTypeDto> expertTypeDtoList) {
        this.expertTypeDtoList = expertTypeDtoList;
    }

    public String getExpertID() {
        return expertID;
    }

    public void setExpertID(String expertID) {
        this.expertID = expertID;
    }

    public String getExpeRttype() {
        return expeRttype;
    }

    public void setExpeRttype(String expeRttype) {
        this.expeRttype = expeRttype;
    }

    public String getMaJorBig() {
        return maJorBig;
    }

    public void setMaJorBig(String maJorBig) {
        this.maJorBig = maJorBig;
    }

    public String getMaJorSmall() {
        return maJorSmall;
    }

    public void setMaJorSmall(String maJorSmall) {
        this.maJorSmall = maJorSmall;
    }

    public String getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(String isJoin) {
        this.isJoin = isJoin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddRess() {
        return addRess;
    }

    public void setAddRess(String addRess) {
        this.addRess = addRess;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
