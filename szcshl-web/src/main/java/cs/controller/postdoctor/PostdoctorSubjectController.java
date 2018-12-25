package cs.controller.postdoctor;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.postdoctor.PostdoctorSubject;
import cs.model.PageModelDto;
import cs.model.postdoctor.PostdoctorSubjectDto;
import cs.model.postdoctor.PostdoctoralStaffDto;
import cs.repository.odata.ODataObj;
import cs.service.postdoctor.PostdoctorSubjectService;
import cs.service.postdoctor.PostdoctoralStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static cs.common.constants.Constant.ERROR_MSG;

/**
 * Description: 博士后基地课题
 * Author: mcl
 * Date: 2018/10/31 10:19
 */
@Controller
@RequestMapping(name = "博士后基地课题", path = "postdoctorSubject")
@MudoleAnnotation(name = "博士后人员管理", value = "permission#postdoctoralManager")
public class PostdoctorSubjectController {

    private String ctrlName = "postdoctoralBase";

    @Autowired
    private PostdoctorSubjectService postdoctorSubjectService;

    @Autowired
    private PostdoctoralStaffService postdoctoralStaffService;


    @RequestMapping(name = "课题列表", path = "findByAll", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<PostdoctorSubjectDto> findByAll(HttpServletRequest request) {
        PageModelDto<PostdoctorSubjectDto> pageModelDto = new PageModelDto<>();
        try {
            ODataObj odataObj = new ODataObj(request);
            pageModelDto = postdoctorSubjectService.findByAll(odataObj);
            if (!Validate.isObject(pageModelDto)) {
                pageModelDto = new PageModelDto<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageModelDto;
    }


    @RequestMapping(name = "通过id查询课题信息", path = "findBySubjectId", method = RequestMethod.POST)
    @ResponseBody
    public PostdoctorSubjectDto findBySubjectId(@RequestParam String id) {
        PostdoctorSubjectDto postdoctorSubjectDto = postdoctorSubjectService.findBySubjectId(id);
        if (!Validate.isObject(postdoctorSubjectDto)) {
            postdoctorSubjectDto = new PostdoctorSubjectDto();
        }
        return postdoctorSubjectDto;
    }

    @RequestMapping(name = "创建课题", path = "createdSubject", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg createdSubject(@RequestBody PostdoctorSubjectDto dto) {
        ResultMsg resultMsg = postdoctorSubjectService.createSubject(dto);
        if (!Validate.isObject(resultMsg)) {
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequestMapping(name = "删除课题", path = "deleteSubject", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg deleteSubject(@RequestParam String id) {
        ResultMsg resultMsg = postdoctorSubjectService.deleteSubject(id);
        if (!Validate.isObject(resultMsg)) {
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequestMapping(name = "查询在站人员", path = "findStationStaff", method = RequestMethod.POST)
    @ResponseBody
    public List<PostdoctoralStaffDto> findStationStaff() {
        List<PostdoctoralStaffDto> postdoctoralStaffDtoList = postdoctoralStaffService.findStationStaff();
        if (!Validate.isList(postdoctoralStaffDtoList)) {
            postdoctoralStaffDtoList = new ArrayList<>();
        }
        return postdoctoralStaffDtoList;
    }

    @RequestMapping(name = "判断是否有权限查看详情", path = "isPermission", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg isPermission() {
        if (SessionUtil.hashRole(Constant.SUPER_ROLE) || SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())) {
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", null);
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您无权查看！", null);
        }
    }


    @RequestMapping(name = "课题列表页", path = "html/subjectList")
    public String subjectList() {
        //设置是否是在站人员
        SessionUtil.getSession().setAttribute("ISSTAFF", postdoctoralStaffService.findByName() ? 1 : 0);
        //博士后基地管理员
        SessionUtil.getSession().setAttribute("isManagement" , SessionUtil.hashRole(Constant.EnumPostdoctoralName.POSTDOCTORAL_ADMIN.getValue()) ? 1 : 0 );
        return ctrlName + "/postdoctorSubjectList";
    }

    @RequestMapping(name = "课题编辑页", path = "html/postdoctoralSubjectAdd")
    public String subjectAdd() {
        return ctrlName + "/postdoctorSubjectAdd";
    }

    @RequestMapping(name = "课题详情页", path = "html/postdoctoralSubjectDetail")
    public String detail() {

        return ctrlName + "/postdoctorSubjectDetail";
    }

}