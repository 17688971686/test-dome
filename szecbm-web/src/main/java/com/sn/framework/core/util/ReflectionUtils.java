package com.sn.framework.core.util;

import com.sn.framework.core.common.Validate;
import org.springframework.beans.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.*;

/**
 * 反射工具类.
 * 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 *
 * @author calvin
 * @version 2013-01-15
 */
@SuppressWarnings("rawtypes")
public class ReflectionUtils {

    private static final String SETTER_PREFIX = "set";

    private static final String GETTER_PREFIX = "get";

    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);


    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     */
    public static Object getFieldValue(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }

        Object result = null;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常{}", e.getMessage());
        }
        return result;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     */
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            logger.error("Could not find field [" + fieldName + "] on target [" + obj + "]");
            return;
            //throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }
        try {
            field.set(obj, convert(value, field.getType()));
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }
    }

    public static Object convert(Object object, Class<?> type) {
        if (object instanceof Number) {
            Number number = (Number) object;
            if (type.equals(byte.class) || type.equals(Byte.class)) {
                return number.byteValue();
            }
            if (type.equals(short.class) || type.equals(Short.class)) {
                return number.shortValue();
            }
            if (type.equals(int.class) || type.equals(Integer.class)) {
                return number.intValue();
            }
            if (type.equals(long.class) || type.equals(Long.class)) {
                return number.longValue();
            }
            if (type.equals(float.class) || type.equals(Float.class)) {
                return number.floatValue();
            }
            if (type.equals(double.class) || type.equals(Double.class)) {
                return number.doubleValue();
            }
        }
        if (type.equals(String.class)) {
            return object == null ? "" : object.toString();
        }
        return object;
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符.
     * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用.
     * 同时匹配方法名+参数类型，
     */
    public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
                                      final Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }

        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符，
     * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
     * 只匹配函数名，如果有多个同名函数调用第一个。
     */
    public static Object invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
        Method method = getAccessibleMethodByName(obj, methodName);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }

        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
     * <p>
     * 如向上转型到Object仍无法找到, 返回null.
     */
    public static Field getAccessibleField(final Object obj, final String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            } catch (NoSuchFieldException e) {//NOSONAR
                // Field不在当前类定义,继续向上转型
                continue;// new add
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * 匹配函数名+参数类型。
     * <p>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName,
                                             final Class<?>... parameterTypes) {

        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            try {
                Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                makeAccessible(method);
                return method;
            } catch (NoSuchMethodException e) {
                // Method不在当前类定义,继续向上转型
                continue;// new add
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * 只匹配函数名。
     * <p>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method getAccessibleMethodByName(final Object obj, final String methodName) {

        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier
                .isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处
     * 如无法找到, 返回Object.class.
     * eg.
     * public UserDao extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassGenricType(final Class clazz) {
        return getClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * 如无法找到, 返回Object.class.
     * <p>
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */
    public static Class getClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }

    public static Class<?> getUserClass(Object instance) {
        Assert.notNull(instance, "Instance must not be null");
        Class clazz = instance.getClass();
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;

    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }

    /**
     * 判断某个对象是否拥有某个属性
     *
     * @param obj       对象
     * @param fieldName 属性名
     * @return 有属性返回true
     * 无属性返回false
     */
    public static boolean hasField(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            return false;
        }
        return true;

    }

    /**
     * 获取两个对象同名属性内容不相同的列表
     *
     * @param class1 old对象
     * @param class2 new对象
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     */
    public static List<Map<String, Object>> compareTwoClass(Object class1, Object class2,String... ingoreProps) throws ClassNotFoundException, IllegalAccessException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //获取对象的class
        Class<?> clazz1 = class1.getClass();
        Class<?> clazz2 = class2.getClass();

        List<Field> field1 = getClassAllField(clazz1);
        List<Field> field2 = getClassAllField(clazz2);
        List<String> ignoreList = (ingoreProps != null ? Arrays.asList(ingoreProps) : null);
        //遍历属性列表field1
        for (int i = 0,l=field1.size(); i < l; i++) {
            String fieldName1 = field1.get(i).getName();
            if(Validate.isList(ignoreList) && ignoreList.contains(fieldName1)){
                continue;
            }
            //遍历属性列表field2
            for (int j = 0,k=field2.size(); j < k; j++) {
                String fieldName2 = field2.get(j).getName();
                //如果field1[i]属性名与field2[j]属性名内容相同
                if (fieldName1.equals(fieldName2)) {
                    //如果field1[i]属性值与field2[j]属性值内容不相同
                    Object value1 = getFieldValueByName(fieldName1, class1),value2 = getFieldValueByName(fieldName2, class2);
                    if (!compareTwo(value1,value2)) {
                        Map<String, Object> map2 = new HashMap<String, Object>();
                        map2.put("name", fieldName1);
                        map2.put("old", value1);
                        map2.put("new", value2);
                        list.add(map2);
                    }
                    break;
                }
            }
        }
        return list;
    }

    /* 根据属性名获取属性值
         * */
    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取属性名数组
     */
    private static String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 对比两个数据是否内容相同
     *
     * @param object1,object2
     * @return boolean类型
     */
    public static boolean compareTwo(Object object1, Object object2) {
        if (object1 == null && object2 == null) {
            return true;
        }
        if (object1 == null && object2 != null) {
            return false;
        }
        if (object1.equals(object2)) {
            return true;
        }
        return false;
    }


    public static List<Field> getClassAllField(Class objClass) {
        List<Field> fieldList = new ArrayList<>();
        Class<?> current = objClass;
        while (current != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass(); //得到父类,然后赋给自己
        }
        /*for (Field f : fieldList) {
            System.out.println("getAllFields---"+f.getName());
        }*/
        return fieldList;
    }
}

