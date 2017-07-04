package cs.domain.project;

import javax.persistence.*;
import org.hibernate.annotations.DynamicUpdate;
import cs.domain.DomainBase;
import org.hibernate.annotations.GenericGenerator;

/**
 * 合并发文/评审
 *
 * @author ldm
 */

@Entity
@Table(name = "cs_merge_option")
@DynamicUpdate(true)
public class MergeOption extends DomainBase {

    /**
     * id
     */
    @Id
    @GeneratedValue(generator= "mergeGenerator")
    @GenericGenerator(name= "mergeGenerator",strategy = "uuid")
    private String id;

    /**
     * 业务ID（(发文/工作方案））
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String businessId;

    /**
     * 主业务ID
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String mainBusinessId;

    /**
     * 合并类型（1：工作方案，2：收文）
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String businessType;

    /**
     * 收文ID（发文可以在没发文前选择合并发文的项目ID，合并发文则公用一个发文编号）
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String signId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getMainBusinessId() {
        return mainBusinessId;
    }

    public void setMainBusinessId(String mainBusinessId) {
        this.mainBusinessId = mainBusinessId;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }
}
