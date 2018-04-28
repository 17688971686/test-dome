package cs.model.expert;

import java.math.BigDecimal;

public class ExpertSelectedDto {

    private String id;
    /**
     * 综合评分(开始评分)
     */
    private Integer compositeScore;
    /**
     * 综合评分(结束评分)
     */
    private Integer compositeScoreEnd;
    //评分
    private Double score;
    //评审费用
    private BigDecimal reviewCost;
    //缴税
    private BigDecimal reviewTaxes;
    //总费用
    private BigDecimal totalCost;
    //抽取类型（随机、自选、境外专家3种）
    private String selectType;
    //评分描述
    private String describes;
    //是否参加
    private String isJoin;
    //突出专业(大类)
    private String maJorBig;
    //突出专业(小类)
    private String maJorSmall;
    //专家类别
    private String expeRttype;
    //抽取结果确认（已经确认的才能成为这次抽取的专家）
    private String isConfrim;
    //抽取次数
    private Integer selectIndex;
    private String principal;//负责人
    /**
     * 业务ID，如项目工作方案ID，课题研究工作方案ID
     */
    private String businessId;

    /**
     * 是否函评
     */
    private String isLetterRw;

    /**
     * 专家抽取条件ID
     */
    private String conditionId;

    /**
     * 专家评审费是否拆分打印
     */
    private String isSplit;

    /**
     * 拆分的第一张表的费用
     */
    private BigDecimal oneCost;

    private Integer expertSeq;

    /**
     * 专家抽取备注
     */
    private String remark;
    /**
     * 创建人
     */
    private String createBy;
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getIsSplit() {
        return isSplit;
    }

    public void setIsSplit(String isSplit) {
        this.isSplit = isSplit;
    }

    public BigDecimal getOneCost() {
        return oneCost;
    }

    public void setOneCost(BigDecimal oneCost) {
        this.oneCost = oneCost;
    }

    private ExpertReviewDto expertReviewDto;
    private ExpertDto expertDto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public BigDecimal getReviewCost() {
        return reviewCost;
    }

    public void setReviewCost(BigDecimal reviewCost) {
        this.reviewCost = reviewCost;
    }

    public BigDecimal getReviewTaxes() {
        return reviewTaxes;
    }

    public void setReviewTaxes(BigDecimal reviewTaxes) {
        this.reviewTaxes = reviewTaxes;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(String isJoin) {
        this.isJoin = isJoin;
    }

    public ExpertDto getExpertDto() {
        return expertDto;
    }

    public void setExpertDto(ExpertDto expertDto) {
        this.expertDto = expertDto;
    }

    public ExpertReviewDto getExpertReviewDto() {
        return expertReviewDto;
    }

    public void setExpertReviewDto(ExpertReviewDto expertReviewDto) {
        this.expertReviewDto = expertReviewDto;
    }

    public String getIsConfrim() {
        return isConfrim;
    }

    public void setIsConfrim(String isConfrim) {
        this.isConfrim = isConfrim;
    }

    public Integer getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(Integer selectIndex) {
        this.selectIndex = selectIndex;
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

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getIsLetterRw() {
        return isLetterRw;
    }

    public void setIsLetterRw(String isLetterRw) {
        this.isLetterRw = isLetterRw;
    }

    public Integer getCompositeScore() {
        return compositeScore;
    }

    public void setCompositeScore(Integer compositeScore) {
        this.compositeScore = compositeScore;
    }

    public Integer getCompositeScoreEnd() {
        return compositeScoreEnd;
    }

    public void setCompositeScoreEnd(Integer compositeScoreEnd) {
        this.compositeScoreEnd = compositeScoreEnd;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public Integer getExpertSeq() {
        return expertSeq;
    }

    public void setExpertSeq(Integer expertSeq) {
        this.expertSeq = expertSeq;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
