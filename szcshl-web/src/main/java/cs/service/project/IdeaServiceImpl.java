package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.domain.project.Idea;
import cs.model.project.IdeaDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.IdeaRepo;

@Service
public class IdeaServiceImpl implements IdeaService{
	
	@Autowired
	private ICurrentUser currentUser;
	
	@Autowired
	private IdeaRepo ideaRepo;

	@Override
	@Transactional
	public void createIdea(IdeaDto ideaDto) {
		
		Idea idea=new Idea();
		BeanCopierUtils.copyProperties(ideaDto, idea);
		idea.setIdeaID(UUID.randomUUID().toString());
		idea.setCreatedDate(new Date());
		idea.setCreatedBy(currentUser.getLoginName());
		idea.setModifiedDate(new Date());
		idea.setModifiedBy(currentUser.getLoginName());
		ideaRepo.save(idea);
		
	}

	@Override
	public List<IdeaDto> get(ODataObj odataObj) {
		
		List<Idea> ideaList=ideaRepo.findByOdata(odataObj);
		
		List<IdeaDto> ideaDtoList=new ArrayList<IdeaDto>();
		for(Idea idea:ideaList){
			
			if(idea.getCreatedBy().equals(currentUser.getLoginName())){
				IdeaDto ideaDto=new IdeaDto();
				BeanCopierUtils.copyProperties(idea, ideaDto);
				ideaDtoList.add(ideaDto);
			}
			
		}
		
		return ideaDtoList;
	}

}
