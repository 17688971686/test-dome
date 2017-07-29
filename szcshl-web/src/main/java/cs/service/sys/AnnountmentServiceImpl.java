package cs.service.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.sys.Annountment;
import cs.domain.sys.Annountment_;
import cs.domain.sys.Org;
import cs.domain.sys.Org_;
import cs.domain.sys.User;
import cs.domain.sys.User_;
import cs.model.PageModelDto;
import cs.model.sys.AnnountmentDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.AnnountmentRepo;
import cs.repository.repositoryImpl.sys.OrgRepo;

@Service
public class AnnountmentServiceImpl implements AnnountmentService {

    @Autowired
    private AnnountmentRepo annountmentRepo;
    

    @Autowired
    private OrgRepo orgRepo;

    /* 
     * 获取个人发布的通知公告
     */
    @Override
    public PageModelDto<AnnountmentDto> findByCurUser(ODataObj odataobj) {
    	PageModelDto<AnnountmentDto> pageModelDto = new PageModelDto<>();
    	Criteria criteria=annountmentRepo.getExecutableCriteria();
    	criteria=odataobj.buildFilterToCriteria(criteria);
    	//创建人为当前用户
    	criteria.add(Restrictions.eq(Annountment_.createdBy.getName(), SessionUtil.getLoginName()));
    	//统计总数
    	Integer totalResult=((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
       pageModelDto.setCount(totalResult);
       //处理分页
       criteria.setProjection(null);
       if(odataobj.getSkip() > 0){
    	   criteria.setFirstResult(odataobj.getSkip());
       }
       if(odataobj.getTop() > 0){
    	   criteria.setMaxResults(odataobj.getTop());
       }
    	
       //处理orderby
       if(Validate.isString(odataobj.getOrderby())){
    	   if(odataobj.isOrderbyDesc()){
    		   criteria.addOrder(Property.forName(odataobj.getOrderby()).desc());
    	   }else{
    		   criteria.addOrder(Property.forName(odataobj.getOrderby()).asc());
    	   }
       }
       
    	List<Annountment> annList =criteria.list();
        List<AnnountmentDto> annountmentDtoList = new ArrayList<>();
        for (Annountment annountment : annList) {
            AnnountmentDto annDto = new AnnountmentDto();
            BeanCopierUtils.copyProperties(annountment, annDto);
            annountmentDtoList.add(annDto);
        }

        pageModelDto.setValue(annountmentDtoList);

        return pageModelDto;
    }



	/* 
	 * 获取所有已经发布的通知公告
	 */
	@Override
	public PageModelDto<AnnountmentDto> findByIssue(ODataObj odataobj) {
		PageModelDto<AnnountmentDto> pageModelDto=new PageModelDto<>();
		Criteria criteria = annountmentRepo.getExecutableCriteria();
		criteria.add(Restrictions.like(Annountment_.issue.getName(), Constant.EnumState.YES.getValue()));
		criteria=odataobj.buildFilterToCriteria(criteria);
		
		//统计总数
    	Integer totalResult=((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
       pageModelDto.setCount(totalResult);
       //处理分页
       criteria.setProjection(null);
       if(odataobj.getSkip() > 0){
    	   criteria.setFirstResult(odataobj.getSkip());
       }
       if(odataobj.getTop() > 0){
    	   criteria.setMaxResults(odataobj.getTop());
       }
    	
       //处理orderby
       if(Validate.isString(odataobj.getOrderby())){
    	   if(odataobj.isOrderbyDesc()){
    		   criteria.addOrder(Property.forName(odataobj.getOrderby()).desc());
    	   }else{
    		   criteria.addOrder(Property.forName(odataobj.getOrderby()).asc());
    	   }
       }
		List<Annountment> annountmentList=criteria.list();
		
		List<AnnountmentDto> annountmentDtoList=new ArrayList<>();
		for(Annountment annountment : annountmentList){
			AnnountmentDto annountmentDto = new AnnountmentDto();
			BeanCopierUtils.copyProperties(annountment, annountmentDto);
			annountmentDtoList.add(annountmentDto);
		}
		
		pageModelDto.setValue(annountmentDtoList);
		return pageModelDto;
	}


    
    @Override
    @Transactional
    public void createAnnountment(AnnountmentDto annountmentDto) {
        Annountment annountment = new Annountment();
        BeanCopierUtils.copyProperties(annountmentDto, annountment);
        Date now = new Date();
        //默认不排序和不置顶
        if(!Validate.isString(annountment.getIssue())){
            annountment.setIssue(Constant.EnumState.NO.getValue());
        }
        if(!Validate.isObject(annountment.getIsStick())){
            annountment.setIsStick(Integer.valueOf(Constant.EnumState.NO.getValue()));
        }else{
            if(Constant.EnumState.YES.getValue().equals(annountment.getIsStick())){
                annountment.setIssueDate(now);
            }
        }
        if(!Validate.isString(annountment.getAnId())){
            annountment.setAnId(null);
        }
        //已发布的要加上发布人和发布时间
        if(Constant.EnumState.YES.getValue().equals(annountment.getIssue())){
            annountment.setIssueDate(now);
            annountment.setIssueUser(SessionUtil.getLoginName());
        }
        annountment.setCreatedBy(SessionUtil.getLoginName());
        annountment.setCreatedDate(now);
        annountment.setModifiedBy(SessionUtil.getLoginName());
        annountment.setModifiedDate(now);
        annountmentRepo.save(annountment);
        annountmentDto.setAnId(annountment.getAnId());
    }

    @Override
    @Transactional
    public String findAnOrg() {
        String loginName = SessionUtil.getLoginName();
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select o from " + Org.class.getSimpleName() + " o where o." + Org_.id.getName() + "=(");
        hqlBuilder.append("select u." + User_.org.getName() + "." + Org_.id.getName() + " from " + User.class.getSimpleName() + " u where u." + User_.loginName.getName() + "=:loginName)");
        hqlBuilder.setParam("loginName", loginName);
        List<Org> orgList = orgRepo.findByHql(hqlBuilder);
        if (orgList != null && orgList.size() > 0) {
            return orgList.get(0).getName();
        } else {
            return null;
        }

    }

    @Override
    @Transactional
    public AnnountmentDto findAnnountmentById(String anId) {
        Annountment annountment = annountmentRepo.findById(Annountment_.anId.getName(),anId);
        AnnountmentDto annountmentDto = new AnnountmentDto();
        BeanCopierUtils.copyProperties(annountment, annountmentDto);
        return annountmentDto;
    }

    @Override
    @Transactional
    public void updateAnnountment(AnnountmentDto annountmentDto) {
        Annountment annountment = annountmentRepo.findById(Annountment_.anId.getName(),annountmentDto.getAnId());
        if (annountment != null) {
            BeanCopierUtils.copyPropertiesIgnoreNull(annountmentDto, annountment);
            annountment.setModifiedBy(SessionUtil.getLoginName());
            annountment.setModifiedDate(new Date());
        }
        annountmentRepo.save(annountment);
    }

    @Override
    @Transactional
    public void deleteAnnountment(String id) {
        annountmentRepo.deleteById(Annountment_.anId.getName(),id);
    }

    /**
     * 获取主页上的通知公告数据，最多6条数据
     * @return
     */
    @Override
    @Transactional
    public List<AnnountmentDto> getHomePageAnnountment() {
       /* HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select a from " + Annountment.class.getSimpleName() + " a order by " + Annountment_.isStick.getName() + " desc," + Annountment_.issueDate.getName() + " desc");
        List<Annountment> annList = annountmentRepo.findByHql(hqlBuilder);*/

        //通过Criteria查询
        Criteria criteria = annountmentRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(Annountment_.issue.getName(), Constant.EnumState.YES.getValue()));
        criteria.addOrder(Order.desc(Annountment_.isStick.getName())).addOrder(Order.desc(Annountment_.issueDate.getName()));
        criteria.setMaxResults(6);
        List<Annountment> annList = criteria.list();

        List<AnnountmentDto> annDtoList = new ArrayList<>();
        for (Annountment ann : annList) {
            AnnountmentDto annDto = new AnnountmentDto();
            BeanCopierUtils.copyProperties(ann, annDto);
            annDtoList.add(annDto);
        }
        return annDtoList;
    }

    /**
     * 上一篇
     * @param id
     * @return
     */
    @Override
    @Transactional
    public AnnountmentDto postAritle(String id) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("with res as(select cs_annountment.*,row_number() over(order by isstick desc,issueDate desc)t from cs_annountment)");
        hqlBuilder.append(" select res.* from res where t=(");
        hqlBuilder.append("select res.t-1 from res where res.anid=:id)");
        hqlBuilder.setParam("id", id);
        List<Annountment> annList = annountmentRepo.findBySql(hqlBuilder);
        AnnountmentDto annDto = new AnnountmentDto();
        if (annList.size() > 0) {
            BeanCopierUtils.copyProperties(annList.get(0), annDto);
        }
        return annDto;
    }

    /**
     * 下一篇
     * @param id
     * @return
     */
    @Override
    @Transactional
    public AnnountmentDto nextArticle(String id) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("with res as(select cs_annountment.*,row_number() over(order by isstick desc,issueDate desc)t from cs_annountment)");
        hqlBuilder.append(" select res.* from res where t=(");
        hqlBuilder.append("select res.t+1 from res where res.anid=:id)");
        hqlBuilder.setParam("id", id);
        List<Annountment> annList = annountmentRepo.findBySql(hqlBuilder);
        AnnountmentDto annDto = new AnnountmentDto();
        if (annList.size() > 0) {
            BeanCopierUtils.copyProperties(annList.get(0), annDto);
        }
        return annDto;
    }

    /**
     * 更改通知公告的发布状态
     * @param ids
     * @param issueState
     */
    @Override
    public void updateIssueState(String ids, String issueState) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update "+Annountment.class.getSimpleName()+" set "+Annountment_.issue.getName()+" =:issueState" );
        hqlBuilder.setParam("issueState",issueState);
        if(Constant.EnumState.YES.getValue().equals(issueState)){
            hqlBuilder.append(","+Annountment_.issueDate.getName()+"=sysdate ");
            hqlBuilder.append(","+Annountment_.issueUser.getName()+"= :issueUser ").setParam("issueUser",SessionUtil.getLoginName());
        }
        List<String> idList = StringUtil.getSplit(ids,",");
        if (idList != null && idList.size() > 1) {
            hqlBuilder.append(" where " + Annountment_.anId.getName() + " in ( ");
            int totalL = idList.size();
            for (int i = 0; i < totalL; i++) {
                if (i == totalL - 1) {
                    hqlBuilder.append(" :id" + i).setParam("id" + i, idList.get(i));
                } else {
                    hqlBuilder.append(" :id" + i + ",").setParam("id" + i, idList.get(i));
                }
            }
            hqlBuilder.append(" )");
        } else {
            hqlBuilder.append(" where " + Annountment_.anId.getName() + " = :id ");
            hqlBuilder.setParam("id", idList.get(0));
        }
        annountmentRepo.executeHql(hqlBuilder);
    }


}
