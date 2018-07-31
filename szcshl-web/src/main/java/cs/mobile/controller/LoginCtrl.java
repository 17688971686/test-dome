package cs.mobile.controller;

import cs.mobile.util.TokenUtil;
import cs.common.ResultMsg;
import cs.common.utils.Validate;
import cs.domain.sys.User;
import cs.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 手机端用户登录
 * Created by ldm on 2018/7/25 0025.
 */
@Controller
@RequestMapping(name = "手机端登录", path = "api/login")
public class LoginCtrl{

    @Autowired
    private UserService userService;

    @RequestMapping(name = "手机登录", path = "signin", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ResultMsg signIn(@RequestParam String loginName, @RequestParam String password){
        User user = userService.findByName(loginName);
        if(!Validate.isObject(user)){
            return ResultMsg.error("账号密码不正确！");
        }
        if(!password.trim().equals(user.getPassword())){
            return ResultMsg.error("账号密码不正确！");
        }
        String userToken = TokenUtil.getUserToken(loginName);
        user.setToken(userToken);
        userService.saveUser(user);
        ResultMsg resultMsg = ResultMsg.ok("登录成功！");
        resultMsg.setIdCode(userToken);
        resultMsg.setReObj(userToken);
        System.out.println("用户登录成功！");
        return resultMsg;
    }
}