package cs.repository;

import cs.common.utils.ObjectUtils;
import cs.common.utils.StringUtil;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

/**
 * Description: 实体信息类
 * Author: tzg
 * Date: 2017/6/1 11:22
 */
public class EntityInfo implements Serializable {

    private static final long serialVersionUID = -2440009724326552612L;

    /**
     * 表名
     */
    private String tableName;
    /**
     * 主键字段
     */
    private EntityFieldInfo idField;

    /**
     * 实体字段
     */
    private Set<EntityFieldInfo> fieldInfos;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public EntityFieldInfo getIdField() {
        return idField;
    }

    public void setIdField(EntityFieldInfo idField) {
        this.idField = idField;
    }

    public Set<EntityFieldInfo> getFieldInfos() {
        return fieldInfos;
    }

    public void setFieldInfos(Set<EntityFieldInfo> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }

    /**
     * 获取指定字段的字段信息
     * @param fieldName
     * @return
     */
    public EntityFieldInfo getFieldInfo(String fieldName) {
        if(StringUtil.isBlank(fieldName)) return null;
        if (ObjectUtils.isNotEmpty(fieldInfos)) {
            Iterator<EntityFieldInfo> iterator = fieldInfos.iterator();
            while (iterator.hasNext()) {
                EntityFieldInfo fieldInfo = iterator.next();
                if (fieldInfo.getFieldName().equals(fieldName))
                    return fieldInfo;
            }
        }
        return null;
    }
}