package cs.quartz.unit;

import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.sys.Quartz;
import cs.domain.sys.Workday;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Description: 预计发文日期计算
 * Author: mcl
 * Date: 2018/5/7 11:56
 */
public class DispathUnit {

    /**
     * 计算预计发文日期 = 从签收日期到剩余工作日为1的日期
     * @param workdayList
     * @param signDate
     * @param reviewDay
     * @return
     */
    public static Date dispathDate(List<Workday> workdayList , Date signDate , int reviewDay){

        Date d = signDate;

        for(int j = 1 , l = reviewDay ; j <= l ; j++) {
            d = DateUtils.addDay(signDate, j);
            Boolean flag = false;
            if(Validate.isList(workdayList)){
                for( int i = 0 ; i < workdayList .size() ; i++ ) {
                    Workday workday = workdayList.get(i);
                    //1、判断两个时间是否一样
                    if(compareDate(d , workday.getDates())){
                        flag = true;
                        //将工作日改为休息日，则日期加1
                        if (!QuartzUnit.isWeekend(workday.getDates()) && "1".equals(workday.getStatus())) {
                            l++;
                            //将休息日改成工作日的，日期减1
                        } else if (QuartzUnit.isWeekend(workday.getDates()) && "2".equals(workday.getStatus())) {
                            l--;
                        }
                        break;
                    }
                }
            }
            if(!flag){

            }//判断是不是周末
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            if( cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                    || cal.get(Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY){
                l++;
            }
        }
        return d;

    }

    /**
     * 比较两个日期之间的大小
     * @param startDate
     * @param endDate
     * @return
     */
    public static Boolean compareDate(Date startDate , Date endDate){

        String startStr = DateUtils.converToString(startDate , "yyyy-MM-dd");
        String endStr = DateUtils.converToString(endDate , "yyyy-MM-dd");

        Date start = DateUtils.converToDate1(startStr , "yyyy-MM-dd");
        Date end = DateUtils.converToDate1(endStr , "yyyy-MM-dd");

        if(start.getTime() == end.getTime()){
            return true ;
        }else{
            return false;
        }

    }


    public static void main(String[] args){

        List<Workday> workdayList = new ArrayList<>();
        Workday workday = new Workday();
        workday.setDates(DateUtils.converToDate("2018-01-02", "yyyy-MM-dd"));
        workday.setStatus("1");
        workdayList.add(workday);

        Workday workday2 = new Workday();
        workday2.setDates(DateUtils.converToDate("2018-04-28", "yyyy-MM-dd"));
        workday2.setStatus("2");
        workdayList.add(workday2);

        Workday workday3 = new Workday();
        workday3.setDates(DateUtils.converToDate("2018-04-30", "yyyy-MM-dd"));
        workday3.setStatus("1");
        workdayList.add(workday3);

        Workday workday4 = new Workday();
        workday4.setDates(DateUtils.converToDate("2018-05-01", "yyyy-MM-dd"));
        workday4.setStatus("1");
        workdayList.add(workday4);

        Date start = DateUtils.converToDate1("2018-04-26" , "yyyy-MM-dd");
//        Date end = DateUtils.addDay(start , 12);
//        System.out.println(DateUtils.converToString(end , "yyyy-MM-dd"));
//       int work = 12- QuartzUnit.getWorkDayNum(DateUtils.date2String(start , "yyyy-MM-dd")
//               , DateUtils.date2String(end , "yyyy-MM-dd") , "yyyy-MM-dd") + 1;
        System.out.println(dispathDate(workdayList , start , 12));
    }

}