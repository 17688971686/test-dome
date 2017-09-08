package cs.controller.book;

import cs.model.PageModelDto;
import cs.model.book.BookBuyDto;
import cs.repository.odata.ODataObj;
import cs.service.book.BookBuyService;
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
public class BookBuyController {

	String ctrlName = "bookBuy";
    @Autowired
    private BookBuyService bookBuyService;

    @RequiresPermissions("bookBuy#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<BookBuyDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<BookBuyDto> bookBuyDtos = bookBuyService.get(odataObj);	
        return bookBuyDtos;
    }

    @RequiresPermissions("bookBuy##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody BookBuyDto record) {
        bookBuyService.save(record);
    }

	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody BookBuyDto findById(@RequestParam(required = true)String id){		
		return bookBuyService.findById(id);
	}
	
    @RequiresPermissions("bookBuy##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	bookBuyService.delete(id);      
    }

    @RequiresPermissions("bookBuy##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody BookBuyDto record) {
        bookBuyService.update(record);
    }

    // begin#html
    @RequiresPermissions("bookBuy#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list"; 
    }

    @RequiresPermissions("bookBuy#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/edit";
    }
    // end#html

}