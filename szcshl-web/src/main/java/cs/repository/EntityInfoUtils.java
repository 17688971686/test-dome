package cs.repository;

import cs.common.utils.StringUtil;
import org.apache.log4j.Logger;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 实体信息操作工具类
 * Author: tzg
 * Date: 2017/6/1 11:29
 */
public class EntityInfoUtils {

    private static final Logger logger = Logger.getLogger(EntityInfoUtils.class);

    /*
     * 缓存实体信息
     */
    private static final Map<String, EntityInfo> entityInfoCache = new ConcurrentHashMap<String, EntityInfo>();

    /**
     * 获取实体映射信息
     *
     * @param cls 反射实体类
     * @return
     */
    public static EntityInfo getEntityInfo(Class<?> cls) {
        return entityInfoCache.get(cls.getName());
    }

    /**
     * 实体类反射获取表信息【初始化】
     *
     * @param cls 反射实体类
     * @return
     */
    public synchronized static EntityInfo initEntityInfo(Class<?> cls) {
        EntityInfo entityInfo = getEntityInfo(cls);
        if (entityInfo != null) {
            return entityInfo;
        }
        entityInfo = new EntityInfo();

        /* 表名 */
        Table table = cls.getAnnotation(Table.class);
        String tableName = null;
        if (table != null && StringUtil.isNotBlank(table.name())) {
            tableName = table.name();
        }
        if (tableName == null) {
            logger.warn("Warn: Entity @Table Not Found!");
        }
        entityInfo.setTableName(tableName);
        // 实体字段
        entityInfo.setFieldInfos(entityFieldInfos(cls, entityInfo));
        entityInfoCache.put(cls.getName(), entityInfo);
        return entityInfo;
    }

    /**
     * 获取实体类所有字段
     *
     * @param cls
     * @param entityInfo
     */
    private static Set<EntityFieldInfo> entityFieldInfos(Class<?> cls, EntityInfo entityInfo) {
        Set<EntityFieldInfo> fieldInfos = new LinkedHashSet<EntityFieldInfo>();
        Field[] fields = cls.getDeclaredFields();
        EntityFieldInfo fieldInfo;
        String columnName;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                fieldInfo = new EntityFieldInfo(field.getName(), field.getType().getName());
                if (field.isAnnotationPresent(Column.class)) {
                    columnName = field.getAnnotation(Column.class).name();
                    if (StringUtil.isNotBlank(columnName)) {
                        fieldInfo.setFieldColumn(field.getAnnotation(Column.class).name());
                    }
                }
                entityInfo.setIdField(fieldInfo);
            } else if (field.isAnnotationPresent(Column.class)) {
                fieldInfo = new EntityFieldInfo(field.getName(), field.getType().getName());
                columnName = field.getAnnotation(Column.class).name();
                if (StringUtil.isNotBlank(columnName)) {
                    fieldInfo.setFieldColumn(field.getAnnotation(Column.class).name());
                }
                fieldInfos.add(fieldInfo);
            }
        }
        return fieldInfos;
    }

}