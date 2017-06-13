package cs.model;

import org.springframework.beans.BeanUtils;

/**
 * 基础页面数据映射模型
 * @author tzg
 * @param <T>   domain数据映射实体
 * @param <D>   model页面映射实体
 */
public abstract class BaseDto2<T> extends BaseDto {

    public BaseDto2() {

    }

    public BaseDto2(T source) {
        BeanUtils.copyProperties(source, this);
    }

    protected abstract Class<T> getCls();

    public T getDomain() {
        return getDomain(getCls());
    }

    public <T> T getDomain(Class<T> cls) {
        T target;
        try {
            target = cls.newInstance();
        } catch (Exception e) {
            return null;
        }
        BeanUtils.copyProperties(this, target);
        return target;
    }

}
