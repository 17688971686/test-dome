package cs.service.sys;

import cs.common.HqlBuilder;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.sys.SMSLog_;
import cs.domain.sys.SysConfig_;
import cs.domain.sys.User;
import cs.model.sys.SMSLogDto;
import cs.domain.sys.SysConfig;
import cs.repository.repositoryImpl.sys.SMSLogRepo;
import cs.repository.repositoryImpl.sys.SysConfigRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */
@Service
public class SMSContentImp implements SMSContent{

    @Autowired
    private SMSLogService smsLogService;
    @Autowired
    private SMSLogRepo smsLogRepo;

    @Autowired
    private SysConfigRepo sysConfigRepo;
//    findByDataKey(String value);


    //您收到一条待办项目|任务，项目名称|任务名称xxx，请及时处理。【评审中心项目管理系统】
    @Override
    public String get(String projectOrTask,String name) {
        StringBuffer stringBuffer=new StringBuffer("\n 您收到一条待办");
        if (projectOrTask.contains("项")){
            stringBuffer.append(projectOrTask+"。\n 项目名称:"+name+",请及时处理").append("\n");
        }
        if (projectOrTask.contains("任")){
            stringBuffer.append(projectOrTask+"。\n 任务名称:"+name+",请及时处理").append("\n");
        }
        stringBuffer.append("【评审中心项目管理系统】");
        return stringBuffer.toString();
    }
    /**
     *收文失败，发送短信（但龙，郭东东）项目名称（委里收文编号）
     * */
    @Override
    public String seekSMSSuccee(String projectName, String fileCode, String type) {
        StringBuffer stringBuffer=new StringBuffer("\n 您收到一条信息:");
        if (type.contains("收文")){
            stringBuffer.append(type+"。\n 项目名称:"+projectName+"("+fileCode+")").append("\n");
        }
        if (type.contains("发文")){
            stringBuffer.append(type+"。\n 项目名称:"+projectName+"("+fileCode+")").append("\n");
        }
        stringBuffer.append("【评审中心项目管理系统】");
        return stringBuffer.toString();
    }

    @Override
    public boolean orNotsendSMS(List<User> userList, String projectName, String fileCode, String type, String infoType) {
        SysConfig sysConfig  = sysConfigRepo.findByDataKey("seek_sms_time_type");
        if ("1".equals(sysConfig.getConfigValue())){//关闭限制次数
            return true;
        }
        if ("0".equals(sysConfig.getConfigValue())){
            User user = null;
            String phone = "",userName="";
            for (int i = 0, l = userList.size(); i < l; i++) {
                user = userList.get(i);
                if (StringUtil.isNotEmpty(user.getUserMPhone())) {
                    if (i == userList.size() - 1) {
                        phone += user.getUserMPhone();
                        userName += user.getDisplayName();
                    } else {
                        phone += user.getUserMPhone() + ",";
                        userName += user.getDisplayName() + ",";
                    }
                }
            }
            //验证fileCode是否已经在短信日志表中
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append("select " + SMSLog_.userName.getName() + "," + SMSLog_.projectName.getName() + "," + SMSLog_.fileCode.getName() + " from cs_sms_log  ");
            if (("待办").equals(infoType)){//暂时没开通待办: 代码限制
                hqlBuilder.append(" where " + SMSLog_.projectName.getName()  + " = '"+projectName+"' and  "+ SMSLog_.smsLogType.getName()  + " = "+"'"+type+"' " +
                        " and "+SMSLog_.smsUserPhone.getName()+" = '"+phone+"' and "+SMSLog_.userName.getName()+"= '"+userName+"'" );
            }else {
                hqlBuilder.append(" where " + SMSLog_.fileCode.getName()  + " = '"+fileCode+"' and  "+ SMSLog_.smsLogType.getName()  + " = "+"'"+type+"' " +
                        " and "+SMSLog_.smsUserPhone.getName()+" = '"+phone+"' and "+SMSLog_.userName.getName()+"= '"+userName+"'" );
            }
            List<SMSLogDto> sysConfigDtoList = new ArrayList<>();
            List<Object[]> list = smsLogRepo.getObjectArray(hqlBuilder);
            if (Validate.isList(list)) {
                if (list.size()>0){
                    return true;
                }
            }
        }

        return false;
    }


}
