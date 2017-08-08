package cs.domain;

import cs.domain.project.WorkProgram;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

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

    /**
     * 属性拷贝方法
     * @param cls
     * @param ignoreProperties
     * @param <T>
     * @return
     */
    public <T> T convert(Class<T> cls, String... ignoreProperties) {
        Assert.notNull(cls);
        try {
            T entity = cls.newInstance();
            BeanUtils.copyProperties(this, entity, ignoreProperties);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error: Conversion Object Failed. Cause:" + e);
        }
    }
}
