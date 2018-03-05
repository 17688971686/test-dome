package cs.mobile.controller;

import cs.controller.flow.FlowController;
import cs.domain.project.SignDispaWork;
import cs.mobile.service.ProStatisticsService;
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
 * Description: APP项目统计
 * Author: mcl
 * Date: 2018/3/2 17:35
 */
@Controller
@RequestMapping(name = "APP项目统计" , path = "api/proStatistics")
public class ProStatisticsController {
    private static Logger log = Logger.getLogger(FlowController.class);
    @Autowired
    private ProStatisticsService proStatisticsService;

//    @RequiresAuthentication
    @RequestMapping(name = "项目查询统计", path = "getSignList", method = RequestMethod.POST)
    @ResponseBody
    public List<SignDispaWork> getSignList(@RequestParam String pageNum , @RequestParam String pageSize , @RequestParam String search){
        int num = Integer.parseInt(pageNum);
        int size = Integer.parseInt(pageSize);
        try {
            search = new String(search.getBytes("ISO-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<SignDispaWork> signDispaWorkList = proStatisticsService.getSignList(num , size , search);
        return signDispaWorkList ;
    }
}