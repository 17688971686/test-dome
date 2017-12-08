package cs.controller.restController;

import com.alibaba.fastjson.JSON;
import cs.ahelper.HttpClientOperate;
import cs.ahelper.HttpResult;
import cs.ahelper.IgnoreAnnotation;
import cs.common.ResultMsg;
import cs.model.project.SignDto;
import cs.service.project.SignService;
import cs.service.topic.TopicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
        return signService.pushProject(signDto);
    }


    /**
     * 课题研究审核
     * @param resultMsg
     * @return
     */
    @RequestMapping(name="课题研究审核",value = "/auditTopicResult", method = RequestMethod.POST)
    @Transactional
    public ResultMsg auditTopicResult(@RequestBody ResultMsg resultMsg) {

        return topicInfoService.dealReturnAudit(resultMsg);
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
