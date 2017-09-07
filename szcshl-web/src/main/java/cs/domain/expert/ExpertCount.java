package cs.domain.expert;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 专家统计信息
 */
@Entity
@Table(name="cs_expert_count")
public class ExpertCount {

    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "uuid")
    private String id;
    @Column(columnDefinition="Integer")
    private Integer weekCount;
    @Column(columnDefinition="Integer")
    private Integer monthCount;
    @Column(columnDefinition="Integer")
    private Integer absenceCount;
    @Column(columnDefinition="Integer")
    private Integer totalCount;
    @OneToOne
    @JoinColumn(name="expertId",unique = true)
    private Expert expert;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getWeekCount() {
        return weekCount;
    }

    public void setWeekCount(Integer weekCount) {
        this.weekCount = weekCount;
    }

    public Integer getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(Integer monthCount) {
        this.monthCount = monthCount;
    }

    public Integer getAbsenceCount() {
        return absenceCount;
    }

    public void setAbsenceCount(Integer absenceCount) {
        this.absenceCount = absenceCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Expert getExpert() {
        return expert;
    }

    public void setExpert(Expert expert) {
        this.expert = expert;
    }
}
