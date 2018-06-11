package cs.controller.restController;

import com.alibaba.fastjson.JSON;
import cs.ahelper.HttpClientOperate;
import cs.ahelper.HttpResult;
import cs.ahelper.IgnoreAnnotation;
import cs.ahelper.LogMsg;
import cs.common.FGWResponse;
import cs.common.IFResultCode;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.DateUtils;
import cs.common.utils.SMSUtils;
import cs.common.utils.Validate;
import cs.domain.sys.Log;
import cs.domain.sys.User;
import cs.model.project.SignDto;
import cs.model.project.SignPreDto;
import cs.model.sys.SysFileDto;
import cs.model.sys.UserDto;
import cs.service.project.SignService;
import cs.service.restService.SignRestService;
import cs.service.rtx.RTXService;
import cs.service.sys.*;
import cs.service.topic.TopicInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import static cs.common.constants.SysConstants.SUPER_ACCOUNT;

/**
 * 系统接口controller
 *
 * @author ldm
 *         Created by ldm on 2017/8/25.
 */
@RestController
@RequestMapping(name = "系统接口", path = "intfc")
@IgnoreAnnotation
public class SysRestController {
    private static Logger logger = Logger.getLogger(SysRestController.class);
    @Autowired
    private SignRestService signRestService;
    @Autowired
    private TopicInfoService topicInfoService;
    @Autowired
    private HttpClientOperate httpClientOperate;
    @Autowired
    private LogService logService;
    @Autowired
    private SignService signService;

    @Autowired
    private UserService userService;

    @Autowired
    private RTXService rtxService;

    @Autowired
    private SMSLogService smsLogService;

    @Autowired
    private SMSContent smsContent;

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 项目签收信息
     *
     * @param signDtoJson
     * @return
     */
    @RequestMapping(name = "项目签收信息", value = "/pushProject", method = RequestMethod.POST)
    @LogMsg(module = "系统接口【委里推送数据接口】",logLevel = "1")
    public synchronized ResultMsg pushProject(@RequestParam String signDtoJson) {
        ResultMsg resultMsg = null;
        SignDto signDto = JSON.parseObject(signDtoJson, SignDto.class);
        //AAA 项目signDto.getFilecode()  委里收文编号
        String msg = "项目【"+signDto.getProjectname()+"("+signDto.getFilecode()+")json="+signDtoJson+"】";
        try{
            //json转出对象
            resultMsg = signRestService.pushProject(signDto,true);
        }catch (Exception e){
            resultMsg = new ResultMsg(false,IFResultCode.IFMsgCode.SZEC_SAVE_ERROR.getCode(),e.getMessage());
            e.printStackTrace();
        }
        // 判断短信日志表中是否已经发送短信 收文类型: incoming_type
       if(resultMsg.isFlag()&& rtxService.rtxSMSEnabled()){
           boolean boo = SMSUtils.getWeek(new Date(),sysConfigService);
           if (boo){
               //发送短信不收次数限制,暂时注销
//               if(! smsContent.orNotsendSMS(signDto.getProjectname(),signDto.getFilecode(),"incoming_type","收文成功")){
                   // AAAGAN 收文失败，发送短信（但龙，郭东东）项目名称（委里收文编号）
                   SMSUtils.seekSMSThread(signRestService.getListUser("收文成功"),signDto.getProjectname(),signDto.getFilecode(),"incoming_type","收文成功",smsContent.seekSMSSuccee(signDto.getProjectname(),signDto.getFilecode(),"收文成功"),  smsLogService);
//               }
           }
       }else {
           if (rtxService.rtxSMSEnabled()) {
               boolean boo = SMSUtils.getWeek(new Date(), sysConfigService);
               if (boo) {
//                   if (!smsContent.orNotsendSMS(signDto.getProjectname(), signDto.getFilecode(), "incoming_type", "收文失败")) {
                       SMSUtils.seekSMSThread(signRestService.getListUser("收文失败"), signDto.getProjectname(), signDto.getFilecode(), "incoming_type", "收文失败", smsContent.seekSMSSuccee(signDto.getProjectname(), signDto.getFilecode(), "收文失败"), smsLogService);
//                   }
               }
           }
       }
        /*//添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setUserName(SUPER_ACCOUNT);
        log.setLogCode(resultMsg.getReCode());
        log.setModule(Constant.LOG_MODULE.INTERFACE.getValue() + "【获取项目信息接口】");
        log.setMessage(msg+resultMsg.getReMsg());
        log.setBuninessId(Validate.isObject(resultMsg.getReObj()) ? resultMsg.getReObj().toString() : "");
        log.setBuninessType(Constant.BusinessType.SIGN.getValue());
        log.setResult(resultMsg.isFlag() ? Constant.EnumState.YES.getValue() : Constant.EnumState.NO.getValue());
        log.setLogger(this.getClass().getName() + ".pushProject");
        //优先级别高
        log.setLogLevel(Constant.EnumState.PROCESS.getValue());
        logService.save(log);*/
        resultMsg.setReObj(null);
        return resultMsg;
    }

