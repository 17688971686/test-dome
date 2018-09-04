package cs.mobile.controller;

import cs.model.PageModelDto;
import cs.model.project.SignDispaWorkDto;
import cs.model.project.SignDto;
import cs.repository.odata.ODataObj;
import cs.service.project.SignDispaWorkService;
import cs.service.project.SignService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * 手机端项目查询接口
 * Created by ldm on 2018/9/4 0004.
 */
@Controller
@RequestMapping(name = "项目控制器", path = "api/sign")
public class SignAppController {

    @Autowired
    private SignService signService;
    @Autowired
    private SignDispaWorkService signDispaWorkService;
    /**
     * 根据ID查询项目收文信息
     * @param signId
     * @return
     */
    @RequestMapping(name = "获取收文数据", path = "findSignById", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public SignDto fingSignById(@RequestParam String signId,boolean queryAll) {
       return signService.findById(signId,queryAll);
    }


    @RequestMapping(name = "项目查询统计", path = "findSignByPage", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public PageModelDto<SignDispaWorkDto> findSignByPage(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SignDispaWorkDto> pageModelDto = signDispaWorkService.getCommQurySign(odataObj);
        return pageModelDto;
    }
}
