package cs.controller.asserts.assertStorageBusiness;

import cs.common.ResultMsg;
import cs.domain.asserts.assertStorageBusiness.AssertStorageBusiness;
import cs.model.PageModelDto;
import cs.model.asserts.assertStorageBusiness.AssertStorageBusinessDto;
import cs.model.asserts.goodsDetail.GoodsDetailDto;
import cs.repository.odata.ODataObj;
import cs.service.asserts.assertStorageBusiness.AssertStorageBusinessService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * Description: 固定资产申购流程 控制层
 * author: zsl
 * Date: 2017-9-15 14:45:23
 */
@Controller
@RequestMapping(name = "固定资产申购流程", path = "assertStorageBusiness")
public class AssertStorageBusinessController {

	String ctrlName = "asserts/assertStorageBusiness";
    @Autowired
    private AssertStorageBusinessService assertStorageBusinessService;

    @RequiresPermissions("assertStorageBusiness#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<AssertStorageBusinessDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<AssertStorageBusinessDto> assertStorageBusinessDtos = assertStorageBusinessService.get(odataObj);	
        return assertStorageBusinessDtos;
    }

    @RequiresPermissions("assertStorageBusiness##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody AssertStorageBusinessDto record) {
        assertStorageBusinessService.save(record);
    }

	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody AssertStorageBusinessDto findById(@RequestParam(required = true)String id){		
		return assertStorageBusinessService.findById(id);
	}
	
    @RequiresPermissions("assertStorageBusiness##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	assertStorageBusinessService.delete(id);      
    }

    @RequiresPermissions("assertStorageBusiness##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody AssertStorageBusinessDto record) {
        assertStorageBusinessService.update(record);
    }


    @RequiresPermissions("bookBuyBusiness#saveBooksDetailList#post")
    @RequestMapping(name = "图书详细信息录入", path = "saveBooksDetailList", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg saveBooksDetailList(@RequestBody GoodsDetailDto[] goodsDetailDtoArrary, AssertStorageBusiness assertStorageBusiness){
        return assertStorageBusinessService.saveGoodsDetailList(goodsDetailDtoArrary,assertStorageBusiness);
    }

    @RequiresPermissions("bookBuyBusiness#startFlow#post")
    @RequestMapping(name = "发起流程", path = "startFlow", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg startFlow(@RequestBody GoodsDetailDto[] goodsDetailDtoArrary, AssertStorageBusiness assertStorageBusiness) {
        return assertStorageBusinessService.startFlow(goodsDetailDtoArrary,assertStorageBusiness);
    }

    // begin#html
    @RequiresPermissions("assertStorageBusiness#html/assertStorageBusinessList#get")
    @RequestMapping(name = "列表页面", path = "html/assertStorageBusinessList", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/assertStorageBusinessList";
    }

    @RequiresPermissions("assertStorageBusiness#html/assertStorageBusinessEdit#get")
    @RequestMapping(name = "编辑页面", path = "html/assertStorageBusinessEdit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/assertStorageBusinessEdit";
    }
    // end#html

}