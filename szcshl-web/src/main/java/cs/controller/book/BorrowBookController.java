package cs.controller.book;

import cs.ahelper.IgnoreAnnotation;
import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.book.BookBorrowInfoDto;
import cs.repository.odata.ODataObj;
import cs.service.book.BorrowBookService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * Description: 图书信息 控制层
 * author: zsl
 * Date: 2017-9-8 10:24:51
 */
@Controller
@RequestMapping(name = "图书信息", path = "bookBorrow")
//@MudoleAnnotation(name = "图书管理",value = "permission#borrowBuy")
@IgnoreAnnotation
public class BorrowBookController {

	String ctrlName = "bookBuy";
    @Autowired
    private BorrowBookService borrowBookService;

    @RequiresAuthentication
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<BookBorrowInfoDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<BookBorrowInfoDto> bookBorrowInfoDto = borrowBookService.get(odataObj);
        return bookBorrowInfoDto;
    }


    @RequiresAuthentication
    //@RequiresPermissions("expertSelected#expertCostDetailTotal#post")
    @RequestMapping(name = "借书列表", path = "expertCostDetailTotal", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getBookBorrowList(@RequestBody BookBorrowInfoDto bookBorrowInfoDto){
        return  borrowBookService.getBookBorrowList(bookBorrowInfoDto);
    }

}