package cs.controller.manager;

import cs.ahelper.MudoleAnnotation;
import cs.model.PageModelDto;
import cs.model.expert.ExpertReviewDto;
import cs.repository.odata.ODataObj;
import cs.service.expert.ExpertReviewService;
import cs.service.manager.ReviewFeeService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * Description: 评审费发放
 * Author: mcl
 * Date: 2017/10/11 16:26
 */
@Controller
@RequestMapping( name = "评审费发放" , path = "reviewFee")
@MudoleAnnotation(name = "系统管理员" , value="permission#manager")
public class ReviewFeeController {

    private String ctrlName = "reviewFee";

    @Autowired
    private ReviewFeeService reviewFeeService;

    @Autowired
    private ExpertReviewService expertReviewService;


    @RequiresAuthentication
    @RequestMapping(name="查询评审费发放超期的（项目、课题）信息" , path = "findReviewFee" , method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<ExpertReviewDto> findSignDispaWork(HttpServletRequest request) throws ParseException {
        ODataObj oDataObj = new ODataObj(request);
       /* String businessType = null;
        if(oDataObj.getFilter() !=null && oDataObj.getFilter().size()>0){
           ODataFilterItem reviewFeeODataFilterItem = oDataObj.getFilter().get(0);
            businessType = reviewFeeODataFilterItem.getValue().toString();
        }*/
        return expertReviewService.findOverTimeReview(oDataObj);
    }


    @RequiresPermissions("reviewFee#html/list#get")
    @RequestMapping(name="评审费发放" , path="html/list" , method = RequestMethod.GET)
    public String list(){
        return ctrlName + "/list";
    }
}