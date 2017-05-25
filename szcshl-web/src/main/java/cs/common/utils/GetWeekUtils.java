package cs.common.utils;

import java.util.Calendar;
import java.util.Date;
/**
 * 
 * @author sjy
 * 获取当前时间是星期几
 *
 */
public class GetWeekUtils {

	public static String getWeek(Date date){
		String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if(week_index<0){
			week_index = 0;
		} 
		return weeks[week_index];
	}
}
