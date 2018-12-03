package cs.controller.sys;

import cs.ahelper.MudoleAnnotation;
import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.utils.Validate;
import cs.model.PageModelDto;
import cs.model.sys.HeaderDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.HeaderService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static cs.common.constants.Constant.ERROR_MSG;

/**
 * Description:
 * Author: mcl
 * Date: 2017/9/12 15:03
 */
@Controller
@RequestMapping(name="表头管理" , path="header")
@MudoleAnnotation(name = "其它",value = "permission#other")
public class HeaderController {

    private String ctrlName = "header";

    @Autowired
    private HeaderService headerService;


    //@RequiresPermissions("header#findAllHeader#post")
    @RequiresAuthentication
    @RequestMapping(name="查询所有表头列表" , path="findAllHeader" , method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<HeaderDto> get(HttpServletRequest request) throws ParseException {
        ODataObj oDataObj = new ODataObj(request);
        PageModelDto<HeaderDto> resultPage = headerService.get(oDataObj);
        if(!Validate.isObject(resultPage)){
            resultPage = new PageModelDto();
        }
        return resultPage;
    }

    //@RequiresPermissions("header#createHeader#post")
    @RequiresAuthentication
    @RequestMapping(name="创建表头" , path="createHeader" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg createHeader(@RequestBody HeaderDto headerDto){
        ResultMsg resultMsg = headerService.createHeader(headerDto);
        if (!Validate.isObject(resultMsg)) {
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }


    //@RequiresPermissions("header#findHeaderListNoSelected#post")
    @RequiresAuthentication
    @RequestMapping(name="获取未选中表头列表" , path="findHeaderListNoSelected" , method = RequestMethod.POST)
    @ResponseBody
    public List<HeaderDto> findHeaderListNoSelected(@RequestBody  HeaderDto headerDto){
        List<HeaderDto> headerDtoList = headerService.findHeaderList(headerDto.getHeaderType(), Constant.EnumState.NO.getValue());
        if(!Validate.isList(headerDtoList)){
            headerDtoList = new ArrayList<>();
        }
        return headerDtoList;
    }

    //@RequiresPermissions("header#findHeaderListSelected#post")
    @RequiresAuthentication
    @RequestMapping(name="获取选中的表头列表" ,path="findHeaderListSelected" , method = RequestMethod.POST)
    @ResponseBody
    public List<HeaderDto> findHeaderListSelected(@RequestBody  HeaderDto headerDto){
        List<HeaderDto> headerDtoList = headerService.findHeaderList(headerDto.getHeaderType(), Constant.EnumState.YES.getValue());
        if(!Validate.isList(headerDtoList)){
            headerDtoList = new ArrayList<>();
        }
        return headerDtoList;
    }

    //@RequiresPermissions("header#updateSelectedHeader#put")
    @RequiresAuthentication
    @RequestMapping(name="改变表头状态（选中）" , path="updateSelectedHeader"  ,method=RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateSelectedHeader(@RequestParam  String idStr){
        headerService.updateSelectedHeader(idStr);
    }

    //设置头顺序
    @RequiresAuthentication
    @RequestMapping(name = "设置表头顺序", path = "headOrder", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void setSelectHeadOrder(@RequestBody HeaderDto[] headerDtos) {
        headerService.setSelectHeadOrder(headerDtos);
    }


    //@RequiresPermissions("header#updateCancelHeader#put")
    @RequiresAuthentication
    @RequestMapping(name="改变表头状态（取消选中）" , path="updateCancelHeader"  ,method=RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateCancelHeader(@RequestParam  String idStr){
        headerService.updateCancelHeader(idStr);
    }


    //@RequiresPermissions("header##delete")
    @RequiresAuthentication
    @RequestMapping(name="删除表头" , path="" , method = RequestMethod.DELETE)
    @ResponseStatus(value=HttpStatus.NO_CONTENT)
    public void deleteHeader(@RequestParam String id){
        headerService.deleteHeader(id);
    }


    //@RequiresPermissions("header#getHeaderById#get")
    @RequiresAuthentication
    @RequestMapping(name="通过id查询表头信息" , path="getHeaderById" , method=RequestMethod.GET)
    @ResponseBody
    public HeaderDto getHeaderById(String id){
        HeaderDto headerDto = headerService.getHeaderById(id);
        if(!Validate.isObject(headerDto)){
            headerDto = new HeaderDto();
        }
        return headerDto;
    }

    //@RequiresPermissions("header#updateHeader#put")
    @RequiresAuthentication
    @RequestMapping(name="更新表头信息" , path="updateHeader" , method= RequestMethod.PUT)
    @ResponseBody
    public ResultMsg updateHeader(@RequestBody  HeaderDto headerDto){
        ResultMsg resultMsg = headerService.updateHeader(headerDto);
        if (!Validate.isObject(resultMsg)) {
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }


    @RequiresPermissions("header#html/list#get")
    @RequestMapping(name="表字段管理" , path="html/list" , method = RequestMethod.GET)
    public  String list(){
        return ctrlName + "/list";
    }

    //@RequiresPermissions("header#html/add#get")
    @RequiresAuthentication
    @RequestMapping(name="添加表头" , path="html/add" , method = RequestMethod.GET)
    public  String add(){
        return ctrlName + "/add";
    }

    //@RequiresPermissions("header#html/selectHeader#get")
    @RequiresAuthentication
    @RequestMapping(name="表字段设置" , path="html/selectHeader" , method = RequestMethod.GET)
    public String selectHeader(){
        return ctrlName + "/selectHeader";
    }



}