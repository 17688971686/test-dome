package cs.mobile.controller;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.common.utils.Validate;
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

import static cs.common.constants.Constant.ERROR_MSG;

/**
 * Description: 图书采购申请业务信息 控制层
 * author: zsl
 * Date: 2017-9-8 10:26:20
 */
@Controller
@RequestMapping(name = "图书采购申请业务信息", path = "api/bookBuyApp")
public class BookBuyAppController {

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

	@RequestMapping(name = "主键查询", path = "findById",method=RequestMethod.GET)
	public @ResponseBody BookBuyBusinessDto findById(@RequestParam(required = true)String id){		
		return bookBuyBusinessService.findById(id);
	}

    @RequiresAuthentication
    @RequestMapping(name = "删除记录", path = "bookDel", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg delete(@RequestParam(required = true)String ids) {
        return bookBuyBusinessService.delete(ids);
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
    @ResponseBody
    public ResultMsg saveBooksDetailList(@RequestBody BookBuyDto[] bookBuyDtoArrary,BookBuyBusiness bookBuyBus){
        ResultMsg resultMsg = bookBuyBusinessService.saveBooksDetailList(bookBuyDtoArrary,bookBuyBus);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    //@RequiresPermissions("bookBuyBusiness#startFlow#post")
    @RequestMapping(name = "发起流程", path = "startFlow", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg startFlow(@RequestBody BookBuyDto[] bookBuyDtoArrary,BookBuyBusiness bookBuyBus) {
        ResultMsg resultMsg = bookBuyBusinessService.startFlow(bookBuyDtoArrary,bookBuyBus);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

}