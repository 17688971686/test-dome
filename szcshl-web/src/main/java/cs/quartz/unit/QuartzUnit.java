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
            //如果当前日期跟签收日期不在同一周
            if (betweenDay > startSatOffset) {
                result = (betweenDay - startSatOffset) / 7;
                result = result * 5;      //计算完整的工作日
                result += (startSatOffset > 5) ? 5 : startSatOffset;           //加上离第一个周六最近工作日
                //计算当前日期里周日有几天
                startSatOffset = weekDay2 - Calendar.SUNDAY;
                result += (startSatOffset > 5) ? 5 : startSatOffset;           //加上离最后一个周日的工作日
            } else {
                result = betweenDay;
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
                    //将工作日改为休息日，则日期减1
                    if (!isWeekend(workday.getDates()) && "1".equals(workday.getStatus())) {
                        result--;
                        //将休息日改成工作日的，日期加1
                    } else if (isWeekend(workday.getDates()) && "2".equals(workday.getStatus())) {
                        result++;
                    }
                }

                //过滤工作日，只要从签收日期之后的即可
                workdayList = filterWorkDay(workdayList, c1);
                if (Validate.isList(workdayList)) {
                    for (int i = 0; i < workdayList.size(); i++) {
                        Workday checkWordDay = workdayList.get(i);
                        //将工作日改为休息日，则日期减1
                        if (!isWeekend(checkWordDay.getDates()) && "1".equals(checkWordDay.getStatus())) {
                            result--;
                            //将休息日改成工作日的，日期加1
                        } else if (isWeekend(checkWordDay.getDates()) && "2".equals(checkWordDay.getStatus())) {
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
     * @param c1
     * @return
     */
    private static List<Workday> filterWorkDay(List<Workday> workdayList, Calendar c1) {
        List<Workday> resultList = new ArrayList<>();
        for (int i = 0, l = workdayList.size(); i < l; i++) {
            Workday workday = workdayList.get(i);
            Calendar cw = Calendar.getInstance();
            c1.setTime(workday.getDates());
            cleanHMSM(cw);
            if (cw.compareTo(c1) > -1) {
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

    public static boolean isWeekend(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        List<Workday> workdayList = new ArrayList<>();
        Workday workday = new Workday();
        workday.setDates(DateUtils.converToDate("2018-01-02", "yyyy-MM-dd"));
        workday.setStatus("1");
        workdayList.add(workday);

        Workday workday2 = new Workday();
        workday2.setDates(DateUtils.converToDate("2018-01-05", "yyyy-MM-dd"));
        workday2.setStatus("1");
        workdayList.add(workday2);
        System.out.println(countWorkday(workdayList, DateUtils.converToDate("2018-01-02", "yyyy-MM-dd")));
    }
}
