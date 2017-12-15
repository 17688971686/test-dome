package cs.controller.restController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import cs.ahelper.HttpClientOperate;
import cs.ahelper.HttpResult;
import cs.ahelper.IgnoreAnnotation;
import cs.common.Constant;
import cs.common.FGWResponse;
import cs.common.IFResultCode;
import cs.common.ResultMsg;
import cs.common.utils.PropertyUtil;
import cs.common.utils.Validate;
import cs.controller.project.SignController;
import cs.domain.sys.Log;
import cs.model.project.SignDto;
import cs.model.topic.TopicInfoDto;
import cs.service.project.SignService;
import cs.service.sys.LogService;
import cs.service.topic.TopicInfoService;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

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
    private SignService signService;
    @Autowired
    private TopicInfoService topicInfoService;
    @Autowired
    private HttpClientOperate httpClientOperate;
    @Autowired
    private LogService logService;

    /**
     * 项目签收信息
     *
     * @param signDtoJson
     * @return
     */
    @RequestMapping(name = "项目签收信息", value = "/pushProject", method = RequestMethod.POST)
    @Transactional
    public ResultMsg pushProject(@RequestParam String signDtoJson) {
        //json转出对象
        SignDto signDto = JSON.parseObject(signDtoJson, SignDto.class);
        ResultMsg resultMsg = signService.pushProject(signDto);
        //添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setLogCode(resultMsg.getReCode());
        log.setModule(Constant.LOG_MODULE.INTERFACE.getValue() + "【获取项目信息接口】");
        log.setMessage(resultMsg.getReMsg());
        log.setBuninessId(Validate.isObject(resultMsg.getReObj()) ? resultMsg.getReObj().toString() : "");
        log.setBuninessType(Constant.BusinessType.SIGN.getValue());
        log.setResult(resultMsg.isFlag() ? Constant.EnumState.YES.getValue() : Constant.EnumState.NO.getValue());
        log.setLogger(this.getClass().getName() + ".pushProject");
        //优先级别高
        log.setLogLevel(Constant.EnumState.PROCESS.getValue());
        logService.save(log);

        resultMsg.setReObj(null);
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
            psgcMap.put("blhj", IFResultCode.PSGCBLHJ.getCodeByValue(nodeNameKey));// 办理环节
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

    /**
     * 课题研究审核
     *
     * @param resultMsg
     * @return
     */
    @RequestMapping(name = "课题研究审核", value = "/auditTopicResult", method = RequestMethod.POST)
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
    }

    @RequestMapping(name = "项目签收信息", value = "/testJson")
    public void testJson() throws IOException {
        String REST_SERVICE_URI = "http://localhost:8080/szcshl-web/intfc/";
        SignDto signDto = new SignDto();
        signDto.setSignid("122");
        signDto.setCreatedBy("系统管理员");
        Map<String, String> params = new HashMap<>();
        params.put("signDtoJson", JSON.toJSONString(signDto));
        HttpResult hst = httpClientOperate.doPost(REST_SERVICE_URI + "/pushProject", params);
        System.out.println(hst.toString());
    }
}
