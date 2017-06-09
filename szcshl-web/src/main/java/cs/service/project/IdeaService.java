package cs.service.project;

import java.util.List;

import cs.model.project.IdeaDto;
import cs.repository.odata.ODataObj;

public interface IdeaService {
	
	public List<IdeaDto> get(ODataObj odataObj);
	public void createIdea(IdeaDto ideaDto);
	
	public void deleteIdea(String ideaId);

}
