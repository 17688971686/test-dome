package cs.freemarker;

import cs.common.utils.Validate;
import freemarker.ext.beans.BeanModel;
import freemarker.template.*;

public class FMUtils {
	
	public static String getAsString(TemplateModel model) {
		if(model == null) return null;
		if(model.getClass().getSimpleName().equals("SimpleNumber")) { // 处理数字类型的数据
			return ((SimpleNumber) model).toString();
		}
		return ((SimpleScalar) model).getAsString();  // 处理字符串类型的数据
	}
	
	public static String getAsString(TemplateModel model, String key) throws TemplateModelException {
		if(model == null || Validate.isEmpty(key)) return null;
		if(model.getClass().getSimpleName().equals("SimpleHash")) { // 处理hash类型的数据
			return getAsString(((SimpleHash) model).get(key));
		}
		return getAsString(((BeanModel) model).get(key));  // 处理ben对象类型的数据
	}
	
	public static Double getAsDouble(TemplateModel model) {
		if(model == null) return null;
		if(model.getClass().getSimpleName().equals("SimpleNumber")) { // 处理数字类型的数据
			return ((SimpleNumber) model).getAsNumber().doubleValue();
		}
		return Double.valueOf(((SimpleScalar) model).toString());  // 处理字符串类型的数据
	}
	
	public static Double getAsDouble(TemplateModel model, String key) throws TemplateModelException {
		if(model == null || Validate.isEmpty(key)) return null;
		if(model.getClass().getSimpleName().equals("SimpleHash")) { // 处理hash类型的数据
			return getAsDouble(((SimpleHash) model).get(key));
		}
		return getAsDouble(((BeanModel) model).get(key));  // 处理ben对象类型的数据
	}
	
}
