package cs.ahelper;

import cs.common.ResultMsg;
import cs.model.project.SignDto;
import org.springframework.web.client.RestTemplate;

/**
 * Created by shenning on 2017/8/25.
 */
public class ResetTestCilent {

    public static final String REST_SERVICE_URI = "http://localhost:8080/szcshl-web/intfc/";
    /* POST */
    private static void pushProject() {
        System.out.println("Testing create User API----------");
        RestTemplate restTemplate = new RestTemplate();
        SignDto signDto = new SignDto();
        signDto.setSignid("122");
        signDto.setCreatedBy("系统管理员");
        ResultMsg resultMsg = restTemplate.postForObject(REST_SERVICE_URI+"/pushProject",signDto, ResultMsg.class);
        System.out.print(resultMsg.toString());
    }

    public static void  main(String[] args){
        ResetTestCilent resetTestCilent = new ResetTestCilent();
        resetTestCilent.pushProject();
    }
}
