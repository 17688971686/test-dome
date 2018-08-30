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
    public PageModelDto<PolicyDto> findFileById(String fileId ,  String skip, String size , String search){

        PageModelDto<PolicyDto> pageModelDto = new PageModelDto<>();
        try{
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

        String[] searchStr = null;
        if(Validate.isString(search)){

           searchStr = search.substring( 1 , search.length()-1).split(",");
       }

        if(Validate.isString(searchStr) ){
            for(String str : searchStr){
                String[] searchArr = str.split(":");
                String searchName = searchArr[0].substring(1 , searchArr[0].length() - 1);
                String searchValue = searchArr[1].substring(1 , searchArr[1].length() - 1);
                //中文乱码处理
                if (searchValue.equals(new String(searchValue.getBytes("iso8859-1"), "iso8859-1"))) {

                    searchValue = new String(searchValue.getBytes("iso8859-1"), "UTF-8");
                }
                if(Validate.isString(searchValue)){
                    switch(searchName){
                        case "standardName":
                            sql.append(" and standardName like '%" + searchValue + "%' ");
                            sql2.append(" and standardName like '%" + searchValue + "%' ");
                            break;

                        case  "standardOrg" :
                            sql.append(" and standardOrg like '%" + searchValue + "%' ");
                            sql2.append(" and standardOrg like '%" + searchValue + "%' ");
                            break;

                        case  "cCSType" :
                            sql.append(" and cCSType like '%" + searchValue + "%' ");
                            sql2.append(" and cCSType like '%" + searchValue + "%' ");
                            break;

                        case  "iCSType" :
                            sql.append(" and iCSType like '%" + searchValue + "%' ");
                            sql2.append(" and iCSType like '%" + searchValue + "%' ");
                            break;

                        case  "publishDateBegin" :
                            sql.append(" and publishDate >=to_date('" + searchValue + " 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
                            sql2.append(" and publishDate >=to_date('" + searchValue + " 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
                            break;

                        case  "publishDateEnd" :
                            sql.append(" and publishDate <=to_date('" + searchValue + " 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
                            sql2.append(" and publishDate <=to_date('" + searchValue + " 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
                            break;

                        case  "implementDateBegin" :
                            sql.append(" and implementDate >=to_date('" + searchValue + " 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
                            sql2.append(" and implementDate >=to_date('" + searchValue + " 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
                            break;

                        case  "implementDateEnd" :
                            sql.append(" and implementDate <=to_date('" + searchValue + " 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
                            sql2.append(" and implementDate <=to_date('" + searchValue + " 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
                            break;

                        default: break;
                    }
                }

            }
        }



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





        }catch (Exception e){
            e.printStackTrace();
        }
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


    /**
     * 通过ID获取政策指标库内容
     * @param policyId
     * @return
     */
    @Override
    public PolicyDto findByPolicyId(String policyId) {

        PolicyDto policyDto = new PolicyDto();

        if(Validate.isString(policyId)){
            Policy policy = this.findById(Policy_.id.getName() , policyId);
            if(Validate.isObject(policy)){
                BeanCopierUtils.copyProperties(policy , policyDto);
            }
        }
        return policyDto;
    }
}
