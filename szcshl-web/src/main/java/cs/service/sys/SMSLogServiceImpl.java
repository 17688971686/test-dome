package cs.service.sys;

import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SMSUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.sys.SMSLog;
import cs.domain.sys.SMSLog_;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.sys.SMSLogDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.SMSLogRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SMSLogServiceImpl implements SMSLogService {
    private static Logger logger = Logger.getLogger(SMSLogServiceImpl.class);

    @Autowired
    private SMSLogRepo smslogRepo;



    @Override
    @Transactional
    public PageModelDto<SMSLogDto> get(ODataObj odataObj) {
        List<SMSLog> logList = smslogRepo.findByOdata(odataObj);
        List<SMSLogDto> smslogDtoList = new ArrayList<>();
        if (Validate.isList(logList)) {
            logList.forEach(ll -> {
                SMSLogDto smsLogDto = new SMSLogDto();
                BeanCopierUtils.copyProperties(ll, smsLogDto);
                smslogDtoList.add(smsLogDto);
            });
        }
        PageModelDto<SMSLogDto> pageModelDto = new PageModelDto<>();
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(smslogDtoList);
        logger.info("查询日志数据");
        return pageModelDto;
    }

    @Override
    @Transactional
    public void save(SMSLog smsLog) {
        smslogRepo.save(smsLog);
    }

    @Override
    @Transactional
    public void update(List<SMSLog> smsLogList) {
        smslogRepo.bathUpdate(smsLogList);
    }

    @Override
    @Transactional
    public ResultMsg sendSMSContent(SMSLogDto smsLogDto) {
        SMSLog smsLog = null;
//        boolean isHeaderExist = smslogRepo.isSMSlogExist(smsLogDto.getSmsLogType() , smsLogDto.getProjectName(),smsLogDto.getSmsUserPhone(),
//                smsLogDto.getSmsUserName(),smsLogDto.getFileCode());
//        if(!isHeaderExist) {
            smsLog = new SMSLog();
            BeanCopierUtils.copyProperties(smsLogDto, smsLog);
            smsLog.setId(UUID.randomUUID().toString());
            smsLog.setUserName(smsLogDto.getUserName());
            smsLog.setSmsUserName(smsLogDto.getSmsUserName());
            smsLog.setSmsUserPhone(smsLogDto.getSmsUserPhone());
            smsLog.setMessage(smsLog.getMessage());
            smsLog.setCreatedBy(SessionUtil.getDisplayName());
            smsLog.setCreatedDate(new Date());
            smsLog.setModifiedDate(new Date());
            smsLog.setSmsLogType("manual_type");
            smsLog.setCustomMessage("手动发送短信");
            smslogRepo.save(smsLog);
            //保存后开始发送短信
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "创建成功", smsLog);
//        }else{
//            return new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , String.format("表头key值：%s,已经存在，请重新输入！",smsLogDto.getProjectName()));
//        }
    }


    @Override
    @Transactional
    public void deleteSMSLog(String id) {
        smslogRepo.deleteById(SMSLog_.id.getName(), id);
        logger.info(String.format("删除短信日志,短信 :%s", id));
    }

    @Override
    @Transactional
    public SMSLog querySMSLog(String userName, String smsUserPhone, String projectName, String filecode, String resultCode, String type, String infoType, String seekContent, SMSLogService smsLogService, boolean success) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select " + SMSLog_.id.getName() + "," + SMSLog_.buninessId.getName() + "," + SMSLog_.createdBy.getName() +","+ SMSLog_.createdDate.getName() +  "," +
                " "+ SMSLog_.customMessage.getName() + ","+SMSLog_.fileCode.getName()+","+SMSLog_.ipAdd.getName()+","+SMSLog_.logCode.getName()+" " +
                ","+SMSLog_.manyOrOne.getName()+","+SMSLog_.message.getName()+","+SMSLog_.modifiedDate.getName()+","+SMSLog_.projectName.getName()+","+SMSLog_.result.getName()+","+SMSLog_.resultCode.getName()+" " +
                ","+SMSLog_.smsLogType.getName()+","+SMSLog_.smsUserName.getName()+","+SMSLog_.smsUserPhone.getName()+","+SMSLog_.userName.getName()+" from cs_sms_log  ");
        if (("待办").equals(infoType)){
            hqlBuilder.append(" where " + SMSLog_.projectName.getName()  + " = '"+projectName+"' and  "+ SMSLog_.smsLogType.getName()  + " = "+"'"+type+"' " +
                    " and "+SMSLog_.smsUserPhone.getName()+" = '"+smsUserPhone+"' and "+SMSLog_.userName.getName()+"= '"+userName+"'" );
        }else {
            hqlBuilder.append(" where " + SMSLog_.fileCode.getName()  + " = '"+filecode+"' and  "+ SMSLog_.smsLogType.getName()  + " = "+"'"+type+"' " +
                    " and "+SMSLog_.smsUserPhone.getName()+" = '"+smsUserPhone+"' and "+SMSLog_.userName.getName()+"= '"+userName+"'" );
        }
        List<Object[]> list = smslogRepo.getObjectArray(hqlBuilder);
        SMSLog smsLog = null;
        if (Validate.isList(list)) {
            if (list.size()>0){
                smsLog = new SMSLog();
                Object[] user = list.get(0);
                smsLog.setId((String) user[0]);
                smsLog.setBuninessId((String) user[1]);
                smsLog.setCreatedBy((String)user[2]);
                smsLog.setCreatedDate((Date) user[3]);
                smsLog.setCustomMessage((String)user[4]);
                smsLog.setFileCode((String)user[5]);
                smsLog.setIpAdd((String)user[6]);
                smsLog.setLogCode((String)user[7]);
                smsLog.setManyOrOne((String)user[8]);
                smsLog.setMessage((String)user[9]);
                smsLog.setModifiedDate((Date)user[10]);
                smsLog.setProjectName((String)user[11]);
                smsLog.setResult((String) user[12]);
                smsLog.setResultCode((String) user[13]);
                smsLog.setSmsLogType((String) user[14]);
                smsLog.setSmsUserName((String) user[15]);
                smsLog.setSmsUserPhone((String) user[16]);
                smsLog.setUserName((String) user[17]);

            }
        }
        return smsLog;
    }


}
