package cs.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cs.model.CompanyDto;
import cs.model.MeetingRoomDto;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;
import cs.service.CompanyService;

@Controller
@RequestMapping(name ="单位管理", path="company")
public class CompanyController {

	private String ctrlName = "company";
	@Autowired
	private CompanyService companyService;
	
	@RequiresPermissions("company##get")
	@RequestMapping(name="获取单位数据",path = "",method =RequestMethod.GET)
	public @ResponseBody PageModelDto<CompanyDto> get(HttpServletRequest request) throws ParseException {
		ODataObj oDataObj = new ODataObj(request);
		PageModelDto<CompanyDto> comDtos=companyService.get(oDataObj);
		return comDtos;
	}
	//begin#html
	@RequiresPermissions("company#html/list#get")
	@RequestMapping(name = "单位列表", path = "html/list" ,method = RequestMethod.GET)
	public String list(){
			
		return ctrlName +"/list";
	}
	@RequiresPermissions("company#html/edit#get")
	@RequestMapping(name = "单位编辑页面", path = "html/edit" ,method = RequestMethod.GET)
	public String edit(){
			
		return ctrlName + "/edit";
	}
	// end#html
}