    public String getContent(String type,String numInfo){
        String str= null;
        /*
        * 收文失败，发送短信（但龙，郭东东）项目名称（委里收文编号）
          发文失败，发送短信（但龙，陈春燕）项目名称（发文号）
        * */
        if ("收文失败".equals(type)){
            type = type+",项目名称: "+numInfo;
        }
        if ("发文失败".equals(type)){
            type = type+",项目名称: "+numInfo;
        }
        return  null;
    }

    /**
     * 根据收文编号查询签收/预签收
     * @return
     */
    @RequestMapping(name = "项目预签收信息", value = "/getPreSign", method = RequestMethod.GET)
    @ResponseBody
    @LogMsg(module = "系统接口【通过收文编号获取项目信息】",logLevel = "1")
    public synchronized ResultMsg findPreSignByfileCode(@RequestParam String fileCode,@RequestParam String signType){
        String signPreInfo = "";
        ResultMsg resultMsg = null;
        try{
            String preUrl = signRestService.getPreReturnUrl();
            preUrl = preUrl + "?swbh="+fileCode;
            signPreInfo =  httpClientOperate.doGet(preUrl);
          //  JSON.
            String msg = "";
            Map resultMap = (Map)JSON.parse(signPreInfo);
            if(resultMap.get("data") != null && !resultMap.get("data").equals("null")){
                SignPreDto signPreDto = JSON.parseObject(signPreInfo, SignPreDto.class);
                 msg = "项目【"+signPreDto.getData().getProjectname()+"("+signPreDto.getData().getFilecode()+")】，";
                //json转出对象
                if(Validate.isString(signType) && signType.equals("1")){
                    //获取项目预签收信息
                    resultMsg = signRestService.pushPreProject(signPreDto.getData(),false);
                }else{
                    resultMsg = signRestService.pushProject(signPreDto.getData(),false);
                }
            }else{
                msg = "该项目信息不存在请核查！";
                resultMsg = new ResultMsg(false,IFResultCode.IFMsgCode.SZEC_SAVE_ERROR.getCode(),msg);
            }
            /*//添加日记记录
            Log log = new Log();
            log.setCreatedDate(new Date());
            log.setUserName(SUPER_ACCOUNT);
            log.setLogCode(resultMsg.getReCode());
            log.setModule(Constant.LOG_MODULE.INTERFACE.getValue() + "【获取项目预签收信息接口】");
            log.setMessage(msg+resultMsg.getReMsg());
            log.setBuninessId(Validate.isObject(resultMsg.getReObj()) ? resultMsg.getReObj().toString() : "");
            log.setBuninessType(Constant.BusinessType.SIGN.getValue());
            log.setResult(resultMsg.isFlag() ? Constant.EnumState.YES.getValue() : Constant.EnumState.NO.getValue());
            log.setLogger(this.getClass().getName() + ".pushProject");
            //优先级别高
            log.setLogLevel(Constant.EnumState.PROCESS.getValue());
            logService.save(log);*/
        }catch (Exception e){
            resultMsg = new ResultMsg(false,IFResultCode.IFMsgCode.SZEC_SAVE_ERROR.getCode(),e.getMessage());
            e.printStackTrace();
        }
        return resultMsg;
    }

