package cs.service.expert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cs.model.expert.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant.EnumExpertState;
import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.NumIncreaseUtils;
import cs.common.utils.Validate;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertType;
import cs.domain.expert.Expert_;
import cs.domain.expert.ProjectExpe;
import cs.domain.expert.WorkExpe;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ProjectExpeRepo;
import cs.repository.repositoryImpl.expert.WorkExpeRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.service.sys.UserServiceImpl;

@Service
public class ExpertServiceImpl implements ExpertService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Autowired
	private ExpertRepo expertRepo;
	@Autowired
	private WorkExpeRepo workExpeRepo;
	@Autowired
	private ProjectExpeRepo projectExpeRepo;
	@Autowired
	private ICurrentUser currentUser;
    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private SignRepo signRepo;
    
    @Autowired
    private ExpertTypeService expertTypeService;
		
	@Override
	public PageModelDto<ExpertDto> get(ODataObj odataObj) {
		List<Expert> listExpert = expertRepo.findByOdata(odataObj);
		PageModelDto<ExpertDto> pageModelDto = new PageModelDto<>();
		List<ExpertDto> listExpertDto = new ArrayList<>();
		for (Expert item : listExpert) {
			ExpertDto expertDto = new ExpertDto();
			BeanCopierUtils.copyProperties(item, expertDto);	
			
			List<ExpertTypeDto> expertTypes=new ArrayList<>();
			
			for(ExpertType expertType:item.getExpertTypeList()){
				
				ExpertTypeDto expertTypeDto=new ExpertTypeDto();
				BeanCopierUtils.copyProperties(expertType, expertTypeDto);
				expertTypes.add(expertTypeDto);
				
			}
			expertDto.setExpertTypeDtoList(expertTypes);
			listExpertDto.add(expertDto);
		}
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(listExpertDto);
		return pageModelDto;
	}		
		
	@Override
	@Transactional
	public String createExpert(ExpertDto expertDto) {
		List<Expert> list = expertRepo.findExpertByIdCard(expertDto.getIdCard());
		
		if (list == null || list.size() == 0) {// 重复专家查询
			Expert expert = new Expert();
			BeanCopierUtils.copyProperties(expertDto, expert);
			//设置默认属性
			expert.setState(EnumExpertState.AUDITTING.getValue());
			expert.setExpertID(UUID.randomUUID().toString());
			expert.setExpertNo(NumIncreaseUtils.getExpertNo());
			Date now = new Date();
			expert.setCreatedDate(now);
			expert.setCreatedBy(currentUser.getLoginName());
			expert.setModifiedBy(currentUser.getLoginName());
			expert.setModifiedDate(now);
			
			expertTypeService.saveExpertType(expertDto.getExpertTypeDtoList(), expert.getExpertID());
			
			//设置返回值
			expertDto.setExpertID(expert.getExpertID());
			expertRepo.save(expert);
			logger.info(String.format("添加专家,专家名为:%s", expert.getName()));
		} else {
			throw new IllegalArgumentException(String.format("身份证号为%s 的专家已存在,请重新输入", expertDto.getIdCard()));
		}
		return expertDto.getExpertID();
	}

    /**
     * 删除操作，实为更新专家状态为删除状态
     * @param id
     */
	@Override
	@Transactional
	public void deleteExpert(String id) {		
		Expert expert = expertRepo.findById(id);
		if (expert != null) {
			HqlBuilder hqlBuilder=HqlBuilder.create();
			
			hqlBuilder.append("update " + Expert.class.getSimpleName()+" set "+ Expert_.state.getName()+ "=:state where " +Expert_.expertID.getName()+"=:id");
			hqlBuilder.setParam("state",EnumExpertState.REMOVE.getValue()).setParam("id", id);
			expertRepo.executeHql(hqlBuilder);

			/*List<WorkExpe> workList = expert.getWork();
			for (WorkExpe workExpe : workList) {
				workExpeRepo.delete(workExpe);
			}
			List<ProjectExpe> projectList = expert.getProject();
			for (ProjectExpe projectExpe : projectList) {
				projectExpeRepo.delete(projectExpe);
			}
			expertRepo.delete(expert);
			logger.info(String.format("删除专家,专家名为:%s", expert.getName()));*/
		}			
	}
	
	@Override
	@Transactional
	public void deleteExpert(String[] ids) {
		for (String id : ids) {
			this.deleteExpert(id);
		}
		logger.info("删除专家");
	}
	
	@Override
	@Transactional
	public void updateAudit(String ids,String state) {	
		if(Validate.isString(ids)){
			HqlBuilder hqlBuilder = HqlBuilder.create();
			hqlBuilder.append(" update "+Expert.class.getSimpleName() +" set "+ Expert_.state.getName() + " = :state ");
			hqlBuilder.setParam("state", state);
			String[] idArr = ids.split(",");
	        if (idArr.length > 1) {
	        	hqlBuilder.append( " where "+Expert_.expertID.getName()+" in ( ");	  
	        	int totalL = idArr.length;
	        	for(int i=0;i<totalL;i++){
	        		if(i==totalL-1){
	        			hqlBuilder.append(" :id"+i).setParam("id"+i, idArr[i]);
	        		}else{
	        			hqlBuilder.append(" :id"+i+",").setParam("id"+i, idArr[i]);
	        		}	        		
	        	}
	        	hqlBuilder.append(" )");
	        } else {
	        	hqlBuilder.append( " where "+Expert_.expertID.getName()+" = :id ");
	        	hqlBuilder.setParam("id", ids);
	        }
	        expertRepo.executeHql(hqlBuilder);
	        logger.info("专家审核");
		}						
	}
	
	@Override
	@Transactional
	public void updateExpert(ExpertDto expertDto) {
		Expert expert = expertRepo.findById(expertDto.getExpertID());
		
		/*List<WorkExpeDto> workDtoList=expertDto.getWorkDto();
		List<WorkExpe> workList=new ArrayList<>();
		if(Validate.isList(workDtoList)){
			for (WorkExpeDto workExpeDto : workDtoList) {
				WorkExpe workExpe=new WorkExpe();
				BeanCopierUtils.copyPropertiesIgnoreNull(workExpeDto, workExpe);
				workList.add(workExpe);
			}
		}
		expert.setWork(workList);
		
		List<ProjectExpeDto> projectExpeDtoList=expertDto.getProjectDto();
		List<ProjectExpe> projectExpeList=new ArrayList<>();
		if(Validate.isList(projectExpeDtoList)){
			for (ProjectExpeDto projectExpeDto : projectExpeDtoList) {
				ProjectExpe projectExpe=new ProjectExpe();
				BeanCopierUtils.copyPropertiesIgnoreNull(projectExpeDto, projectExpe);
				projectExpeList.add(projectExpe);
			}
		}
		expert.setProject(projectExpeList);*/
		
		BeanCopierUtils.copyPropertiesIgnoreNull(expertDto, expert);
		
		expert.setModifiedDate(new Date());
		expert.setModifiedBy(currentUser.getLoginName());
		expertRepo.save(expert);
		logger.info(String.format("更新专家,专家名为:%s", expertDto.getName()));
	}

	@Override
	public ExpertDto findById(String id) {			
		Expert expert = expertRepo.findById(id);
		ExpertDto expertDto = new ExpertDto();
		if(expert != null){
			BeanCopierUtils.copyProperties(expert, expertDto);
			//工作经验
			if(expert.getWork() != null && expert.getWork().size() > 0){
				List<WorkExpeDto> workDtoList=new ArrayList<>(expert.getWork().size());
				expert.getWork().forEach(ew ->{
					WorkExpeDto workDto=new WorkExpeDto();
					BeanCopierUtils.copyProperties(ew, workDto);				
					workDtoList.add(workDto);
				});
				expertDto.setWorkDto(workDtoList);
			}
			//项目经验
			if(expert.getProject() != null && expert.getProject().size() > 0){
				List<ProjectExpeDto> projectDtoList=new ArrayList<>(expert.getProject().size());
				(expert.getProject()).forEach(ep ->{
					ProjectExpeDto projectDto=new ProjectExpeDto();
					BeanCopierUtils.copyProperties(ep, projectDto);							
					projectDtoList.add(projectDto);
				});
				expertDto.setProjectDto(projectDtoList);
			}
			//专家聘书
            if(expert.getExpertOfferList() != null && expert.getExpertOfferList().size() > 0){
                List<ExpertOfferDto> expertOfferList=new ArrayList<>(expert.getExpertOfferList().size());
                (expert.getExpertOfferList()).forEach(epo ->{
                    ExpertOfferDto expertOfferDto = new ExpertOfferDto();
                    BeanCopierUtils.copyProperties(epo, expertOfferDto);
                    expertOfferList.add(expertOfferDto);
                });
                expertDto.setExpertOfferDtoList(expertOfferList);
            }
		}
		return expertDto;
	}

	/**
	 * 查询重名专家
	 */
	@Override
	public List<ExpertDto> findAllRepeat() {	
		List<Expert> list = expertRepo.findAllRepeat();
		List<ExpertDto> dtoList = new ArrayList<ExpertDto>();
		if(list != null && list.size() > 0){
			list.forEach(el ->{
				ExpertDto expertDto = new ExpertDto();
				BeanCopierUtils.copyProperties(el, expertDto);
				dtoList.add(expertDto);
			});
		}
		return dtoList;
	}

    /**
     * 专家抽取(与统计sql语句基本相同，目前先不整合)
     * @param epSelConditions
     * @return
     */
    @Override
    public List<ExpertDto> findExpert(String workprogramId,String reviewId,ExpertSelConditionDto[] epSelConditions) {
        if(!Validate.isString(workprogramId) || epSelConditions == null || epSelConditions.length < 1){
            return null;
        }
		HqlBuilder hqlBuilder = HqlBuilder.create();
		hqlBuilder.append("select ep.* from CS_EXPERT ep ");
		//1、关联与工作方案相同单位的专家
        hqlBuilder.append(" left join (select WP.ID ID, WP.BUILDCOMPANY bcp,WP.DESIGNCOMPANY dcp from CS_WORK_PROGRAM wp ");
		hqlBuilder.append(" where WP.ID = :workProgramId) lwp on (lwp.bcp = ep.COMPANY or lwp.dcp = ep.COMPANY) ");
		hqlBuilder.setParam("workProgramId",workprogramId);
		//2、关联本周已抽取2次或者本月已抽取四次的专家
		hqlBuilder.append(" left join ( SELECT TO_CHAR (er.REVIEWDATE, 'mm') mnum, COUNT (er.id) mcount,");
		hqlBuilder.append(" TO_CHAR (er.REVIEWDATE, 'iw') wnum, COUNT (er.id) wcount,cde.EXPERTID as EXPERTID");
		hqlBuilder.append(" FROM CS_EXPERT_REVIEW er ,(SELECT CD.EXPERTREVIEWID, ES.EXPERTID  FROM CS_EXPERT_CONDITION cd, CS_EXPERT_SELECTED es ");
		hqlBuilder.append(" WHERE CD.ID = ES.CONDITIONID ) cde  WHERE cde.EXPERTREVIEWID = er.id ");
		hqlBuilder.append(" GROUP BY TO_CHAR (er.REVIEWDATE, 'mm'),TO_CHAR (er.REVIEWDATE, 'iw'), cde.EXPERTID ");
		hqlBuilder.append(" HAVING COUNT(TO_CHAR (er.REVIEWDATE, 'mm')) = 4  or COUNT(TO_CHAR (er.REVIEWDATE, 'iw')) = 2  ");
		hqlBuilder.append("  ) outep on ep.EXPERTID = outep.EXPERTID ");
		//3、排除本次已经选择的专家
		hqlBuilder.append(" left join (SELECT CD.EXPERTREVIEWID, ES.EXPERTID FROM CS_EXPERT_CONDITION cd, CS_EXPERT_SELECTED es WHERE CD.ID = ES.CONDITIONID and CD.EXPERTREVIEWID =:reviewId )");
		hqlBuilder.append(" ERM ON ERM.EXPERTID = EP.EXPERTID ");
		hqlBuilder.setParam("reviewId",reviewId);

		hqlBuilder.append(" where (ep.STATE = :state1 or ep.STATE = :state2 ) ");
		hqlBuilder.setParam("state1",EnumExpertState.OFFICIAL.getValue()).setParam("state2",EnumExpertState.ALTERNATIVE.getValue());
		hqlBuilder.append(" and lwp.ID is null " );    //排除单位相同的专家
		hqlBuilder.append(" and outep.EXPERTID is null ");  //排除已满抽取次数的专家
		hqlBuilder.append(" and ERM.EXPERTID is null  ");   //排除本次已经选择的专家

        //拼接专家抽取条件
        int totalLength = epSelConditions.length;
        //1、只有一个抽取条件的时候
        if(totalLength == 1){
            hqlBuilder.append(" and (select count(ept.ID) from CS_EXPERT_TYPE ept where ept.expertid = ep.expertid ");
            buildCondition(hqlBuilder,"ept",epSelConditions[0]);
            hqlBuilder.append(" ) > 0");

        //2、多个专家的抽取条件
        }else{
            hqlBuilder.append(" and ( ");
            for(int i=0;i<totalLength;i++){
                if(i > 0){
                    hqlBuilder.append(" or");
                }
                hqlBuilder.append("(select count(ept"+i+".ID) from CS_EXPERT_TYPE ept"+i+" where ept"+i+".expertid = ep.expertid ");
                buildCondition(hqlBuilder,"ept"+i,epSelConditions[i]);
                hqlBuilder.append(" ) > 0");
            }
            hqlBuilder.append(" ) ");
        }

        List<Expert> listExpert = expertRepo.findBySql(hqlBuilder);
        List<ExpertDto> listExpertDto = new ArrayList<>();
        if(listExpert != null && listExpert.size() > 0){
            for (Expert item : listExpert) {
                ExpertDto epDto = new ExpertDto();
                BeanCopierUtils.copyProperties(item, epDto);
                listExpertDto.add(epDto);
            }
        }
        return listExpertDto;
    }

	/**
	 * 根据抽取条件，统计符合条件的专家
	 * @param epSelCondition
	 * @return
	 */
	@Override
	public Integer countExpert(String workprogramId,String reviewId,ExpertSelConditionDto epSelCondition) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select count(ep.EXPERTID) from CS_EXPERT ep left join (select WP.ID ID, WP.BUILDCOMPANY bcp,WP.DESIGNCOMPANY dcp from CS_WORK_PROGRAM wp ");
        hqlBuilder.append(" where WP.ID = :workProgramId) lwp on (lwp.bcp = ep.COMPANY or lwp.dcp = ep.COMPANY) ");
        hqlBuilder.setParam("workProgramId",workprogramId);
        //2、关联本周已抽取2次或者本月已抽取四次的专家
        hqlBuilder.append(" left join ( SELECT TO_CHAR (er.REVIEWDATE, 'mm') mnum, COUNT (er.id) mcount,");
        hqlBuilder.append(" TO_CHAR (er.REVIEWDATE, 'iw') wnum, COUNT (er.id) wcount,cde.EXPERTID as EXPERTID");
        hqlBuilder.append(" FROM CS_EXPERT_REVIEW er ,(SELECT CD.EXPERTREVIEWID, ES.EXPERTID  FROM CS_EXPERT_CONDITION cd, CS_EXPERT_SELECTED es ");
		hqlBuilder.append(" WHERE CD.ID = ES.CONDITIONID ) cde  WHERE cde.EXPERTREVIEWID = er.id ");
		hqlBuilder.append(" GROUP BY TO_CHAR (er.REVIEWDATE, 'mm'),TO_CHAR (er.REVIEWDATE, 'iw'), cde.EXPERTID ");
        hqlBuilder.append(" HAVING COUNT(TO_CHAR (er.REVIEWDATE, 'mm')) = 4  or COUNT(TO_CHAR (er.REVIEWDATE, 'iw')) = 2  ");
        hqlBuilder.append("  ) outep on ep.EXPERTID = outep.EXPERTID ");
        //3、排除本次已经选择的专家
        hqlBuilder.append(" left join (SELECT CD.EXPERTREVIEWID, ES.EXPERTID FROM CS_EXPERT_CONDITION cd, CS_EXPERT_SELECTED es WHERE CD.ID = ES.CONDITIONID and CD.EXPERTREVIEWID =:reviewId )");
        hqlBuilder.append(" ERM ON ERM.EXPERTID = EP.EXPERTID ");
        hqlBuilder.setParam("reviewId",reviewId);

        hqlBuilder.append(" where (ep.STATE = :state1 or ep.STATE = :state2 ) ");
        hqlBuilder.setParam("state1",EnumExpertState.OFFICIAL.getValue()).setParam("state2",EnumExpertState.ALTERNATIVE.getValue());
        hqlBuilder.append(" and lwp.ID is null " );    //排除单位相同的专家
        hqlBuilder.append(" and outep.EXPERTID is null ");  //排除已满抽取次数的专家
        hqlBuilder.append(" and ERM.EXPERTID is null  ");   //排除本次已经选择的专家

        //加上选择的条件
		if(Validate.isString(epSelCondition.getMaJorBig()) || Validate.isString(epSelCondition.getMaJorSmall()) || Validate.isString(epSelCondition.getExpeRttype())){
            hqlBuilder.append(" and (select count(ept.ID) from CS_EXPERT_TYPE ept where ept.expertid = ep.expertid ");
            buildCondition(hqlBuilder,"ept",epSelCondition);
            hqlBuilder.append(" ) > 0");
		}

		return expertRepo.countBySql(hqlBuilder);
	}

    /**
     * 查询条件封装
     * @param hqlBuilder
     * @param alias
     * @param epSelCondition
     */
	private void buildCondition(HqlBuilder hqlBuilder,String alias,ExpertSelConditionDto epSelCondition){
        //突出专业，大类
        if(Validate.isString(epSelCondition.getMaJorBig())){
            hqlBuilder.append(" and "+alias+".maJorBig = :maJorBig ").setParam("maJorBig",epSelCondition.getMaJorBig());
        }
        //突出专业，小类
        if(Validate.isString(epSelCondition.getMaJorSmall())){
            hqlBuilder.append(" and "+alias+".maJorSmall = :maJorSmall ").setParam("maJorSmall",epSelCondition.getMaJorSmall());
        }
        //专家类型
        if(Validate.isString(epSelCondition.getExpeRttype())){
            hqlBuilder.append(" and "+alias+".expertType = :rttype ").setParam("rttype",epSelCondition.getExpeRttype());
        }
    }

	@Override
    @Transactional
	public void savePhone(byte[] bytes, String expertId) {
		Expert expert = expertRepo.findById(expertId);
		expert.setPhoto(bytes);
		expertRepo.save(expert);
	}

    @Override
    public byte[] findExpertPhoto(String expertId) {
        Expert expert = expertRepo.findByIds(Expert_.expertID.getName(),expertId,null).get(0);
        return expert.getPhoto();
    }

}
