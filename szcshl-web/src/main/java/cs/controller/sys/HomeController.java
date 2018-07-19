package cs.controller.sys;

import cs.ahelper.IgnoreAnnotation;
import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.utils.RTXUtils;
import cs.service.rtx.RTXService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

@Controller
@IgnoreAnnotation
public class HomeController {
	private String ctrlName = "home";
	private static Logger logger = Logger.getLogger(HomeController.class.getName());

	@Autowired
	private RepositoryService repositoryService;
	
	@RequestMapping(name = "登录", path = "/")
	public String login() {

		return this.ctrlName + "/login";
	}

    /**
     * 腾讯通测试
     * http://172.18.225.26:8000/sendnotify.cgi?msg=hello&receiver=%E4%BD%86%E9%BE%99
     * @return
     */
	@RequiresAuthentication
	@RequestMapping(name = "腾讯通测试",path = "testRTX",method = RequestMethod.GET)
    @ResponseBody
	public ResultMsg testRTX(){
        String reStr = RTXUtils.sendRTXMsg(null,"消息测试","但龙");
        if(reStr == null || "false".equals(reStr)|| "null".equals(reStr)){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"发送失败！");
        }else{
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"发送成功！");
        }
	}

	//初始化流程
    @RequiresAuthentication
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

    @RequiresAuthentication
	@RequestMapping(name = "课题研究流程",path = "initTopicFlow",method = RequestMethod.GET)
	@Transactional
	public @ResponseBody String initTopicFlow(){
		//部署下一个版本
		logger.info("开始部署课题研究流程...");
		InputStream in=this.getClass().getClassLoader().getResourceAsStream("activiti/newtopic.zip");
		ZipInputStream zipIn=new ZipInputStream(in);
		Deployment  deployment = repositoryService.createDeployment().addZipInputStream(zipIn).name("课题研究流程").deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
		logger.info("课题研究程部署成功,流程名称-"+processDefinition.getName()+",流程ID-"+processDefinition.getId());
		return "init Topic Flow success";
	}

    @RequiresAuthentication
	@RequestMapping(name = "图书采购流程",path = "initBooksBuyFlow",method = RequestMethod.GET)
	@Transactional
	public @ResponseBody String initBooksBuyFlow(){
		//部署下一个版本
		logger.info("开始部署图书采购流程...");
		InputStream in=this.getClass().getClassLoader().getResourceAsStream("activiti/booksbuyflow.zip");
		ZipInputStream zipIn=new ZipInputStream(in);
		Deployment  deployment = repositoryService.createDeployment().addZipInputStream(zipIn).name("图书购买流程").deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
		logger.info("图书采购流程部署成功,流程名称-"+processDefinition.getName()+",流程ID-"+processDefinition.getId());

		return "init BooksBuy Flow success";
	}

    @RequiresAuthentication
	@RequestMapping(name = "资产入库流程",path = "initAssertStorageFlow",method = RequestMethod.GET)
	@Transactional
	public @ResponseBody String initAssertStorageFlow(){
		//部署下一个版本
		logger.info("开始部署资产流程...");
		InputStream in=this.getClass().getClassLoader().getResourceAsStream("activiti/assetstorage.zip");
		ZipInputStream zipIn=new ZipInputStream(in);
		Deployment  deployment = repositoryService.createDeployment().addZipInputStream(zipIn).name("资产入库流程").deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
		logger.info("资产入库流程部署成功,流程名称-"+processDefinition.getName()+",流程ID-"+processDefinition.getId());

		return "init AssertStorage Flow success";
	}

	@RequiresAuthentication
	@RequestMapping(name = "项目暂停流程",path = "projectStopFlow",method = RequestMethod.GET)
	@Transactional
	public @ResponseBody String projectStopFlow(){
		//部署下一个版本
		logger.info("开始项目暂停流程...");
		InputStream in=this.getClass().getClassLoader().getResourceAsStream("activiti/projectStop.zip");
		ZipInputStream zipIn=new ZipInputStream(in);
		Deployment  deployment = repositoryService.createDeployment().addZipInputStream(zipIn).name("项目暂停审批流程").deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
		logger.info("项目暂停流程部署成功,流程名称-"+processDefinition.getName()+",流程ID-"+processDefinition.getId());

		return "init ProjectStop Flow success";
	}

    @RequiresAuthentication
    @RequestMapping(name = "档案借阅流程",path = "archivesFlow",method = RequestMethod.GET)
    @Transactional
    public @ResponseBody String archivesFlow(){
        //部署下一个版本
        logger.info("开始档案借阅流程...");
        InputStream in=this.getClass().getClassLoader().getResourceAsStream("activiti/archives.zip");
        ZipInputStream zipIn=new ZipInputStream(in);
        Deployment  deployment = repositoryService.createDeployment().addZipInputStream(zipIn).name("项目档案查阅流程").deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        logger.info("项目档案查阅流程部署成功,流程名称-"+processDefinition.getName()+",流程ID-"+processDefinition.getId());

        return "init Archives Flow success";
    }

	@RequiresAuthentication
	@RequestMapping(name = "优秀评审报告申报流程",path = "appraiseReportFlow",method = RequestMethod.GET)
	@Transactional
	public @ResponseBody String appraiseReportFlow(){
		//部署下一个版本
		logger.info("开始项目暂停流程...");
		InputStream in=this.getClass().getClassLoader().getResourceAsStream("activiti/appraiseReport.zip");
		ZipInputStream zipIn=new ZipInputStream(in);
		Deployment  deployment = repositoryService.createDeployment().addZipInputStream(zipIn).name("优秀评审报告申报").deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
		logger.info("优秀评审报告申报流程部署成功,流程名称-"+processDefinition.getName()+",流程ID-"+processDefinition.getId());

		return "init AppraiseReport Flow success";
	}

	@RequiresAuthentication
	@RequestMapping(name = "拟补充资料函流程",path = "suppLetterFlow",method = RequestMethod.GET)
	@Transactional
	public @ResponseBody String suppLetterFlow(){
		//部署下一个版本
		logger.info("开始拟补充资料函流程...");
		InputStream in=this.getClass().getClassLoader().getResourceAsStream("activiti/suppLetter.zip");
		ZipInputStream zipIn=new ZipInputStream(in);
		Deployment  deployment = repositoryService.createDeployment().addZipInputStream(zipIn).name("拟补充资料函流程").deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
		logger.info("拟补充资料函流程部署成功,流程名称-"+processDefinition.getName()+",流程ID-"+processDefinition.getId());

		return "init SuppLetterFlow Flow success";
	}

	@RequestMapping(name = "月报简报流程",path = "monthlyBulletinFlow",method = RequestMethod.GET)
	@Transactional
	public @ResponseBody String monthlyBulletinFlow(){
		//部署下一个版本
		logger.info("开始月报简报审批流程...");
		InputStream in=this.getClass().getClassLoader().getResourceAsStream("activiti/monthlyBulletin.zip");
		ZipInputStream zipIn=new ZipInputStream(in);
		Deployment  deployment = repositoryService.createDeployment().addZipInputStream(zipIn).name("月报简报流程").deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
		logger.info("月报简报流程部署成功,流程名称-"+processDefinition.getName()+",流程ID-"+processDefinition.getId());
		return "init MonthlyBulletinFlow Flow success";
	}

	@RequestMapping(name = "通知公告流程",path = "annountMentFlow",method = RequestMethod.GET)
	@Transactional
	public @ResponseBody String annountMentFlow(){
		//部署下一个版本
		logger.info("开始通知公告审批流程...");
		InputStream in=this.getClass().getClassLoader().getResourceAsStream("activiti/annountMent.zip");
		ZipInputStream zipIn=new ZipInputStream(in);
		Deployment  deployment = repositoryService.createDeployment().addZipInputStream(zipIn).name("通知公告流程").deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
		logger.info("通知公告流程部署成功,流程名称-"+processDefinition.getName()+",流程ID-"+processDefinition.getId());
		return "init annountMentFlow Flow success";
	}


	@RequestMapping(name = "重做工作方案流程",path = "reworkFlow",method = RequestMethod.GET)
	@Transactional
	public @ResponseBody String reworkFlow(){
		//部署下一个版本
		logger.info("开始重做工作方案流程...");
		InputStream in=this.getClass().getClassLoader().getResourceAsStream("activiti/workhis.zip");
		ZipInputStream zipIn=new ZipInputStream(in);
		Deployment  deployment = repositoryService.createDeployment().addZipInputStream(zipIn).name("重做工作方案流程").deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
		logger.info("重做工作方案流程部署成功,流程名称-"+processDefinition.getName()+",流程ID-"+processDefinition.getId());
		return "init reworkFlow Flow success";
	}
}
