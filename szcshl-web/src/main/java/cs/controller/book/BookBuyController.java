package cs.controller.book;

import cs.service.flow.FlowService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * Created by zsl on 2017-09-04.
 */
public class BookBuyController {
   private String ctrlName = "bookBuy";
    private static Logger logger = Logger.getLogger(BookBuyController.class.getName());
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private FlowService flowService;

    //初始化图书购买流程
    @RequestMapping(name = "图书购买流程",path = "initBookBuyFlow",method = RequestMethod.POST)
    @Transactional
    public @ResponseBody
    String initProjectFlow(){
        flowService.deployementProcessByZip("activiti/booksbuyflow.zip","图书购买流程");
        logger.info("开始部署图书购买流程...");
        InputStream in=this.getClass().getClassLoader().getResourceAsStream("activiti/booksbuyflow.zip");
        ZipInputStream zipIn=new ZipInputStream(in);
        Deployment deployment = repositoryService.createDeployment().addZipInputStream(zipIn).name("图书购买流程").deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        logger.info("图书购买流程部署成功,流程名称-"+processDefinition.getName()+",流程ID-"+processDefinition.getId());
        return "init bookBuy Flow success";
    }
}
