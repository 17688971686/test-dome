package cs.service.postdoctor;

import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.postdoctor.PostdoctoralBase_;
import cs.domain.postdoctor.PostdoctoralStaff;
import cs.domain.sys.Role;
import cs.domain.sys.Role_;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.postdoctor.PostdoctoralStaffDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.postdoctor.PostdoctorStaffRepo;
import cs.repository.repositoryImpl.sys.RoleRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 博士后 业务操作接口
 * author: zsl
 * Date: 2018-10-23 15:04:55
 * @author Administrator
 */
@Service
public class PostdoctoralStaffServiceImpl implements PostdoctoralStaffService {

    @Autowired
    private PostdoctorStaffRepo postdoctorStaffRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserRepo userRepo;


    @Override
    public PageModelDto<PostdoctoralStaffDto> get(ODataObj odataObj) {
        PageModelDto<PostdoctoralStaffDto> pageModelDto = new PageModelDto<PostdoctoralStaffDto>();
        List<PostdoctoralStaff> resultList = postdoctorStaffRepo.findByOdata(odataObj);
        List<PostdoctoralStaffDto> resultDtoList = new ArrayList<PostdoctoralStaffDto>(resultList.size());
        if (Validate.isList(resultList)) {
            resultList.forEach(x -> {
                PostdoctoralStaffDto modelDto = new PostdoctoralStaffDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                resultDtoList.add(modelDto);
            });
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(resultDtoList);
        return pageModelDto;
    }

    @Override
    public ResultMsg save(PostdoctoralStaffDto record) {

       Role r = roleRepo.findById(Role_.roleName.getName(),Constant.EnumPostdoctoralName.POSTDOCTORAL_PERSON.getValue());

        if(!Validate.isObject(r)){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "博士后人员角色还没创建，请先创建！");
        }
        Date now = new Date();
        if (!Validate.isString(record.getId())) {
            record.setId(UUID.randomUUID().toString());
            User u = new User();
            u.setId(UUID.randomUUID().toString());
            u.setDisplayName(record.getName());
            u.setLoginName(record.getName());
            u.setPassword("1");
            u.setCreatedDate(now);
            u.setModifiedDate(now);
            u.setCreatedBy(SessionUtil.getDisplayName());
            u.setModifiedBy(SessionUtil.getDisplayName());
            u.setLoginFailCount(0);
            u.getRoles().add(r);
            userRepo.save(u);
        }


        PostdoctoralStaff domain = new PostdoctoralStaff();
        BeanCopierUtils.copyProperties(record, domain);
        if(!Validate.isString(domain.getStatus())){
            domain.setStatus("0");
        }

        domain.setCreatedBy(SessionUtil.getDisplayName());
        domain.setModifiedBy(SessionUtil.getDisplayName());
        domain.setCreatedDate(now);
        domain.setModifiedDate(now);
        postdoctorStaffRepo.save(domain);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功", record);

    }

    @Override
    public void update(PostdoctoralStaffDto record) {

    }


    @Override
    public ResultMsg delete(String id) {
        postdoctorStaffRepo.deleteById(PostdoctoralBase_.id.getName(), id);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "删除成功！");
    }

    @Override
    public PostdoctoralStaffDto findDetailById(String id) {
        PostdoctoralStaffDto modelDto = new PostdoctoralStaffDto();
        PostdoctoralStaff domain = postdoctorStaffRepo.findById(id);
        BeanCopierUtils.copyProperties(domain, modelDto);
        return modelDto;
    }

    @Override
    public ResultMsg approvePostdoctoralStaff(String id , String status) {
        if(!Validate.isString(id)){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "审核失败，id不能为空！");
        }
        if(!Validate.isString(status)){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "状态异常请核查！");
        }
        PostdoctoralStaff domain =  postdoctorStaffRepo.findById(id);
        if(Validate.isString(status) && status.equals("1")){
            domain.setStatus("2");
            domain.setEnterStackApproveDate(new Date());
        }else if(Validate.isString(status) && status.equals("3")){
            domain.setStatus("4");
            domain.setPooStackDate(new Date());
        }
        Date now = new Date();
        domain.setCreatedBy(SessionUtil.getUserId());
        domain.setModifiedBy(SessionUtil.getUserId());
        domain.setCreatedDate(now);
        domain.setModifiedDate(now);
        postdoctorStaffRepo.save(domain);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "审核成功!");

    }


    @Override
    public ResultMsg backPostdoctoralStaff(String id , String status) {
        if(!Validate.isString(id)){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "审核失败，id不能为空！");
        }
        if(!Validate.isString(status)){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "状态异常请核查！");
        }
        PostdoctoralStaff domain =  postdoctorStaffRepo.findById(id);
        Date now = new Date();
        domain.setCreatedBy(SessionUtil.getUserId());
        domain.setModifiedBy(SessionUtil.getUserId());
        domain.setCreatedDate(now);
        domain.setModifiedDate(now);
        domain.setStatus(status);
        postdoctorStaffRepo.save(domain);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "回退成功!");

    }



}
