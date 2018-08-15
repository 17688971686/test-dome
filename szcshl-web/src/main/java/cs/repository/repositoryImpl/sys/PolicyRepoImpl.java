package cs.repository.repositoryImpl.sys;
import cs.common.constants.Constant;
import cs.common.utils.BeanCopierUtils;
import cs.domain.sys.Policy;
import cs.domain.sys.Policy_;
import cs.model.PageModelDto;
import cs.model.sys.PolicyDto;
import cs.repository.AbstractRepository;
import cs.repository.odata.ODataObj;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PolicyRepoImpl extends AbstractRepository<Policy, String> implements PolicyRepo {

    /**
     * 初始化政策指标库的所有文件夹
     * @return
     */
    @Override
    public List<PolicyDto> initFileFolder() {
        Criteria criteria = this.getExecutableCriteria();
        criteria.add(Restrictions.eq(Policy_.stardandType.getName() , Constant.libraryType.FOLDER_TYPE.getValue()));
        List<Policy> policyList = criteria.list();
        List<PolicyDto> policyDtoList = new ArrayList<>();

        if(policyList != null && policyList.size() > 0 ){
            for(Policy policy : policyList){
                PolicyDto policyDto = new PolicyDto();
                BeanCopierUtils.copyProperties(policy , policyDto);
                policyDtoList.add(policyDto);
            }
        }

        return policyDtoList;
    }

    /**
     * 通过Id获取文件
     * @param  oDataObj
     * @param standardId
     * @return
     */
    @Override
    public PageModelDto<PolicyDto> findFileById(ODataObj oDataObj , String standardId){

        PageModelDto<PolicyDto> pageModelDto = new PageModelDto<>();
        Criteria criteria = this.getExecutableCriteria();
        criteria = oDataObj.buildFilterToCriteria(criteria);
        criteria.add(Restrictions.eq(Policy_.standardPId.getName() , standardId));
        criteria.add(Restrictions.eq(Policy_.stardandType.getName() , Constant.libraryType.FILE_TYPE.getValue()));
        List<Policy> policyList = criteria.list();
        List<PolicyDto> policyDtoList = new ArrayList<>();

        if(policyList != null && policyList.size() > 0 ){
            for(Policy policy : policyList){
                PolicyDto policyDto = new PolicyDto();
                BeanCopierUtils.copyProperties(policy , policyDto);
                policyDtoList.add(policyDto);
            }
        }
        pageModelDto.setValue(policyDtoList);
        pageModelDto.setCount(oDataObj.getCount());


        return pageModelDto;
    }
}
