package cs.domain.project;

import cs.domain.DomainBase;
import cs.domain.sys.Company;

import javax.persistence.*;

/**
 * 单位评分表
 * Created by Administrator on 2018/3/9.
 */
@Entity
@Table(name = "cs_unitScore")
public class UnitScore extends DomainBase {
    @Id
    private String id; //ID
    @Column(columnDefinition="varchar(255)")
    private String signid;//项目id

    //单位关系（多对一）
    @ManyToOne
    @JoinColumn(name = "companyId")
    private Company company;
    //评分
    @Column(columnDefinition = "NUMBER")
    private Double score;
    //评级描述
    @Column(columnDefinition = "VARCHAR(256)")
    private String describes;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSignid() {
        return signid;
    }

    public void setSignid(String signid) {
        this.signid = signid;
    }



    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }
}
