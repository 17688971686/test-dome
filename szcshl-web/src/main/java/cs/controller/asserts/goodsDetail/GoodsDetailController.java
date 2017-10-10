package cs.controller.asserts.goodsDetail;

import cs.ahelper.IgnoreAnnotation;
import cs.model.PageModelDto;
import cs.model.asserts.goodsDetail.GoodsDetailDto;
import cs.repository.odata.ODataObj;
import cs.service.asserts.goodsDetail.GoodsDetailService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * Description: 物品明细 控制层
 * author: zsl
 * Date: 2017-9-15 14:15:55
 */
@Controller
@RequestMapping(name = "物品明细", path = "goodsDetail")
@IgnoreAnnotation
public class GoodsDetailController {

	String ctrlName = "goodsDetail";
    @Autowired
    private GoodsDetailService goodsDetailService;

    @RequiresAuthentication
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<GoodsDetailDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<GoodsDetailDto> goodsDetailDtos = goodsDetailService.get(odataObj);	
        return goodsDetailDtos;
    }

    @RequiresAuthentication
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody GoodsDetailDto record) {
        goodsDetailService.save(record);
    }

	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody GoodsDetailDto findById(@RequestParam(required = true)String id){		
		return goodsDetailService.findById(id);
	}
	
    @RequiresAuthentication
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	goodsDetailService.delete(id);      
    }

    @RequiresAuthentication
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody GoodsDetailDto record) {
        goodsDetailService.update(record);
    }

    // begin#html
    @RequiresAuthentication
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list"; 
    }

    @RequiresAuthentication
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/edit";
    }
    // end#html

}