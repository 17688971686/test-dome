package cs.domain.expert;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cs_expert_condition")
@DynamicUpdate(true)
public class ExpertSelCondition {

    @Id
    @GeneratedValue(generator = "selConditionGenerator")
    @GenericGenerator(name = "selConditionGenerator", strategy = "uuid")
    private String id;

    @Column(columnDefinition = "varchar(128) ")
    private String maJorBig;//突出专业(大类)

    @Column(columnDefinition = "varchar(128) ")
    private String maJorSmall;//突出专业(小类)

    @Column(columnDefinition = "varchar(30) ")
    private String expeRttype;//专家类别

    @Column(columnDefinition = "integer")
    private Integer officialNum;    //正式专家个数

    @Column(columnDefinition = "integer")
    private Integer alternativeNum;    //备选专家个数

    @Column(columnDefinition = "integer")
    private Integer sort;

    //抽取类型（随机、自选、境外专家3种）
    @Column(columnDefinition = "VARCHAR(2)")
    private String selectType;
    /**
     * 抽取的专家信息
     */
    @OneToMany(mappedBy = "expertSelCondition")
    private List<ExpertSelected> expertSelectedList;

    /**
     * 专家评审方案
     */
    @ManyToOne
    @JoinColumn(name = "expertReviewId")
    private ExpertReview expertReview;

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

    public List<ExpertSelected> getExpertSelectedList() {
        return expertSelectedList;
    }

    public void setExpertSelectedList(List<ExpertSelected> expertSelectedList) {
        this.expertSelectedList = expertSelectedList;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public ExpertReview getExpertReview() {
        return expertReview;
    }

    public void setExpertReview(ExpertReview expertReview) {
        this.expertReview = expertReview;
    }
}
