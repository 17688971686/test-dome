package cs.domain.sharing;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "cs_sharing_privilege")
@DynamicUpdate(true)
public class SharingPrivilege {

    @Id
    @GeneratedValue(generator= "sharePriviGenerator")
    @GenericGenerator(name= "sharePriviGenerator",strategy = "uuid2")
    private String id;

    /**
     * 业务ID（用户ID或者部门ID）
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String businessId;
    /**
     * 业务类型（用户或者部门,1是部门，2是用户，3是组）
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String businessType;

    /**
     * 共享ID
     */
    @ManyToOne
    @JoinColumn(name="sharId")
    private SharingPlatlform sharingPlatlform;

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

    public SharingPlatlform getSharingPlatlform() {
        return sharingPlatlform;
    }

    public void setSharingPlatlform(SharingPlatlform sharingPlatlform) {
        this.sharingPlatlform = sharingPlatlform;
    }
}
