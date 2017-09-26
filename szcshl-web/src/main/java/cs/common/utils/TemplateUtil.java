package cs.common.utils;

import com.ibm.icu.text.SimpleDateFormat;
import cs.domain.sys.SysFile;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;

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
    	

    	
    	 Map<String,Object> dataMap = new HashMap<>();

         TemplateUtil.createDoc(dataMap,"budget/roster","G:\\test\\test.doc");
        
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

    /**
     * 生成模板并且同时生成附件
     * @param signId   收文ID
     * @param workProjectId   工作方案ID
     * @param mainType   附件模块
     * @param businessType   附件业务
     * @param reviewStage  评审阶段(项目阶段)
     * @param templateUrl  模板路径
     * @param fileName   文件名称
     * @param fileType   文件类型
     * @param dataMap    数据
     * @return
     */
    public static SysFile createTemplate(String signId , String workProjectId , String mainType , String businessType , String reviewStage,
                                        String templateUrl , String fileName , String fileType , Map<String , Object> dataMap){

        String  showName = fileName + fileType;
        String path = SysFileUtil.getUploadPath();
        String relativeFileUrl = SysFileUtil.generatRelativeUrl(path ,  mainType ,signId , businessType , showName);

        File docFile = createDoc(dataMap , templateUrl , path + File.separator + relativeFileUrl);
        SysFile sysFile = null;
        if(docFile !=null){
//    public SysFile(String sysFileId, String businessId, String fileUrl, String showName, Integer fileSize, String fileType,
//                    String mainId,String mainType, String sysfileType, String sysBusiType)
           sysFile = new SysFile(UUID.randomUUID().toString() , workProjectId , relativeFileUrl , showName ,
                    Integer.valueOf(String.valueOf(docFile.length())) , fileType , signId , mainType , reviewStage , businessType);
        }
        return sysFile;
    }
}
