package cs.repository.repositoryImpl.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.DateUtils;
import cs.domain.expert.*;
import cs.domain.project.SignDispaWork;
import cs.domain.project.SignDispaWork_;
import cs.model.PageModelDto;
import cs.model.expert.ProReviewConditionDto;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

/**
 * Description: 项目统计视图 数据操作实现类
 * author: ldm
 * Date: 2017-7-10 9:35:22
 */
@Repository
public class SignDispaWorkRepoImpl extends AbstractRepository<SignDispaWork, String> implements SignDispaWorkRepo {
    @Override
    public PageModelDto<SignDispaWork> reviewProject(String expertId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + SignDispaWork.class.getSimpleName() + " where " + SignDispaWork_.signid.getName() + " in (");
        hqlBuilder.append(" select " + ExpertReview_.businessId.getName() + " from " + ExpertReview.class.getSimpleName() + " where " + ExpertReview_.id.getName() + " in (");
        hqlBuilder.append(" select " + ExpertSelected_.expertReview.getName() + "." + ExpertReview_.id.getName() + " from " + ExpertSelected.class.getSimpleName() + " where ");
        hqlBuilder.append(" " + ExpertSelected_.isConfrim.getName() + "=:isConfrim");
        hqlBuilder.append(" and " + ExpertSelected_.isJoin.getName() + "=:isJoin");
        hqlBuilder.append(" and " + ExpertSelected_.expert.getName() + "." + Expert_.expertID.getName() + "=:expertId))");
        hqlBuilder.setParam("isConfrim" , Constant.EnumState.YES.getValue());
        hqlBuilder.setParam("isJoin" , Constant.EnumState.YES.getValue());
        hqlBuilder.setParam("expertId" , expertId);

        List<SignDispaWork> signDispaWorkList =this.findByHql(hqlBuilder);
        PageModelDto<SignDispaWork> pageModelDto = new PageModelDto<>();
        pageModelDto.setValue(signDispaWorkList);
        pageModelDto.setCount(signDispaWorkList.size());
        return pageModelDto;
    }

    /**
     * 通过时间段查询项目信息，用于项目查询统计分析
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<Map<String , Object[]>> findByTime(String startTime, String endTime) {

        Date start = DateUtils.converToDate(startTime , "yyyy-MM-dd");
        Date end = DateUtils.converToDate(endTime , "yyyy-MM-dd");
        List<Map<String , Object[]>> resultList = new ArrayList<>();
        if(DateUtils.daysBetween(start , end) >0){
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append(" select reviewstage , sum(appalyinvestment) appalyinvestment , sum(authorizeValue) authorizeValue , count(projectcode) projectCount from v_sign_disp_work" );
            hqlBuilder.append(" where " + SignDispaWork_.signdate.getName() + " >=:start and " + SignDispaWork_.signdate.getName() + "<=:end");
            hqlBuilder.append(" group by " + SignDispaWork_.reviewstage.getName());
            hqlBuilder.setParam("start" , start);
            hqlBuilder.setParam("end" , end);
            List<Object[]> objctList = this.getObjectArray(hqlBuilder);

            if(objctList != null && objctList.size()>0){
                for(int i = 0 ; i<objctList.size() ; i++){
                    Object[] obj = objctList.get(i);
                    Map< String , Object[]> map = new HashMap<>();
                    Object[] value = {((BigDecimal) obj[1] ) == null ? 0 : ((BigDecimal) obj[1]).divide(new BigDecimal(10000)) ,
                            ((BigDecimal) obj[2] ) == null ? 0 : ((BigDecimal) obj[2]).divide(new BigDecimal(10000)) ,
                     obj[3] == null ? 0 : obj[3]}; //[申报金额，审定金额，数目]
                    map.put( (String)obj[0] , value);
                    resultList.add(map);
                }
            }
        }


        return resultList;
    }

}