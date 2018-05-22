package cs.service.expert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.expert.WorkExpe_;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.utils.BeanCopierUtils;
import cs.domain.expert.WorkExpe;
import cs.model.expert.WorkExpeDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.WorkExpeRepo;
import cs.service.sys.UserServiceImpl;

@Service
public class WorkExpeServiceImpl implements WorkExpeService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
    private ExpertRepo expertRepo;
	@Autowired
    private WorkExpeRepo workExpeRepo;
	
	@Override
	public List<WorkExpeDto> getWork(ODataObj odataObj) {
		List<WorkExpe> listWork = workExpeRepo.findByOdata(odataObj);
		List<WorkExpeDto> listWorktDto = null;
		if(listWork != null && listWork.size() > 0){
			listWorktDto = new ArrayList<>(listWork.size());
			for (WorkExpe item : listWork) {
				WorkExpeDto workDto = new WorkExpeDto();
				BeanCopierUtils.copyProperties(item, workDto);
				listWorktDto.add( workDto);
			}
		}		
		logger.info("查询工作经验");
		return listWorktDto;
	}

	/**
	 * 保存工作经历
	 * @param workExpeDto
	 * @return
	 */
	@Override
	@Transactional
	public ResultMsg saveWorkExpe(WorkExpeDto workExpeDto) {
		WorkExpe work = new WorkExpe();
        Date now = new Date();
		if(!Validate.isString(workExpeDto.getWeID())){
            workExpeDto.setWeID(UUID.randomUUID().toString());
            workExpeDto.setCreatedBy(SessionUtil.getLoginName());
            workExpeDto.setCreatedDate(now);
		}
        workExpeDto.setModifiedBy(SessionUtil.getDisplayName());
        workExpeDto.setModifiedDate(now);

		BeanCopierUtils.copyProperties(workExpeDto, work);
		work.setExpert(expertRepo.findById(workExpeDto.getExpertID()));
		workExpeRepo.save(work);

		return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"保存成功！",workExpeDto);
	}

	/**
	 * 删除专家工作经历信息
	 * @param ids
	 */
	@Override
	@Transactional
	public void deleteWork(String ids) {
		workExpeRepo.deleteById(WorkExpe_.weID.getName(),ids);
	}
	
	@Override
	@Transactional
	public void updateWork(WorkExpeDto workExpeDto) {
		WorkExpe workExpe = workExpeRepo.findById(workExpeDto.getWeID());
		BeanCopierUtils.copyPropertiesIgnoreNull(workExpeDto, workExpe);
		workExpeRepo.save(workExpe);
		logger.info(String.format("更新工作经验,单位名称为:%s", workExpeDto.getCompanyName()));
	}
}
