package cs.service.sys;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.*;
import cs.domain.sys.Policy;
import cs.model.PageModelDto;
import cs.model.sys.PolicyDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.PolicyRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PolicyServiceImpl implements PolicyService {
    private static Logger logger = Logger.getLogger(UserServiceImpl.class);
    @Autowired
    private PolicyRepo policyRepo;

    @Override
    public PageModelDto<PolicyDto> get(ODataObj odataObj) {
        PageModelDto<PolicyDto> pageModelDto = new PageModelDto<PolicyDto>();
        List<Policy> resultList = policyRepo.findByOdata(odataObj);
        List<PolicyDto> resultDtoList = new ArrayList<>();
        if (Validate.isList(resultList)) {
            resultList.forEach(x -> {
                PolicyDto modelDto = new PolicyDto();
                BeanCopierUtils.copyProperties(x, modelDto);

                resultDtoList.add(modelDto);
            });
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(resultDtoList);
        return pageModelDto;
    }

    @Override
    public ResultMsg save(PolicyDto record) {
        Policy domain = new Policy();
        Date now = new Date();
        if (!Validate.isString(record.getId())) {
            record.setId(UUID.randomUUID().toString());
            domain.setCreatedBy(SessionUtil.getUserId());
            domain.setCreatedDate(now);
        }
        BeanCopierUtils.copyProperties(record, domain);

        domain.setModifiedBy(SessionUtil.getUserId());
        domain.setModifiedDate(now);
        policyRepo.save(domain);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功", record);
    }

    /**
     * 初设化政策指标库的所有文件
     * @return
     */
    @Override
    public List<PolicyDto> initFileFolder() {
        return policyRepo.initFileFolder();
    }


    /**
     * 通过ID获取文件
     * @return
     */
    @Override
    public PageModelDto<PolicyDto> findFileById(String fileId ,  String skip, String size , String search) {
        return policyRepo.findFileById(fileId , skip , size , search);
    }

    /**
     * 删除政策指标库
     * @param idStr
     */
    @Override
    public void deletePolicy(String idStr) {
        policyRepo.deletePolicy(idStr);
    }

    /**
     * 通过ID获取政策指标库内容
     * @param policyId
     * @return
     */
    @Override
    public PolicyDto findByPolicyId(String policyId) {
        return policyRepo.findByPolicyId(policyId);
    }
}
