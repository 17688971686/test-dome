package cs.mobile.controller;

import cs.controller.flow.FlowController;
import cs.domain.project.SignDispaWork;
import cs.mobile.service.ProStatisticsService;
import cs.model.archives.ArchivesLibraryDto;
import cs.model.asserts.assertStorageBusiness.AssertStorageBusinessDto;
import cs.model.book.BookBuyBusinessDto;
import cs.model.monthly.MonthlyNewsletterDto;
import cs.model.project.AddSuppLetterDto;
import cs.model.project.AppraiseReportDto;
import cs.model.project.ProjectStopDto;
import cs.model.sys.AnnountmentDto;
import cs.model.topic.TopicInfoDto;
import cs.service.archives.ArchivesLibraryService;
import cs.service.asserts.assertStorageBusiness.AssertStorageBusinessService;
import cs.service.book.BookBuyBusinessService;
import cs.service.monthly.MonthlyNewsletterService;
import cs.service.project.AddSuppLetterService;
import cs.service.project.ProjectStopService;
import cs.service.reviewProjectAppraise.AppraiseService;
import cs.service.sys.AnnountmentService;
import cs.service.topic.TopicInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Description: APP待办任务获取详细信息
 * Author: hjm
 * Date: 2018/3/7 9:35
 */
@Controller
@RequestMapping(name = "APP待办任务" , path = "api/pendingTask")
public class PendingTaskController {
    private static Logger log = Logger.getLogger(FlowController.class);
    @Autowired
    private BookBuyBusinessService bookBuyBusinessService;
    @Autowired
    private MonthlyNewsletterService monthlyNewsletterService;
    @Autowired
    private AnnountmentService annService;
    @Autowired
    private AddSuppLetterService addSuppLetterService;
    @Autowired
    private AppraiseService appraiseService;
    @Autowired
    private ProjectStopService projectStopService;
    @Autowired
    private TopicInfoService topicInfoService;
    @Autowired
    private AssertStorageBusinessService assertStorageBusinessService;
    @Autowired
    private ArchivesLibraryService archivesLibraryService;


    @RequestMapping(name = "图书详情", path = "bookBuy",method=RequestMethod.GET)
    public @ResponseBody
    BookBuyBusinessDto bookBuy(@RequestParam(required = true)String id){
        return bookBuyBusinessService.findById(id);
    }

    @RequestMapping(name = "通知公告详情", path = "annount",method=RequestMethod.GET)
    public @ResponseBody
    AnnountmentDto annount(@RequestParam(required = true)String id){
        return annService.findAnnountmentById(id);
    }

    @RequestMapping(name = "拟补充资料函/月报详情", path = "suppletter",method=RequestMethod.GET)
    public @ResponseBody
    AddSuppLetterDto suppLetter(@RequestParam(required = true)String id){
        return addSuppLetterService.findById(id);
    }

    @RequestMapping(name = "优秀评审报告申报详情", path = "appraiseReport",method=RequestMethod.GET)
    public @ResponseBody
    AppraiseReportDto appraiseReport(@RequestParam(required = true)String id){
        return  appraiseService.getAppraiseById(id);
    }

    @RequestMapping(name = "项目暂停详情", path = "projectStop",method=RequestMethod.GET)
    public @ResponseBody
    ProjectStopDto projectStop(@RequestParam(required = true)String id){
        return projectStopService.getProjectStopByStopId(id);
    }

    @RequestMapping(name = "课题研究详情", path = "topic",method=RequestMethod.GET)
    public @ResponseBody
    TopicInfoDto topic(@RequestParam(required = true)String id){
        return topicInfoService.findById(id);
    }

    @RequestMapping(name = "资产入库详情", path = "assertStorage",method=RequestMethod.GET)
    public @ResponseBody
    AssertStorageBusinessDto assertStorage(@RequestParam(required = true)String id){
        return assertStorageBusinessService.findById(id);
    }
    @RequestMapping(name = "档案借阅详情", path = "archives",method=RequestMethod.GET)
    public @ResponseBody
    ArchivesLibraryDto archives(@RequestParam(required = true)String id){
        return archivesLibraryService.findById(id);
    }


}