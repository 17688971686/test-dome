package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.monthly.MonthlyNewsletter_;
import cs.domain.project.AddSuppLetter;
import cs.domain.project.AddSuppLetter_;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.domain.project.WorkProgram;
import cs.model.PageModelDto;
import cs.model.monthly.MonthlyNewsletterDto;
import cs.model.project.AddSuppLetterDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.AddSuppLetterRepo;
import cs.repository.repositoryImpl.project.FileRecordRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;


/**
 * Description: 项目资料补充函 业务操作实现类
 * author: ldm
 * Date: 2017-8-1 18:05:57
 */
@Service
public class AddSuppLetterServiceImpl implements AddSuppLetterService {

    @Autowired
    private AddSuppLetterRepo addSuppLetterRepo;

    @Autowired
    private SignRepo signRepo;
    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private FileRecordRepo fileRecordRepo;
    /**
     * 保存补充资料函
     */
    @Override
    @Transactional
    public ResultMsg addSupp(AddSuppLetterDto addSuppLetterDto) {
        if(!Validate.isString(addSuppLetterDto.getBusinessId())){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，获取项目信息失败，请联系相关人员处理！");
        }
        AddSuppLetter addSuppLetter = null;
        Date now = new Date();
        if (Validate.isString(addSuppLetterDto.getId())) {
            addSuppLetter = addSuppLetterRepo.findById(addSuppLetterDto.getId());
            BeanCopierUtils.copyPropertiesIgnoreNull(addSuppLetterDto, addSuppLetter);
        } else {
            addSuppLetter = new AddSuppLetter();
            BeanCopierUtils.copyProperties(addSuppLetterDto, addSuppLetter);
            addSuppLetter.setId(UUID.randomUUID().toString());
            addSuppLetter.setCreatedBy(SessionUtil.getUserInfo().getId());
            addSuppLetter.setModifiedBy(SessionUtil.getUserInfo().getId());
        }
        addSuppLetter.setModifiedDate(now);
        addSuppLetter.setCreatedDate(now);

        addSuppLetterRepo.save(addSuppLetter);

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", addSuppLetterDto);
    }

    @Override
    public AddSuppLetterDto getbyId(String id) {
        AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(id);
        AddSuppLetterDto addSuppLetterDto = new AddSuppLetterDto();
        BeanCopierUtils.copyPropertiesIgnoreNull(addSuppLetter, addSuppLetterDto);
        return addSuppLetterDto;
    }


