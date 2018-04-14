package cs.controller.book;

import cs.ahelper.IgnoreAnnotation;
import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.book.BookBorrowInfoDto;
import cs.model.book.BookBuyDto;
import cs.repository.odata.ODataObj;
import cs.service.book.BookBuyService;
import cs.service.book.BorrowBookService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * Description: 图书信息 控制层
 * author: zsl
 * Date: 2017-9-8 10:24:51
 */
@Controller
@RequestMapping(name = "图书信息", path = "bookBuy")
//@MudoleAnnotation(name = "图书管理",value = "permission#bookBuy")
@IgnoreAnnotation
public class BookBuyController {

	String ctrlName = "bookBuy";
    @Autowired
    private BookBuyService bookBuyService;
    @Autowired
    private BorrowBookService borrowBookService;

    @RequiresAuthentication
    //@RequiresPermissions("bookBuy#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<BookBuyDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<BookBuyDto> bookBuyDtos = bookBuyService.get(odataObj);	
        return bookBuyDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("bookBuy##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody BookBuyDto record) {
        bookBuyService.save(record);
    }

    @RequiresAuthentication
    @RequestMapping(name = "保存借书信息", path = "saveBorrowDetail", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveBorrowDetail(@RequestBody BookBorrowInfoDto bookBorrowInfoDto) {
        return  borrowBookService.saveBooksDetail(bookBorrowInfoDto);
    }

    @RequiresAuthentication
    @RequestMapping(name = "保存还书信息", path = "saveReturnDetail", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveReturnDetail(@RequestBody BookBorrowInfoDto bookBorrowInfoDto) {
        return borrowBookService.saveReturnDetail(bookBorrowInfoDto);
    }

    @RequiresAuthentication
	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody BookBuyDto findById(@RequestParam(required = true)String id){		
		return bookBuyService.findById(id);
	}

    @RequiresAuthentication
    //@RequiresPermissions("bookBuy##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	bookBuyService.delete(id);      
    }

    @RequiresAuthentication
  //  @RequiresPermissions("bookBuy##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody BookBuyDto record) {
        bookBuyService.update(record);
    }

    // begin#html
    @RequiresAuthentication
    @RequiresPermissions("bookBuy#html/bookBuyList#get")
    @RequestMapping(name = "列表页面", path = "html/bookBuyList", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/bookBuyList";
    }

    @RequiresAuthentication
    //@RequiresPermissions("bookBuy#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/edit";
    }
    // end#html

}