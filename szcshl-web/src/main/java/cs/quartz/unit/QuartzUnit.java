package cs.quartz.unit;

import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.sys.Workday;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        Date now = new Date();
        String signDateString = DateUtils.date2String(signDate, DateUtils.DATE_PATTERN);
        String nowDateString = DateUtils.date2String(now, DateUtils.DATE_PATTERN);
        //签进来第二天才开始计数
        int result = getWorkDayNum(signDateString, nowDateString, DateUtils.DATE_PATTERN)-1;
        //2、是否计算当前日期
        if (Validate.isList(workdayList)) {
            int l = workdayList.size();
            //判断今天是否要计算工作日
            Workday workday = workdayList.get(l - 1);
            String workdayString = DateUtils.date2String(workday.getDates(), DateUtils.DATE_PATTERN);
            //如果今天已经计算日工作日，则先计算
            if (workdayString.equals(nowDateString)) {
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
            workdayList = filterWorkDay(workdayList, signDate);
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
        //签收日期
        Calendar c1 = Calendar.getInstance();
        c1.setTime(signDate);
        cleanHMSM(c1);

        List<Workday> resultList = new ArrayList<>();
        for (int i = 0, l = workdayList.size(); i < l; i++) {
            Workday workday = workdayList.get(i);
            Calendar cw = Calendar.getInstance();
            cw.setTime(workday.getDates());
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

    /**
     * 获取2个日期之间周六，周日的天数
     *
     * @param startDate
     * @param endDate
     * @param format
     * @return
     * @author zhaigx
     * @date 2013-3-13
     */
    public static int getWorkDayNum(String startDate, String endDate, String format) {
        List yearMonthDayList = new ArrayList();
        Date start = null, stop = null;
        try {
            start = new SimpleDateFormat(format).parse(startDate);
            stop = new SimpleDateFormat(format).parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (start.after(stop)) {
            Date tmp = start;
            start = stop;
            stop = tmp;
        }
        //将起止时间中的所有时间加到List中
        Calendar calendarTemp = Calendar.getInstance();
        calendarTemp.setTime(start);
        while (calendarTemp.getTime().getTime() <= stop.getTime()) {
            yearMonthDayList.add(new SimpleDateFormat(format)
                    .format(calendarTemp.getTime()));
            calendarTemp.add(Calendar.DAY_OF_YEAR, 1);
        }
        Collections.sort(yearMonthDayList);
        int num = 0;//周六，周日的总天数
        int size = yearMonthDayList.size();
        int week = 0;
        for (int i = 0; i < size; i++) {
            String day = (String) yearMonthDayList.get(i);
            if (isWorkDay(day, DateUtils.DATE_PATTERN)) {
                num++;
            }
        }
        return num;
    }

    /**
     * 获取某个日期是星期几
     *
     * @param date
     * @param format
     * @return 0-星期日
     * @author zhaigx
     * @date 2013-3-13
     */
    public static boolean isWorkDay(String date, String format) {
        Calendar calendarTemp = Calendar.getInstance();
        try {
            calendarTemp.setTime(new SimpleDateFormat(format).parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int i = calendarTemp.get(Calendar.DAY_OF_WEEK);
        if (i == Calendar.SATURDAY || i == Calendar.SUNDAY) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        List<Workday> workdayList = new ArrayList<>();
        Workday workday = new Workday();
        workday.setDates(DateUtils.converToDate("2018-01-02", "yyyy-MM-dd"));
        workday.setStatus("1");
        workdayList.add(workday);

        Workday workday2 = new Workday();
        workday2.setDates(DateUtils.converToDate("2018-04-01", "yyyy-MM-dd"));
        workday2.setStatus("1");
        workdayList.add(workday2);
        //int i = getSundayNum("2018-4-9", "2018-04-12", "yyyy-MM-dd");
        //System.out.println(i);
        System.out.println(countWorkday(workdayList, DateUtils.converToDate("2018-04-12", "yyyy-MM-dd")));
    }
}
