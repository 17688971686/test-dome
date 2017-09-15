package cs.controller.expert;

import com.google.gson.JsonObject;
import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertSelConditionDto;
import cs.repository.odata.ODataObj;
import cs.service.expert.ExpertService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping(name = "专家", path = "expert")
public class ExpertController {
	private String ctrlName = "expert";
	@Autowired
	private ExpertService expertService;
	
	@RequiresPermissions("expert#findByOData#post")	
	@RequestMapping(name = "获取专家数据", path = "findByOData", method = RequestMethod.POST)
	public @ResponseBody PageModelDto<ExpertDto> findPageByOData(HttpServletRequest request) throws ParseException {	
		ODataObj odataObj = new ODataObj(request);
		PageModelDto<ExpertDto> expertDtos = expertService.get(odataObj);
		return expertDtos;
	}	
	
	@RequiresPermissions("expert#findRepeatByOData#post")	
	@RequestMapping(name = "查询重复专家", path = "findRepeatByOData", method = RequestMethod.POST)
	public @ResponseBody PageModelDto<ExpertDto> findRepeatByOData(){
		List<ExpertDto> list = expertService.findAllRepeat();
		PageModelDto<ExpertDto> pageModelDto = new PageModelDto<ExpertDto>();				
		pageModelDto.setCount(list.size());
		pageModelDto.setValue(list);
		return pageModelDto;
	}

	@RequestMapping(name = "统计符合条件的专家", path = "countReviewExpert", method = RequestMethod.POST)
	@ResponseBody
	public List<ExpertDto> countReviewExpert(@RequestParam String minBusinessId,@RequestParam String reviewId,@RequestBody ExpertSelConditionDto epSelCondition) {
		return expertService.countExpert(minBusinessId,reviewId,epSelCondition);
	}

    @RequestMapping(name = "专家抽取", path = "autoExpertReview", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg findReviewExpert(@RequestParam String minBusinessId, @RequestParam String reviewId, @RequestBody ExpertSelConditionDto[] paramArrary) {
        return expertService.autoExpertReview(minBusinessId,reviewId,paramArrary);
    }

	@RequiresPermissions("expert##post")
	@RequestMapping(name = "创建专家", path = "",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.CREATED)
	public void  post(@RequestBody ExpertDto expert,HttpServletResponse response)  {
		  JsonObject jsonObject = new JsonObject();
		String expertID=expertService.createExpert(expert);	
		jsonObject.addProperty("expertID",expertID);
		try {
			response.getWriter().print(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequiresPermissions("expert##delete")
	@RequestMapping(name = "删除专家", path = "",method=RequestMethod.DELETE)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  delete(@RequestBody String id)  {		
		String[] ids=id.split(",");
		if(ids.length>1){
			expertService.deleteExpert(ids);	
		}else{
			expertService.deleteExpert(id);	
		}		
	}

    @RequestMapping(name = "头像上传", path = "uploadPhoto",method=RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void uploadPhoto(HttpServletRequest request, @RequestParam("file")MultipartFile multipartFile,
                           @RequestParam(required = true)String expertId) throws IOException{
        expertService.savePhone(multipartFile.getBytes(),expertId);
    }

    @RequestMapping(name = "显示头像", path = "transportImg",method=RequestMethod.GET)
    public void transportImg(@RequestParam(required = true)String expertId,HttpServletRequest request,HttpServletResponse response) throws IOException{
        byte[] photos = expertService.findExpertPhoto(expertId);
        if(photos != null){
            OutputStream outputStream = response.getOutputStream();
            InputStream in = new ByteArrayInputStream(photos);
            response.setContentType("img/jpeg");
            response.setCharacterEncoding("utf-8");
            try {
                int len = 0;
                byte[] buf=new byte[1024];
                while((len = in.read(buf,0,1024))!=-1){
                    outputStream.write(buf, 0, len);
                }
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


	
	@RequiresPermissions("expert#findById#post")
	@RequestMapping(name = "初始化详情页面", path = "findById",method=RequestMethod.GET)	
	public @ResponseBody ExpertDto findById(@RequestParam(required = true)String id){
		return expertService.findById(id);
	}
	
	@RequiresPermissions("expert#updateAudit#post")
	@RequestMapping(name = "评审专家", path = "updateAudit",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  updateAudit(@RequestParam String ids,String flag)  {
		expertService.updateAudit(ids,flag);	
	}
	
	@RequiresPermissions("expert##put")
	@RequestMapping(name = "更新专家", path = "",method=RequestMethod.PUT)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  put(@RequestBody ExpertDto expertDto){		
		expertService.updateExpert(expertDto);	
	}
	
	// begin#html
	@RequiresPermissions("expert#html/repeat#get")
	@RequestMapping(name = "专家重复查询列表页面", path = "html/repeat", method = RequestMethod.GET)
	public String list() {
		return ctrlName + "/queryReList";
	}

	@RequiresPermissions("expert#html/edit#get")
	@RequestMapping(name = "编辑专家页面", path = "html/edit", method = RequestMethod.GET)
	public String edit() {

		return ctrlName + "/edit";
	}
	@RequiresPermissions("expert#html/queryAllList#get")
	@RequestMapping(name = "专家综合查询页面", path = "html/queryAllList", method = RequestMethod.GET)
	public String query() {

		return ctrlName + "/queryAllList";
	}
	@RequiresPermissions("expert#html/audit#get")
	@RequestMapping(name = "专家审核页面", path = "html/audit", method = RequestMethod.GET)
	public String audit() {

		return ctrlName + "/audit";
	}
	@RequiresPermissions("expert#html/workExpe#get")
	@RequestMapping(name = "工作经验", path = "html/workExpe", method = RequestMethod.GET)
	public String workExpe() {

		return ctrlName + "/workExpe";
	}
	@RequiresPermissions("expert#html/projectExpe#get")
	@RequestMapping(name = "项目经验", path = "html/projectExpe", method = RequestMethod.GET)
	public String projectExpe() {

		return ctrlName + "/projectExpe";
	}
	@RequiresPermissions("expert#html/reviewList#get")
	@RequestMapping(name = "专家评分一览表", path = "html/reviewList", method = RequestMethod.GET)
	public String reviewList() {

		return ctrlName + "/reviewList";
	}
}
