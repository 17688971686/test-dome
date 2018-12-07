package cs.freemarker;

import cs.common.utils.Validate;
import freemarker.ext.beans.BeanModel;
import freemarker.template.*;

public class FMUtils {
	
	public static String getAsString(TemplateModel model) {
		if(model == null) {
			return null;
		}
		if(model.getClass().getSimpleName().equals("SimpleNumber")) { // 澶勭悊鏁板瓧绫诲瀷鐨勬暟鎹�
			return ((SimpleNumber) model).toString();
		}
		return ((SimpleScalar) model).getAsString();  // 澶勭悊瀛楃涓茬被鍨嬬殑鏁版嵁
	}
	
	public static String getAsString(TemplateModel model, String key) throws TemplateModelException {
		if(model == null || Validate.isEmpty(key)) {
			return null;
		}
		if(model.getClass().getSimpleName().equals("SimpleHash")) { // 澶勭悊hash绫诲瀷鐨勬暟鎹�
			return getAsString(((SimpleHash) model).get(key));
		}
		return getAsString(((BeanModel) model).get(key));  // 澶勭悊ben瀵硅薄绫诲瀷鐨勬暟鎹�
	}
	
	public static Double getAsDouble(TemplateModel model) {
		if(model == null) {
			return null;
		}
		if(model.getClass().getSimpleName().equals("SimpleNumber")) { // 澶勭悊鏁板瓧绫诲瀷鐨勬暟鎹�
			return ((SimpleNumber) model).getAsNumber().doubleValue();
		}
		return Double.valueOf(((SimpleScalar) model).toString());  // 澶勭悊瀛楃涓茬被鍨嬬殑鏁版嵁
	}
	
	public static Double getAsDouble(TemplateModel model, String key) throws TemplateModelException {
		if(model == null || Validate.isEmpty(key)) {
			return null;
		}
		if(model.getClass().getSimpleName().equals("SimpleHash")) { // 澶勭悊hash绫诲瀷鐨勬暟鎹�
			return getAsDouble(((SimpleHash) model).get(key));
		}
		return getAsDouble(((BeanModel) model).get(key));  // 澶勭悊ben瀵硅薄绫诲瀷鐨勬暟鎹�
	}
	
}
