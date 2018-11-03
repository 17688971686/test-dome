package cs.service.postdoctor;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.postdoctor.PostdoctorSubjectDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.postdoctor.PostdoctorSubjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: 博士后基地课题业务类
 * Author: mcl
 * Date: 2018/10/31 10:41
 */
@Service
public class PostdoctorSubjectServiceImpl implements PostdoctorSubjectService {

    @Autowired
    private PostdoctorSubjectRepo postdoctorSubjectRepo;

    @Override
    public PageModelDto<PostdoctorSubjectDto> findByAll(ODataObj odataObj) {
        return postdoctorSubjectRepo.findByAll(odataObj);
    }

    @Override
    public PostdoctorSubjectDto findBySubjectId(String id) {
        return postdoctorSubjectRepo.findBySubjectId(id);
    }

    @Override
    public ResultMsg createSubject(PostdoctorSubjectDto dto) {
        return postdoctorSubjectRepo.createSubject(dto);
    }
}