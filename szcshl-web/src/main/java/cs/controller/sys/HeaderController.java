package cs.controller.sys;

import cs.common.ResultMsg;
import cs.domain.sys.Header;
import cs.model.PageModelDto;
import cs.model.sys.HeaderDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.HeaderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.List;

/**
 * Description:
 * Author: mcl
 * Date: 2017/9/12 15:03
 */
@Controller
@RequestMapping(name="表头管理" , path="header")
public class HeaderController {

    private String ctrlName = "header";

    @Autowired
    private HeaderService headerService;


    @RequiresPermissions("header#findAllHeader#post")
    @RequestMapping(name="查询所有表头列表" , path="findAllHeader" , method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<HeaderDto> get(HttpServletRequest request) throws ParseException {
        ODataObj oDataObj = new ODataObj(request);
        return headerService.get(oDataObj);
    }

    @RequiresPermissions("header#createHeader#post")
    @RequestMapping(name="创建表头" , path="createHeader" , method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void createHeader(@RequestBody HeaderDto headerDto){
         headerService.createHeader(headerDto);
    }


    @RequiresPermissions("header#getHeaderList#get")
    @RequestMapping(name="获取表头列表通过类型" , path="getHeaderList" , method = RequestMethod.GET)
    @ResponseBody
    public List<HeaderDto> getHeaderList(@RequestParam  String headerType){
        return headerService.getHeaderList(headerType);
    }

    @RequiresPermissions("header#findHeaderListByState#post")
    @RequestMapping(name="获取选中的表头" ,path="findHeaderListByState" , method = RequestMethod.POST)
    @ResponseBody
    public List<HeaderDto> findHeaderListByState(){
        return headerService.findHeaderListByState();
    }

    @RequiresPermissions("header#updateSelectedHeader#put")
    @RequestMapping(name="改变表头状态（选中）" , path="updateSelectedHeader"  ,method=RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateSelectedHeader(@RequestParam  String idStr){
        headerService.updateSelectedHeader(idStr);
    }

    @RequiresPermissions("header#updateCancelHeader#put")
    @RequestMapping(name="改变表头状态（取消选中）" , path="updateCancelHeader"  ,method=RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateCancelHeader(@RequestParam  String idStr){
        headerService.updateCancelHeader(idStr);
    }

    @RequiresPermissions("header#html/list#get")
    @RequestMapping(name="列表" , path="html/list" , method = RequestMethod.GET)
    public  String list(){
        return ctrlName + "/list";
    }

    @RequiresPermissions("header#html/add#get")
    @RequestMapping(name="添加表头" , path="html/add" , method = RequestMethod.GET)
    public  String add(){
        return ctrlName + "/add";
    }

    @RequiresPermissions("header#html/selectHeader#get")
    @RequestMapping(name="表头设置" , path="html/selectHeader" , method = RequestMethod.GET)
    public String selectHeader(){
        return ctrlName + "/selectHeader";
    }


    @RequiresPermissions("header#html/statisticalList#get")
    @RequestMapping(name="统计列表页" , path="html/statisticalList" , method = RequestMethod.GET)
    public String statisticalList(){
        return ctrlName + "/statisticalList";
    }


}