package cs.repository.repositoryImpl.sys;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
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
     * @return
     */
    @Override
    public PageModelDto<PolicyDto> findFileById(String fileId ,  String skip, String size){

        PageModelDto<PolicyDto> pageModelDto = new PageModelDto<>();
       /* Criteria criteria = this.getExecutableCriteria();
        criteria = oDataObj.buildFilterToCriteria(criteria);
//        criteria.add(Restrictions.eq(Policy_.standardPId.getName() , standardId));
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
        pageModelDto.setCount(oDataObj.getCount());*/

        HqlBuilder sql = HqlBuilder.create();
        HqlBuilder sql2 = HqlBuilder.create();

        sql.append("select * from ( select p.* , rownum as num from cs_policy p where p."+ Policy_.stardandType.getName() +"=:stardandType");

        sql2.append("select count(id) from cs_policy p where  p."+ Policy_.stardandType.getName() +"=:stardandType");

        if(Validate.isString(fileId)){
            sql.append(" and p.standardPId =:standardPId");
            sql.setParam("standardPId" , fileId);

            sql2.append(" and p.standardPId =:standardPId");
            sql2.setParam("standardPId" , fileId);
        }

        sql.append(" ) where num between " + skip + " and " + size );

        sql.setParam("stardandType" , Constant.libraryType.FILE_TYPE.getValue());
        sql2.setParam("stardandType" , Constant.libraryType.FILE_TYPE.getValue());

        int count = this.returnIntBySql(sql2);

        List<Policy> policyList = this.findBySql(sql);
        List<PolicyDto> policyDtoList = new ArrayList<>();

        if(policyList != null && policyList.size() > 0 ){
            for(Policy policy : policyList){
                PolicyDto policyDto = new PolicyDto();
                BeanCopierUtils.copyProperties(policy , policyDto);
                policyDtoList.add(policyDto);
            }
        }

        pageModelDto.setValue(policyDtoList);
        pageModelDto.setCount(count);




        return pageModelDto;
    }

    /**
     * 删除政策指标库
     * @param idStr
     */
    @Override
    public void deletePolicy(String idStr) {
        if(Validate.isString(idStr)){
                HqlBuilder sql = HqlBuilder.create();
                sql.append(" delete from cs_policy  where id in ( '" + idStr.replaceAll("," , "','") + "')");
                sql.append(" or standardPId in ('" + idStr.replaceAll("," , "','") + "')");

              this.executeSql(sql);
        }
    }
}
