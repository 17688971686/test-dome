package cs.controller.sys;

import com.alibaba.fastjson.JSON;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.service.sys.DictService;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cs.model.sys.UserDto;
import cs.service.sys.UserService;

@Controller
@RequestMapping(name = "管理界面", path = "admin")
public class AdminController {
    private String ctrlName = "admin";
    private static Logger logger = Logger.getLogger(AdminController.class.getName());
    @Autowired
    private UserService userService;
    @Autowired
    private DictService dictService;


    @RequiresPermissions("admin#index#get")
    @RequestMapping(name = "首页", path = "index")
    public String index(Model model) {
        model.addAttribute("user", SessionUtil.getLoginName());
        model.addAttribute("DICT_ITEMS", JSON.toJSONString(dictService.getDictItemByCode(null)));
        return ctrlName + "/index";
    }

    @RequiresPermissions("admin#welcome#get")
    @RequestMapping(name = "欢迎页", path = "welcome")
    public String welcome(Model model) {
        UserDto user = userService.findUserByName(SessionUtil.getLoginName());
        if (user != null) {
            model.addAttribute("user", user.getLoginName());
            model.addAttribute("lastLoginDate", DateUtils.toStringDay(user.getLastLoginDate()));
        }

        return ctrlName + "/welcome";
    }

    @RequiresPermissions("admin#gtasks#get")
    @RequestMapping(name = "待办事项", path = "gtasks")
    public String gtasks(Model model) {

        return ctrlName + "/gtasks";
    }


    @RequiresPermissions("admin#dtasks#get")
    @RequestMapping(name = "办结事项", path = "dtasks")
    public String dtasks(Model model) {

        return ctrlName + "/dtasks";
    }

    @RequiresPermissions("admin#etasks#get")
    @RequestMapping(name = "办结事项", path = "etasks")
    public String etasks(Model model) {

        return ctrlName + "/etasks";
    }
    
    @RequiresPermissions("admin#edit#get")
    @RequestMapping(name = "拟补充资料函",  path = "edit")
    public String eidt(Model model) {
    	
    	return ctrlName + "/edit";
    }
}
