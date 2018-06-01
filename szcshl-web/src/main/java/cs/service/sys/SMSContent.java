package cs.service.sys;

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
}
