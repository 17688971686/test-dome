package cs.repository;

import java.io.Serializable;

/**
 * Description: 实体字段信息类
 * Author: tzg
 * Date: 2017/6/1 11:25
 */
public class EntityFieldInfo implements Serializable {

    private static final long serialVersionUID = -7290706947042089210L;

    private String fieldName;
    private String fieldType;
    private String fieldColumn;

    public EntityFieldInfo() {
    }

    public EntityFieldInfo(String fieldName, String fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldColumn() {
        return fieldColumn;
    }

    public void setFieldColumn(String fieldColumn) {
        this.fieldColumn = fieldColumn;
    }
}