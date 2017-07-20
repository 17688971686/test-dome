package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.common.Constant;
import cs.common.ICurrentUser;
import cs.common.utils.DateUtils;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.project.ProjectStop;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.repository.repositoryImpl.project.ProjectStopRepo;
import cs.repository.repositoryImpl.project.SignRepo;

@Service
public class ProjectStopServiceImp implements ProjectStopService {

	@Autowired
	private ProjectStopRepo projectStopRepo;

	@Autowired
	private SignRepo signRepo;

	@Autowired
	private ICurrentUser currentUser;

	@Transactional
	@Override
	public void addProjectStop(String signid) {
		if (Validate.isString(signid)) {
			Date now = new Date();
			ProjectStop projectStop = new ProjectStop();
			projectStop.setPausetime(now);
			projectStop.setIspause(Constant.EnumState.STOP.getValue());
			projectStop.setStopid(UUID.randomUUID().toString());
			projectStop.setModifiedBy(currentUser.getLoginName());
			projectStop.setCreatedBy(currentUser.getLoginName());

			Sign sign = signRepo.findById(Sign_.signid.getName(), signid);
			if (sign != null && Validate.isString(sign.getSignid())) {
				sign.setIspause(Constant.EnumState.STOP.getValue());
				projectStop.setSign(sign);
			}

			projectStopRepo.save(projectStop);
		}
	}

	@Transactional
	@Override
	public void projectStart(String signid) {
		if (Validate.isString(signid)) {
			List<ProjectStop> pList = new ArrayList<>();
			Date now = new Date();
			//获取已暂停项目
			pList = projectStopRepo.getProjectStop(signid, Constant.EnumState.STOP.getValue());
			if (!pList.isEmpty()) {
				ProjectStop projectStop = pList.get(0);
				projectStop.setStartTime(now);
				long longtime = DateUtils.daysBetween(now, projectStop.getPausetime());
				projectStop.setPausedays((float) longtime);
				projectStop.setIspause(Constant.EnumState.PROCESS.getValue());

				Sign sign = signRepo.findById(Sign_.signid.getName(), signid);
				if (sign != null && Validate.isString(sign.getSignid())) {
					sign.setIspause(Constant.EnumState.PROCESS.getValue());
					projectStop.setSign(sign);
				}
				projectStopRepo.save(projectStop);
			}
		}
	}

}
