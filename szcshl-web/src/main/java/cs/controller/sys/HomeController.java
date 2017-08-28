package cs.controller.sys;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

import cs.common.Constant;
import cs.common.ResultMsg;
import cs.service.rtx.SendRTXService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
	private String ctrlName = "home";
	private static Logger logger = Logger.getLogger(HomeController.class.getName());

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private SendRTXService sendRTXService;
	
	@RequestMapping(name = "登录", path = "/")
	public String login() {

		return this.ctrlName + "/login";
	}

    /**
     * 腾讯通测试
     * http://172.18.225.26:8000/sendnotify.cgi?msg=hello&receiver=%E4%BD%86%E9%BE%99
     * @return
     */
	@RequestMapping(name = "腾讯通测试",path = "testRTX",method = RequestMethod.GET)
    @ResponseBody
	public ResultMsg testRTX(){
        String reStr = sendRTXService.testSendRTXMsg();
        if(reStr == null || "null".equals(reStr)){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"发送失败！");
        }else{
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"发送成功！");
        }
	}

	//初始化流程
	@RequestMapping(name = "项目签收流程",path = "initProjectFlow",method = RequestMethod.GET)
	@Transactional
	public @ResponseBody String initProjectFlow(){
		//部署下一个版本
		logger.info("开始部署项目签收流程...");
		InputStream in=this.getClass().getClassLoader().getResourceAsStream("activiti/signflow.zip");
		ZipInputStream zipIn=new ZipInputStream(in);
		Deployment  deployment = repositoryService.createDeployment().addZipInputStream(zipIn).name("项目签收流程").deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
		logger.info("项目签收流程部署成功,流程名称-"+processDefinition.getName()+",流程ID-"+processDefinition.getId());
		
		return "init Project Flow success";
	}
}
