package cs.service.expert;

import java.util.List;

import cs.model.expert.ExpertGloryDto;
import cs.repository.odata.ODataObj;

public interface ExpertGloryService {
	 List<ExpertGloryDto> getGlory(ODataObj odataObj);
	 void createGlory(ExpertGloryDto expertGloryDto);
	 void deleteGlory(String id);
	 void deleteGlory(String[] ids);
	 void updateGlory(ExpertGloryDto expertGloryDto);
}
