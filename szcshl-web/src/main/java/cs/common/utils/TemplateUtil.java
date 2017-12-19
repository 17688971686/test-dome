package cs.common.utils;

import com.ibm.icu.text.SimpleDateFormat;
import cs.common.Constant;
import cs.domain.sys.SysFile;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        if (cfg == null) {
            cfg = getConfiguration();
        }
        File f = new File(outputFile);
        Template t = allTemplates.get(templateName);
        try {
            if (t == null) {
                t = cfg.getTemplate(templateName + suffix);
                allTemplates.put(templateName, t);
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

    public static void nextWeekMeeting() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        //星期一
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date nextMonday = cal.getTime();

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
        Date nextSunday = cal.getTime();

        /*System.out.println(nextMonday);
        System.out.println(nextTuesday);
        System.out.println(nextWednesday);
        System.out.println(nextThursday);
        System.out.println(nextFriday);
        System.out.println(nextSunday);*/

    }

    /**
     * 生成模板并且同时生成附件
     *
     * @param signId       收文ID
     * @param mainType     附件模块
     * @param businessType 附件业务
     * @param reviewStage  评审阶段(项目阶段)
     * @param templateUrl  模板路径
     * @param fileName     文件名称
     * @param fileType     文件类型
     * @param dataMap      数据
     * @return
     */
    public static SysFile createTemplate(String signId, String mainType, String businessType, String reviewStage,
                                         String templateUrl, String fileName, String fileType, Map<String, Object> dataMap) {
        SysFile sysFile = null;
        String showName = fileName + fileType;
        String path = SysFileUtil.getUploadPath();
        String relativeFileUrl = SysFileUtil.generatRelativeUrl(path, mainType, signId, businessType, showName);
        File docFile = createDoc(dataMap, templateUrl, path + File.separator + relativeFileUrl);
        fileType = fileType.toLowerCase();//统一转成小写

        try {
           /* PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName); //系统业务属性文件名
            boolean result = FtpUtil.uploadFile(propertyUtil.readProperty(Constant.FTP_IP1), Integer.parseInt(propertyUtil.readProperty(Constant.FTP_PORT1)),
                    propertyUtil.readProperty(Constant.FTP_USER), propertyUtil.readProperty(Constant.FTP_PWD), propertyUtil.readProperty(Constant.FTP_BASE_PATH),
                    "", new String(showName.getBytes("GBK"), "ISO-8859-1"), new FileInputStream(docFile));
            if (result) {
                sysFile = new SysFile();
                sysFile = new SysFile(UUID.randomUUID().toString(), signId, relativeFileUrl, showName,
                        Integer.valueOf(String.valueOf(docFile.length())), fileType, signId, mainType, reviewStage, businessType);
                sysFile.setFtpIp(propertyUtil.readProperty(Constant.FTP_IP1));
                sysFile.setPort(propertyUtil.readProperty(Constant.FTP_PORT1));
                sysFile.setFtpUser(propertyUtil.readProperty(Constant.FTP_USER));
                sysFile.setFtpPwd(propertyUtil.readProperty(Constant.FTP_PWD));
                sysFile.setFtpBasePath(propertyUtil.readProperty(Constant.FTP_BASE_PATH));
                sysFile.setFtpFilePath("");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sysFile;
    }


    /**
     * 通过遍历实体类，获取属性名、属性类型、属性值，并添加到Map集合中（适用于导出功能所添加的数据过多）
     * @param obj
     * @return
     */
    public static Map<String , Object> entryAddMap(Object obj){
        Map<String , Object> dataMap = new HashMap<>();
        try{
            Class c = obj.getClass();
            //获取实体类的所有属性，返回field数字
            Field[] fields = c.getDeclaredFields();
            if(fields != null && fields.length > 0 ) {
                for(Field field : fields){

                    //获取属性名
                    String name = field.getName();
                    //将属性名的首字母转为大写，方便构造get、set方法
                    name = name.substring(0,1).toUpperCase()  + name.substring(1);
                    //构造get的方法
                    Method method = c.getMethod("get" + name);

                    //获取属性类型
                    String type = field.getGenericType().toString();
                    //如果数据类型是Date类型，则需要转换为yyyy年MM月dd日
                    if(type.equals("class java.util.Date")){
                        dataMap.put(field.getName() , DateUtils.converToString((Date) method.invoke(obj) , "yyyy年MM月dd日"));
                    }else{
                        dataMap.put( field.getName() , method.invoke(obj));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataMap;
    }



    public static void main(String[] args) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("sugProDealOriginal" , "9");
        dataMap.put("sugProDealCount" , 23);
//        dataMap.put("sugProAdviseOriginal" , "0");
//        TemplateUtil.createDoc(dataMap, "report/roster", "G:\\test\\建议书报审.doc");
        TemplateUtil.createDoc(dataMap, "print/dispatchDoc/dispatchXmjys", "G:\\test\\发文测试.doc");
    }
}
