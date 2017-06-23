package cs.controller.sys;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

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
	
	@RequestMapping(name = "登录", path = "/")
	public String login() {

		return this.ctrlName + "/login";
	}

	//初始化流程
	@RequestMapping(name = "项目签收流程",path = "initProjectFlow",method = RequestMethod.GET)
	@Transactional
	public @ResponseBody String initProccess(){	
		//部署下一个版本
		logger.info("开始部署项目签收流程...");
		InputStream in=this.getClass().getClassLoader().getResourceAsStream("activiti/xieshen.zip");
		ZipInputStream zipIn=new ZipInputStream(in);
		Deployment  deployment = repositoryService.createDeployment().addZipInputStream(zipIn).name("项目概算流程").deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
		logger.info("项目签收流程部署成功,流程名称-"+processDefinition.getName()+",流程ID-"+processDefinition.getId());
		
		return "init Project Flow success";
	}
}
