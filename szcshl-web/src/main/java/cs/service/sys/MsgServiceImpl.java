package cs.service.sys;

import cs.ahelper.HttpClientOperate;
import cs.common.RandomGUID;
import cs.common.ResultMsg;
import cs.common.cache.CacheManager;
import cs.common.cache.ICache;
import cs.common.constants.Constant;
import cs.common.utils.SMSUtils;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.sys.SMSLog;
import cs.domain.sys.User;
import cs.model.sys.SysConfigDto;
import cs.repository.repositoryImpl.sys.SMSLogRepo;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cs.common.cache.CacheConstant.IP_CACHE;
import static cs.common.constants.SysConstants.SUPER_ACCOUNT;

/**
 * Created by Administrator on 2018/5/31.
 */
@Service
public class MsgServiceImpl implements MsgService{

    private static Logger logger = Logger.getLogger(SMSUtils.class);

    @Autowired
    private SMSLogRepo smsLogRepo;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private HttpClientOperate httpClientOperate;

    @Override
    public ResultMsg getMsgToken() {
        ResultMsg resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"获取token没有返回信息！");
        Map<String, String> params = new HashMap<>();
        params.put("account", SMSUtils.USER_NAME);
        params.put("password", SMSUtils.USER_PASS);
        try {
            String jsonInfo = httpClientOperate.doGet(SMSUtils.GET_TOKEN_URL, params);
            if(Validate.isString(jsonInfo)){
                //先把String 形式的 JSON 转换位 JSON 对象
                JSONObject json = new JSONObject(jsonInfo);
                //得到 JSON 属性对象列表
                if(Validate.isObject(json) ){
                    String resultCode = json.getString("resultCode");
                    if("0000000".equals(resultCode)){
                        JSONObject jo = json.getJSONObject("data");
                        SMSUtils.setTOKEN(jo.getString("accessToken"));
                        SMSUtils.setGetTokenTime(new Date().getTime());
                        SMSUtils.setTokenExpireValue(jo.getLong("expiredValue"));
                        resultMsg.setFlag(true);
                    }
                    resultMsg.setReCode(resultCode);
                    resultMsg.setReMsg(SMSUtils.getMsgInfoByCode(resultCode));
                }
            }
        } catch (Exception e) {
            String errorString = "获取短信token异常："+e.getMessage();
            logger.error(errorString);
            resultMsg.setReMsg(errorString);
        }
        return resultMsg;
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
    public String querySmsNumber(List<User> receiverList, String projectName, String fileCode, String type, String infoType,String xianzhiNumber) {
        User user = null;
        String phone = "",userName = "";
        //获取电话号码
        if (receiverList.size() > 1) {
            for (int i = 0, l = receiverList.size(); i < l; i++) {
                user = receiverList.get(i);
                if (StringUtil.isNotEmpty(user.getUserMPhone())) {
                    if (i == receiverList.size() - 1) {
                        phone += user.getUserMPhone().trim();
                        userName += user.getDisplayName().trim();
                    } else {
                        phone += user.getUserMPhone().trim() + ",";
                        userName += user.getDisplayName().trim() + ",";
                    }
                }
            }
        }
        boolean booleans = smsLogRepo.isSMSlogExist(type, projectName, phone,  userName,  fileCode,infoType);
        if (!booleans) {
            return "已记录在短信日志中";
        }
        return null;
    }


    @Override
    public void sendMsg(List<User> recvUserList, String msgContent,SMSLog smsLog) {
        //是否多个人
        int mphoneCount = 0,error = 0;
        String errorInfo = "",       //异常用户信息
               allUserName = "",     //所用的用户
               sendUserName = "",    //发送短信的用户
               phone="";             //发送的手机号码
        ResultMsg resultMsg =  new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"");
        for(User user : recvUserList){
            allUserName += user.getDisplayName()+" ";
            if(Validate.isString(user.getUserMPhone()) && Validate.isPhone(user.getUserMPhone())){
                if(mphoneCount > 0){
                    phone += ",";
                    sendUserName += ",";
                }
                phone += Validate.removeSpace(user.getUserMPhone());
                sendUserName = Validate.removeSpace(user.getDisplayName());
                mphoneCount ++;
            }else{
                errorInfo += "用户["+user.getDisplayName()+"]的手机号码无效;";
                error ++;
            }
        }
        smsLog.setIsCallApi(Constant.EnumState.NO.getValue());

