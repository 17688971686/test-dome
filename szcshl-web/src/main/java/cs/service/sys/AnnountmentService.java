package cs.service.sys;

import java.util.List;

import cs.model.PageModelDto;
import cs.model.sys.AnnountmentDto;
import cs.repository.odata.ODataObj;

public interface AnnountmentService {
	
	PageModelDto<AnnountmentDto> findByCurUser(ODataObj odataobj);
	
	
	void createAnnountment(AnnountmentDto annountmentDto);
	
	String findAnOrg();
	
	AnnountmentDto findAnnountmentById(String anId);
	
	void updateAnnountment(AnnountmentDto annountmentDto);
	
	
	void deleteAnnountment(String id);
	
	List<AnnountmentDto> getHomePageAnnountment();
	
	AnnountmentDto postAritle(String id);
	
	AnnountmentDto nextArticle(String id);

	void updateIssueState(String ids, String issueState);

	PageModelDto<AnnountmentDto> findByIssue(ODataObj odataobj);
}
