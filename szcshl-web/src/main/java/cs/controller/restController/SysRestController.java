package cs.controller.restController;

import com.alibaba.fastjson.JSON;
import cs.ahelper.HttpClientOperate;
import cs.ahelper.HttpResult;
import cs.ahelper.IgnoreAnnotation;
import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.Validate;
import cs.domain.sys.Log;
import cs.model.project.SignDto;
import cs.model.topic.TopicInfoDto;
import cs.service.project.SignService;
import cs.service.sys.LogService;
import cs.service.topic.TopicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统接口controller
 * @author ldm
 * Created by ldm on 2017/8/25.
 */
@RestController
@RequestMapping(name = "系统接口", path = "intfc")
@IgnoreAnnotation
public class SysRestController {

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
     * @param signDtoJson
     * @return
     */
    @RequestMapping(name="项目签收信息",value = "/pushProject", method = RequestMethod.POST)
    @Transactional
    public ResultMsg pushProject(@RequestParam String signDtoJson) {
        //json转出对象
        SignDto signDto = JSON.parseObject(signDtoJson,SignDto.class);
        ResultMsg resultMsg = signService.pushProject(signDto);
        //添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setLogCode(resultMsg.getReCode());
        log.setModule(Constant.LOG_MODULE.INTERFACE.getValue()+"【项目推送接口】");
        log.setMessage(resultMsg.getReMsg());
        log.setBuninessId(Validate.isObject(resultMsg.getReObj())?resultMsg.getReObj().toString():"");
        log.setBuninessType(Constant.BusinessType.SIGN.getValue());
        log.setResult(resultMsg.isFlag()? Constant.EnumState.YES.getValue(): Constant.EnumState.NO.getValue());
        log.setLogger(this.getClass().getName()+".pushProject");
        //优先级别高
        log.setLogLevel(Constant.EnumState.PROCESS.getValue());
        logService.save(log);

        resultMsg.setReObj(null);
        return resultMsg;
    }


    /**
     * 课题研究审核
     * @param resultMsg
     * @return
     */
    @RequestMapping(name="课题研究审核",value = "/auditTopicResult", method = RequestMethod.POST)
    @Transactional
    public ResultMsg auditTopicResult(@RequestBody ResultMsg resultMsg) {
        TopicInfoDto topicInfoDto = (TopicInfoDto) resultMsg.getReObj();
        ResultMsg returnMsg = topicInfoService.dealReturnAudit(resultMsg,topicInfoDto);
        //添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setLogCode(returnMsg.getReCode());
        log.setMessage(returnMsg.getReMsg());
        log.setModule(Constant.LOG_MODULE.INTERFACE.getValue()+"【课题研究审核】");
        log.setBuninessId(Validate.isObject(topicInfoDto)?topicInfoDto.getId():"");
        log.setBuninessType(Constant.BusinessType.TOPIC.getValue());
        log.setResult(returnMsg.isFlag()? Constant.EnumState.YES.getValue(): Constant.EnumState.NO.getValue());
        log.setLogger(this.getClass().getName()+".auditTopicResult");
        //优先级别高
        log.setLogLevel(Constant.EnumState.PROCESS.getValue());
        logService.save(log);
        return returnMsg;
    }


    @RequestMapping(name="项目签收信息",value = "/testJson")
    public void testJson() throws IOException {
        String REST_SERVICE_URI = "http://localhost:8080/szcshl-web/intfc/";
        SignDto signDto = new SignDto();
        signDto.setSignid("122");
        signDto.setCreatedBy("系统管理员");
        Map<String, String> params = new HashMap<>();
        params.put("signDtoJson",JSON.toJSONString(signDto));
        HttpResult hst = httpClientOperate.doPost(REST_SERVICE_URI+"/pushProject", params);
        System.out.println(hst.toString());
    }
}
