package cs.controller.asserts.userAssertDetail;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.asserts.userAssertDetail.UserAssertDetailDto;
import cs.repository.odata.ODataObj;
import cs.service.asserts.goodsDetail.GoodsDetailService;
import cs.service.asserts.userAssertDetail.UserAssertDetailService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * Description: 用户资产明细 控制层
 * author: zsl
 * Date: 2017-9-20 15:35:02
 */
@Controller
@RequestMapping(name = "用户资产明细", path = "userAssertDetail")
@MudoleAnnotation(name = "资产管理",value = "permission#assert")
public class UserAssertDetailController {

	String ctrlName = "asserts/userAssertDetail";
    @Autowired
    private UserAssertDetailService userAssertDetailService;
    @Autowired
    private GoodsDetailService goodsDetailService;

    @RequiresAuthentication
    //@RequiresPermissions("userAssertDetail#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<UserAssertDetailDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<UserAssertDetailDto> userAssertDetailDtos = userAssertDetailService.get(odataObj);	
        return userAssertDetailDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("userAssertDetail##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody UserAssertDetailDto record) {
        userAssertDetailService.save(record);
    }

    @RequiresAuthentication
	//@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody UserAssertDetailDto findById(@RequestParam(required = true)String id){		
		return userAssertDetailService.findById(id);
	}

    @RequiresAuthentication
    //@RequiresPermissions("userAssertDetail##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	userAssertDetailService.delete(id);      
    }

    @RequiresAuthentication
    //@RequiresPermissions("userAssertDetail##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody UserAssertDetailDto record) {
        userAssertDetailService.update(record);
    }

    @RequiresAuthentication
   // @RequiresPermissions("userAssertDetail#html/initFillPageData#post")
    @RequestMapping(name = "初始化资产使用申请页面", path = "html/initFillPageData", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg initFillPageData() {
            return  goodsDetailService.getStoreAssertData();
    }

    // begin#html
    @RequiresAuthentication
//    @RequiresPermissions("userAssertDetail#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list"; 
    }

    @RequiresPermissions("userAssertDetail#html/userAssertDetailAdd#get")
    @RequestMapping(name = "固定资产申请使用流程", path = "html/userAssertDetailAdd", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/userAssertDetailAdd";
    }
    // end#html

}