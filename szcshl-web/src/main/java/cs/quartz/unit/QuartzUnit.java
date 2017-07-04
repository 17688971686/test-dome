package cs.quartz.unit;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import cs.domain.sys.Workday;
import cs.service.sys.WorkdayService;

/**
 * 任务管理器工具类
 * @author MCL
 *@date 2017年7月3日 上午11:46:11 
 */
@Component
public class QuartzUnit {
	
	@Autowired
	private WorkdayService workdayService;
	
	/**计算工作日
	 * @param beginTime
	 * @return
	 */
	public int countWorkday(Date beginTime) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		int result=0;
		Date endTime=new Date();
		Calendar c1=Calendar.getInstance();
		Calendar c2=Calendar.getInstance();
		c1.setTime(beginTime);
		c2.setTime(endTime);
		while(c1.compareTo(c2)<=0){
			if(c1.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || c1.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){	//判断是否是周末
				List<Workday> workdayList=workdayService.selectSpecialDays("2");//判断周末是否需要加班的
				for(Workday workday:workdayList){
					Calendar c3=Calendar.getInstance();
					c3.setTime(workday.getDates());
					if(c1.compareTo(c3)==0){
						result++;
					}
				}
			}else{//不是周末，判断周一到周五是否有调休的，有天数减1
				List<Workday> workdayList=workdayService.selectSpecialDays("1");
				boolean flag=false;
				for(Workday workday: workdayList){
					Calendar c4=Calendar.getInstance();
					c4.setTime(workday.getDates());
					if(c1.compareTo(c4)==0){
						flag=true;
						result--;
					}
				}
				if(!flag){
					result++;
				}
			}
			c1.add(c1.DATE, 1);//日期加1
		}
		return result;
	}


}
