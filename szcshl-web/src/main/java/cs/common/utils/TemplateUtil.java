package cs.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.icu.text.SimpleDateFormat;

import cs.domain.meeting.RoomBooking;
import cs.service.meeting.RoomBookingSerivce;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

/**
 * 模板工具类
 */

public class TemplateUtil {
    /**
     * freeemarker配置信息
     */
    protected static Configuration cfg = null;
    private static Map<String, Template> allTemplates = null;   //模板缓存类
    private static String suffix = ".ftl";

    /**
     * 获取freemarker的配置 freemarker本身支持classpath,目录和从ServletContext获取.
     *
     * @return 返回Configuration对象
     */
    static Configuration getConfiguration() {
        cfg = new Configuration(Configuration.getVersion());
        cfg.setClassForTemplateLoading(TemplateUtil.class, "/template");
        // setEncoding这个方法一定要设置国家及其编码，不然在flt中的中文在生成html后会变成乱码
        cfg.setEncoding(Locale.getDefault(), "UTF-8");

        // 设置对象的包装器
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        // 设置异常处理器//这样的话就可以${a.b.c.d}即使没有属性也不会出错
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        //初始化模板
        allTemplates = new HashMap<>();
        return cfg;
    }


    public static File createDoc(Map<?, ?> dataMap, String templateName, String outputFile) {
        if(cfg == null){
            cfg = getConfiguration();
        }
        File f = new File(outputFile);
        Template t = allTemplates.get(templateName);
        try {
            if(t == null ){
                t = cfg.getTemplate(templateName+suffix);
                allTemplates.put(templateName,t);
            }
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
            t.process(dataMap, w);
            w.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return f;
    }



    public static void main(String[] args){
    	
    	/*Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //获取本周一的日期
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date  monday=cal.getTime();
        String MONDAY =df.format(monday);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        Date tuseday = cal.getTime();
        String TUESDAY = df.format(tuseday);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        Date wednesday =cal.getTime();
        String WEDNESDAY = df.format(wednesday);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        Date thursday = cal.getTime();
        String THURSDAY = df.format(thursday);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        Date friday = cal.getTime();
        String FRIDAT = df.format(friday);*/
    /*	
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        //星期一
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date nextMonday=cal.getTime();
        String MONDAY = df.format(nextMonday);
        
        //星期二
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date nextTuesday = cal.getTime();
        String TUESDAY = df.format(nextTuesday);
        //星期三
        cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date nextWednesday = cal.getTime();
        String WEDNESDAY =df.format(nextWednesday);
        
        //星期四
        cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date nextThursday = cal.getTime();
        String THURSDAY =   df.format(nextThursday);
        
        //星期五
        cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date nextFriday = cal.getTime();
        String FRIDAY = df.format(nextFriday);
        
        //星期六
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date nextSatday = cal.getTime();
      
        //获取下周星期日
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        Date nextSunday=cal.getTime();
        
        Map<String,Object> dataMap = new HashMap<>();
        Map<String,Object> dataMap1 = new HashMap<>();
        dataMap.put("projectName","系统测试项目");
        dataMap.put("reviewStage","科研究型阶段");
        TemplateUtil.createDoc(dataMap,"notice","E:\\szcshl_upload\\test.doc");
      
        dataMap.put("MONDAY",MONDAY);
        dataMap.put("TUESDAY",TUESDAY);
        dataMap.put("WEDNESDAY",WEDNESDAY);
        dataMap.put("THURSDAY",THURSDAY);
        dataMap.put("FRIDAY",FRIDAY);
        dataMap.put("stageProject","资金申请报告书");
        
       File docFile =  TemplateUtil.createDoc(dataMap,"nextStageMeeting","E:\\szcshl_upload\\nexstmeeating.doc");
*/
//        System.out.print(docFile);
//    	nextWeekMeeting();
    	
    	 Map<String,Object> dataMap = new HashMap<>();
    	 List<String> timeList=new ArrayList<String>();
    	 timeList.add("星期一");
    	 timeList.add("星期二");
    	 timeList.add("星期三");
    	 timeList.add("星期四");
    	 timeList.add("星期五");
    	 timeList.add("星期六");
    	 timeList.add("星期日");
    	 List<String> contentList=new ArrayList<String>();
    	 contentList.add("aaa1");
    	 contentList.add("aaa2");
    	 contentList.add("aaa3");
    	 contentList.add("aaa4");
    	 contentList.add("aaa5");
    	 contentList.add("aaa6");
    	 contentList.add("aaa7");
       
         dataMap.put("TITLE","评审会");
         dataMap.put("START","2017-07-10");
         dataMap.put("END","2017-07-14");
         dataMap.put("contentList",contentList);
         dataMap.put("timeList", timeList);
         TemplateUtil.createDoc(dataMap,"exportRoom","E:\\szcshl_upload\\1234.doc");
        
    }
    
    public static void nextWeekMeeting(){
    	
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        //星期一
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date nextMonday=cal.getTime();
        
        //星期二
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date nextTuesday = cal.getTime();
        //星期三
        cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date nextWednesday = cal.getTime();
        
        //星期四
        cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date nextThursday = cal.getTime();
        
        //星期五
        cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date nextFriday = cal.getTime();
        
        //星期六
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date nextSatday = cal.getTime();
      
        //获取下周星期日
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        Date nextSunday=cal.getTime();
        
        System.out.println(nextMonday);
        System.out.println(nextTuesday);
        System.out.println(nextWednesday);
        System.out.println(nextThursday);
        System.out.println(nextFriday);
        System.out.println(nextSunday);

    }
}
