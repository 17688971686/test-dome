package cs.service.reviewProjectAppraise;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.project.*;
import cs.model.PageModelDto;
import cs.model.project.AppraiseReportDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.SignDispaWorkRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.reviewProjectAppraise.AppraiseRepo;
import org.apache.regexp.RE;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 优秀评审项目
 * Author: mcl
 * Date: 2017/9/23 16:10
 */
@Service
public class AppraiseServiceImpl implements AppraiseService {

    @Autowired
    private SignDispaWorkRepo signDispaWorkRepo;

    @Autowired
    private AppraiseRepo appraiseRepo;


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
        criteria.add(Restrictions.eq(SignDispaWork_.mUserName.getName() , SessionUtil.getDisplayName()));

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

    /**
     * 查询优秀评审报告列表
     * @param oDataObj
     * @return
     */
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

    /**
     * 初始化 申请人
     * @return
     */
    @Override
    public AppraiseReportDto initProposer() {

        AppraiseReportDto appraiseReportDto = new AppraiseReportDto();
        appraiseReportDto.setProposerName(SessionUtil.getDisplayName());
        return appraiseReportDto;
    }

    /**
     * 保存申请信息
     * @param appraiseReportDto
     */
    @Override
    @Transactional
    public void saveApply(AppraiseReportDto appraiseReportDto) {

        AppraiseReport appraiseReport = new AppraiseReport();
        BeanCopierUtils.copyProperties(appraiseReportDto , appraiseReport);
        appraiseReport.setId(UUID.randomUUID().toString());
        appraiseReport.setCreatedBy(SessionUtil.getDisplayName());
        Date now = new Date();
        appraiseReport.setCreatedDate(now);
        appraiseReport.setModifiedBy(SessionUtil.getDisplayName());
        appraiseReport.setModifiedDate(now);
        if(SessionUtil.getUserInfo().getOrg() != null && SessionUtil.getUserInfo().getOrg().getOrgDirectorName()!= null){
            appraiseReport.setMinisterName(SessionUtil.getUserInfo().getOrg().getOrgDirectorName());//部长
        }else{
            appraiseReport.setMinisterName(SessionUtil.getDisplayName());
        }

        appraiseReport.setGeneralConductorName(Constant.GENERALCONDUTOR);//综合部处理人

        appraiseReport.setApproveStatus(Constant.EnumState.NO.getValue());//默认审批环节为：未审批

        appraiseRepo.save(appraiseReport);


        //修改收文 状态
        HqlBuilder hql = HqlBuilder.create();
        hql.append("update " +Sign.class.getSimpleName() + " set " + Sign_.isAppraising.getName() + "=:isAppraising");
        hql.append(" where " + Sign_.signid.getName() + "=:signId");
        hql.setParam("signId" , appraiseReportDto.getSignId());
        hql.setParam("isAppraising" , Constant.EnumState.PROCESS.getValue());//修改收文为：审核中
        signRepo.executeHql(hql);
    }

