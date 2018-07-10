package com.sn.framework.module.sys.controller;

import com.sn.framework.common.RSAKey;
import com.sn.framework.common.RSAUtils;
import com.sn.framework.core.boot.properties.ShiroProperties;
import com.sn.framework.core.common.SNKit;
import com.sn.framework.core.shiro.JCaptchaUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


/**
 * Description: 
 * @author: tzg
 * @date: 2018/5/17 19:14
 */
@Controller
@RequestMapping(name = "账户控制器")
public class AccountController {

    private final static Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private ShiroProperties shiroProperties;

    @RequestMapping(name = "登录页", path = "login", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(HttpServletRequest request, Map<String, Object> map) {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            return "forward:/admin/index";
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" 用户登录页 ");
        }
        // 登录失败从request中获取shiro处理的异常信息，shiroLoginFailure就是shiro异常类的全类名.
        String exception = (String) request.getAttribute("shiroLoginFailure");

        map.put("msg", SNKit.extractErrorMsg(exception));
        map.put("loginCaptcha", shiroProperties.isLoginCaptcha());

        return "/login";
    }

//    @RequestMapping(name = "退出", path = "logout")
//    public String logout() {
////        Subject subject = SecurityUtils.getSubject();
////        String username = (String) subject.getPrincipal();
////        if (subject.isAuthenticated()) {
////            subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
////            if (logger.isDebugEnabled()) {
////                logger.debug(String.format("用户【%s】退出登录", username));
////            }
////        }
//        return "forward:/login";
//    }

    @RequestMapping(name = "生成验证码", path = "captchaImage")
    public void getJCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        BufferedImage bi = JCaptchaUtil.getInstance().getImageChallengeForID(request.getSession(true).getId());
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    @RequestMapping(name = "生成秘钥", path = "rsaKey")
    @ResponseBody
    public String getRSA(HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        RSAKey keyMap = RSAUtils.createKeys();
        request.getSession().setAttribute("resKey", keyMap);
        return keyMap.getPublicKey();
    }

    @RequiresAuthentication
    @RequestMapping(name = "系统主页", path = {"", "index"})
    public String index() {
        return "forward:/admin/index";
    }

}
