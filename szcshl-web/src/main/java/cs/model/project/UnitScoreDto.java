package cs.model.project;

import cs.domain.sys.Company;
import cs.model.BaseDto;
import cs.model.sys.CompanyDto;

/**
 * Created by Administrator on 2018/3/9.
 */
public class UnitScoreDto extends BaseDto {
    private String id; //ID
    //评分
    private Double score;
    //评级描述
    private String describes;

    private String signid;
    private Company company;

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

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
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
}
