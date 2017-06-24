package cs.model.expert;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertSelected;
import cs.model.BaseDto;

import javax.persistence.ManyToMany;
import java.util.Date;
import java.util.List;

/**
 * 专家信息管理
 *
 * @author Administrator
 */
public class ExpertDto extends BaseDto {
    private String expertID;//专家ID
    private String expertNo;//专家编号
    private String name;//专家姓名
    private String sex;//性别
    @JSONField(format = "yyyy-MM-dd")
    private Date birthDay;//出生日期
    private String idCard;//身份证号
    private String qualifiCations;//最高学历
    private String acaDemy;//毕业院校
    private String degRee;//最高学位
    private String userPhone;//手机号码
    @JSONField(format = "yyyy-MM-dd")
    private Date graduateDate;//毕业时间
    private String comPany;//工作单位
    private String job;//现任职位
    private String post;//职称
    private String phone;//办公电话
    private String fax;//传真
    private String email;//电子邮箱
    private String addRess;//通讯地址
    private String zipCode;//邮编
    private String majorStudy;//所学专业
    private String majorWork;//从事专业
    private String procoSttype;//工程造价类
    private String proteChtype;//项目类型
    private String remark;//备注
    private String state;//专家范围(审核中1,正式专家2,备选专家3,已停用4 ，已删除5)
    private String openingBank;
    private String bankAccount;
    private String expertSort;    //专家类别
    private String expertField;
    public byte[] photo;    //专家照片

    private List<WorkExpeDto> workDto;
    
    private List<ProjectExpeDto> projectDto;
    
    private List<ExpertReviewDto> expertReviewDtoList;
    
    private List<ExpertTypeDto> expertTypeDtoList;

    private List<ExpertOfferDto> expertOfferDtoList;    //专家聘书

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getQualifiCations() {
        return qualifiCations;
    }

    public void setQualifiCations(String qualifiCations) {
        this.qualifiCations = qualifiCations;
    }

    public String getAcaDemy() {
        return acaDemy;
    }

    public void setAcaDemy(String acaDemy) {
        this.acaDemy = acaDemy;
    }

    public String getDegRee() {
        return degRee;
    }

    public void setDegRee(String degRee) {
        this.degRee = degRee;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddRess() {
        return addRess;
    }

    public void setAddRess(String addRess) {
        this.addRess = addRess;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


    public String getProcoSttype() {
        return procoSttype;
    }

    public void setProcoSttype(String procoSttype) {
        this.procoSttype = procoSttype;
    }

    public String getProteChtype() {
        return proteChtype;
    }

    public void setProteChtype(String proteChtype) {
        this.proteChtype = proteChtype;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExpertID() {
        return expertID;
    }

    public void setExpertID(String expertID) {
        this.expertID = expertID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMajorStudy() {
        return majorStudy;
    }

    public void setMajorStudy(String majorStudy) {
        this.majorStudy = majorStudy;
    }

    public String getMajorWork() {
        return majorWork;
    }

    public void setMajorWork(String majorWork) {
        this.majorWork = majorWork;
    }

    public String getOpeningBank() {
        return openingBank;
    }

    public void setOpeningBank(String openingBank) {
        this.openingBank = openingBank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getExpertNo() {
        return expertNo;
    }

    public void setExpertNo(String expertNo) {
        this.expertNo = expertNo;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Date getGraduateDate() {
        return graduateDate;
    }

    public void setGraduateDate(Date graduateDate) {
        this.graduateDate = graduateDate;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public List<ExpertReviewDto> getExpertReviewDtoList() {
        return expertReviewDtoList;
    }

    public void setExpertReviewDtoList(List<ExpertReviewDto> expertReviewDtoList) {
        this.expertReviewDtoList = expertReviewDtoList;
    }

	public List<ProjectExpeDto> getProjectDto() {
		return projectDto;
	}

	public void setProjectDto(List<ProjectExpeDto> projectDto) {
		this.projectDto = projectDto;
	}

	public List<WorkExpeDto> getWorkDto() {
		return workDto;
	}

	public void setWorkDto(List<WorkExpeDto> workDto) {
		this.workDto = workDto;
	}

	public List<ExpertTypeDto> getExpertTypeDtoList() {
		return expertTypeDtoList;
	}

	public void setExpertTypeDtoList(List<ExpertTypeDto> expertTypeDtoList) {
		this.expertTypeDtoList = expertTypeDtoList;
	}

    public List<ExpertOfferDto> getExpertOfferDtoList() {
        return expertOfferDtoList;
    }

    public void setExpertOfferDtoList(List<ExpertOfferDto> expertOfferDtoList) {
        this.expertOfferDtoList = expertOfferDtoList;
    }

	public String getExpertSort() {
		return expertSort;
	}

	public void setExpertSort(String expertSort) {
		this.expertSort = expertSort;
	}

	public String getExpertField() {
		return expertField;
	}

	public void setExpertField(String expertField) {
		this.expertField = expertField;
	}
}