    /**
     * 查询优秀评审报告审批列表
     * @param oDataObj
     * @return
     */
    @Override
    public PageModelDto<AppraiseReportDto> getAppraiseReport(ODataObj oDataObj) {
        PageModelDto<AppraiseReportDto> pageModelDto = new PageModelDto<>();
        Criteria criteria = appraiseRepo.getExecutableCriteria();
        criteria = oDataObj.buildFilterToCriteria(criteria);
        boolean b = false;
        if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())
                || SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue())){//部长
            criteria.add(Restrictions.eq(AppraiseReport_.ministerName.getName() , SessionUtil.getDisplayName()));
            criteria.add(Restrictions.eq(AppraiseReport_.approveStatus.getName() , Constant.EnumState.NO.getValue()));
            b=true;
        }else if(Constant.GENERALCONDUTOR.equals(SessionUtil.getDisplayName())){//综合部
            criteria.add(Restrictions.eq(AppraiseReport_.generalConductorName.getName() , SessionUtil.getDisplayName()));
            criteria.add(Restrictions.eq(AppraiseReport_.approveStatus.getName() , Constant.EnumState.PROCESS.getValue()));
            b=true;
        }
        if(b) {
            Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

            pageModelDto.setCount(totalResult);

            criteria.setProjection(null);
            if (oDataObj.getSkip() > 0) {
                criteria.setFirstResult(oDataObj.getSkip());
            }
            if (oDataObj.getTop() > 0) {
                criteria.setMaxResults(oDataObj.getTop());
            }

            if (Validate.isString(oDataObj.getOrderby())) {
                if (oDataObj.isOrderbyDesc()) {
                    criteria.addOrder(Property.forName(oDataObj.getOrderby()).desc());
                } else {
                    criteria.addOrder(Property.forName(oDataObj.getOrderby()).asc());
                }
            }
            List<AppraiseReport> appraiseRepoList = criteria.list();
            List<AppraiseReportDto> appraiseReportDtoList = new ArrayList<>();
            for (AppraiseReport appraiseReport : appraiseRepoList) {
                AppraiseReportDto appraiseReportDto = new AppraiseReportDto();
                BeanCopierUtils.copyProperties(appraiseReport, appraiseReportDto);
                appraiseReportDtoList.add(appraiseReportDto);
            }
            pageModelDto.setValue(appraiseReportDtoList);
        }
        return pageModelDto;
    }

    /**
     * 通过id获取优秀评审报告的信息
     * @param id
     * @return
     */
    @Override
    public AppraiseReportDto getAppraiseById(String id) {
        AppraiseReport appraiseReport = appraiseRepo.findById(AppraiseReport_.id.getName() , id);
        AppraiseReportDto appraiseReportDto = new AppraiseReportDto();
        BeanCopierUtils.copyProperties(appraiseReport, appraiseReportDto);
        return appraiseReportDto;
    }

    /**
     * 保存审批意见
     * @param appraiseReportDto
     */
    @Override
    @Transactional
    public void saveApprove(AppraiseReportDto appraiseReportDto) {

        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("update " + AppraiseReport.class.getSimpleName()+" set " );
        hqlBuilder.append(" " + AppraiseReport_.approveStatus.getName() + "=:approveStatus");
        if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())
                || SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue())){
            hqlBuilder.append(" , " + AppraiseReport_.ministerOpinion.getName() + "=:ministerOption");
            hqlBuilder.setParam("ministerOption" , appraiseReportDto.getMinisterOpinion());
            hqlBuilder.setParam("approveStatus" , Constant.EnumState.PROCESS.getValue());

        }else if(Constant.GENERALCONDUTOR.equals(SessionUtil.getDisplayName())){

            hqlBuilder.append(" , " + AppraiseReport_.generalConductorOpinion.getName() + "=:generalConductorOpinion");

            //修改收文 状态
            HqlBuilder hql = HqlBuilder.create();
            hql.append("update " +Sign.class.getSimpleName() + " set " + Sign_.isAppraising.getName() + "=:isAppraising");
            hql.append(" where " + Sign_.signid.getName() + "=:signId");
            hql.setParam("signId" , appraiseReportDto.getSignId());

            if("8".equals(appraiseReportDto.getGeneralConductorOpinion())){//未通过

                hqlBuilder.setParam("generalConductorOpinion" , "未通过");

                hql.setParam("isAppraising" , Constant.EnumState.FORCE.getValue());//修改收文为：未通过
            }else {
                hqlBuilder.setParam("generalConductorOpinion", "通过");

                hql.setParam("isAppraising" , Constant.EnumState.YES.getValue());//修改收文为：通过
            }

            hqlBuilder.setParam("approveStatus", Constant.EnumState.YES.getValue());

            signRepo.executeHql(hql);

        }

        hqlBuilder.append(" where " + AppraiseReport_.id.getName() + "=:id");
        hqlBuilder.setParam("id" , appraiseReportDto.getId());

        appraiseRepo.executeHql(hqlBuilder);


    }


    /**
     * 优秀评审报告统计
     * @return
     */
    @Override
    @Transactional
    public int countApprove() {
        Criteria criteria = appraiseRepo.getExecutableCriteria();
        //部长审核
        if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())
                || SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue())){
            criteria.add(Restrictions.eq(AppraiseReport_.ministerName.getName() , SessionUtil.getDisplayName()));
            criteria.add(Restrictions.eq(AppraiseReport_.approveStatus.getName() , Constant.EnumState.NO.getValue()));
        }
        //综合部
        else if(Constant.GENERALCONDUTOR.equals(SessionUtil.getDisplayName())){
            criteria.add(Restrictions.eq(AppraiseReport_.generalConductorName.getName(),SessionUtil.getLoginName()));
            criteria.add(Restrictions.eq(ProjectStop_.approveStatus.getName(),Constant.EnumState.PROCESS.getValue()));
        }else{
            return 0;
        }
        return ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

    @Override
    public List<AppraiseReportDto> findHomeAppraise() {

        Criteria criteria = appraiseRepo.getExecutableCriteria();
        List<AppraiseReportDto> appraiseReportDtoList = new ArrayList<>();
        //部长审核
        if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())
                || SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue())){
            criteria.add(Restrictions.eq(AppraiseReport_.ministerName.getName() , SessionUtil.getDisplayName()));
            criteria.add(Restrictions.eq(AppraiseReport_.approveStatus.getName() , Constant.EnumState.NO.getValue()));
        }
        //综合部
        else if(Constant.GENERALCONDUTOR.equals(SessionUtil.getDisplayName())){
            criteria.add(Restrictions.eq(AppraiseReport_.generalConductorName.getName(),SessionUtil.getLoginName()));
            criteria.add(Restrictions.eq(ProjectStop_.approveStatus.getName(),Constant.EnumState.PROCESS.getValue()));
        }else{
            return appraiseReportDtoList;
        }
        criteria.setMaxResults(6);
        criteria.addOrder(Order.desc(AppraiseReport_.createdDate.getName()));
        List<AppraiseReport> appraiseReportList = criteria.list();
        for(AppraiseReport a : appraiseReportList){
            AppraiseReportDto appraiseReportDto = new AppraiseReportDto();
            BeanCopierUtils.copyProperties(a , appraiseReportDto);
            appraiseReportDtoList.add(appraiseReportDto);
        }
        return appraiseReportDtoList;
    }
}