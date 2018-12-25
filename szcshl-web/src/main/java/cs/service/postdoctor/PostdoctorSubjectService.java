package cs.service.postdoctor;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.postdoctor.PostdoctorSubjectDto;
import cs.repository.odata.ODataObj;

/**
 * Created by MCL
 * 2018/10/31
 */
public interface PostdoctorSubjectService  {
    PageModelDto<PostdoctorSubjectDto> findByAll(ODataObj odataObj);

    PostdoctorSubjectDto findBySubjectId(String id);

    ResultMsg createSubject(PostdoctorSubjectDto dto);

    /**
     * 删除课题
     * @param id
     * @return
     */
    ResultMsg deleteSubject(String id);

}
