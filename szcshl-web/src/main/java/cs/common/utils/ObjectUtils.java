package cs.common.utils;

import org.apache.log4j.Logger;

import javax.persistence.Id;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Description: 对象工具类
 * Author: tzg
 * Date: 2017/5/13 12:44
 */
public class ObjectUtils {

    private static final Logger logger = Logger.getLogger(ObjectUtils.class);

    /**
     * 把对象转成字符串
     *
     * @param object
     * @return
     */
    public static String toString(Object object) {
        if (null == object) {
            return null;
        }
        return String.valueOf(object);
    }

    /**
     * 把对象转为整数
     *
     * @param obj
     * @return
     */
    public static Integer toInteger(Object obj) {
        if (isEmpty(obj)) {
            return null;
        }
        return Integer.parseInt(toString(obj));
    }

    /**
     * 把对象转为长整数
     *
     * @param obj
     * @return
     */
    public static Long toLong(Object obj) {
        if (isEmpty(obj)) {
            return null;
        }
        return Long.parseLong(toString(obj));
    }

    /**
     * 把对象转为双精度型
     *
     * @param obj
     * @return
     */
    public static Double toDouble(Object obj) {
        if (isEmpty(obj)) {
            return null;
        }
        return Double.parseDouble(toString(obj));
    }

    /**
     * 判断对象是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (null == obj) return true;
        if (obj instanceof CharSequence) {
            return toString(obj).trim().isEmpty(); // 去除前后空格
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        } else if (obj.getClass().isArray()) {
            return isAnyEmpty((Object[]) obj);
        } else if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        return false;
    }

    /**
     * 判断对象不为空
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 判断多个对象是否为空，只要有一个为空，就返回false
     *
     * @param objects
     * @return
     */
    public static boolean isAnyEmpty(Object... objects) {
        boolean flag = false;
        for (int i = 0, len = objects.length; i < len; i++) {
            flag = isEmpty(objects[i]);
            if (flag) {
                break;
            }
        }
        return flag;
    }

    /**
     * 判断对象不为空
     *
     * @param objects
     * @return
     */
    public static boolean isNoneEmpty(Object... objects) {
        return !isAnyEmpty(objects);
    }

    /**
     * 设置对象的属性值
     *
     * @param obj       字段所属对象
     * @param fieldName 字段名
     * @param value     字段值
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public static void setFieldValue(Object obj, String fieldName, Object value) throws IllegalAccessException, NoSuchFieldException {
        if (ObjectUtils.isAnyEmpty(obj, fieldName)) {
            throw new IllegalArgumentException("参数有误");
        }
        Field f = obj.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(obj, getAttrValue(f, value));
    }

    /**
     * 获取字段值
     *
     * @param f
     * @param attrVal
     * @return
     */
    public static Object getAttrValue(Field f, Object attrVal) {
        if (f.getType().equals(Long.class)) {
            attrVal = ObjectUtils.toLong(attrVal);
        } else if (f.getType().equals(Integer.class)) {
            attrVal = ObjectUtils.toInteger(attrVal);
        } else if (f.getType().equals(Date.class)) {
            attrVal = new Date();
        } else if (f.getType().equals(String.class)) {
            attrVal = toString(attrVal);
        }
        return attrVal;
    }

    /**
     * 调用set方法设置字段值
     *
     * @param obj       字段所属对象
     * @param fieldName 字段名
     * @param value     字段值
     */
    public static void methodInvokeSetFieldValue(Object obj, String fieldName, Object value) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (StringUtil.isBlank(fieldName)) {
            throw new IllegalArgumentException("参数fieldName有误");
        }
        methodInvoke(obj, "set".concat(StringUtil.upperCaseFirst(fieldName)), value);
    }

    /**
     * 获取对象的属性值
     *
     * @param obj       字段所属对象
     * @param fieldName 字段名
     * @param <T>
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static <T> T getFieldValue(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        if (ObjectUtils.isAnyEmpty(obj, fieldName)) {
            throw new IllegalArgumentException("参数有误");
        }
        Field f = obj.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        return (T) f.get(obj);
    }

    /**
     * 通过get方法获取字段值
     *
     * @param obj       字段所属对象
     * @param fieldName 字段名
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public static <T> T methodInvokeGetFieldValue(Object obj, String fieldName) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (StringUtil.isBlank(fieldName)) {
            throw new IllegalArgumentException("参数fieldName有误");
        }
        return (T) methodInvoke(obj, "get".concat(StringUtil.upperCaseFirst(fieldName)));
    }

    /**
     * 调用对象的方法
     *
     * @param obj        方法所属对象
     * @param methodName 方法名
     * @param args       方法入参
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public static <T> T methodInvoke(Object obj, String methodName, Object... args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (ObjectUtils.isEmpty(obj)) {
            throw new IllegalArgumentException("参数有误");
        }
        return methodInvoke((Class<T>) obj.getClass(), obj, methodName, args);
    }

    /**
     * 调用对象方法
     *
     * @param cls
     * @param obj
     * @param methodName
     * @param args
     * @param <T>
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public static <T> T methodInvoke(Class<T> cls, Object obj, String methodName, Object... args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (ObjectUtils.isAnyEmpty(cls, obj, methodName)) {
            throw new IllegalArgumentException("参数有误");
        }
        Class[] argCls = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argCls[i] = args[i].getClass();
        }
        Method getM = cls.getMethod(methodName, argCls);
        return (T) getM.invoke(obj, args);
    }

    /**
     * 获取实体的ID字段（针对javax.persistence.Id注解）
     *
     * @param cls
     * @return
     */
    public static Field getIdField(Class cls) {
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(Id.class)) {
                return f;
            }
        }
        return null;
    }

    /**
     * 获取实体的主键值（针对javax.persistence.Id注解）
     *
     * @param entity
     * @return
     * @throws IllegalAccessException
     */
    public static Object getIdFieldValue(Object entity) throws IllegalAccessException {
        Field idField = getIdField(entity.getClass());
        idField.setAccessible(true);
        return idField.get(entity);
    }

    /**
     * 反射对象获取泛型
     *
     * @param cls   对象
     * @param index 泛型所在位置
     * @return Class
     */
    public static Class getSuperClassGenricType(final Class cls, final int index) {
        Type genType = cls.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            logger.warn(String.format("Warn: %s's superclass not ParameterizedType", cls.getSimpleName()));
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            logger.warn(String.format("Warn: Index: %s, Size of %s's Parameterized Type: %s .", index, cls.getSimpleName(), params.length));
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(String.format("Warn: %s not set the actual class on superclass generic parameter", cls.getSimpleName()));
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * Object 转BigDecimal
     * @param value
     * @return
     */
    public static BigDecimal getBigDecimal(Object value ) {
        BigDecimal ret = null;
        if( value != null ) {
            if( value instanceof BigDecimal ) {
                ret = (BigDecimal) value;
            } else if( value instanceof String ) {
                ret = new BigDecimal( (String) value );
            } else if( value instanceof BigInteger ) {
                ret = new BigDecimal( (BigInteger) value );
            } else if( value instanceof Number ) {
                ret = new BigDecimal( ((Number)value).doubleValue() );
            } else {
                throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");
            }
        }
        return ret;
    }
}