package cs.controller.sys;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.model.project.UnitScoreDto;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.model.sys.CompanyDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.CompanyService;

@Controller
@RequestMapping(name = "单位管理", path = "company")
@MudoleAnnotation(name = "系统管理",value = "permission#system")
public class CompanyController {

    private String ctrlName = "company";
    @Autowired
    private CompanyService companyService;

    //@RequiresPermissions("company#fingByOData#post")
    @RequiresAuthentication
    @RequestMapping(name = "获取单位数据", path = "fingByOData", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<CompanyDto> get(HttpServletRequest request) throws ParseException {
        ODataObj oDataObj = new ODataObj(request);
        PageModelDto<CompanyDto> comDtos = companyService.get(oDataObj);
        return comDtos;
    }

    //@RequiresPermissions("company#html/findByIdCompany#get")
    @RequiresAuthentication
    @RequestMapping(name = "根据ID获取单位数据", path = "html/findByIdCompany", method = RequestMethod.GET)
    public @ResponseBody
    CompanyDto findByIdCompany(@RequestParam(required = true) String id) {
        CompanyDto comDto = companyService.findByIdCompany(id);
        return comDto;
    }

    //@RequiresPermissions("company#findCcompanys#get")
    @RequiresAuthentication
    @RequestMapping(name = "获取单位所有数据", path = "findCcompanys", method = RequestMethod.GET)
    @ResponseBody
    public List<CompanyDto> findCcompanys() {
        List<CompanyDto> comDto = companyService.findCompanys();
        return comDto;
    }

    //begin#html
    @RequiresPermissions("company#html/list#get")
    @RequestMapping(name = "单位管理", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }

    //@RequiresPermissions("company#html/edit#get")
    @RequiresAuthentication
    @RequestMapping(name = "单位编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/edit";
    }

    // end#html
    //@RequiresPermissions("company##post")
    @RequiresAuthentication
    @RequestMapping(name = "创建单位", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody CompanyDto companyDto) {
        companyService.createCompany(companyDto);
    }

    //@RequiresPermissions("company##put")
    @RequiresAuthentication
    @RequestMapping(name = "更新单位", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody CompanyDto companyDto) {
        companyService.updateCompany(companyDto);
    }

    //@RequiresPermissions("company##put")
    @RequiresAuthentication
    @RequestMapping(name = "更新单位评分", path = "saveUnitScore", method = RequestMethod.PUT)
    @ResponseBody
    public ResultMsg saveUnitScore(@RequestBody UnitScoreDto unitScoreDto) {
        return companyService.updateUnitScore(unitScoreDto);
    }

    //@RequiresPermissions("company##delete")
    @RequiresAuthentication
    @RequestMapping(name = "删除单位", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        String[] ids = id.split(",");
        if (ids.length > 1) {
            companyService.deleteCompanys(ids);
        } else {
            companyService.deleteCompany(id);
        }
    }

}
