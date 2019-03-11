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
import cs.common.constants.SysConstants;
import cs.common.utils.*;
import cs.domain.project.Sign;
import cs.domain.sys.Log;
import cs.domain.sys.SMSLog;
import cs.domain.sys.User;
import cs.model.project.SignDto;
import cs.model.project.SignPreDto;
import cs.model.sys.SysFileDto;
import cs.service.restService.SignRestService;
import cs.service.rtx.RTXService;
import cs.service.sys.LogService;
import cs.service.sys.MsgService;
import cs.service.sys.WorkdayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static cs.common.constants.Constant.MsgType.incoming_type;

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
    private HttpClientOperate httpClientOperate;

    @Autowired
    private LogService logService;

    @Autowired
    private RTXService rtxService;

    @Autowired
    private MsgService msgService;

    @Autowired
    private WorkdayService workdayService;
    /**
     * 项目签收信息
     *
     * @param signDtoJson
     * @return
     */
    @RequestMapping(name = "项目签收信息", value = "/pushProject", method = RequestMethod.POST)
    public ResultMsg pushProject(@RequestParam String signDtoJson) {
        ResultMsg resultMsg = null;
        String projName = "";
        String fileCode = "";
        try {
            //解析json串
            SignDto signDto = JSON.parseObject(signDtoJson, SignDto.class);
            if(Validate.isObject(signDto)){
                projName = signDto.getProjectname();
                fileCode = signDto.getFilecode();
            }
            //日记信息
            Log log = new Log();
            log.setModule("系统接口【委里推送数据接口】");
            log.setParamsInfo(signDtoJson);
            log.setLogLevel("1");
            HttpServletRequest request = RequestUtils.getHttpServletRequest();
            //IP地址
            log.setIpAdd(RequestUtils.getIpAddr(request));
            log.setUserName("委里");
            //操作日期
            log.setCreatedDate(new Date());
            //类名
            log.setLogger("cs.controller.restController.SysRestController");
            //方法名
            log.setLogMethod("pushProject");
            //保存
            resultMsg = signRestService.pushProject(signDto, true,log);
        } catch (Exception e) {
            logger.info("保存委里推送项目异常："+e.getMessage());
            resultMsg = new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SAVE_ERROR.getCode(), e.getMessage());
        }
        //如果已经启用短信提醒,并且在早上8点-晚上8点时间段内
        if (rtxService.rtxSMSEnabled() && SMSUtils.isSendTime()) {
            List<User> recvUserList = msgService.getNoticeUserByConfigKey(SysConstants.SYS_CONFIG_ENUM.SMS_SING_NOTICE_USER.toString());
            if(Validate.isList(recvUserList)){
                String msgContent = SMSUtils.buildSendMsgContent(null,incoming_type.name(),projName+"["+fileCode+"]",resultMsg.isFlag());
                SMSLog smsLog = new SMSLog();
                smsLog.setSmsLogType(incoming_type.name());
                smsLog.setProjectName(projName);
                smsLog.setFileCode(fileCode);
                if(resultMsg.isFlag()){
                    Sign sign = (Sign) resultMsg.getReObj();
                    smsLog.setBuninessId(sign.getSignid());
                }
                //发送短信
                SMSUtils.sendMsg(msgService,recvUserList,msgContent,smsLog);
            }
        }
        resultMsg.setReObj(null);
        return resultMsg;
    }

    /**
     * 根据委里收文编号获取项目信息
     *
     * @param fileCode 委里收文编号
     * @param signType 签收类型（1为预签收，其它为正式签收）
     * @return
     */
    @RequestMapping(name = "项目预签收信息", value = "/getPreSign", method = RequestMethod.GET)
    @ResponseBody
    @LogMsg(module = "系统接口【通过收文编号获取项目信息】", logLevel = "1")
    public synchronized ResultMsg findPreSignByfileCode(@RequestParam String fileCode, @RequestParam String signType) {
        String signPreInfo = "";
        ResultMsg resultMsg = null;
        try {
            String preUrl = signRestService.getPreReturnUrl();
            preUrl = preUrl + "?swbh=" + fileCode;
            signPreInfo = httpClientOperate.doGet(preUrl);
            //  JSON.
            String msg = "";
            Map resultMap = (Map) JSON.parse(signPreInfo);
            if (resultMap.get("data") != null && !"null".equals(resultMap.get("data"))) {
                SignPreDto signPreDto = JSON.parseObject(signPreInfo, SignPreDto.class);
                //json转出对象
                if (Validate.isString(signType) && "1".equals(signType)) {
                    //获取项目预签收信息
                    resultMsg = signRestService.pushPreProject(signPreDto.getData(), false);
                } else {
                    resultMsg = signRestService.pushProject(signPreDto.getData(), false,null);
                }
            } else {
                msg = "该项目信息不存在请核查！";
                resultMsg = new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SAVE_ERROR.getCode(), msg);
            }
            if(!Validate.isObject(resultMsg)){
                resultMsg = new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_DEAL_ERROR.getCode(), IFResultCode.IFMsgCode.SZEC_DEAL_ERROR.getValue());
            }
        } catch (Exception e) {
            resultMsg = new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SAVE_ERROR.getCode(), e.getMessage());
            e.printStackTrace();
        }
        return resultMsg;
    }

    /**
     * 通过收文编号存储批复金额下载pdf文件
     *
     * @return
     */
    @RequestMapping(name = "项目批复金额与pdf文件下载", value = "/downRemoteFile", method = RequestMethod.POST)
    @LogMsg(module = "系统接口【通过收文编号存储批复金额下载pdf文件】", logLevel = "1")
    public synchronized ResultMsg downRemoteFile(@RequestParam String signDtoJson) {
        ResultMsg resultMsg = null;
        //反编译json串
        //signDtoJson = XssShieldUtil.getInstance().unStripXss(signDtoJson);
        //解析json串
        SignDto signDto = JSON.parseObject(signDtoJson, SignDto.class);
        try {
            resultMsg = signRestService.signProjAppr(signDto, true);
        } catch (Exception e) {
            resultMsg = new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SAVE_ERROR.getCode(), e.getMessage());
            e.printStackTrace();
        }
        return resultMsg;
    }

    /**
     * 项目信息返回委里接口
     *
     * @return
     */
    @RequestMapping(name = "项目批复金额与pdf文件下载", value = "/testSendSignMsg", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultMsg sendSignMsg(String nodeNameKey) {
        ResultMsg resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败！");
        try {
            // 接口地址
            String endpoint = "http://203.91.46.83:8032/FGWPM/restservices/FGWPMRest/uploadPszxData/query";
            //1、评审意见对象
            Map<String, Object> dataMap = new HashMap<String, Object>();
            // dataMap.put("xmmc", "HLT备案11201644");// 项目名称
            // dataMap.put("jsdw", "测试建设单位");// 建设单位
            dataMap.put("swbh", "KY201700022");// 收文编号
            dataMap.put("psfs", "1");// 评审方式
            dataMap.put("sssj", (new Date()).getTime());// 送审日期
            dataMap.put("psjssj", (new Date()).getTime());// 评审结束时间
            dataMap.put("xmpsyd", "1、《可研报告》提出的主要改造内容和规模与项目建议书批复一致。由于外挂电梯工程增加了楼栋主体结构荷载，下阶段应根据相关标准与规范，补充结构安全检测鉴定报告，并根据鉴定结果进一步优化完善改造内容和方案。\n2、经审核，本项目建安工程费、医用设施购置费和工程建设其他费均与项目建议书批复一致，本阶段预备费费率按5%计取，则投资估算调整为4848.55万元，其中，建安工程费3114.73万元（综合单价为4822元/m2）、医用设施购置费1221.4万元、工程建设其他费339.7万元、预备费172.72万元。");// 项目评审要点
            dataMap.put("sb", 1.2);// 申报投资额（万元）
            dataMap.put("sd", 2.1);// 审定投资额（万元）
            dataMap.put("hjz", 6.2);// 核减（增）投资额（万元）
            dataMap.put("hjzl", 58.62);// 核减（增）率
            dataMap.put("psbz", "备注备注备注备注备注备注备注");// 备注
            dataMap.put("xmjsbyx", "项目建设必要性项目建设必要性项目建设必要性项目建设必要性项目建设必要性");// 项目建设必要性
            dataMap.put("jsnrjgm", "建设内容及规模建设内容及规模建设内容及规模建设内容及规模建设内容及规模建设内容及规模建设内容及规模");// 建设内容及规模
            dataMap.put("tzksjzjly", "投资估算及资金来源投资估算及资金来源投资估算及资金来源投资估算及资金来源投资估算及资金来源投资估算及资金来源10%投资估算及资金来源,##22");// 投资估算及资金来源
            dataMap.put("xyjdgzyq", "下一阶段工作要求下一阶段工作要求下一阶段工作要求");// 下一阶段工作要求

            //2、附件列表
            ArrayList<HashMap<String, Object>> fjList = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> fjMap = new HashMap<String, Object>();
            fjMap.put("url", "http://localhost:8089/FGWPM/LEAP/Download/default/2017/12/10/pdftest.pdf");
            fjMap.put("filename", "评审报告pdf");
            fjMap.put("tempName", "欧可宏");
            fjList.add(fjMap);
            fjMap = new HashMap<String, Object>();
            fjMap.put("url", "http://localhost:8089/FGWPM/LEAP/Download/default/2017/12/10/wordtest.docx");
            fjMap.put("filename", "评审报告word");
            fjMap.put("tempName", "欧可宏");
            fjList.add(fjMap);
            dataMap.put("psbg", fjList);// 评审报告（需上传pdf和word文档）

            /*fjList = new ArrayList<HashMap<String, Object>>();
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
            dataMap.put("qtfj", fjList);// 其它*/

            //3、办理意见
            ArrayList<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> psgcMap = new HashMap<String, Object>();
            psgcMap.put("blhj", "1");// 办理环节
            psgcMap.put("psblyj", "办理意见办理意见办理意见办理意见办理意见");// 办理意见
            psgcMap.put("blr", "办理人");// 办理人
            psgcMap.put("blsj", (new Date()).getTime());// 办理时间
            /*fjList = new ArrayList<HashMap<String, Object>>();
            fjMap = new HashMap<String, Object>();
            fjMap.put("url", "http://localhost:8089/FGWPM/LEAP/Download/default/2017/12/10/pdftest.pdf");
            fjMap.put("filename", "投资估算审核表pdf");
            fjMap.put("tempName", "黄凌涛");
            fjList.add(fjMap);
            psgcMap.put("cl", fjList);// 材料（附件）*/
            dataList.add(psgcMap);

            Map<String, String> params = new HashMap<>();
            params.put("dataMap", JSON.toJSONString(dataMap));
            params.put("dataList", JSON.toJSONString(dataList));

            HttpResult hst = httpClientOperate.doPost(endpoint, params,true);
            System.out.println(hst.toString());

            if (Validate.isObject(hst) && (200 == hst.getStatusCode())) {
                FGWResponse fGWResponse = JSON.toJavaObject(JSON.parseObject(hst.getContent()), FGWResponse.class);
                resultMsg.setReCode(fGWResponse.getRestate());
                resultMsg.setReMsg(fGWResponse.getRedes());
                resultMsg.setFlag((IFResultCode.RECODE.OK.getCode()).equals(fGWResponse.getRestate()));
            } else {
                resultMsg.setReCode("ERROR");
                resultMsg.setReMsg("异常");
                resultMsg.setFlag(false);
            }

        } catch (Exception e) {
            String errorMsg = e.getMessage();
            logger.info("项目回调接口异常：" + errorMsg);
            resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), errorMsg);
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

//        signRestService.getListUser("收文成功");
        //项目签收案例
        String REST_SERVICE_URI = "http://localhost:8181/szcshl-web/intfc/pushProject";
        SignDto signDto = new SignDto();
        //委里收文编号
        signDto.setFilecode("D201900732");
        signDto.setIschangeEstimate(null);
        signDto.setDeclaration((BigDecimal.valueOf(6835)));
        signDto.setUrgencydegree("一般");
        signDto.setMaindeptName("社会发展处");
        signDto.setAssistDeptUserName("李斌");
        signDto.setCountryCode("2017-440300-83-01-102870");
        signDto.setProjectname("深圳大学卡尔森国际肿瘤中心用房改造装修工程2");
        signDto.setProjectcode("Z-2017-Q83-102870-02-01");
        signDto.setBuiltCompUserName("刘泽慧");
        signDto.setAssistdeptName("投资处");
        signDto.setDesigncompanyName(null);
        signDto.setYearplantype("C类");
        signDto.setReviewstage("STAGEBUDGET");
        signDto.setSendusersign("张路");
        signDto.setAcceptDate(DateUtils.converToDate("2018-12-26 09:48:54.0","yyyy-MM-dd HH:mm:ss"));
        signDto.setSecrectlevel("公开");
        signDto.setMainDeptContactPhone("13723444083");
        signDto.setMainDeptUserName("张路");
        signDto.setBuiltcompanyName("深圳大学");
        signDto.setMaindeptOpinion("请张路主办。[社会处处长]2018-12-26;经核，该项目申报材料已完备，建议送审。妥否，请领导审定。[张路]2018-12-26;请评审中心评审。[社会处处长]2018-12-27");

        //附件列表
        List<SysFileDto> fileDtoList = new ArrayList<>();
        SysFileDto sysFileDto = new SysFileDto();
        sysFileDto.setShowName("测试附件[1].pdf");
        sysFileDto.setFileSize(1002398L);
        sysFileDto.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160056527.pdf");
        fileDtoList.add(sysFileDto);

        SysFileDto sysFileDto1 = new SysFileDto();
        sysFileDto1.setShowName("测试附件[2].pdf");
        sysFileDto1.setFileSize(1423571L);
        sysFileDto1.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059436.pdf");
        fileDtoList.add(sysFileDto1);

        SysFileDto sysFileDto2 = new SysFileDto();
        sysFileDto2.setShowName("测试附件[3].pdf");
        sysFileDto2.setFileSize(608288L);
        sysFileDto2.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059514.pdf");
        fileDtoList.add(sysFileDto2);

        SysFileDto sysFileDto3 = new SysFileDto();
        sysFileDto3.setShowName("测试附件[4].pdf");
        sysFileDto3.setFileSize(13270274L);
        sysFileDto3.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059614.pdf");
        fileDtoList.add(sysFileDto3);

        SysFileDto sysFileDto4 = new SysFileDto();
        sysFileDto4.setShowName("测试附件[5].pdf");
        sysFileDto4.setFileSize(13270274L);
        sysFileDto4.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160057945.pdf");
        fileDtoList.add(sysFileDto4);

        SysFileDto sysFileDto5 = new SysFileDto();
        sysFileDto5.setShowName("测试附件[6].pdf");
        sysFileDto5.setFileSize(13270274L);
        sysFileDto5.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160058007.pdf");
        fileDtoList.add(sysFileDto5);

        SysFileDto sysFileDto6 = new SysFileDto();
        sysFileDto6.setShowName("测试附件[7].pdf");
        sysFileDto6.setFileSize(1002398L);
        sysFileDto6.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160056527.pdf");
        fileDtoList.add(sysFileDto6);

        SysFileDto sysFileDto7 = new SysFileDto();
        sysFileDto7.setShowName("测试附件[8].pdf");
        sysFileDto7.setFileSize(1423571L);
        sysFileDto7.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059436.pdf");
        fileDtoList.add(sysFileDto7);

        SysFileDto sysFileDto8 = new SysFileDto();
        sysFileDto8.setShowName("测试附件[9].pdf");
        sysFileDto8.setFileSize(608288L);
        sysFileDto8.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059514.pdf");
        fileDtoList.add(sysFileDto8);

        SysFileDto sysFileDto9 = new SysFileDto();
        sysFileDto9.setShowName("测试附件[10].pdf");
        sysFileDto9.setFileSize(13270274L);
        sysFileDto9.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059614.pdf");
        fileDtoList.add(sysFileDto9);

        SysFileDto sysFileDto10 = new SysFileDto();
        sysFileDto10.setShowName("测试附件[11].pdf");
        sysFileDto10.setFileSize(13270274L);
        sysFileDto10.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160057945.pdf");
        fileDtoList.add(sysFileDto10);

        SysFileDto sysFileDto11 = new SysFileDto();
        sysFileDto11.setShowName("测试附件[12].pdf");
        sysFileDto11.setFileSize(13270274L);
        sysFileDto11.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160058007.pdf");
        fileDtoList.add(sysFileDto11);

        SysFileDto sysFileDto12 = new SysFileDto();
        sysFileDto12.setShowName("测试附件[13].pdf");
        sysFileDto12.setFileSize(1002398L);
        sysFileDto12.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160056527.pdf");
        fileDtoList.add(sysFileDto12);

        SysFileDto sysFileDto13 = new SysFileDto();
        sysFileDto13.setShowName("测试附件[14].pdf");
        sysFileDto13.setFileSize(1423571L);
        sysFileDto13.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059436.pdf");
        fileDtoList.add(sysFileDto13);

        SysFileDto sysFileDto14 = new SysFileDto();
        sysFileDto14.setShowName("测试附件[15].pdf");
        sysFileDto14.setFileSize(608288L);
        sysFileDto14.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059514.pdf");
        fileDtoList.add(sysFileDto14);

        SysFileDto sysFileDto15 = new SysFileDto();
        sysFileDto15.setShowName("测试附件[16].pdf");
        sysFileDto15.setFileSize(13270274L);
        sysFileDto15.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059614.pdf");
        fileDtoList.add(sysFileDto15);

        SysFileDto sysFileDto16 = new SysFileDto();
        sysFileDto16.setShowName("测试附件[17].pdf");
        sysFileDto16.setFileSize(13270274L);
        sysFileDto16.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160057945.pdf");
        fileDtoList.add(sysFileDto16);

        SysFileDto sysFileDto17 = new SysFileDto();
        sysFileDto17.setShowName("测试附件[18].pdf");
        sysFileDto17.setFileSize(13270274L);
        sysFileDto17.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160058007.pdf");
        fileDtoList.add(sysFileDto17);

        SysFileDto sysFileDto18 = new SysFileDto();
        sysFileDto18.setShowName("测试附件[19].pdf");
        sysFileDto18.setFileSize(1002398L);
        sysFileDto18.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160056527.pdf");
        fileDtoList.add(sysFileDto18);

        SysFileDto sysFileDto19 = new SysFileDto();
        sysFileDto19.setShowName("测试附件[20].pdf");
        sysFileDto19.setFileSize(1423571L);
        sysFileDto19.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059436.pdf");
        fileDtoList.add(sysFileDto19);

        SysFileDto sysFileDto20 = new SysFileDto();
        sysFileDto20.setShowName("测试附件[21].pdf");
        sysFileDto20.setFileSize(608288L);
        sysFileDto20.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059514.pdf");
        fileDtoList.add(sysFileDto20);

        SysFileDto sysFileDto21 = new SysFileDto();
        sysFileDto21.setShowName("测试附件[22].pdf");
        sysFileDto21.setFileSize(13270274L);
        sysFileDto21.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059614.pdf");
        fileDtoList.add(sysFileDto21);

        SysFileDto sysFileDto22 = new SysFileDto();
        sysFileDto22.setShowName("测试附件[23].pdf");
        sysFileDto22.setFileSize(13270274L);
        sysFileDto22.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160057945.pdf");
        fileDtoList.add(sysFileDto22);

        SysFileDto sysFileDto23 = new SysFileDto();
        sysFileDto23.setShowName("测试附件[24].pdf");
        sysFileDto23.setFileSize(13270274L);
        sysFileDto23.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160058007.pdf");
        fileDtoList.add(sysFileDto23);

        SysFileDto sysFileDto24 = new SysFileDto();
        sysFileDto24.setShowName("测试附件[25].pdf");
        sysFileDto24.setFileSize(1002398L);
        sysFileDto24.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160056527.pdf");
        fileDtoList.add(sysFileDto24);

        SysFileDto sysFileDto25 = new SysFileDto();
        sysFileDto25.setShowName("测试附件[26].pdf");
        sysFileDto25.setFileSize(1423571L);
        sysFileDto25.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059436.pdf");
        fileDtoList.add(sysFileDto25);

        SysFileDto sysFileDto26 = new SysFileDto();
        sysFileDto26.setShowName("测试附件[27].pdf");
        sysFileDto26.setFileSize(608288L);
        sysFileDto26.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059514.pdf");
        fileDtoList.add(sysFileDto26);

        SysFileDto sysFileDto27 = new SysFileDto();
        sysFileDto27.setShowName("测试附件[28].pdf");
        sysFileDto27.setFileSize(13270274L);
        sysFileDto27.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059614.pdf");
        fileDtoList.add(sysFileDto27);

        SysFileDto sysFileDto28 = new SysFileDto();
        sysFileDto28.setShowName("测试附件[29].pdf");
        sysFileDto28.setFileSize(13270274L);
        sysFileDto28.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160057945.pdf");
        fileDtoList.add(sysFileDto28);

        SysFileDto sysFileDto29 = new SysFileDto();
        sysFileDto29.setShowName("测试附件[30].pdf");
        sysFileDto29.setFileSize(13270274L);
        sysFileDto29.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160058007.pdf");
        fileDtoList.add(sysFileDto29);

        SysFileDto sysFileDto30 = new SysFileDto();
        sysFileDto30.setShowName("测试附件[31].pdf");
        sysFileDto30.setFileSize(1002398L);
        sysFileDto30.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160056527.pdf");
        fileDtoList.add(sysFileDto30);

        SysFileDto sysFileDto31 = new SysFileDto();
        sysFileDto31.setShowName("测试附件[32].pdf");
        sysFileDto31.setFileSize(1423571L);
        sysFileDto31.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059436.pdf");
        fileDtoList.add(sysFileDto31);

        SysFileDto sysFileDto32 = new SysFileDto();
        sysFileDto32.setShowName("测试附件[33].pdf");
        sysFileDto32.setFileSize(608288L);
        sysFileDto32.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059514.pdf");
        fileDtoList.add(sysFileDto32);

        SysFileDto sysFileDto33 = new SysFileDto();
        sysFileDto33.setShowName("测试附件[34].pdf");
        sysFileDto33.setFileSize(13270274L);
        sysFileDto33.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160059614.pdf");
        fileDtoList.add(sysFileDto33);

        SysFileDto sysFileDto34 = new SysFileDto();
        sysFileDto34.setShowName("测试附件[35].pdf");
        sysFileDto34.setFileSize(13270274L);
        sysFileDto34.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160057945.pdf");
        fileDtoList.add(sysFileDto34);

        SysFileDto sysFileDto35 = new SysFileDto();
        sysFileDto35.setShowName("测试附件[36].pdf");
        sysFileDto35.setFileSize(13270274L);
        sysFileDto35.setFileUrl("http://203.91.46.83:8031/FGWPM/LEAP/Download/default/2019/3/5/20190305160058007.pdf");
        fileDtoList.add(sysFileDto35);

        //项目添加附件列表
        signDto.setSysFileDtoList(fileDtoList);

        Map<String, String> params = new HashMap<>();
        params.put("signDtoJson", JSON.toJSONString(signDto));
        HttpResult hst = httpClientOperate.doPost(REST_SERVICE_URI, params,false);
        System.out.println(params.get("signDtoJson"));
        System.out.println(hst.toString());
    }

    @RequestMapping(name = "项目签收信息", value = "/checkWorkDay")
    public String checkWorkDay() throws IOException {
        if( workdayService.isWorkDay(DateUtils.converToDate("2018-05-02",DateUtils.DATE_PATTERN))){
            return "是";
        }
        return "否";
    }

}