    /**
     * 获取最大拟稿编号
     *
     * @param dispaDate
     * @return
     */
    private int findCurMaxSeq(Date dispaDate) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max(" + AddSuppLetter_.fileSeq.getName() + ") from cs_add_suppLetter where " + AddSuppLetter_.disapDate.getName() + " between ");
        sqlBuilder.append(" to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') and to_date(:endTime,'yyyy-mm-dd hh24:mi:ss' )");
        sqlBuilder.setParam("beginTime", DateUtils.converToString(dispaDate, "yyyy") + "-01-01 00:00:00");
        sqlBuilder.setParam("endTime", DateUtils.converToString(dispaDate, "yyyy") + "-12-31 23:59:59");
        return addSuppLetterRepo.returnIntBySql(sqlBuilder);
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub

    }

    /**
     * 根据业务ID和业务类型初始化补充资料函信息
     * @param businessId
     * @param businessType
     * @return
     */
    @Override
    public AddSuppLetterDto initSuppLetter(String businessId, String businessType) {
        AddSuppLetterDto suppletterDto = new AddSuppLetterDto();
        if(Constant.BusinessType.SIGN.getValue().equals(businessType)){
            Sign sign = signRepo.findById(Sign_.signid.getName(),businessId);
            suppletterDto.setUserName(SessionUtil.getDisplayName());
            suppletterDto.setOrgName(SessionUtil.getUserInfo().getOrg() == null ? "" : SessionUtil.getUserInfo().getOrg().getName());
            suppletterDto.setBusinessId(businessId);
            suppletterDto.setBusinessType(businessType);
            suppletterDto.setTitle("《" + sign.getProjectname() + sign.getReviewstage() + "》");
            suppletterDto.setSecretLevel(sign.getSecrectlevel());
            suppletterDto.setMergencyLevel(sign.getUrgencydegree());
        }

        return suppletterDto;
    }

    /**
     * 生成文件字号
     */
    @Override
    @Transactional
    public ResultMsg fileNum(String id) {
        AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(AddSuppLetter_.id.getName(),id);
        if(Validate.isString(addSuppLetter.getFilenum())){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"该补充资料函已经生成过发文字号，不能重复生成！");
        }
        //获取拟稿最大编号
        int curYearMaxSeq = findCurMaxSeq(addSuppLetter.getDisapDate());
        String filenum = Constant.DISPATCH_PREFIX + "[" + DateUtils.converToString(addSuppLetter.getDisapDate(), "yyyy") + "]" + (curYearMaxSeq + 1);
        addSuppLetter.setFilenum(filenum);
        addSuppLetter.setFileSeq((curYearMaxSeq + 1));
        addSuppLetterRepo.save(addSuppLetter);

        //如果是收文，则要更新对应的资料信息(如果生成了文件字号，工作方案的是否补充资料函则显示为是，并且显示最新的日期。如果没有，则显示为否)
        if(Constant.BusinessType.SIGN.getValue().equals(addSuppLetter.getBusinessType())){
            Date now = new Date();
            Sign sign = signRepo.findById(Sign_.signid.getName(),addSuppLetter.getBusinessId());
            if(!Validate.isString(sign.getIsHaveSuppLetter()) || Constant.EnumState.NO.getValue().equals(sign.getIsHaveSuppLetter())){
                sign.setIsHaveSuppLetter(Constant.EnumState.YES.getValue());
                sign.setSuppLetterDate(now);
                signRepo.save(sign);
            }
            List<WorkProgram> wpList = workProgramRepo.findByIds(Sign_.signid.getName(),addSuppLetter.getBusinessId(),null);
            if(Validate.isList(wpList)){
                List<WorkProgram> saveList = new ArrayList<>();
                for(WorkProgram wp : wpList){
                    if(!Validate.isString(wp.getIsHaveSuppLetter()) || Constant.EnumState.NO.getValue().equals(wp.getIsHaveSuppLetter())){
                        wp.setIsHaveSuppLetter(Constant.EnumState.YES.getValue());
                        wp.setSuppLetterDate(now);
                        saveList.add(wp);
                    }
                }
                workProgramRepo.bathUpdate(saveList);
            }
        }
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"操作成功！",addSuppLetter);
    }

    /**
     * 根据业务ID查询拟补充资料函
     * @param businessId
     * @return
     */
    @Override
    public List<AddSuppLetterDto> initSuppList(String businessId) {
        HqlBuilder hql = HqlBuilder.create();
        hql.append(" from " + AddSuppLetter.class.getSimpleName() + " where " + AddSuppLetter_.businessId.getName() + " = :businessId ");
        hql.setParam("businessId", businessId);
        List<AddSuppLetter> suppletterlist = addSuppLetterRepo.findByHql(hql);
        List<AddSuppLetterDto> addSuppLetterDtos = new ArrayList<AddSuppLetterDto>();
        if (Validate.isList(suppletterlist)) {
            suppletterlist.forEach(a -> {
                AddSuppLetterDto addDto = new AddSuppLetterDto();
                BeanCopierUtils.copyProperties(a, addDto);
                addSuppLetterDtos.add(addDto);
            });
        }

        return addSuppLetterDtos;
    }

    /**
     * 根据业务ID判断是否有补充资料函
     * @param businessId
     * @return
     */
    @Override
    public boolean isHaveSuppLetter(String businessId) {
        return addSuppLetterRepo.isHaveSuppLetter(businessId);
    }

    /**
     * 保存中心文件稿纸
     */
	@Override
	@Transactional
	public ResultMsg saveMonthlyMultiyear(MonthlyNewsletterDto record) {
		Date now = new Date();
		AddSuppLetter addSuppLetter = new AddSuppLetter();
		BeanCopierUtils.copyProperties(record, addSuppLetter);
		addSuppLetter.setId(UUID.randomUUID().toString());
		addSuppLetter.setCreatedBy(SessionUtil.getUserInfo().getId());
		addSuppLetter.setModifiedBy(SessionUtil.getUserInfo().getId());
		addSuppLetter.setModifiedDate(now);
		addSuppLetter.setCreatedDate(now);
		addSuppLetter.setMonthlyStatus(Constant.EnumState.NO.getValue());
		addSuppLetterRepo.save(addSuppLetter);
		return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", addSuppLetter);
	}

	/**
	 * 获取年度月报简报列表数据
	 */
	@Override
	public PageModelDto<AddSuppLetterDto> monthlyMultiyearListData(ODataObj odataObj) {
		PageModelDto<AddSuppLetterDto> pageModelDto = new PageModelDto<AddSuppLetterDto>();
		Criteria criteria = addSuppLetterRepo.getExecutableCriteria();
		criteria = odataObj.buildFilterToCriteria(criteria);
		criteria.add(Restrictions.eq(MonthlyNewsletter_.monthlyType.getName(), EnumState.NO.getValue()));
		Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	    pageModelDto.setCount(totalResult);
	    criteria.setProjection(null);
	    if(odataObj.getSkip() > 0){
	    	criteria.setFirstResult(odataObj.getTop());
	    }
	    if(odataObj.getTop() > 0){
	    	criteria.setMaxResults(odataObj.getTop());
	    }
	    List<AddSuppLetter> addlist =criteria.list();
		List<AddSuppLetterDto> addDtos = new ArrayList<AddSuppLetterDto>(addlist == null ? 0 : addlist.size());
		
		if(addlist != null && addlist.size() > 0){
			addlist.forEach(x->{
				AddSuppLetterDto addDto = new AddSuppLetterDto();
				BeanCopierUtils.copyProperties(x, addDto);
				addDtos.add(addDto);
			});						
		}		
		pageModelDto.setValue(addDtos);	
		
		return pageModelDto;
	}

	/**
	 * 初始化中心文件稿纸
	 */
	@Override
	public AddSuppLetterDto initMonthlyMutilyear() {
		 AddSuppLetterDto suppletterDto = new AddSuppLetterDto();
	     suppletterDto.setUserName(SessionUtil.getLoginName());
	      suppletterDto.setOrgName(SessionUtil.getUserInfo().getOrg() == null ? "" : SessionUtil.getUserInfo().getOrg().getName());
		return suppletterDto;
	}



}