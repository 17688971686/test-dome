package cs.service.sys;

import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/5/31.
 */
@Service
public class SMSContentImp implements SMSContent{

    //您收到一条待办项目|任务，项目名称|任务名称xxx，请及时处理。【评审中心项目管理系统】
    @Override
    public String get(String projectOrTask,String name) {
        StringBuffer stringBuffer=new StringBuffer("您收到一条待办");
        if (projectOrTask.contains("项")){
            stringBuffer.append(projectOrTask+",");
        }
        if (projectOrTask.contains("任")){
            stringBuffer.append(projectOrTask+",");
        }
        stringBuffer.append("项目名称"+name);
        stringBuffer.append(",请及时处理 ").append("\n");
        stringBuffer.append("【评审中心项目管理系统】");
        return stringBuffer.toString();
    }
    /**
     *
     * */
    @Override
    public String seekSMSSuccee(String projectOrTask, String name, String type) {
        StringBuffer stringBuffer=new StringBuffer();
        if ("".equals(type)){
            stringBuffer.append("收文失败，发送短信。项目名称:"+name).append("\n");
        }
        if ("".equals(type)){
            stringBuffer.append("发文失败，发送短信。项目名称:"+name).append("\n");
        }
        stringBuffer.append("【评审中心项目管理系统】");
        return stringBuffer.toString();
    }
}
