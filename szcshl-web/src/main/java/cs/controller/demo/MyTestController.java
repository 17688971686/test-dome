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
 * Description: My test 控制层
 * author: tzg
 * Date: 2017-5-8 15:20:53
 */
@Controller
@RequestMapping(name = "My test", path = "myTest")
public class MyTestController {

    @Autowired
    private MyTestService myTestService;

    @RequiresPermissions("myTest##get")
    @RequestMapping(name = "获取数据", path = "", method = RequestMethod.GET)
    @ResponseBody
    public PageModelDto<MyTestDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        return myTestService.getDto(odataObj);
    }

    @RequiresPermissions("myTest##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody MyTestDto record) {
        myTestService.create(record);
    }

    @RequiresPermissions("myTest##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        String[] ids = id.split(",");
        if (ids.length > 1) {
            myTestService.delete(ids);
        } else {
            myTestService.delete(id);
        }
    }

    @RequiresPermissions("myTest##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody MyTestDto record) {
        myTestService.update(record);
    }

    // begin#html
    @RequiresPermissions("myTest#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return "myTest/myTestList";
    }

    @RequiresPermissions("myTest#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return "myTest/myTestEdit";
    }
    // end#html

}