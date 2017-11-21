package cs.model.expert;

/**
 * Description: 专家抽取条件 页面数据模型
 * author: ldm
 * Date: 2017-5-23 19:49:58
 */
public class ExpertSelConditionDto {

    private String id;

    /**
     * 综合评分(大于这个分数)
     */
    private Integer compositeScore;
    /**
     * 综合评分(小于这个分数)
     */
    private Integer compositeScoreEnd;
    /**
     * 突出专业(大类)
     */
    private String maJorBig;
    /**
     * 突出专业(小类)
     */
    private String maJorSmall;
    /**
     * 专家类别
     */
    private String expeRttype;
    /**
     * 正式专家个数
     */
    private Integer officialNum;
    /**
     * 备选专家个数
     */
    private Integer alternativeNum;
    /**
     * 排序
     */
    private Integer sort;

    /**
     * 抽取次数
     */
    private Integer selectIndex;

    /**
     * 业务ID
     */
    private String businessId;

    /**
     * 评审方案ID
     */
    private String expertReviewId;

    /**
     * 评审方案对象
     */
    private ExpertReviewDto expertReviewDto;

    public ExpertSelConditionDto() {
    }

    public Integer getCompositeScore() {
        return compositeScore;
    }

    public void setCompositeScore(Integer compositeScore) {
        this.compositeScore = compositeScore;
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


    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
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

    public Integer getCompositeScoreEnd() {
        return compositeScoreEnd;
    }

    public void setCompositeScoreEnd(Integer compositeScoreEnd) {
        this.compositeScoreEnd = compositeScoreEnd;
    }
}