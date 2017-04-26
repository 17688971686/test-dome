package cs.service;

import java.util.Map;

import cs.model.PageModelDto;
import cs.model.SignDto;
import cs.repository.odata.ODataObj;

public interface SignService{

	void createSign(SignDto signDto);

	PageModelDto<SignDto> get(ODataObj odataObj);

	void updateSign(SignDto signDto);

	void completeFillSign(SignDto signDto) throws Exception;

	Map<String, Object> initFillPageData(String signId);

	PageModelDto<SignDto> getFlow(ODataObj odataObj);

	void claimSignFlow(String taskId);
}
