package cs.service.project;

import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.project.SignDto;
import cs.model.sys.OrgDto;
import cs.repository.odata.ODataObj;

public interface SignService{

	void createSign(SignDto signDto);

	PageModelDto<SignDto> get(ODataObj odataObj);

	void updateSign(SignDto signDto);

	Map<String, Object> initFillPageData(String signId);

	void claimSignFlow(String taskId);
	
	List<OrgDto> selectSign(ODataObj odataObj);
	
	void deleteSign(String signid);

	public void deleteSigns(String[] signids);

	void startFlow(String signid) throws Exception;

	void stopFlow(String signid);

	void restartFlow(String signid);

	void endFlow(String signid);
	
	SignDto findById(String signid);
	
	
	//以下是新流程处理
	void startNewFlow(String signid) throws Exception;

	PageModelDto<SignDto> getPendingSign(ODataObj odataObj);

	ResultMsg dealFlow(ProcessInstance processInstance, FlowDto flowDto) throws Exception;	
}
