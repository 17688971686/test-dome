package cs.controller.expert;

import cs.model.PageModelDto;
import cs.model.expert.ExpertOfferDto;
import cs.repository.odata.ODataObj;
import cs.service.expert.ExpertOfferService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;

/**
 * Description: 专家聘书 控制层
 * author: ldm
 * Date: 2017-6-19 19:13:35
 */
@Controller
@RequestMapping(name = "专家聘书", path = "expertOffer")
public class ExpertOfferController {

	String ctrlName = "expertOffer";
    @Autowired
    private ExpertOfferService expertOfferService;

    @RequiresPermissions("expertOffer#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<ExpertOfferDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<ExpertOfferDto> expertOfferDtos = expertOfferService.get(odataObj);	
        return expertOfferDtos;
    }

    @RequiresPermissions("expertOffer##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody ExpertOfferDto record) {
        expertOfferService.save(record);
    }

	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody ExpertOfferDto findById(@RequestParam(required = true)String id){		
		return expertOfferService.findById(id);
	}
	
    @RequiresPermissions("expertOffer##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	expertOfferService.delete(id);      
    }

    @RequiresPermissions("expertOffer##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody ExpertOfferDto record) {
        expertOfferService.update(record);
    }

    // begin#html
    @RequiresPermissions("expertOffer#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list"; 
    }

    @RequiresPermissions("expertOffer#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/edit";
    }
    // end#html

}