package cs.service.postdoctor;

import cs.common.RandomGUID;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.*;
import cs.domain.postdoctor.PostdoctoralBase;
import cs.domain.postdoctor.PostdoctoralBase_;
import cs.model.PageModelDto;
import cs.model.postdoctor.PostdoctoralBaseDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.postdoctor.PostdoctorInfoRepo;
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
public class PostdoctoralBaseServiceImpl implements PostdoctoralBaseService {

    @Autowired
    private PostdoctorInfoRepo postdoctorInfoRepo;

    @Override
    public PageModelDto<PostdoctoralBaseDto> get(ODataObj odataObj) {
        PageModelDto<PostdoctoralBaseDto> pageModelDto = new PageModelDto<PostdoctoralBaseDto>();
        List<PostdoctoralBase> resultList = postdoctorInfoRepo.findByOdata(odataObj);
        List<PostdoctoralBaseDto> resultDtoList = new ArrayList<PostdoctoralBaseDto>(resultList.size());
        if (Validate.isList(resultList)) {
            resultList.forEach(x -> {
                PostdoctoralBaseDto modelDto = new PostdoctoralBaseDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                resultDtoList.add(modelDto);
            });
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(resultDtoList);
        return pageModelDto;
    }

    @Override
    public ResultMsg save(PostdoctoralBaseDto record) {
        if (!Validate.isString(record.getId())) {
            record.setId((new RandomGUID()).valueAfterMD5);
        }
        PostdoctoralBase domain = new PostdoctoralBase();
        BeanCopierUtils.copyProperties(record, domain);
        Date now = new Date();
        domain.setCreatedBy(SessionUtil.getUserId());
        domain.setModifiedBy(SessionUtil.getUserId());
        domain.setCreatedDate(now);
        domain.setModifiedDate(now);
        postdoctorInfoRepo.save(domain);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功", record);
    }

    @Override
    public void update(PostdoctoralBaseDto record) {

    }


    @Override
    public ResultMsg delete(String id) {
        postdoctorInfoRepo.deleteById(PostdoctoralBase_.id.getName(), id);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "删除成功！");
    }

    @Override
    public PostdoctoralBaseDto findDetailById(String id) {
        PostdoctoralBaseDto modelDto = new PostdoctoralBaseDto();
        PostdoctoralBase domain = postdoctorInfoRepo.findById(id);
        BeanCopierUtils.copyProperties(domain, modelDto);
        return modelDto;
    }


}
