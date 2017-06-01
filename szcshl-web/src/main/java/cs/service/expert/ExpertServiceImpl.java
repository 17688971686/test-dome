package cs.service.expert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cs.common.utils.StringUtil;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
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
import cs.domain.expert.ExpertGlory;
import cs.domain.expert.Expert_;
import cs.domain.expert.ProjectExpe;
import cs.domain.expert.WorkExpe;
import cs.domain.sys.SysFile;
import cs.domain.sys.SysFile_;
import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertGloryDto;
import cs.model.expert.ExpertSelConditionDto;
import cs.model.expert.ProjectExpeDto;
import cs.model.expert.WorkExpeDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertGloryRepo;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ProjectExpeRepo;
import cs.repository.repositoryImpl.expert.WorkExpeRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import cs.service.sys.SysFileService;
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
	private ExpertGloryRepo expertGloryRepo;
	@Autowired
	private ICurrentUser currentUser;
    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private SignRepo signRepo;
    @Autowired
	private SysFileRepo sysFileRepo;
    
    @Autowired
    private SysFileService fileService;
	
	@Override
	public PageModelDto<ExpertDto> get(ODataObj odataObj) {
		List<Expert> listExpert = expertRepo.findByOdata(odataObj);
		PageModelDto<ExpertDto> pageModelDto = new PageModelDto<>();
		List<ExpertDto> listExpertDto = new ArrayList<>();
		for (Expert item : listExpert) {
			ExpertDto expertDto = new ExpertDto();
			BeanCopierUtils.copyProperties(item, expertDto);	
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
			//设置返回值
			expertDto.setExpertID(expert.getExpertID());
			expertRepo.save(expert);
			logger.info(String.format("添加专家,专家名为:%s", expert.getName()));
		} else {
			throw new IllegalArgumentException(String.format("身份证号为%s 的专家已存在,请重新输入", expertDto.getIdCard()));
		}
		return expertDto.getExpertID();
	}

	@Override
	@Transactional
	public void deleteExpert(String id) {		
		Expert expert = expertRepo.findById(id);
		if (expert != null) {
			List<WorkExpe> workList = expert.getWork();
			if(Validate.isList(workList)){
				for (WorkExpe workExpe : workList) {
					workExpeRepo.delete(workExpe);
				}
			}
			
			List<ProjectExpe> projectList = expert.getProject();
			if(Validate.isList(workList)){
				for (ProjectExpe projectExpe : projectList) {
					projectExpeRepo.delete(projectExpe);
				}
			}
			
			List<ExpertGlory> gloryList = expert.getGlory();
			if(Validate.isList(workList)){
				for (ExpertGlory expertGlory : gloryList) {
					expertGloryRepo.delete(expertGlory);
				}
			}
			
			Criteria criteria=sysFileRepo.getSession().createCriteria(SysFile.class);
			criteria.add(Restrictions.eq(SysFile_.businessId.getName(), id));
			List<SysFile> sysFileList=criteria.list();
			if(Validate.isList(sysFileList)){
				SysFile sysFile=sysFileList.get(0);
				//删除关联的头像
				fileService.deleteById(sysFile.getSysFileId());
			}
			
			expertRepo.delete(expert);
			logger.info(String.format("删除专家,专家名为:%s", expert.getName()));
		}			
	}
	
	@Override
	@Transactional
	public void deleteExpert(String[] ids) {
		for (String id : ids) {
			Criteria criteria=sysFileRepo.getSession().createCriteria(SysFile.class);
			criteria.add(Restrictions.eq(SysFile_.businessId.getName(), id));
			List<SysFile> sysFileList=criteria.list();
			if(sysFileList.size()!=0){
				SysFile sysFile=sysFileList.get(0);
				fileService.deleteById(sysFile.getSysFileId());
			}
			//删除关联的头像
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
				expertDto.setWork(workDtoList);
			}
			//项目经验
			if(expert.getProject() != null && expert.getProject().size() > 0){
				List<ProjectExpeDto> projectDtoList=new ArrayList<>(expert.getProject().size());
				(expert.getProject()).forEach(ep ->{
					ProjectExpeDto projectDto=new ProjectExpeDto();
					BeanCopierUtils.copyProperties(ep, projectDto);							
					projectDtoList.add(projectDto);
				});
				expertDto.setProject(projectDtoList);
			}			 			
			//专家聘书
			if(expert.getGlory() != null && expert.getGlory().size() > 0){
				List<ExpertGloryDto> gloryDtoList=new ArrayList<>(expert.getGlory().size());
				(expert.getGlory()).forEach(ep ->{
					ExpertGloryDto gloryDto=new ExpertGloryDto();
					BeanCopierUtils.copyProperties(ep, gloryDto);							
					gloryDtoList.add(gloryDto);
				});
				expertDto.setGlory(gloryDtoList);
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
    public List<ExpertDto> findExpert(ExpertSelConditionDto[] epSelConditions) {
        String workProgramId = epSelConditions[0].getWorkProgramId();
        if(!Validate.isString(workProgramId)){
            return null;
        }
        HqlBuilder hqlBuilder = HqlBuilder.create();
        //1、专家规避：与项目建设单位相同单位的专家需要规避，同本项目方案编制单位的专家需要规避
        hqlBuilder.append(" select * from CS_EXPERT ep left join (select WP.ID ID, WP.BUILDCOMPANY bcp,WP.DESIGNCOMPANY dcp from CS_WORK_PROGRAM wp ");
        hqlBuilder.append(" where WP.ID = :workProgramId) lwp on (lwp.bcp = ep.COMPANY or lwp.dcp = ep.COMPANY) ");
        hqlBuilder.setParam("workProgramId",workProgramId);
        //2、关联本周已抽取2次或者本月已抽取四次的专家
        hqlBuilder.append(" left join ( SELECT TO_CHAR (er.REVIEWDATE, 'mm') mnum, COUNT (er.id) mcount,");
        hqlBuilder.append(" TO_CHAR (er.REVIEWDATE, 'iw') wnum, COUNT (er.id) wcount,EXPERTID");
        hqlBuilder.append(" FROM CS_EXPERT_REVIEW er GROUP BY TO_CHAR (er.REVIEWDATE, 'mm'),TO_CHAR (er.REVIEWDATE, 'iw'), EXPERTID ");
        hqlBuilder.append(" HAVING COUNT(TO_CHAR (er.REVIEWDATE, 'mm')) = 4  or COUNT(TO_CHAR (er.REVIEWDATE, 'iw')) = 2  ");
        hqlBuilder.append("  ) outep on ep.EXPERTID = outep.EXPERTID ");
        //3、排除本次已经选择的专家
        hqlBuilder.append(" left join CS_EXPERT_REVIEW erm on erm.EXPERTID = ep.EXPERTID and erm.WORKPROGRAMID = :workProgramId2 ");
        hqlBuilder.setParam("workProgramId2",workProgramId);

        hqlBuilder.append(" where (ep.STATE = :state1 or ep.STATE = :state2 ) ");
        hqlBuilder.setParam("state1",EnumExpertState.OFFICIAL.getValue()).setParam("state2",EnumExpertState.ALTERNATIVE.getValue());
        hqlBuilder.append(" and lwp.ID is null " );    //排除单位相同的专家
        hqlBuilder.append(" and outep.EXPERTID is null ");  //排除已满抽取次数的专家
        hqlBuilder.append(" and ERM.EXPERTID is null  ");   //排除本次已经选择的专家

        //拼接专家抽取条件
        int totalLength = epSelConditions.length;
        List<String> jorBig = new ArrayList<>(totalLength),jorSmall = new ArrayList<>(totalLength),rttype =new ArrayList<>(totalLength);
        for(ExpertSelConditionDto obj:epSelConditions){
            if(Validate.isString(obj.getMaJorBig()) && !jorBig.contains(obj.getMaJorBig()) ){
                jorBig.add(obj.getMaJorBig());
            }
            if(Validate.isString(obj.getMaJorSmall()) && !jorSmall.contains(obj.getMaJorSmall())){
                jorSmall.add(obj.getMaJorSmall());
            }
            if(Validate.isString(obj.getExpeRttype())  && !rttype.contains(obj.getExpeRttype())){
                rttype.add(obj.getExpeRttype());
            }
        }
        if(jorBig.size() == 1){
            hqlBuilder.append(" and ep.MAJORBIG = :maJorBig ").setParam("maJorBig",jorBig.get(0));
        }else if(jorBig.size() > 1){
            hqlBuilder.append(" and ep.MAJORBIG in ( ");
            for(int i=0,l=jorBig.size();i<l;i++){
                if(i==0){
                    hqlBuilder.append(" :jorBig"+i);
                }else{
                    hqlBuilder.append(", :jorBig"+i);
                }
                hqlBuilder.setParam("jorBig"+i, jorBig.get(i));
            }
            hqlBuilder.append(" ) ");
        }

        if(jorSmall.size() == 1){
            hqlBuilder.append(" and ep.MAJORSMALL = :maJorSmall ").setParam("maJorSmall",jorSmall.get(0));
        }else if(jorSmall.size() > 1){
            hqlBuilder.append(" and ep.MAJORSMALL in ( ");
            for(int i=0,l=jorSmall.size();i<l;i++){
                if(i==0){
                    hqlBuilder.append(" :jorSamll"+i);
                }else{
                    hqlBuilder.append(", :jorSamll"+i);
                }
                hqlBuilder.setParam("jorSamll"+i, jorSmall.get(i));
            }
            hqlBuilder.append(" ) ");
        }

        if(rttype.size() == 1){
            hqlBuilder.append(" and ep.EXPERTTYPE = :rttype ").setParam("rttype",rttype.get(0));
        }else if(rttype.size() > 1){
            hqlBuilder.append(" and ep.EXPERTTYPE in ( ");
            for(int i=0,l=rttype.size();i<l;i++){
                if(i==0){
                    hqlBuilder.append(" :rttype"+i);
                }else{
                    hqlBuilder.append(", :rttype"+i);
                }
                hqlBuilder.setParam("rttype"+i, rttype.get(i));
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
	public Integer countExpert(ExpertSelConditionDto epSelCondition) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select count(ep.EXPERTID) from CS_EXPERT ep left join (select WP.ID ID, WP.BUILDCOMPANY bcp,WP.DESIGNCOMPANY dcp from CS_WORK_PROGRAM wp ");
        hqlBuilder.append(" where WP.ID = :workProgramId) lwp on (lwp.bcp = ep.COMPANY or lwp.dcp = ep.COMPANY) ");
        hqlBuilder.setParam("workProgramId",epSelCondition.getWorkProgramId());
        //2、关联本周已抽取2次或者本月已抽取四次的专家
        hqlBuilder.append(" left join ( SELECT TO_CHAR (er.REVIEWDATE, 'mm') mnum, COUNT (er.id) mcount,");
        hqlBuilder.append(" TO_CHAR (er.REVIEWDATE, 'iw') wnum, COUNT (er.id) wcount,EXPERTID");
        hqlBuilder.append(" FROM CS_EXPERT_REVIEW er GROUP BY TO_CHAR (er.REVIEWDATE, 'mm'),TO_CHAR (er.REVIEWDATE, 'iw'), EXPERTID ");
        hqlBuilder.append(" HAVING COUNT(TO_CHAR (er.REVIEWDATE, 'mm')) = 4  or COUNT(TO_CHAR (er.REVIEWDATE, 'iw')) = 2  ");
        hqlBuilder.append("  ) outep on ep.EXPERTID = outep.EXPERTID ");
        //3、排除本次已经选择的专家
        hqlBuilder.append(" left join CS_EXPERT_REVIEW erm on erm.EXPERTID = ep.EXPERTID and erm.WORKPROGRAMID = :workProgramId2 ");
        hqlBuilder.setParam("workProgramId2",epSelCondition.getWorkProgramId());

        hqlBuilder.append(" where (ep.STATE = :state1 or ep.STATE = :state2 ) ");
        hqlBuilder.setParam("state1",EnumExpertState.OFFICIAL.getValue()).setParam("state2",EnumExpertState.ALTERNATIVE.getValue());
        hqlBuilder.append(" and lwp.ID is null " );    //排除单位相同的专家
        hqlBuilder.append(" and outep.EXPERTID is null ");  //排除已满抽取次数的专家
        hqlBuilder.append(" and ERM.EXPERTID is null  ");   //排除本次已经选择的专家

        //加上选择的条件
        //突出专业，大类
        if(Validate.isString(epSelCondition.getMaJorBig())){
            hqlBuilder.append(" and ep.MAJORBIG = :maJorBig ").setParam("maJorBig",epSelCondition.getMaJorBig());
        }
        //突出专业，小类
        if(Validate.isString(epSelCondition.getMaJorSmall())){
            hqlBuilder.append(" and ep.MAJORSMALL = :maJorSmall ").setParam("maJorSmall",epSelCondition.getMaJorSmall());
        }
        //专家类型
        if(Validate.isString(epSelCondition.getExpeRttype())){
            hqlBuilder.append(" and ep.EXPERTTYPE = :rttype ").setParam("rttype",epSelCondition.getExpeRttype());
        }
		return expertRepo.countBySql(hqlBuilder);
	}

}
