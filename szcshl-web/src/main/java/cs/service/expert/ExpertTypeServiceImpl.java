package cs.service.expert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.expert.Expert_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.utils.BeanCopierUtils;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertType;
import cs.domain.expert.ExpertType_;
import cs.model.expert.ExpertTypeDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ExpertTypeRepo;

@Service
public class ExpertTypeServiceImpl implements ExpertTypeService {


    @Autowired
    private ExpertTypeRepo expertTypeRepo;

    @Autowired
    private ExpertRepo expertRepo;

    /**
     * 保存专家类别消息
     *
     * @param expertTypeDto
     * @return
     */
    @Override
    @Transactional
    public ResultMsg saveExpertType(ExpertTypeDto expertTypeDto) {
        boolean isExpertTypeExist = expertTypeRepo.checkExpertTypeExist(expertTypeDto.getMaJorBig(),
                expertTypeDto.getMaJorSmall(), expertTypeDto.getExpertType(), expertTypeDto.getExpertID());
        if (!isExpertTypeExist) {
            if(!Validate.isString(expertTypeDto.getId())){
                expertTypeDto.setId(UUID.randomUUID().toString());
                expertTypeDto.setCreatedBy(SessionUtil.getDisplayName());
                expertTypeDto.setCreatedDate(new Date());
            }
            expertTypeDto.setModifiedBy(SessionUtil.getDisplayName());
            expertTypeDto.setModifiedDate(new Date());

            ExpertType expertType = new ExpertType();
            BeanCopierUtils.copyProperties(expertTypeDto, expertType);
            Expert expert = expertRepo.findById(Expert_.expertID.getName(), expertTypeDto.getExpertID());
            expertType.setExpert(expert);
            expertTypeRepo.save(expertType);

            return new ResultMsg(true , Constant.MsgCode.OK.getValue(),"操作成功！",expertTypeDto);
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), String.format("专家类型：%s 已经存在,请重新输入！", expertTypeDto.getExpertType()));
        }
    }

    @Override
    @Transactional
    public List<ExpertTypeDto> getExpertType(ODataObj odataObj) {
        List<ExpertType> expertTypeList = expertTypeRepo.findByOdata(odataObj);
        List<ExpertTypeDto> expertTypeDtoList = new ArrayList<>();
        for (ExpertType expertType : expertTypeList) {
            ExpertTypeDto expertTypeDto = new ExpertTypeDto();
            BeanCopierUtils.copyProperties(expertTypeDto, expertType);
            expertTypeDtoList.add(expertTypeDto);
        }
        return expertTypeDtoList;
    }

    @Override
    @Transactional
    public ExpertTypeDto getExpertTypeById(String expertTypeId) {
        ExpertType expertType = expertTypeRepo.findById(expertTypeId);
        ExpertTypeDto expertTypeDto = new ExpertTypeDto();
        if (expertType != null) {
            BeanCopierUtils.copyProperties(expertType, expertTypeDto);

            expertTypeDto.setExpertID(expertType.getExpert().getExpertID());
        }
        return expertTypeDto;
    }

    @Override
    @Transactional
    public void updateExpertType(ExpertTypeDto expertTypeDto) {
        ExpertType expertType = new ExpertType();
        BeanCopierUtils.copyProperties(expertTypeDto, expertType);
        expertType.setModifiedBy(SessionUtil.getDisplayName());
        expertType.setExpert(expertRepo.findById(expertTypeDto.getExpertID()));
        expertType.setModifiedDate(new Date());
        expertTypeRepo.save(expertType);

    }

    /**
     * 删除专家类型信息
     * @param ids
     */
    @Override
    @Transactional
    public void deleteExpertType(String ids) {
        expertTypeRepo.deleteById(ExpertType_.id.getName(), ids);
    }

}
