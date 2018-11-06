package cs.repository.repositoryImpl.postdoctor;

import cs.common.ResultMsg;
import cs.domain.postdoctor.PostdoctorSubject;
import cs.model.PageModelDto;
import cs.model.postdoctor.PostdoctorSubjectDto;
import cs.repository.IRepository;
import cs.repository.odata.ODataObj;

/**
 * Created by MCL
 * 2018/10/31
 */
public interface PostdoctorSubjectRepo extends IRepository<PostdoctorSubject , String > {

    /**
     * 查询博士后基地课题列表
     * @param odataObj
     * @return
     */
    PageModelDto<PostdoctorSubjectDto> findByAll(ODataObj odataObj);

    /**
     * 通过Id获取课题信息
     * @param id
     * @return
     */
    PostdoctorSubjectDto findBySubjectId(String id);

    /**
     * 新增或更新课题信息
     * @param dto
     * @return
     */
    ResultMsg createSubject(PostdoctorSubjectDto dto);
}
