package cs.controller.achievement;

import cs.ahelper.IgnoreAnnotation;
import cs.service.expert.ExpertReviewService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Description: 业绩管理 控制层
 * author: zsl
 * Date: 2017-8-7 11:32:03
 */
@Controller
@RequestMapping(name = "业绩管理", path = "achievementManager")
@IgnoreAnnotation
public class AchievementController {

    String ctrlName = "achievement";

    @Autowired
    private ExpertReviewService expertReviewService;

    // begin#html
    @RequiresAuthentication
    //@RequiresPermissions("achievementManager#html/achievement#get")
    @RequestMapping(name = "员工业绩汇总", path = "html/achievement", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/achievementSum";
    }


}