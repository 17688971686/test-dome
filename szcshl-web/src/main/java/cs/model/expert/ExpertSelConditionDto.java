package cs.model.expert;

import javax.persistence.Column;

/**
 * Description: 专家抽取条件 页面数据模型
 * author: ldm
 * Date: 2017-5-23 19:49:58
 */
public class ExpertSelConditionDto {

    private String id;
    private String maJorBig;
    private String maJorSmall;
    private String expeRttype;
    private Integer officialNum;
    private Integer alternativeNum;
    private String signId;
    private Integer sort;
    private String selectType;
    private Integer selectIndex;
    private String expertReviewId;
    private ExpertReviewDto expertReviewDto;

    public ExpertSelConditionDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {

        this.id = id;
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

    public String getExpeRttype() {
        return expeRttype;
    }

    public void setExpeRttype(String expeRttype) {

        this.expeRttype = expeRttype;
    }

    public Integer getOfficialNum() {

        return officialNum;
    }

    public void setOfficialNum(Integer officialNum) {

        this.officialNum = officialNum;
    }

    public Integer getAlternativeNum() {

        return alternativeNum;
    }

    public void setAlternativeNum(Integer alternativeNum) {

        this.alternativeNum = alternativeNum;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public ExpertReviewDto getExpertReviewDto() {
        return expertReviewDto;
    }

    public void setExpertReviewDto(ExpertReviewDto expertReviewDto) {
        this.expertReviewDto = expertReviewDto;
    }

    public String getExpertReviewId() {
        return expertReviewId;
    }

    public void setExpertReviewId(String expertReviewId) {
        this.expertReviewId = expertReviewId;
    }

    public Integer getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(Integer selectIndex) {
        this.selectIndex = selectIndex;
    }
}