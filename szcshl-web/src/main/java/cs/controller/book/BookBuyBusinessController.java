package cs.controller.book;

import cs.common.ResultMsg;
import cs.domain.book.BookBuyBusiness;
import cs.model.PageModelDto;
import cs.model.book.BookBuyBusinessDto;
import cs.model.book.BookBuyDto;
import cs.repository.odata.ODataObj;
import cs.service.book.BookBuyBusinessService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * Description: 图书采购申请业务信息 控制层
 * author: zsl
 * Date: 2017-9-8 10:26:20
 */
@Controller
@RequestMapping(name = "图书采购申请业务信息", path = "bookBuyBusiness")
public class BookBuyBusinessController {

	String ctrlName = "bookBuyBusiness";
    @Autowired
    private BookBuyBusinessService bookBuyBusinessService;

    @RequiresPermissions("bookBuyBusiness#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<BookBuyBusinessDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<BookBuyBusinessDto> bookBuyBusinessDtos = bookBuyBusinessService.get(odataObj);	
        return bookBuyBusinessDtos;
    }

    @RequiresPermissions("bookBuyBusiness##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody BookBuyBusinessDto record) {
        bookBuyBusinessService.save(record);
    }

	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody BookBuyBusinessDto findById(@RequestParam(required = true)String id){		
		return bookBuyBusinessService.findById(id);
	}
	
    @RequiresPermissions("bookBuyBusiness##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	bookBuyBusinessService.delete(id);      
    }

    @RequiresPermissions("bookBuyBusiness##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody BookBuyBusinessDto record) {
        bookBuyBusinessService.update(record);
    }

    @RequiresPermissions("bookBuyBusiness#saveBooksDetailList#post")
    @RequestMapping(name = "图书详细信息录入", path = "saveBooksDetailList", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg saveBooksDetailList(@RequestBody BookBuyDto[] BookBuyDtoArrary,BookBuyBusiness bookBuyBus){
       return bookBuyBusinessService.saveBooksDetailList(BookBuyDtoArrary,bookBuyBus);
    }

    @RequiresPermissions("bookBuyBusiness#startFlow#post")
    @RequestMapping(name = "发起流程", path = "startFlow", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg startFlow(@RequestBody BookBuyDto[] BookBuyDtoArrary,BookBuyBusiness bookBuyBus) {
        return bookBuyBusinessService.startFlow(BookBuyDtoArrary,bookBuyBus);
    }

    // begin#html
    @RequiresPermissions("bookBuyBusiness#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list"; 
    }

    @RequiresPermissions("bookBuyBusiness#html/bookBuyBusinessEdit#get")
    @RequestMapping(name = "编辑页面", path = "html/bookBuyBusinessEdit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/bookBuyBusinessEdit";
    }
    // end#html

}