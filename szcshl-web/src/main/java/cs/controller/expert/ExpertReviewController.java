package cs.controller.expert;

import cs.ahelper.IgnoreAnnotation;
import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertNewInfoDto;
import cs.model.expert.ExpertReviewDto;
import cs.model.expert.ExpertSelectedDto;
import cs.repository.odata.ODataObj;
import cs.service.expert.ExpertReviewService;
import cs.service.expert.ExpertSelConditionService;
import cs.service.expert.ExpertSelectedService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

import static cs.common.constants.Constant.ERROR_MSG;

/**
 * Description: 专家评审 控制层
 * author: ldm
 * Date: 2017-5-17 14:02:25
 */
@Controller
@IgnoreAnnotation
@RequestMapping(name = "专家评审", path = "expertReview")
public class ExpertReviewController {

    String ctrlName = "expertReview";

    @Autowired
    private ExpertReviewService expertReviewService;

    @Autowired
    private ExpertSelConditionService expertSelConditionService;

    @Autowired
    private ExpertSelectedService expertSelectedService;

    @RequiresAuthentication
    //@RequiresPermissions("expertReview#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<ExpertReviewDto> findByOData(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<ExpertReviewDto> expertReviewDtos = expertReviewService.get(odataObj);
        return expertReviewDtos;
    }

    /**
     * 更改抽取专家是否参加会议状态
     *
     * @param expertSelId
     * @param state
     */
    @RequiresAuthentication
    //@RequiresPermissions("expertReview#updateJoinState#post")
    @RequestMapping(name = "更改抽取专家状态", path = "updateJoinState", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg updateExpertState( @RequestParam(required = true)String reviewId,String minBusinessId, String businessType,
                                        @RequestParam(required = true) String expertSelId,@RequestParam(required = true) String state) {
        return expertReviewService.updateJoinState(reviewId,minBusinessId, businessType, expertSelId, state);
    }

    /**
     * 删除新专家信息
     *
     * @param minBusinessId
     * @param
     */
    @RequiresAuthentication
    @RequestMapping(name = "删除新专家信息", path = "deleteExpertNewInfo", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteExpertNewInfo(@RequestParam(required = true) String minBusinessId) {
        expertReviewService.deleteExpertNewInfo(minBusinessId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertReview#affirmAutoExpert#post")
    @RequestMapping(name = "确认抽取专家", path = "affirmAutoExpert", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg affirmAutoExpert(@RequestParam(required = true) String reviewId, @RequestParam(required = true) String minBusinessId,
                                      @RequestParam(required = true) String businessType, @RequestParam(required = true) String expertSelId,
                                      @RequestParam(required = true) String state) {
        //判断已经确认的专家，和设定的专家人数对比
        int totalCount = expertSelConditionService.getExtractEPCount(minBusinessId);
        int selectCount = expertSelectedService.getSelectEPCount(minBusinessId);
        List<String> ids = StringUtil.getSplit(expertSelId, ",");
        if (selectCount + (Validate.isList(ids) ? ids.size() : 0) > totalCount) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，选择专家人数已经超过了抽取设定人数！");
        }
        expertReviewService.updateConfirmState(reviewId,minBusinessId, businessType, expertSelId, state);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertReview#saveExpertReview#post")
    @RequestMapping(name = "保存抽取专家信息", path = "saveExpertReview", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveExpertReview(@RequestParam(required = true) String businessId, @RequestParam(required = true) String minBusinessId, String reviewId,
                                      @RequestParam(required = true) String businessType, @RequestParam(required = true) String expertIds, @RequestParam(required = true) String selectType) {
        ResultMsg resultMsg = expertReviewService.save(businessId, minBusinessId, businessType, reviewId, expertIds, selectType);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "获取业务模块确认的专家", path = "refleshBusinessEP", method = RequestMethod.POST)
    @ResponseBody
    public List<ExpertDto> refleshBusinessEP(@RequestParam(required = true) String businessId) {
        return expertReviewService.refleshBusinessEP(businessId);
    }

    @RequiresAuthentication
    @RequestMapping(name = "主键查询", path = "html/findById", method = RequestMethod.GET)
    @Transactional
    @ResponseBody
    public ExpertReviewDto findById(@RequestParam(required = true) String id) {
        return expertReviewService.findById(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertReview##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        expertReviewService.delete(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertReview##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody ExpertReviewDto record) {
        expertReviewService.update(record);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertReview#initBybusinessId#post")
    @RequestMapping(name = "专家抽取", path = "initBybusinessId", method = RequestMethod.POST)
    public @ResponseBody
    ExpertReviewDto initBybusinessId(@RequestParam(required = true) String businessId, String minBusinessId) {
        return expertReviewService.initBybusinessId(businessId, minBusinessId);
    }

    /*@RequiresPermissions("expertReview#expertMark#get")
    @RequestMapping(name = "编辑专家评分", path = "html/expertMark", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void expertMark(@RequestBody ExpertReviewDto expertReviewDto) throws ParseException {
        expertReviewService.expertMark(expertReviewDto);
    }

    @RequiresPermissions("expertReview#savePayment#get")
    @RequestMapping(name = "编辑专家费用", path = "html/savePayment", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void savePayment(@RequestBody ExpertReviewDto expertReviewDto) throws Exception {
        expertReviewService.savePayment(expertReviewDto);
    }
*/

    //查找专家当月的评审费用
    /*@RequiresAuthentication
    @RequestMapping(name = "获取专家某月的评审费用", path = "getExpertReviewCost", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getExpertReviewCost(
            @RequestParam(required = true) String reviewId,
            @RequestParam(required = true) String expertIds,
            @RequestParam(required = true) String month, HttpServletRequest request) {
        return expertReviewService.getExpertReviewCost(reviewId, expertIds, month);
    }*/

    //保存评审费用(单个)
    @RequiresAuthentication
    @RequestMapping(name = "保存评审费用", path = "saveExpertReviewCostSingle", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveExpertReviewCostSingle(@RequestBody ExpertReviewDto expertReview,@RequestParam(defaultValue = "true")boolean isCountTaxes) {
        return expertReviewService.saveExpertReviewCost(expertReview,isCountTaxes);
    }

    //保存评审费用(多个)
   /* @RequiresAuthentication
    @RequestMapping(name = "保存评审费用", path = "html/saveExpertReviewCost", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void saveExpertReviewCost(@RequestBody ExpertReviewDto[] expertReviews) throws ParseException {
        expertReviewService.saveExpertReviewCost(expertReviews);
    }*/


    //保存新的专家信息
    @RequiresAuthentication
    @RequestMapping(name = "保存新的专家信息", path = "expertNewInfo", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveExpertNewInfo(@RequestBody ExpertSelectedDto[] expertSelectedDtos) {
        return expertReviewService.saveExpertNewInfo(expertSelectedDtos);
    }

    //保存新的专家信息
    @RequiresAuthentication
    @RequestMapping(name = "获取新的专家信息", path = "getExpertInfo", method = RequestMethod.POST)
    @ResponseBody
    public List<ExpertNewInfoDto> getExpertInfo(@RequestParam(required = true) String businessId) {
        return expertReviewService.getExpertInfo(businessId);
    }

    @RequiresAuthentication
    @RequestMapping(name = "保存专家评审费发放打印方案信息", path = "saveSplit", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void saveSplit(@RequestBody ExpertSelectedDto expertSelectedDto) {
        expertReviewService.saveSplit(expertSelectedDto);
    }


    // begin#html
    @RequiresAuthentication
    //@RequiresPermissions("expertReview#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }


    @RequiresAuthentication
    //@RequiresPermissions("expertReview#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/edit";
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertReview#html/selectExpert#get")
    @RequestMapping(name = "编辑专家评审方案", path = "html/selectExpert", method = RequestMethod.GET)
    public String selectExpert() {
        return ctrlName + "/selectExpert";
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertReview#html/reviewList#get")
    @RequestMapping(name = "专家评分", path = "html/reviewList", method = RequestMethod.GET)
    public String reviewList() {

        return ctrlName + "/reviewList";
    }
    // end#html

}