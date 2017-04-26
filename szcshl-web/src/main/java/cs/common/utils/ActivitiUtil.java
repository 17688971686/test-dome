package cs.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程工具类
 * @author ldm
 *
 */
public class ActivitiUtil {

	/**
	 * 封装流程参数
	 * @param map
	 * @param users
	 * @param groups
	 * @param isClear
	 * @return
	 */
	public static Map<String,Object> flowArguments(Map<String,Object> map,String users,String groups,boolean isClear){
		if(map == null){
			map = new HashMap<String,Object>(2);
		}else{
			if(isClear){
				map.clear();
			}
		}
		
		if(Validate.isString(users)){
			map.put("users", users);
		}
		if(Validate.isString(groups)){
			map.put("groups", groups);
		}
		return map;
	}
}
