package cs.controller.demo;

import cs.model.PageModelDto;
import cs.model.demo.MyTestDto;
import cs.repository.odata.ODataObj;
import cs.service.demo.MyTestService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;

/**
 * Description: my test controller
 * User: Administrator
 * Date: 2017/5/4 17:33
 */
@Controller
@RequestMapping(name = "My test", path = "mytest")
public class MyTestController {

    @Autowired
    private MyTestService myTestService;

    @RequiresPermissions("mytest##get")
    @RequestMapping(name = "获取My test数据", path = "", method = RequestMethod.GET)
    @ResponseBody
    public PageModelDto<MyTestDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        return myTestService.getDto(odataObj);
    }

    @RequiresPermissions("mytest##post")
    @RequestMapping(name = "创建My test", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody MyTestDto record) {
        myTestService.create(record);
    }

    @RequiresPermissions("mytest##delete")
    @RequestMapping(name = "删除My test", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        String[] ids = id.split(",");
        if (ids.length > 1) {
            myTestService.delete(ids);
        } else {
            myTestService.delete(id);
        }
    }

    @RequiresPermissions("mytest##put")
    @RequestMapping(name = "更新My test", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody MyTestDto record) {
        myTestService.update(record);
    }

    // begin#html
    @RequiresPermissions("mytest#html/list#get")
    @RequestMapping(name = "用户列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return "mytest/list";
    }

    @RequiresPermissions("mytest#html/edit#get")
    @RequestMapping(name = "编辑用户页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return "mytest/edit";
    }
    // end#html

}