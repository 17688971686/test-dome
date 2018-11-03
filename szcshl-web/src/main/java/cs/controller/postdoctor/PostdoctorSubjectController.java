package cs.controller.postdoctor;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.domain.postdoctor.PostdoctorSubject;
import cs.model.PageModelDto;
import cs.model.postdoctor.PostdoctorSubjectDto;
import cs.repository.odata.ODataObj;
import cs.service.postdoctor.PostdoctorSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: 博士后基地课题
 * Author: mcl
 * Date: 2018/10/31 10:19
 */
@Controller
@RequestMapping(name = "博士后基地课题" , path = "postdoctorSubject")
@MudoleAnnotation(name = "博士后人员管理",value = "permission#postdoctoralManager")
public class PostdoctorSubjectController {

    private String ctrlName = "postdoctoralBase";

    @Autowired
    private PostdoctorSubjectService postdoctorSubjectService;


    @RequestMapping(name = "课题列表" , path = "findByAll" , method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<PostdoctorSubjectDto> findByAll(HttpServletRequest request ){

         PageModelDto<PostdoctorSubjectDto> pageModelDto = new PageModelDto<>();
        try{
            ODataObj odataObj = new ODataObj(request);
            pageModelDto = postdoctorSubjectService.findByAll(odataObj);

        }catch (Exception e){
            e.printStackTrace();
        }
        return pageModelDto;
    }


    @RequestMapping(name = "通过id查询课题信息" , path = "findBySubjectId" , method = RequestMethod.POST)
    @ResponseBody
    public PostdoctorSubjectDto findBySubjectId(@RequestParam  String id){
        return postdoctorSubjectService.findBySubjectId(id);
    }

    @RequestMapping(name = "创建课题" , path = "createdSubject" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg createdSubject(@RequestBody  PostdoctorSubjectDto dto){
        return postdoctorSubjectService.createSubject(dto);
    }

    @RequestMapping(name = "课题列表页" , path = "html/subjectList")
    public String subjectList(){
        return ctrlName + "/postdoctorSubjectList";
    }

    @RequestMapping(name = "课题编辑页" , path = "html/postdoctoralSubjectAdd")
    public String subjectAdd(){
        return ctrlName + "/postdoctorSubjectAdd";
    }

}