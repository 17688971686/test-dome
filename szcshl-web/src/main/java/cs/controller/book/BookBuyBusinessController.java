package cs.controller.book;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.domain.book.BookBuyBusiness;
import cs.model.PageModelDto;
import cs.model.book.BookBuyBusinessDto;
import cs.model.book.BookBuyDto;
import cs.repository.odata.ODataObj;
import cs.service.book.BookBuyBusinessService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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
@MudoleAnnotation(name = "图书业务管理",value = "permission#bookBuyBusiness")
public class BookBuyBusinessController {

	String ctrlName = "bookBuyBusiness";
    @Autowired
    private BookBuyBusinessService bookBuyBusinessService;

    @RequiresAuthentication
    //@RequiresPermissions("bookBuyBusiness#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<BookBuyBusinessDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<BookBuyBusinessDto> bookBuyBusinessDtos = bookBuyBusinessService.get(odataObj);	
        return bookBuyBusinessDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("bookBuyBusiness##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody BookBuyBusinessDto record) {
        bookBuyBusinessService.save(record);
    }

    @RequiresAuthentication
	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody BookBuyBusinessDto findById(@RequestParam(required = true)String id){		
		return bookBuyBusinessService.findById(id);
	}

    @RequiresAuthentication
   // @RequiresPermissions("bookBuyBusiness##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	bookBuyBusinessService.delete(id);      
    }

    @RequiresAuthentication
   // @RequiresPermissions("bookBuyBusiness##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody BookBuyBusinessDto record) {
        bookBuyBusinessService.update(record);
    }

    @RequiresAuthentication
    //@RequiresPermissions("bookBuyBusiness#saveBooksDetailList#post")
    @RequestMapping(name = "图书详细信息录入", path = "saveBooksDetailList", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg saveBooksDetailList(@RequestBody BookBuyDto[] bookBuyDtoArrary,BookBuyBusiness bookBuyBus){
       return bookBuyBusinessService.saveBooksDetailList(bookBuyDtoArrary,bookBuyBus);
    }

    @RequiresAuthentication
    //@RequiresPermissions("bookBuyBusiness#startFlow#post")
    @RequestMapping(name = "发起流程", path = "startFlow", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg startFlow(@RequestBody BookBuyDto[] bookBuyDtoArrary,BookBuyBusiness bookBuyBus) {
        return bookBuyBusinessService.startFlow(bookBuyDtoArrary,bookBuyBus);
    }

    // begin#html
    @RequiresPermissions("bookBuyBusiness#html/bookBuyBusinessList#get")
    @RequestMapping(name = "图书采购流程查询", path = "html/bookBuyBusinessList", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/bookBuyBusinessList";
    }

    @RequiresPermissions("bookBuyBusiness#html/bookBuyBusinessEdit#get")
    @RequestMapping(name = "图书信息录入", path = "html/bookBuyBusinessEdit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/bookBuyBusinessEdit";
    }

    @RequiresAuthentication
    @RequiresPermissions("bookBuyBusiness#html/bookBuyList#get")
    @RequestMapping(name = "列表页面", path = "html/bookBuyList", method = RequestMethod.GET)
    public String bookList() {
        return "bookBuy/bookBuyList";
    }
    // end#html

}