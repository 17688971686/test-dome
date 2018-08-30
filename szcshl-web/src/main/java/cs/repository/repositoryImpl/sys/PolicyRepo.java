package cs.repository.repositoryImpl.sys;

import cs.common.ResultMsg;
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
     * @return
     */
    PageModelDto<PolicyDto> findFileById(String fileId ,  String skip, String size , String search);

    /**
     * 删除政策指标库
     * @param idStr
     */
    void deletePolicy(String idStr);

    /**
     * 通过ID获取政策指标库内容
     * @param policyId
     * @return
     */
    PolicyDto findByPolicyId(String policyId);


}
