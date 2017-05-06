package cs.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@MappedSuperclass
public abstract class DomainBase {
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable = false, columnDefinition = "date")
    private Date createdDate = new Date();
    @Column(updatable = false, nullable = false, columnDefinition = "varchar(255)")
    private String createdBy = "";

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, columnDefinition = "date")
    private Date modifiedDate = new Date();
    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String modifiedBy = "";

    //@Column(columnDefinition="int(11)")
    private int itemOrder;

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public int getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(int itemOrder) {
        this.itemOrder = itemOrder;
    }


}
