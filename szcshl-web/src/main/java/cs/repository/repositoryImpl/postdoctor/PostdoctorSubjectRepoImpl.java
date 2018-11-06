package cs.repository.repositoryImpl.postdoctor;

import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.postdoctor.PostdoctorSubject;
import cs.domain.postdoctor.PostdoctoralStaff;
import cs.model.PageModelDto;
import cs.model.postdoctor.PostdoctorSubjectDto;
import cs.repository.AbstractRepository;
import cs.repository.odata.ODataObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 博士后基地课题
 * Author: mcl
 * Date: 2018/10/31 10:29
 */
@Repository
public class PostdoctorSubjectRepoImpl extends AbstractRepository<PostdoctorSubject , String > implements  PostdoctorSubjectRepo {

    @Autowired
    private PostdoctorStaffRepo postdoctorStaffRepo;


    @Override
    public PageModelDto<PostdoctorSubjectDto> findByAll(ODataObj odataObj) {

        PageModelDto<PostdoctorSubjectDto> pageModelDto = new PageModelDto<>();

        List<PostdoctorSubject> psList = this.findByOdata(odataObj);

        List<PostdoctorSubjectDto> dtoList = new ArrayList<>();

        if(psList != null && psList.size() > 0 ){
            for(PostdoctorSubject ps : psList){
                PostdoctorSubjectDto dto = new PostdoctorSubjectDto();
                if(Validate.isString(ps.getPricipalId())){
                    PostdoctoralStaff p = postdoctorStaffRepo.findById(ps.getPricipalId());
                    dto.setPricipalName(p == null ? "" : p.getName());
                }else{

                dto.setPricipalName(ps.getPricipalId());
                }
                BeanCopierUtils.copyPropertiesIgnoreProps(ps , dto);
                dtoList.add(dto);
            }
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(dtoList);

        return pageModelDto;
    }

    @Override
    public PostdoctorSubjectDto findBySubjectId(String id) {
        PostdoctorSubjectDto dto = new PostdoctorSubjectDto();
        PostdoctorSubject ps = this.findById(id);
        if(ps != null){

            BeanCopierUtils.copyPropertiesIgnoreProps( ps , dto );
        }
        return dto;
    }

    @Override
    @Transactional
    public ResultMsg createSubject(PostdoctorSubjectDto dto) {
        PostdoctorSubject ps = new PostdoctorSubject();
        Date now  = new Date();
        if(dto != null && !Validate.isString(dto.getId())) {
            ps.setId(UUID.randomUUID().toString());
            ps.setCreatedBy(SessionUtil.getDisplayName());
            ps.setCreatedDate(now);
            dto.setId(ps.getId());
        }else{
            ps = this.findById(dto.getId());
        }
        BeanCopierUtils.copyPropertiesIgnoreProps(dto , ps);
        ps.setModifiedBy(SessionUtil.getDisplayName());
        ps.setModifiedDate(now);
        this.save(ps);

        return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "创建成功" , dto);
    }


}