package cs.controller.expert;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.expert.ExpertSelectedDto;
import cs.repository.odata.ODataObj;
import cs.service.expert.ExpertSelectedService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * Description: 抽取专家 控制层
 * author: ldm
 * Date: 2017-6-14 14:26:49
 */
@Controller
@RequestMapping(name = "抽取专家", path = "expertSelected")
public class ExpertSelectedController {
    @Autowired
    private ExpertSelectedService expertSelectedService;

    @RequiresPermissions("expertSelected##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody ExpertSelectedDto record) {
        expertSelectedService.save(record);
    }

    @RequestMapping(name = "主键查询", path = "html/findById", method = RequestMethod.GET)
    public @ResponseBody
    ExpertSelectedDto findById(@RequestParam(required = true) String id) {
        return expertSelectedService.findById(id);
    }

    @RequiresPermissions("expertSelected##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg delete(@RequestParam(required = true)String reviewId, @RequestParam(required = true) String id, boolean deleteAll) {
        return expertSelectedService.delete(reviewId,id,deleteAll);
    }

    @RequiresPermissions("expertSelected##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody ExpertSelectedDto record) {
        expertSelectedService.update(record);
    }

    @RequiresPermissions("expertSelected#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<ExpertSelectedDto> findByOData(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<ExpertSelectedDto> expertSelectedDtos = expertSelectedService.get(odataObj);
        return expertSelectedDtos;
    }

    @RequiresPermissions("expertSelected#expertCostTotal#post")
    @RequestMapping(name = "获取数据", path = "expertCostTotal", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg expertCostTotal(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        return  expertSelectedService.expertCostTotal(odataObj);
    }

}