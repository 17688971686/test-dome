package cs.controller.sys;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import cs.model.sys.AnnountmentDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.AnnountmentService;
import cs.ueditor.ActionEnter;

@Controller
@RequestMapping(name = "通知公告", path = "annountment")
public class AnnountmentController {

    private String ctrlName = "annountment";

    @Autowired
    private AnnountmentService annService;

    @RequiresPermissions("annountment#fingByOData#post")
    @RequestMapping(name = "获取所有数据", path = "fingByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<AnnountmentDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<AnnountmentDto> pageModelDto = annService.get(odataObj);
        return pageModelDto;
    }

    @RequiresPermissions("annountment##post")
    @RequestMapping(name = "新增通知公告", path = "", method = RequestMethod.POST)
    @ResponseBody
    public AnnountmentDto create(@RequestBody AnnountmentDto annountmentDto) {
        annService.createAnnountment(annountmentDto);
        return annountmentDto;
    }

    @RequiresPermissions("annountment#initAnOrg#get")
    @RequestMapping(name = "初始化发布单位", path = "initAnOrg", method = RequestMethod.GET)
    @ResponseBody
    public String inintAnOrg() {
        return annService.findAnOrg();
    }

    @RequiresPermissions("annountment#findAnnountmentById#get")
    @RequestMapping(name = "通过ID获取通知公告", path = "findAnnountmentById", method = RequestMethod.GET)
    @ResponseBody
    public AnnountmentDto findAnnoungmentById(@RequestParam String anId) {
        return annService.findAnnountmentById(anId);
    }

    @RequiresPermissions("annountment##put")
    @RequestMapping(name = "更新通知公告", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody AnnountmentDto annountmentDto) {
        annService.updateAnnountment(annountmentDto);

    }
    @RequiresPermissions("annountment#updateIssueState#post")
    @RequestMapping(name = "更改发布状态", path = "updateIssueState", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateIssueState(@RequestParam String ids,@RequestParam String issueState) {
        annService.updateIssueState(ids,issueState);
    }

    @RequiresPermissions("annountment#getHomePageAnnountment#get")
    @RequestMapping(name = "获取主页上的通知公告", path = "getHomePageAnnountment", method = RequestMethod.GET)
    @ResponseBody
    public List<AnnountmentDto> getHomePageAnnountment() {
        return annService.getHomePageAnnountment();
    }

    @RequiresPermissions("annountment#postArticle#get")
    @RequestMapping(name = "获取上一篇", path = "postArticle", method = RequestMethod.GET)
    @ResponseBody
    public AnnountmentDto postpostArticle(@RequestParam String anId) {
        return annService.postAritle(anId);
    }

    @RequiresPermissions("annountment#nextArticle#get")
    @RequestMapping(name = "获取下一篇", path = "nextArticle", method = RequestMethod.GET)
    @ResponseBody
    public AnnountmentDto nextArticle(@RequestParam String anId) {
        return annService.nextArticle(anId);
    }

    @RequiresPermissions("annountment##delete")
    @RequestMapping(name = "更新通知公告", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String anId) {
        annService.deleteAnnountment(anId);
    }
    
    //begin  html
    @RequiresPermissions("annountment#html/list#get")
    @RequestMapping(name = "通知公告管理", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }

    @RequiresPermissions("annountment#html/edit#get")
    @RequestMapping(name = "通知公告编辑", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/edit";
    }

    @RequiresPermissions("admin#html/detail#get")
    @RequestMapping(name = "通知公告详情页", path = "html/detail")
    public String annountment() {
        return ctrlName + "/detail";
    }
    
    @RequiresPermissions("annountment#html/annList#get")
    @RequestMapping(name = "通知公告列表", path = "html/annList", method = RequestMethod.GET)
    public String annList() {
        return ctrlName + "/annList";
    }
}
