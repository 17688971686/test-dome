package cs.service.project;

import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;

import cs.common.ResultMsg;
import cs.domain.sys.Org;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.project.SignDto;
import cs.model.sys.OrgDto;
import cs.repository.odata.ODataObj;

public interface SignService{

	void createSign(SignDto signDto);

	PageModelDto<SignDto> get(ODataObj odataObj);

	void updateSign(SignDto signDto) throws Exception;

	Map<String, Object> initFillPageData(String signId);

	PageModelDto<SignDto> getFlow(ODataObj odataObj);

	void claimSignFlow(String taskId);
	
	ResultMsg dealSignFlow(ProcessInstance processInstance,FlowDto flowDto) throws Exception;

	List<OrgDto> selectSign(ODataObj odataObj);
	
	void deleteSign(String signid);

	public void deleteSigns(String[] signids);

	void startFlow(String signid) throws Exception;

	void stopFlow(String signid);

	void restartFlow(String signid);

	void endFlow(String signid);
	
	SignDto findById(String signid);
}