    /**
     * 项目信息返回委里接口
     *
     * @return
     */
    public ResultMsg sendSignMsg(String nodeNameKey) {
        ResultMsg resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"操作失败！");
        try {
            // 接口地址
            String endpoint = "http://localhost:8089/FGWPM/restservices/FGWPMRest/uploadPszxData/query";
            //1、评审意见对象
            Map<String, Object> dataMap = new HashMap<String, Object>();
            // dataMap.put("xmmc", "HLT备案11201644");// 项目名称
            // dataMap.put("jsdw", "测试建设单位");// 建设单位
            dataMap.put("swbh", "KY201700022");// 收文编号
            // dataMap.put("swrq", sdf.parse("2017/11/20").getTime());// 收文日期
            dataMap.put("psfs", "1");// 评审方式
            // dataMap.put("xmbm", "S-2017-A01-500046-11-01");// 项目编码
            dataMap.put("sssj", (new Date()).getTime());// 送审日期
            dataMap.put("psjssj", (new Date()).getTime());// 评审结束时间
            dataMap.put("xmpsyd", "项目评审要点项目评审要点项目评审要点项目评审要点项目评审要点项目评审要点项目评审要点");// 项目评审要点
            dataMap.put("sb", 1.2);// 申报投资额（万元）
            dataMap.put("sd", 2.1);// 审定投资额（万元）
            dataMap.put("hjz", 6.2);// 核减（增）投资额（万元）
            dataMap.put("hjzl", 58.62);// 核减（增）率
            dataMap.put("psbz", "备注备注备注备注备注备注备注");// 备注
            dataMap.put("xmjsbyx", "项目建设必要性项目建设必要性项目建设必要性项目建设必要性项目建设必要性");// 项目建设必要性
            dataMap.put("jsnrjgm", "建设内容及规模建设内容及规模建设内容及规模建设内容及规模建设内容及规模建设内容及规模建设内容及规模");// 建设内容及规模
            dataMap.put("tzksjzjly", "投资估算及资金来源投资估算及资金来源投资估算及资金来源投资估算及资金来源投资估算及资金来源投资估算及资金来源投资估算及资金来源");// 投资估算及资金来源
            dataMap.put("xyjdgzyq", "下一阶段工作要求下一阶段工作要求下一阶段工作要求");// 下一阶段工作要求

            //2、附件列表
            ArrayList<HashMap<String, Object>> fjList = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> fjMap = new HashMap<String, Object>();
            fjMap.put("url", "http://localhost:8089/FGWPM/LEAP/Download/default/2017/12/10/pdftest.pdf");
            fjMap.put("filename", "评审报告pdf");
            fjMap.put("tempName", "黄凌涛");
            fjList.add(fjMap);
            fjMap = new HashMap<String, Object>();
            fjMap.put("url", "http://localhost:8089/FGWPM/LEAP/Download/default/2017/12/10/wordtest.docx");
            fjMap.put("filename", "评审报告word");
            fjMap.put("tempName", "黄凌涛");
            fjList.add(fjMap);
            dataMap.put("psbg", fjList);// 评审报告（需上传pdf和word文档）

            fjList = new ArrayList<HashMap<String, Object>>();
            fjMap = new HashMap<String, Object>();
            fjMap.put("url", "http://localhost:8089/FGWPM/LEAP/Download/default/2017/12/10/pdftest.pdf");
            fjMap.put("filename", "投资估算审核表pdf");
            fjMap.put("tempName", "黄凌涛");
            fjList.add(fjMap);
            fjMap = new HashMap<String, Object>();
            fjMap.put("url", "http://localhost:8089/FGWPM/LEAP/Download/default/2017/12/10/wordtest.docx");
            fjMap.put("filename", "投资估算审核表word");
            fjMap.put("tempName", "黄凌涛");
            fjList.add(fjMap);
            dataMap.put("tzgsshb", fjList);// 投资估算审核表（需上传pdf和word文档）
            fjList = new ArrayList<HashMap<String, Object>>();
            dataMap.put("zjpsyj", fjList);// 专家评审意见
            fjList = new ArrayList<HashMap<String, Object>>();
            dataMap.put("zjpsmd", fjList);// 专家评审名单
            fjList = new ArrayList<HashMap<String, Object>>();
            dataMap.put("qtfj", fjList);// 其它

            //3、办理意见
            ArrayList<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> psgcMap = new HashMap<String, Object>();
            psgcMap.put("blhj", "1");// 办理环节
            psgcMap.put("psblyj", "办理意见办理意见办理意见办理意见办理意见");// 办理意见
            psgcMap.put("blr", "办理人");// 办理人
            psgcMap.put("blsj", (new Date()).getTime());// 办理时间
            fjList = new ArrayList<HashMap<String, Object>>();
            fjMap = new HashMap<String, Object>();
            fjMap.put("url", "http://localhost:8089/FGWPM/LEAP/Download/default/2017/12/10/pdftest.pdf");
            fjMap.put("filename", "投资估算审核表pdf");
            fjMap.put("tempName", "黄凌涛");
            fjList.add(fjMap);
            psgcMap.put("cl", fjList);// 材料（附件）
            dataList.add(psgcMap);

            Map<String, String> params = new HashMap<>();
            params.put("dataMap", JSON.toJSONString(dataMap));
            params.put("dataList", JSON.toJSONString(dataList));

            HttpResult hst = httpClientOperate.doPost(endpoint, params);
            System.out.println(hst.toString());
            FGWResponse fGWResponse = JSON.toJavaObject(JSON.parseObject(hst.getContent()),FGWResponse.class);
            resultMsg.setReCode(fGWResponse.getRestate());
            resultMsg.setReMsg(fGWResponse.getRedes());
            resultMsg.setFlag((IFResultCode.RECODE.OK.getCode()).equals(fGWResponse.getRestate()));
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            logger.info("项目回调接口异常："+errorMsg);
            resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),errorMsg);
        }
        //添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setLogCode(resultMsg.getReCode());
        log.setMessage(resultMsg.getReMsg());
        log.setModule(Constant.LOG_MODULE.INTERFACE.getValue() + "【项目回调接口】");
        log.setResult(resultMsg.isFlag() ? Constant.EnumState.YES.getValue() : Constant.EnumState.NO.getValue());
        log.setLogger(this.getClass().getName() + ".sendSignMsg");
        //优先级别高
        log.setLogLevel(Constant.EnumState.PROCESS.getValue());
        logService.save(log);
        return resultMsg;
    }


    /*@RequestMapping(name = "课题研究审核", value = "/auditTopicResult", method = RequestMethod.POST)
    @Transactional
    public ResultMsg auditTopicResult(@RequestBody ResultMsg resultMsg) {
        TopicInfoDto topicInfoDto = (TopicInfoDto) resultMsg.getReObj();
        ResultMsg returnMsg = topicInfoService.dealReturnAudit(resultMsg, topicInfoDto);
        //添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setLogCode(returnMsg.getReCode());
        log.setMessage(returnMsg.getReMsg());
        log.setModule(Constant.LOG_MODULE.INTERFACE.getValue() + "【课题研究审核】");
        log.setBuninessId(Validate.isObject(topicInfoDto) ? topicInfoDto.getId() : "");
        log.setBuninessType(Constant.BusinessType.TOPIC.getValue());
        log.setResult(returnMsg.isFlag() ? Constant.EnumState.YES.getValue() : Constant.EnumState.NO.getValue());
        log.setLogger(this.getClass().getName() + ".auditTopicResult");
        //优先级别高
        log.setLogLevel(Constant.EnumState.PROCESS.getValue());
        logService.save(log);
        return returnMsg;
    }*/

    @RequestMapping(name = "项目签收信息", value = "/testJson")
    public void testJson() throws IOException {
        String REST_SERVICE_URI = "http://localhost:8080/szcshl-web/intfc/pushProject";
        SignDto signDto = new SignDto();
        //委里收文编号
        signDto.setFilecode("D201800117");
        signDto.setIschangeEstimate(null);
        signDto.setDeclaration(null);
        signDto.setMaindeptName("投资处");
        signDto.setAssistDeptUserName("罗松");
        signDto.setCountryCode("2018-440300-65-01-502631");
        signDto.setReviewstage("STAGEBUDGET");
        signDto.setProjectcode("Z-2018-I65-502631-03-01");
        signDto.setProjectname("深圳市投资项目在线审批 监管平台升级拓展项目");
        signDto.setUrgencydegree("一般");
        signDto.setBuiltCompUserName("田云");
        signDto.setAssistdeptName("高技术产业处");
        signDto.setDesigncompanyName("深圳市艾泰克工程咨询监理有限公司");
        signDto.setYearplantype("C类");
        signDto.setAcceptDate(DateUtils.converToDate("2018-05-17 00:00:00","yyyy-MM-dd HH:mm:ss"));
        signDto.setSecrectlevel("公开");
        signDto.setMainDeptContactPhone("13510285489");
        signDto.setMainDeptUserName("李斌");
        signDto.setBuiltcompanyName("深圳市政务服务管理办公室");
        signDto.setMaindeptOpinion("请李斌同志主办。[投资处]2018-05-17;\n项目单位申报项目总概算1656.75万元，主要功能模块包括业务应用平台升级拓展、数据补充登记系统、数据分析与监管、业务梳理交叉设计、协同审批业务实施等。建议将有关资料转请评审中心评审，妥否，请领导审定。[李斌]2018-05-17;\n同意转请评审中心评审。[吴江]2018-05-18;\n转请评审中心进行评审。[李斌]2018-05-18");
        //附件列表
        List<SysFileDto> fileDtoList = new ArrayList<>();
        SysFileDto sysFileDto = new SysFileDto();
        //显示名称，后缀名也要
        sysFileDto.setShowName("空白.docx.docx");
        //附件大小，Long类型
        sysFileDto.setFileSize(11213L);
        //附件下载地址
        sysFileDto.setFileUrl("http://172.18.225.56:8089/FGWPM/LEAP/Download/default/2018/5/17/20180517143816590.docx");
        fileDtoList.add(sysFileDto);
        //项目添加附件列表
        signDto.setSysFileDtoList(fileDtoList);

        Map<String, String> params = new HashMap<>();
        params.put("signDtoJson", JSON.toJSONString(signDto));
        HttpResult hst = httpClientOperate.doPost(REST_SERVICE_URI, params);
        //System.out.println(params.get("signDtoJson"));
        System.out.println(hst.toString());
    }


}
