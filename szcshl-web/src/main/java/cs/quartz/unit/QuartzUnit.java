package cs.quartz.unit;

import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.sys.Workday;
import cs.service.sys.WorkdayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 任务管理器工具类
 *
 * @author MCL
 * @date 2017年7月3日 上午11:46:11
 */
public class QuartzUnit {
    /**
     * 计算工作日
     *
     * @param signDate
     * @return
     */
    public static int countWorkday(List<Workday> workdayList, Date signDate) {
        int result = 0;

        //签收日期
        Calendar c1 = Calendar.getInstance();
        c1.setTime(signDate);
        cleanHMSM(c1);

        //当前日期
        Date now = new Date();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(now);
        cleanHMSM(c2);

        //如果当期日期大于签收日期，则计算
        if (c1.compareTo(c2) <= 0) {
            //1、计算当前日期-签收日期之间有多少个星期
            int weekDay1 = c1.get(Calendar.DAY_OF_WEEK);
            int weekDay2 = c2.get(Calendar.DAY_OF_WEEK);
            long totalDay = DateUtils.daysBetween(signDate, now);
            int betweenDay = new Long(totalDay).intValue();
            //离第一个星期六的天数
            int startSatOffset = Calendar.SATURDAY - weekDay1;//判断一年的第一天隔最近的星期六有几天
            //计算一共有几个星期六
            if (betweenDay > startSatOffset) {
                result = (betweenDay - startSatOffset) / 7;
                result = result * 5;      //计算完整的工作日
                result += (startSatOffset > 5) ? 5 : startSatOffset;           //加上离第一个周六最近工作日
                //计算当前日期里周日有几天
                startSatOffset = weekDay2 - Calendar.SUNDAY;
                result += (startSatOffset > 5) ? 5 : startSatOffset;           //加上离最后一个周日的工作日
            }

            //2、是否计算当前日期
            if (Validate.isList(workdayList)) {
                int l = workdayList.size();
                //判断今天是否要计算工作日
                Workday workday = workdayList.get(l - 1);
                Calendar c = Calendar.getInstance();
                c.setTime(workday.getDates());
                cleanHMSM(c);
                //如果今天已经计算日工作日，则先计算
                if (c2.compareTo(c) == 0) {
                    l = l - 1;
                    if ("1".equals(workday.getStatus())) {
                        result--;
                    } else {
                        result++;
                    }
                }

                //过滤工作日，只要从签收日期之后的即可
                workdayList = filterWorkDay(workdayList, signDate);
                if(workdayList != null && workdayList.size()>0){
                    for (int i = 0;  i < workdayList.size(); i++) {
                        //1表示将工作日改为休息日，则日期减1，否则日期加1
                        if ("1".equals(workdayList.get(i).getStatus())) {
                            result--;
                        } else {
                            result++;
                        }
                    }
                }

            }
        }
        return result;
    }

    /**
     * 按照给定的时间，过滤日期
     *
     * @param workdayList
     * @param signDate
     * @return
     */
    private static List<Workday> filterWorkDay(List<Workday> workdayList, Date signDate) {
        List<Workday> resultList = new ArrayList<>();
        for (int i = 0, l = workdayList.size(); i < l; i++) {
            Workday workday = workdayList.get(i);
            if (workday.getDates().after(signDate)) {
                resultList.add(workday);
            }
        }
        return resultList;
    }

    /**
     * 清除日期的小时、分、秒和毫秒
     *
     * @param calendar
     */
    private static void cleanHMSM(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
