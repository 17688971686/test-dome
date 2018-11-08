package cs.mobile.controller;

import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.flow.RuProcessTask;
import cs.domain.sys.OrgDept_;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.project.SignDispaWorkDto;
import cs.model.project.SignDto;
import cs.model.sys.DictDto;
import cs.repository.odata.ODataObj;
import cs.service.project.SignDispaWorkService;
import cs.service.project.SignService;
import cs.service.sys.DictService;
import cs.service.sys.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private UserService userService;

    @Autowired
    private DictService dictService;

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


    @RequestMapping(name = "获取项目取回数据", path = "findByGetBack", method = RequestMethod.POST)
    @ResponseBody
    public
    PageModelDto<RuProcessTask> getBackList(HttpServletRequest request) throws ParseException {
        PageModelDto<RuProcessTask> signDispaWork = new PageModelDto<>();
        //是否为部长或者小组组长
        boolean isOrgLeader = false;
        String username = request.getParameter("username");
        User user = userService.findByName(username);
        String level = userService.getUserLevel(user);
        if ((Validate.isString(level) && level.equals("3")) ) {
            isOrgLeader = true;
        } else  if (Validate.isString(level) && (level.equals("1") || level.equals("2"))) {
            isOrgLeader = false;
        } else {
            return signDispaWork;
        }
        ODataObj odataObj = new ODataObj(request);
        signDispaWork = signService.getBackAppList(odataObj, isOrgLeader,user);
        return signDispaWork;
    }

    @RequestMapping(name = "初始化评审部门", path = "initDeptList", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg initDeptList() {
        return signService.initSignList();
    }

    @RequestMapping(name = "初始化数字字典", path = "initDictList", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg initSignDictList() {
        Map map = new HashMap<String,Object>();
        map.put("PRO_STAGE",dictService.getDictForAppByCode("PRO_STAGE"));
        map.put("SECRECTLEVEL",(dictService.getDictForAppByCode("SECRECTLEVEL")));
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "数字字典初始化",map);
    }

}
