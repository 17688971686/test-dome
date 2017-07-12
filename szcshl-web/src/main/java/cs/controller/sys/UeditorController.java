package cs.controller.sys;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cs.ueditor.ActionEnter;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 富文本编辑器方法
 *
 * @author liangdengming
 */
@Controller
@RequestMapping(name = "富文本编辑器", path = "ueditor")
public class UeditorController {
    private static final Logger log = LoggerFactory.getLogger(UeditorController.class);

    @RequestMapping(name = "uedit配置路径", path = "config", method = RequestMethod.GET)
    public void config(HttpServletRequest request, HttpServletResponse response) {
        // response.setContentType("application/json");
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        response.setHeader("Content-Type", "text/html");
        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
