package cs.domain.project;

import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 项目合并评审操作
 */
@Entity
@Table(name = "cs_sign_merge")
@DynamicUpdate(true)
@IdClass(SignMergeId.class)
public class SignMerge extends DomainBase {
    // 主键，这里需要添加@Id标记
    @Id
    @Column(name="signId",columnDefinition="VARCHAR(64)")
    private String signId;
    // 主键，这里需要添加@Id标记
    @Id
    @Column(name="mergeId",columnDefinition="VARCHAR(64)")
    private String mergeId;
    // 主键，这里需要添加@Id标记
    @Id
    @Column(name="mergeType",columnDefinition="VARCHAR(64)")
    private String mergeType;

    public String getMergeId() {
        return mergeId;
    }

    public void setMergeId(String mergeId) {
        this.mergeId = mergeId;
    }

    public String getMergeType() {
        return mergeType;
    }

    public void setMergeType(String mergeType) {
        this.mergeType = mergeType;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }
}
