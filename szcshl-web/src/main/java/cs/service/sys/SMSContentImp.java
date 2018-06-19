package cs.service.sys;

import cs.common.HqlBuilder;
import cs.common.utils.SMSUtils;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.sys.SMSLog_;
import cs.domain.sys.SysConfig_;
import cs.domain.sys.User;
import cs.model.sys.SMSLogDto;
import cs.domain.sys.SysConfig;
import cs.model.sys.SysConfigDto;
import cs.repository.repositoryImpl.sys.SMSLogRepo;
import cs.repository.repositoryImpl.sys.SysConfigRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private SysConfigService sysConfigService;


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
    @Transactional
    public String orNotsendSMS(List<User> userList, String projectName, String fileCode, String type, String infoType,String xianzhiNumber) {
        SysConfigDto sysConfigDto  =null;
        if ("打开限制次数".equals(xianzhiNumber)){
            sysConfigDto  = sysConfigService.findByDataKey("seek_sms_time_type");
            if ("0".equals(sysConfigDto.getConfigValue())){
                return "打开限制次数";
            }
        }
        if ("关闭限制次数".equals(xianzhiNumber)){
            sysConfigDto  = sysConfigService.findByDataKey("seek_sms_time_type");
            if ("1".equals(sysConfigDto.getConfigValue())){
                return "关闭限制次数";
            }
        }
        return null;
    }

    @Override
    @Transactional
    public String querySmsNumber(List<User> userList, String projectName, String fileCode, String type, String infoType,String xianzhiNumber) {
        SMSUtils.packList(userList);
        //验证fileCode是否已经在短信日志表中
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select " + SMSLog_.userName.getName() + "," + SMSLog_.projectName.getName() + "," + SMSLog_.fileCode.getName() + " from cs_sms_log  ");
        if (("待办").equals(infoType)){//暂时没开通待办: 代码限制
            hqlBuilder.append(" where " + SMSLog_.projectName.getName()  + " = '"+projectName+"' and  "+ SMSLog_.smsLogType.getName()  + " = "+"'"+type+"' " +
                    " and "+SMSLog_.smsUserPhone.getName()+" = '"+SMSUtils.phone+"' and "+SMSLog_.userName.getName()+"= '"+SMSUtils.userName+"'" );
        }else {
            hqlBuilder.append(" where " + SMSLog_.fileCode.getName()  + " = '"+fileCode+"' and  "+ SMSLog_.smsLogType.getName()  + " = "+"'"+type+"' " +
                    " and "+SMSLog_.smsUserPhone.getName()+" = '"+SMSUtils.phone+"' and "+SMSLog_.userName.getName()+"= '"+SMSUtils.userName+"'" );
        }
        List<Object[]> list = smsLogRepo.getObjectArray(hqlBuilder);
        if (Validate.isList(list)) {
            if (list.size()>0){
                return "已记录在短信日志中";
            }
        }
        return null;
    }


}
