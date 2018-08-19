package com.sn.framework.module.sys.controller;

import com.sn.framework.module.sys.domain.OrgDept;
import com.sn.framework.module.sys.repo.IOrgDeptRepo;
import com.sn.framework.module.sys.repo.impl.OrgDeptRepoImpl;
import com.sn.framework.module.sys.service.IOrgDeptService;
import com.sn.framework.odata.impl.jpa.OdataJPA;
import com.sn.framework.core.web.PageModelDto;
import com.sn.framework.module.sys.domain.Resource;
import com.sn.framework.module.sys.model.OrganDto;
import com.sn.framework.module.sys.service.IOrganService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.sn.framework.common.StringUtil.SEPARATE_COMMA;

/**
 *
 * @author qbl
 * @date 2017/9/7
 */
@Controller
@RequestMapping(name = "机构管理", path = "sys/organ")
public class OrganController {

    @Autowired
    private IOrganService organService;

    @Autowired
    private IOrgDeptService orgDeptService;

    @RequestMapping(name = "获取部门和组别信息", path = "findAllOrgDept", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public List<OrgDept> findAllOrgDept() {
        return orgDeptService.findAll();
    }

    @RequestMapping(name = "获取机构数据", path = "", method = RequestMethod.GET)
    @ResponseBody
    public PageModelDto<OrganDto> get(OdataJPA odata) {
//        if (!SecurityUtils.getSubject().hasRole(ROLE_KEY_ADMIN)) {
//            odata.addFilter(Organ_.organRel.getName(), "NOT_LIKE", "|" + ORGAN_TYPE_GXBSYZDW + "|");
//        }
        return organService.findPageByOdata(odata);
    }

    @RequiresPermissions("sys:organ:post")
    @RequestMapping(name = "创建机构", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody OrganDto organDto) {
        organService.create(organDto);
    }

    @RequiresPermissions("sys:organ:delete")
    @RequestMapping(name = "删除机构", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam String organId) {
        if (organId.contains(SEPARATE_COMMA)) {
            organService.deleteByIds(organId.split(SEPARATE_COMMA));
        } else {
            organService.deleteById(organId);
        }
    }

    @RequestMapping(name = "根据id获取机构信息", path = "{organId}", method = RequestMethod.GET)
    @ResponseBody
    public OrganDto findByOrganId(@PathVariable String organId) {
        return organService.getById(organId);
    }

    @RequestMapping(name = "获取业主机构列表", path = "findOrganList", method = RequestMethod.GET)
    @ResponseBody
    public List<OrganDto> findOrganList() {
        return organService.findOrganList();
    }

    @RequiresPermissions("sys:organ:put")
    @RequestMapping(name = "更新机构信息", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody OrganDto organDto) {
        organService.update(organDto);
    }

    @RequiresPermissions("sys:organ:authorization")
//    @RequiresRoles(ROLE_KEY_ADMIN)
    @RequestMapping(name = "为机构授权", path = "authorization", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void authorization(String organId, @RequestBody Set<Resource> resources) {
        organService.authorization(organId, resources);
    }

    //    @RequiresPermissions(ModuleDefine.SYS + "#organ#html/list#get")
    @RequestMapping(name = "机构管理列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return "sys/organ/organList";
    }

    @RequestMapping(name = "机构新增页面", path = "html/add", method = RequestMethod.GET)
    public String add() {
        return "sys/organ/organAdd";
    }

    //    @RequiresPermissions(ModuleDefine.SYS + "#organ#html/edit#get")
    @RequestMapping(name = "机构编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return "sys/organ/organEdit";
    }

    //    @RequiresPermissions(ModuleDefine.SYS + "#organ#html/user#get")
    @RequestMapping(name = "机构编辑页面", path = "html/user", method = RequestMethod.GET)
    public String user() {
        return "sys/organ/organUser";
    }


}
