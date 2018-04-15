package cs.domain.expert;

import cs.domain.DomainBase;
import javax.persistence.*;

/**
 * 拟请的专家，新的分类表
 * Created by zsl on 2018/3/18.
 */
@Entity
@Table(name = "cs_expert_new_type")
public class ExpertNewType extends DomainBase {

    @Id
    private String id;

    @Column(columnDefinition = "VARCHAR(64)")
    private String businessId;

    @Column(columnDefinition = "VARCHAR(300)")
    private String expertType;//专家类别

    @Column(columnDefinition = "VARCHAR(200)")
    private String maJorBig;//突出专业（大类）

    @Column(columnDefinition = "VARCHAR(200)")
    private String maJorSmall;//突出专业（小类）

    //抽取专家关系（多对一）
    @ManyToOne
    @JoinColumn(name = "expertNewInfoId")
    private ExpertNewInfo expertNewInfo;

    /**
     * 抽取条件ID，这里不做关联，只是保存
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String conditionId;

    /**
     * 抽取专家id
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String expertSelectedId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpertType() {
        return expertType;
    }

    public void setExpertType(String expertType) {
        this.expertType = expertType;
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

    public ExpertNewInfo getExpertNewInfo() {
        return expertNewInfo;
    }

    public void setExpertNewInfo(ExpertNewInfo expertNewInfo) {
        this.expertNewInfo = expertNewInfo;
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

    public String getExpertSelectedId() {
        return expertSelectedId;
    }

    public void setExpertSelectedId(String expertSelectedId) {
        this.expertSelectedId = expertSelectedId;
    }




}