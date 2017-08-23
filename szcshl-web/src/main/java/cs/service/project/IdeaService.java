package cs.service.project;

import java.util.List;

import cs.common.ResultMsg;
import cs.model.project.IdeaDto;
import cs.repository.odata.ODataObj;

public interface IdeaService {
	
	List<IdeaDto> get(ODataObj odataObj);
	void createIdea(IdeaDto ideaDto);
	void deleteIdea(String ideaId);
	List<IdeaDto> findMyIdea();

    ResultMsg bathSave(IdeaDto[] ideaDtos);
}
