package cs.service.project;

import java.util.Map;

import cs.common.ResultMsg;
import cs.domain.project.AddSuppLetter;
import cs.model.PageModelDto;
import cs.model.project.AddSuppLetterDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 项目资料补充函 业务操作接口
 * author: ldm
 * Date: 2017-8-1 18:05:57
 */
public interface AddSuppLetterService {
    
    AddSuppLetterDto getbyId(String id);
    
    void addSupp(AddSuppLetterDto addSuppLetterDto);
    
    AddSuppLetterDto initSupp(String signid,String id);

	void delete(String id);
	
	void updateSupp(AddSuppLetterDto addSuppLetterDto);
	
    ResultMsg fileNum(String id);

}
