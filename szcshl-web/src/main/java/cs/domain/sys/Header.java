package cs.domain.sys;

import cs.domain.DomainBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Description: 表头管理
 * Author: mcl
 * Date: 2017/9/12 14:10
 */
@Entity
@Table(name="cs_header")
public class Header extends DomainBase{

    @Id
    private String id;

    @Column(columnDefinition = "VARCHAR(255)")
    private String headerName;//表头名称

    @Column(columnDefinition = "VARCHAR(255)")
    private String headerKey;//表头值

    @Column(columnDefinition = "VARCHAR(255)")
    private String headerType;//类型

    @Column(columnDefinition = "VARCHAR(2)")
    private String headerstate;//表头状态 （9：选中 ， 0 ：未选）

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderKey() {
        return headerKey;
    }

    public void setHeaderKey(String headerKey) {
        this.headerKey = headerKey;
    }

    public String getHeaderType() {
        return headerType;
    }

    public void setHeaderType(String headerType) {
        this.headerType = headerType;
    }

    public String getHeaderstate() {
        return headerstate;
    }

    public void setHeaderstate(String headerstate) {
        this.headerstate = headerstate;
    }
}