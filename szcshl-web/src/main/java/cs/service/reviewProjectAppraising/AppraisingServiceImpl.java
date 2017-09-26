package cs.service.reviewProjectAppraising;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.Validate;
import cs.domain.project.Sign;
import cs.domain.project.SignDispaWork;
import cs.domain.project.SignDispaWork_;
import cs.domain.project.Sign_;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.SignDispaWorkRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description: 优秀评审项目
 * Author: mcl
 * Date: 2017/9/23 16:10
 */
@Service
public class AppraisingServiceImpl implements AppraisingService{

    @Autowired
    private SignDispaWorkRepo signDispaWorkRepo;


    @Autowired
    private SignRepo signRepo;

    /**
     * 查询办结项目信息
     * @param oDataObj
     * @return
     */
    @Override
    public PageModelDto<SignDispaWork> findEndProject(ODataObj oDataObj) {
        PageModelDto<SignDispaWork> pageModelDto = new PageModelDto<>();
        Criteria criteria = signDispaWorkRepo.getExecutableCriteria();
        criteria = oDataObj.buildFilterToCriteria(criteria);
        criteria.add(Restrictions.eq(SignDispaWork_.signState.getName() , Constant.EnumState.YES.getValue()));

        //统计总数
        Integer totalResult=((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        //处理分页
        criteria.setProjection(null);
        if(oDataObj.getSkip() > 0){
            criteria.setFirstResult(oDataObj.getSkip());
        }
        if(oDataObj.getTop() > 0){
            criteria.setMaxResults(oDataObj.getTop());
        }

        //处理orderby
        if(Validate.isString(oDataObj.getOrderby())){
            if(oDataObj.isOrderbyDesc()){
                criteria.addOrder(Property.forName(oDataObj.getOrderby()).desc());
            }else{
                criteria.addOrder(Property.forName(oDataObj.getOrderby()).asc());
            }
        }

        List<SignDispaWork> signDispaWorkList = criteria.list();

        pageModelDto.setValue(signDispaWorkList);


        return pageModelDto;
    }

    @Override
    public PageModelDto<SignDispaWork> findAppraisingProject(ODataObj oDataObj) {
        PageModelDto<SignDispaWork> pageModelDto = new PageModelDto<>();
        Criteria criteria = signDispaWorkRepo.getExecutableCriteria();
        criteria = oDataObj.buildFilterToCriteria(criteria);
        criteria.add(Restrictions.eq(SignDispaWork_.isAppraising.getName() , Constant.EnumState.YES.getValue()));

        //统计总数
        Integer totalResult=((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        //处理分页
        criteria.setProjection(null);
        if(oDataObj.getSkip() > 0){
            criteria.setFirstResult(oDataObj.getSkip());
        }
        if(oDataObj.getTop() > 0){
            criteria.setMaxResults(oDataObj.getTop());
        }

        //处理orderby
        if(Validate.isString(oDataObj.getOrderby())){
            if(oDataObj.isOrderbyDesc()){
                criteria.addOrder(Property.forName(oDataObj.getOrderby()).desc());
            }else{
                criteria.addOrder(Property.forName(oDataObj.getOrderby()).asc());
            }
        }

        List<SignDispaWork> signDispaWorkList = criteria.list();

        pageModelDto.setValue(signDispaWorkList);


        return pageModelDto;
    }

    @Override
    @Transactional
    public void updateIsAppraising(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("update "+ Sign.class.getSimpleName()+" set " + Sign_.isAppraising.getName() + "=:isAppraising " + " where " + Sign_.signid.getName()+"=:signId");
        hqlBuilder.setParam("isAppraising" , Constant.EnumState.YES.getValue());
        hqlBuilder.setParam("signId" , signId);
        signRepo.executeHql(hqlBuilder);

    }
}