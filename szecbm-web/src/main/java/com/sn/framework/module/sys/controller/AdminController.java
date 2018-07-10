package com.sn.framework.module.sys.controller;

import com.sn.framework.common.StringUtil;
import com.sn.framework.core.boot.properties.ShiroProperties;
import com.sn.framework.core.common.SessionUtil;
import com.sn.framework.module.sys.model.UserInfoDto;
import com.sn.framework.module.sys.service.IUserService;
import com.sn.framework.odata.impl.jpa.OdataJPA;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.sn.framework.core.Constants.ORGAN_TYPE_GXBSSLD;

/**
 * Description: 
 * @author: tzg
 * @date: 2018/5/17 19:14
 */
@Controller
@RequestMapping(name = "后台管理", path = "admin")
public class AdminController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ShiroProperties shiroProperties;

    @RequiresAuthentication
    @RequestMapping(name = "主页", path = "index")
    public String index(Map<String, Object> map) {
        map.put("loginCaptcha", shiroProperties.isLoginCaptcha());
        return "admin/index";
    }

    @RequiresAuthentication
    @RequestMapping(name = "欢迎页", path = "welcome")
    public String welcome() {
        String organId = SessionUtil.getOrganId();
        if (StringUtil.isNotBlank(organId) && ORGAN_TYPE_GXBSSLD.equals(organId)) {
            return "admin/welcomeLeader";
        }
        return "admin/welcome";
    }

    @RequiresAuthentication
    @RequestMapping(name = "个人信息", path = "userInfo", method = RequestMethod.GET)
    public String userInfo() {
        return "admin/userInfo";
    }

    @RequiresAuthentication
    @RequestMapping(name = "个人信息", path = "userInfo", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveUserInfo(@RequestBody UserInfoDto userDto) {
        userService.saveCurrentUserInfo(userDto);
    }

}