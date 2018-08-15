package cs.repository.repositoryImpl.sys;

import cs.domain.sys.Policy;
import cs.model.PageModelDto;
import cs.model.sys.PolicyDto;
import cs.repository.IRepository;
import cs.repository.odata.ODataObj;

import java.util.List;

/**
 * Created by zsl
 * 2017/8/21
 */
public interface PolicyRepo extends IRepository<Policy,String> {

    /**
     * 初始化政策指标库的所有文件夹
     * @return
     */
    List<PolicyDto> initFileFolder();

    /**
     * 通过Id获取文件
     * @param oDataObj
     * @param standardId
     * @return
     */
    PageModelDto<PolicyDto> findFileById(ODataObj oDataObj , String standardId);


}
