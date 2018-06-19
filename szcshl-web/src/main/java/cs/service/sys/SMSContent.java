package cs.service.sys;

import cs.domain.sys.User;

import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */
public interface SMSContent {
    /**
     * 封装短信发送内容
     * @param projectOrTask
     * @param name
     * */
    public String get(String projectOrTask,String name);

    /**
     * 封装短信发送失败内容
     * @param projectOrTask
     * @param name
     * @param type
     * */
    public String seekSMSSuccee(String projectOrTask,String name,String type);


    /**
     * 判断是否已经已发送过短信.规则是发送过短信就不再发送短信
     * @param projectName
     * @param fileCode
     * @param type
     * */
    public String orNotsendSMS(List<User> list, String projectName, String fileCode, String type, String infoType,String xianzhiNumber);

    /**
     * 判断是否已经已发送过短信.规则是发送过短信就不再发送短信
     * @param projectName
     * @param fileCode
     * @param type
     * */
    public String querySmsNumber(List<User> list, String projectName, String fileCode, String type, String infoType,String xianzhiNumber);

}
