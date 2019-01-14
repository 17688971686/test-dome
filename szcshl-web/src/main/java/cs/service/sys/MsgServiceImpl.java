package cs.service.sys;

import com.alibaba.fastjson.JSON;
import cs.ahelper.HttpClientOperate;
import cs.common.RandomGUID;
import cs.common.ResultMsg;
import cs.common.cache.CacheManager;
import cs.common.cache.ICache;
import cs.common.constants.Constant;
import cs.common.constants.SysConstants;
import cs.common.utils.SMSUtils;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.sys.SMSLog;
import cs.domain.sys.User;
import cs.model.sys.SysConfigDto;
import cs.repository.repositoryImpl.sys.SMSLogRepo;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static cs.common.cache.CacheConstant.IP_CACHE;
import static cs.common.constants.SysConstants.SEPARATE_COMMA;
import static cs.common.constants.SysConstants.SUPER_ACCOUNT;
import static cs.common.utils.SMSUtils.*;

/**
 * Created by ldm on 2018/6/31.
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
        ResultMsg resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "获取token没有返回信息！");
        Map<String, String> params = new HashMap<>();
        params.put("account", SMSUtils.USER_NAME);
        params.put("password", SMSUtils.USER_PASS);
        try {
            String jsonInfo = httpClientOperate.doGet(SMSUtils.GET_TOKEN_URL, params);
            if (Validate.isString(jsonInfo)) {
                //先把String 形式的 JSON 转换位 JSON 对象
                JSONObject json = new JSONObject(jsonInfo);
                //得到 JSON 属性对象列表
                if (Validate.isObject(json)) {
                    String resultCode = json.getString("resultCode");
                    if ("0000000".equals(resultCode)) {
                        JSONObject jo = json.getJSONObject("data");
                        SMSUtils.resetTokenInfo(jo.getString("accessToken"), System.currentTimeMillis(), jo.getLong("expiredValue"));
                        resultMsg.setFlag(true);
                    }
                    resultMsg.setReCode(resultCode);
                    resultMsg.setReMsg(SMSUtils.getMsgInfoByCode(resultCode));
                }
            }
        } catch (Exception e) {
            String errorString = "获取短信token异常：" + e.getMessage();
            logger.error(errorString);
            resultMsg.setReMsg(errorString);
        }
        return resultMsg;
    }

    @Override
    public void sendMsg(List<User> recvUserList, String msgContent, SMSLog smsLog) {
        //是否多个人
        int mphoneCount = 0, error = 0;
        //异常用户信息
        String errorInfo = "",
                //所用的用户
                allUserName = "",
                //发送短信的用户
                sendUserName = "",
                //发送的手机号码
                phone = "";
        ResultMsg resultMsg = ResultMsg.error("");
        for (User user : recvUserList) {
            allUserName += user.getDisplayName() + " ";
            if (Validate.isString(user.getUserMPhone()) && Validate.isPhone(user.getUserMPhone())) {
                if (mphoneCount > 0) {
                    phone += SEPARATE_COMMA;
                    sendUserName += SEPARATE_COMMA;
                }
                phone += Validate.removeSpace(user.getUserMPhone());
                sendUserName += Validate.removeSpace(user.getDisplayName());
                mphoneCount++;
            } else {
                errorInfo += "用户[" + user.getDisplayName() + "]的手机号码无效;";
                error++;
            }
        }
        smsLog.setIsCallApi(Constant.EnumState.NO.getValue());

        if (Validate.isString(phone)) {
            boolean isTokenEnable = true;
            if (SMSUtils.isTokenTimeout()) {
                SMSUtils.resetTokenInfo("", 0L, 0L);
                resultMsg = getMsgToken();
                isTokenEnable = resultMsg.isFlag();
            }
            if (isTokenEnable) {
                resultMsg.setReCode(Constant.MsgCode.ERROR.getValue());
                resultMsg.setReMsg("发送短信没有返回信息!");
                try {
                    String smsUrl = SMSUtils.SM_URL + "?mobile=" + phone;
                    //发送短信
                    Map<String, String> params = new HashMap<>();
                    params.put("accessToken", SMSUtils.getTOKEN());
                    //params.put("mobile", phone);
                    params.put("content", msgContent);
                    //1、多人发送
                    if (mphoneCount == 1) {
                        params.put("apiSecret", SMSUtils.APISECRET_ONE);
                        params.put("serCode", SMSUtils.ONE_SERCODE);
                        smsLog.setManyOrOne("1");
                        //2、单人发送
                    } else {
                        params.put("apiSecret", SMSUtils.APISECRET_MANY);
                        params.put("serCode", SMSUtils.MANY_SERCODE);
                        smsLog.setManyOrOne("2");
                    }
                    smsLog.setIsCallApi(Constant.EnumState.YES.getValue());
                    List<NameValuePair> parameters = new ArrayList<>();
                    if (params != null) {
                        for (String key : params.keySet()) {
                            parameters.add(new BasicNameValuePair(key, params.get(key)));
                        }
                    }
                    String url = EntityUtils.toString(new UrlEncodedFormEntity(parameters, Consts.UTF_8));
                    smsUrl += "&" + url;
                    String smsContent = httpClientOperate.doGet(smsUrl);
                    String resultCode = SMSUtils.analysisResult(smsContent);
                    if (Validate.isString(resultCode)) {
                        if ("0000000".equals(resultCode)) {
                            resultMsg.setFlag(true);
                            //如果是token失效，则获取新token重新发送
                        } else if ("0190007".equals(resultCode)) {
                            SMSUtils.resetTokenInfo("", 0L, 0L);
                            ResultMsg newResultMsg = getMsgToken();
                            if (newResultMsg.isFlag()) {
                                smsContent = httpClientOperate.doGet(smsUrl);
                                resultCode = SMSUtils.analysisResult(smsContent);
                                if ("0000000".equals(resultCode)) {
                                    resultMsg.setFlag(true);
                                }
                            }
                        }
                        resultMsg.setReCode(resultCode);
                        resultMsg.setReMsg(SMSUtils.getMsgInfoByCode(resultCode));
                    }
                } catch (Exception e) {
                    logger.error("发送短信异常：" + e.getMessage());
                    resultMsg.setReMsg("发送短信异常：" + e.getMessage());
                }
            } else {
                SMSUtils.resetTokenInfo("", 0L, 0L);
            }
        } else {
            resultMsg.setReMsg("发送短信用户列表，没有符合条件的手机号码!");
        }

        //保存短信发送记录
        if (resultMsg.isFlag()) {
            smsLog.setResult(Constant.EnumState.YES.getValue());
        } else {
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
        smsLog.setIpAdd(Validate.isObject(ipObj) ? ipObj.toString() : "");
        smsLog.setCustomMessage(resultMsg.getReMsg());
        if (error > 0) {
            smsLog.setCustomMessage(smsLog.getCustomMessage() + "(" + errorInfo + ")");
        }
        if (!Validate.isString(smsLog.getId())) {
            smsLog.setId((new RandomGUID()).valueAfterMD5);
        }
        smsLogRepo.save(smsLog);
    }

    @Override
    public ResultMsg getMsgTokenNew() {
        ResultMsg resultMsg = ResultMsg.error("获取token没有返回信息！");
        try {
            Map<String, String> params = new HashMap<>();
            params.put(SMSUtils.MSG_PARAMS.appid.toString(), SM_APPID_NEW);
            params.put(SMSUtils.MSG_PARAMS.appsecret.toString(), SM_APPSECRET_NEW);
            String jsonInfo = httpClientOperate.doGet(SMSUtils.SM_TOKEN_URL_NEW, params);
            logger.error("发送短信获取token返回信息：" + jsonInfo);
            if (Validate.isString(jsonInfo)) {
                //先把String 形式的 JSON 转换位 JSON 对象
                JSONObject json = new JSONObject(jsonInfo);
                //得到 JSON 属性对象列表
                if (Validate.isObject(json)) {
                    int resultCode = json.getInt(SMSUtils.MSG_PARAMS.resultCode.toString());
                    if (SMSUtils.RESULT_CODE.SUCCESS.ordinal() == resultCode) {
                        JSONObject jo = json.getJSONObject(SMSUtils.MSG_PARAMS.resultData.toString());
                        //token5分钟有效
                        SMSUtils.resetTokenInfo(jo.getString(SMSUtils.MSG_PARAMS.accessToken.toString()), System.currentTimeMillis(), 300L);
                        resultMsg.setReMsg(jo.getString(SMSUtils.MSG_PARAMS.tokenMsg.toString()));
                        resultMsg.setFlag(true);
                    }
                    resultMsg.setReCode(String.valueOf(resultCode));
                }
            }
        } catch (Exception e) {
            String errorString = "获取短信token异常：" + e.getMessage();
            logger.error(errorString);
            resultMsg.setReMsg(errorString);
        }
        return resultMsg;
    }

    @Override
    public void sendMsgNew(List<User> recvUserList, String msgContent, SMSLog smsLog) {
        //是否多个人
        int mphoneCount = 0, error = 0;
        //异常用户信息
        String errorInfo = "",
                //所用的用户
                allUserName = "",
                //发送短信的用户
                sendUserName = "",
                //发送的手机号码
                phone = "";
        ResultMsg resultMsg = ResultMsg.error("");
        for (User user : recvUserList) {
            allUserName += user.getDisplayName() + " ";
            if (Validate.isString(user.getUserMPhone()) && Validate.isPhone(user.getUserMPhone())) {
                if (mphoneCount > 0) {
                    phone += SEPARATE_COMMA;
                    sendUserName += SEPARATE_COMMA;
                }
                phone += Validate.removeSpace(user.getUserMPhone());
                sendUserName += Validate.removeSpace(user.getDisplayName());
                mphoneCount++;
                //多人发送，最多只能发送50个人
                if (mphoneCount >= 50) {
                    break;
                }
            } else {
                errorInfo += "用户[" + user.getDisplayName() + "]的手机号码无效;";
                error++;
            }
        }
        smsLog.setIsCallApi(Constant.EnumState.NO.getValue());

        if (Validate.isString(phone)) {
            boolean isTokenEnable = true;
            if (SMSUtils.isTokenTimeout()) {
                SMSUtils.resetTokenInfo("", 0L, 0L);
                resultMsg = getMsgTokenNew();
                isTokenEnable = resultMsg.isFlag();
            }
            if (isTokenEnable) {
                resultMsg.setReCode(Constant.MsgCode.ERROR.getValue());
                resultMsg.setReMsg("发送短信没有返回信息!");
                try {
                    //url和密钥参数
                    String smsUrl,authorSecret;
                    //创建一个json对象
                    Map<String, String> paramsMap = new HashMap<>();
                    //添加电话号码参数
                    paramsMap.put(SMSUtils.MSG_PARAMS.Phone.toString(), phone);
                    //添加短信内容参数
                    paramsMap.put(SMSUtils.MSG_PARAMS.SmsContent.toString(), msgContent);

                    if (mphoneCount == 1) {
                        //1、单人发送
                        smsUrl = SMSUtils.SM_ONE_URL;
                        smsLog.setManyOrOne("1");
                        authorSecret = APISECRET_ONE_NEW;
                    } else {
                        //2、多人发送
                        smsUrl = SMSUtils.SM_MORE_URL;
                        smsLog.setManyOrOne("2");
                        authorSecret = APISECRET_MANY_NEW;
                    }
                    smsLog.setIsCallApi(Constant.EnumState.YES.getValue());
                    String msgJsonInfo = JSON.toJSONString(paramsMap);
                    String smsResultJson = httpClientOperate.sendMsgByPost(smsUrl, msgJsonInfo, authorSecret, SMSUtils.getTOKEN());
                    logger.error("发送短信返回信息1：" + smsResultJson);
                    if (Validate.isString(smsResultJson)) {
                        //先把String 形式的 JSON 转换位 JSON 对象
                        JSONObject json = new JSONObject(smsResultJson);
                        //得到 JSON 属性对象列表
                        if (Validate.isObject(json)) {
                            int resultCode = json.getInt(SMSUtils.MSG_PARAMS.resultCode.toString());
                            resultMsg.setReMsg(json.getString(SMSUtils.MSG_PARAMS.resultMsg.toString()));
                            resultMsg.setReCode(String.valueOf(resultCode));

                            if (SMSUtils.RESULT_CODE.SUCCESS.ordinal() == resultCode) {
                                //发送成功
                                resultMsg.setFlag(true);
                            } else {
                                //发送失败，重新获取一次token
                                SMSUtils.resetTokenInfo("", 0L, 0L);
                                ResultMsg newResultMsg = getMsgTokenNew();
                                if (newResultMsg.isFlag()) {
                                    //重新发一次
                                    smsResultJson = httpClientOperate.sendMsgByPost(smsUrl, msgJsonInfo, SM_APPSECRET_NEW, SMSUtils.getTOKEN());
                                    logger.error("发送短信返回信息2：" + smsResultJson);
                                    if (Validate.isString(smsResultJson)) {
                                        //先把String 形式的 JSON 转换位 JSON 对象
                                        JSONObject json2 = new JSONObject(smsResultJson);
                                        //得到 JSON 属性对象列表
                                        if (Validate.isObject(json2)) {
                                            int resultCode2 = json2.getInt(SMSUtils.MSG_PARAMS.resultCode.toString());
                                            resultMsg.setReMsg(json2.getString(SMSUtils.MSG_PARAMS.resultMsg.toString()));
                                            resultMsg.setReCode(String.valueOf(resultCode2));
                                            if (SMSUtils.RESULT_CODE.SUCCESS.ordinal() == resultCode2) {
                                                //发送成功
                                                resultMsg.setFlag(true);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("发送短信异常：" + e.getMessage());
                    resultMsg.setReMsg("发送短信异常：" + e.getMessage());
                }
            } else {
                SMSUtils.resetTokenInfo("", 0L, 0L);
            }
        } else {
            resultMsg.setReMsg("发送短信用户列表，没有符合条件的手机号码!");
        }

        //保存短信发送记录
        if (resultMsg.isFlag()) {
            smsLog.setResult(Constant.EnumState.YES.getValue());
        } else {
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
        smsLog.setIpAdd(Validate.isObject(ipObj) ? ipObj.toString() : "");
        smsLog.setCustomMessage(resultMsg.getReMsg());
        if (error > 0) {
            smsLog.setCustomMessage(smsLog.getCustomMessage() + "(" + errorInfo + ")");
        }
        if (!Validate.isString(smsLog.getId())) {
            smsLog.setId((new RandomGUID()).valueAfterMD5);
        }
        smsLogRepo.save(smsLog);
    }


    @Override
    public List<User> getNoticeUserByConfigKey(String configKey) {
        SysConfigDto sysConfigDto = sysConfigService.findByKey(configKey);
        if (Validate.isObject(sysConfigDto) && Validate.isString(sysConfigDto.getConfigValue())) {
            String configValue = sysConfigDto.getConfigValue();
            //替换所有的中文字符
            configValue = configValue.replaceAll("；", ";").replaceAll("，", ",");
            List<String> totalUser = StringUtil.getSplit(configValue, ";");
            if (Validate.isList(totalUser)) {
                List<User> resultList = new ArrayList<>();
                for(String userInfo :totalUser){
                    List<String> user = StringUtil.getSplit(userInfo, SysConstants.SEPARATE_COMMA);
                    if(Validate.isList(user)){
                        User u = new User();
                        u.setUserMPhone(Validate.removeSpace(user.get(0)));
                        u.setDisplayName(Validate.removeSpace(user.get(1)));
                        resultList.add(u);
                    }
                }
                return resultList;
            }
        }
        return null;
    }

    @Override
    public boolean checkMsgType() {
        SysConfigDto msgTypeDto = sysConfigService.findByKey(Constant.RevireStageKey.MSG_TYPE.getValue());
        return SMSUtils.checkMsgType(msgTypeDto);
    }

}