        if(Validate.isString(phone)){
            boolean isTokenEnable = true;
            if(SMSUtils.isTokenTimeout()){
                resultMsg = getMsgToken();
                isTokenEnable = resultMsg.isFlag();
            }
            if(isTokenEnable){
                resultMsg.setReCode(Constant.MsgCode.ERROR.getValue());
                resultMsg.setReMsg("发送短信没有返回信息!");
                try {
                    String msgUrl = "";
                    //发送短信
                    Map<String, String> params = new HashMap<>();
                    params.put("accessToken", SMSUtils.getTOKEN());
                    params.put("mobile", phone);
                    params.put("content", msgContent);
                    //1、多人发送
                    if(mphoneCount > 1){
                        params.put("apiSecret", SMSUtils.apiSecret_one);
                        msgUrl = SMSUtils.SM_URL_ONE;
                        smsLog.setManyOrOne("1");
                        //2、单人发送
                    }else{
                        params.put("apiSecret", SMSUtils.apiSecret_many);
                        msgUrl = SMSUtils.SM_URL_MANY;
                        smsLog.setManyOrOne("2");
                    }
                    smsLog.setIsCallApi(Constant.EnumState.YES.getValue());

                    String sendResult = httpClientOperate.doGet(msgUrl, params);
                    if(Validate.isString(sendResult)){
                        JSONObject json = new JSONObject(sendResult);
                        if(Validate.isObject(json) ){
                            String resultCode = json.getString("resultCode");
                            if("0000000".equals(resultCode)){
                                resultMsg.setFlag(true);
                            }
                            resultMsg.setReCode(resultCode);
                            resultMsg.setReMsg(SMSUtils.getMsgInfoByCode(resultCode));
                        }
                    }
                }catch (Exception e){
                    logger.error("发送短信异常："+e.getMessage());
                    resultMsg.setReMsg("发送短信异常："+e.getMessage());
                }
            }else{
                SMSUtils.setTOKEN("");
                SMSUtils.setGetTokenTime(0L);
                SMSUtils.setTokenExpireValue(0L);
            }
        }else{
            resultMsg.setReMsg("发送短信用户列表，没有符合条件的手机号码!");
        }

        //保存短信发送记录
        if(resultMsg.isFlag()){
            smsLog.setResult(Constant.EnumState.YES.getValue());
        }else{
            smsLog.setResult(Constant.EnumState.NO.getValue());
        }

        smsLog.setUserName(allUserName);
        smsLog.setSmsUserName(sendUserName);
        smsLog.setSmsUserPhone(phone);
        smsLog.setLogCode(resultMsg.getReCode());
        smsLog.setResultCode(resultMsg.getReCode());
        smsLog.setMessage(msgContent);
        smsLog.setCreatedDate(new Date());
        smsLog.setCreatedBy(SUPER_ACCOUNT);

        ICache cache = CacheManager.getCache();
        Object ipObj = cache.get(IP_CACHE);
        smsLog.setIpAdd(Validate.isObject(ipObj)?ipObj.toString():"");
        smsLog.setCustomMessage(resultMsg.getReMsg());
        if(error > 0){
            smsLog.setCustomMessage(smsLog.getCustomMessage()+"("+errorInfo+")");
        }
        if(!Validate.isString(smsLog.getId())){
            smsLog.setId((new RandomGUID()).valueAfterMD5);
        }
        smsLogRepo.save(smsLog);
    }


}
