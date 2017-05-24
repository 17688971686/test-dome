package cs.domain.expert;

import org.hibernate.annotations.Generated;

import javax.persistence.*;

/**
 * 专家统计信息
 */
public class ExpertCount {

    @Id
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
}
