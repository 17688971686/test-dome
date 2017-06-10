package cs.controller.expert;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertReviewDto;
import cs.repository.odata.ODataObj;
import cs.service.expert.ExpertReviewService;

/**
 * Description: 专家评审 控制层
 * author: ldm
 * Date: 2017-5-17 14:02:25
 */
@Controller
@RequestMapping(name = "专家评审", path = "expertReview")
public class ExpertReviewController {

	String ctrlName = "expertReview";
	
    @Autowired
    private ExpertReviewService expertReviewService;

    @RequiresPermissions("expertReview#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<ExpertReviewDto> findByOData(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<ExpertReviewDto> expertReviewDtos = expertReviewService.get(odataObj);	
        return expertReviewDtos;
    }

    @RequiresPermissions("expertReview#refleshExpert#get")
    @RequestMapping(name = "刷新已选专家信息", path = "refleshExpert", method = RequestMethod.GET)
    @ResponseBody
    public List<ExpertDto> refleshExpert(@RequestParam(required=true)String workProgramId,@RequestParam(required=true)String selectType) {      
        return expertReviewService.refleshExpert(workProgramId,selectType);
    }      
    
    @RequiresPermissions("expertReview#updateExpertState#post")
    @RequestMapping(name = "更改专家状态", path = "updateExpertState", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateExpertState(@RequestParam(required=true)String workProgramId,@RequestParam(required=true)String expertIds,
    		@RequestParam(required=true)String state){
        expertReviewService.updateExpertState(workProgramId, expertIds, state);
    }
    
    
    @RequiresPermissions("expertReview#deleteExpert#post")
    @RequestMapping(name = "删除已选专家", path = "deleteExpert", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteExpert(@RequestParam(required=true)String workProgramId,String expertIds,String seleType,String expertSelConditionId){
        expertReviewService.deleteExpert(workProgramId,expertIds,seleType,expertSelConditionId);
    } 
    
    @RequiresPermissions("expertReview##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody ExpertReviewDto record) throws Exception{
        expertReviewService.save(record);
    }
    
    @RequiresPermissions("expertReview#saveExpertReview#post")
    @RequestMapping(name = "保存记录", path = "saveExpertReview", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void saveExpertReview(@RequestParam(required=true)String workProgramId,@RequestParam(required=true)String selectType,
    		@RequestParam(required=true)String expertIds) {
        expertReviewService.save(workProgramId,expertIds,selectType);
    }
    

	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)	
	@Transactional
	public @ResponseBody ExpertReviewDto findById(@RequestParam(required = true)String id){		
		return expertReviewService.findById(id);
	}
	
    @RequiresPermissions("expertReview##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	expertReviewService.delete(id);      
    }

    @RequiresPermissions("expertReview##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody ExpertReviewDto record) {
        expertReviewService.update(record);
    }

    @RequiresPermissions("expertReview#initByWorkProgramId#get")
	@RequestMapping(name = "专家选择", path = "html/initByWorkProgramId",method=RequestMethod.GET)	
	public @ResponseBody List<ExpertReviewDto> initByWorkProgramId(@RequestParam(required = true) String workProgramId){
		return expertReviewService.initByWorkProgramId(workProgramId);
	}
    
    @RequiresPermissions("expertReview#getReviewList#get")
    @RequestMapping(name = "查询专家评分", path = "html/getReviewList",method=RequestMethod.GET)	
    public @ResponseBody Map<String,Object> getReviewList(){
    	return expertReviewService.getReviewList(" ","2017","1");
    }
    
    @RequiresPermissions("expertReview#getSelectExpert#get")
    @RequestMapping(name = "获取已选专家", path = "html/getSelectExpert",method=RequestMethod.POST)	
    public @ResponseBody PageModelDto<ExpertDto> getSelectExpert() throws ParseException{
    	 List<ExpertDto> ExpertDtoList=expertReviewService.getSelectExpert();
    	 PageModelDto<ExpertDto> pageModelDto = new PageModelDto<>();
    	 pageModelDto.setCount(ExpertDtoList.size());
 		 pageModelDto.setValue(ExpertDtoList);	
    	return pageModelDto;
    }
    
    @RequiresPermissions("expertReview#getSelectExpertById#get")
    @RequestMapping(name = "获取已选专家", path = "html/getSelectExpertById",method=RequestMethod.GET)	
    public @ResponseBody ExpertReviewDto getSelectExpertById(String expertId) throws ParseException{
    	ExpertReviewDto expertReviewDto=expertReviewService.getSelectExpertById(expertId);
    	return expertReviewDto;
    }
    
    @RequiresPermissions("expertReview#expertMark#get")
    @RequestMapping(name = "编辑专家评分", path = "html/expertMark",method=RequestMethod.POST)	
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void expertMark(@RequestParam String expertId,String expertMark,String expertDecride) throws ParseException{
    	expertReviewService.expertMark(expertId, expertMark, expertDecride);
    }
    
    @RequiresPermissions("expertReview#savePayment#get")
    @RequestMapping(name = "编辑专家费用", path = "html/savePayment",method=RequestMethod.POST)	
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void savePayment(@RequestBody ExpertReviewDto expertReviewDto) throws Exception{
    	expertReviewService.savePayment(expertReviewDto);
    }
    
    // begin#html
    @RequiresPermissions("expertReview#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list"; 
    }

    @RequiresPermissions("expertReview#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/edit";
    }
    
    @RequiresPermissions("expertReview#html/selectExpert#get")
    @RequestMapping(name = "选择专家", path = "html/selectExpert", method = RequestMethod.GET)
    public String selectExpert() {
    	
        return ctrlName+"/selectExpert";
    } 
    
    @RequiresPermissions("expertReview#html/reviewList#get")
    @RequestMapping(name = "专家评分", path = "html/reviewList", method = RequestMethod.GET)
    public String reviewList() {
    	
    	return ctrlName+"/reviewList";
    } 
    // end#html

}