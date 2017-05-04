package cs.common.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 对象拷贝工具类
 * @author ldm
 *
 */
public class BeanCopierUtils {
	public static Map<String,BeanCopier> beanCopierMap = new HashMap<String,BeanCopier>();  
    
	public static void copyProperties(Object source, Object target){  
        String beanKey =  generateKey(source.getClass(), target.getClass());  
        BeanCopier copier =  null;  
        if(!beanCopierMap.containsKey(beanKey)){  
             copier = BeanCopier.create(source.getClass(), target.getClass(), false);  
             beanCopierMap.put(beanKey, copier);  
        }else{  
             copier = beanCopierMap.get(beanKey);  
        }  
        copier.copy(source, target,null);  
    }     
    private static String generateKey(Class<?> class1,Class<?>class2){  
        return class1.toString() + class2.toString();  
    }  
    
    public static String[] getNullPropertyNames(Object source) {  
        final BeanWrapper src = new BeanWrapperImpl(source);  
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();  
   
        Set<String> emptyNames = new HashSet<String>();  
        for (java.beans.PropertyDescriptor pd : pds) {  
            Object srcValue = src.getPropertyValue(pd.getName());  
            if (srcValue == null)  
                emptyNames.add(pd.getName());  
        }  
        String[] result = new String[emptyNames.size()];  
        return emptyNames.toArray(result);  
    }
    
    public static void copyPropertiesIgnoreNull(Object source, Object target) {  
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));  
    }  
}