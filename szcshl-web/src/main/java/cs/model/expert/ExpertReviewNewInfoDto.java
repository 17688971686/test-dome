package cs.model.expert;

import cs.model.BaseDto;

import java.util.List;

/**
 * Created by zsl on 2018/3/18.
 * 新的专家聘请信息
 */
public class ExpertReviewNewInfoDto extends BaseDto {

    private String id;
    private String businessId;//id
    private String conditionId;
    private String expeRttype;
    private String isConfrim;
    private String isJoin;
    private String isLetterRw;
    private String maJorBig;
    private String maJorSmall;
    private String selectIndex;
    private String selectType;
    private ExpertNewInfoDto expertDto;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public String getExpeRttype() {
        return expeRttype;
    }

    public void setExpeRttype(String expeRttype) {
        this.expeRttype = expeRttype;
    }

    public String getIsConfrim() {
        return isConfrim;
    }

    public void setIsConfrim(String isConfrim) {
        this.isConfrim = isConfrim;
    }

    public String getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(String isJoin) {
        this.isJoin = isJoin;
    }

    public String getIsLetterRw() {
        return isLetterRw;
    }

    public void setIsLetterRw(String isLetterRw) {
        this.isLetterRw = isLetterRw;
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

    public String getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(String selectIndex) {
        this.selectIndex = selectIndex;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public ExpertNewInfoDto getExpertDto() {
        return expertDto;
    }

    public void setExpertDto(ExpertNewInfoDto expertDto) {
        this.expertDto = expertDto;
    }
}
