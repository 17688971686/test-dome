package cs.model.sys;

import cs.domain.DomainBase;
import cs.model.BaseDto;

/**
 * Description: 表头管理
 * Author: mcl
 * Date: 2017/9/12 14:22
 */
public class HeaderDto extends BaseDto {

    private String id;

    private String headerName;//表头名称

    private String headerKey;//表头值

    private String headerType;//类型

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