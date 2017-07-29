package cs.service.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.HqlBuilder;
import cs.domain.project.ProjectStop;
import cs.domain.project.ProjectStop_;
import cs.domain.project.Sign_;
import cs.repository.repositoryImpl.project.ProjectStopRepo;

@Service
public class ProjectStopServiceImp implements ProjectStopService {
	@Autowired
	private ProjectStopRepo projectStopRepo;

	@Override
	@Transactional
	public List<ProjectStop> findProjectStopBySign(String signId) {
		
		HqlBuilder hqlBuilder=HqlBuilder.create();
		
		hqlBuilder.append("select ps from "+ProjectStop.class.getSimpleName()+" ps where ps."+ProjectStop_.sign.getName()+"."+Sign_.signid.getName()+"=:signId");
		hqlBuilder.setParam("signId", signId);
		List<ProjectStop> psList=projectStopRepo.findByHql(hqlBuilder);
		return psList;
	}

}
