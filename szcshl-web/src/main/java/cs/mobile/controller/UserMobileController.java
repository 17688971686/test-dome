package cs.mobile.controller;

import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.domain.sys.Role;
import cs.domain.sys.User;
import cs.model.sys.RoleDto;
import cs.model.sys.UserDto;
import cs.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 移动端 控制层
 * author: hjm
 * Date: 2018-2-26 11:23:41
 */
@Controller
@RequestMapping(name = "用户登录", path = "api/user")
public class UserMobileController {
    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param loginName 登录名
     * @param password 密码
     * */
    @RequestMapping(name = "移动端登录登录", path = "login", method = RequestMethod.POST)
    @ResponseBody
    ResultMsg post(HttpServletRequest request, HttpServletResponse response,String loginName, String password) {
        ResultMsg loginResult=new ResultMsg();
        try {
        loginName = new String(loginName.getBytes("ISO-8859-1"),"UTF-8");
         loginResult= userService.Login(request,response,loginName, password);
        } catch (Exception e){
            e.printStackTrace();
        }
        return loginResult;
    }


    @RequestMapping(name = "根据登录名获取用户信息", path = "getUserInfo", method = RequestMethod.GET)
    public @ResponseBody
    UserDto getUserInfo(String loginName) throws ParseException {
        User user = userService.findByName(loginName);
        UserDto userDto = new UserDto();
        userDto.setLoginName(loginName);//登录名
        userDto.setId(user.getId());//id
        userDto.setPassword(user.getPassword());//密码
        userDto.setDisplayName(user.getDisplayName());//显示名
        List<Role> roleList = user.getRoles();
        if (roleList != null && roleList.size() > 0) {
            List<RoleDto> roleDtoList = new ArrayList<RoleDto>(roleList.size());
            RoleDto roleDto = new RoleDto();
            roleList.forEach(r -> {
                BeanCopierUtils.copyProperties(r, roleDto);
                roleDtoList.add(roleDto);
            });
            userDto.setRoleDtoList(roleDtoList);
        }
        return userDto;
    }


}