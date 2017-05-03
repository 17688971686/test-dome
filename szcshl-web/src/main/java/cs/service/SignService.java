package cs.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;

import cs.common.ResultMsg;
import cs.model.FlowDto;
import cs.model.OrgDto;
import cs.model.PageModelDto;
import cs.model.SignDto;
import cs.repository.odata.ODataObj;

public interface SignService{

	void createSign(SignDto signDto);

	PageModelDto<SignDto> get(ODataObj odataObj);

	void updateSign(SignDto signDto) throws Exception;

	void completeFillSign(SignDto signDto) throws Exception;

	Map<String, Object> initFillPageData(String signId);

	PageModelDto<SignDto> getFlow(ODataObj odataObj);

	void claimSignFlow(String taskId);
	
	ResultMsg dealSignFlow(ProcessInstance processInstance,FlowDto flowDto) throws Exception;

	List<OrgDto> selectSign(ODataObj odataObj);
	
	void deleteSign(String signid);

	public void deleteSigns(String[] signids);
}
