package cs.service.project;

import cs.common.ResultMsg;
import cs.model.project.DispatchDocDto;
import cs.model.project.SignDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DispatchDocService {

    ResultMsg save(DispatchDocDto dispatchDocDto);

    Map<String, Object> initDispatchData(String signId);

    /**
     * 生成发文编号
     * @param signId
     * @param dispatchId
     * @return
     */
    ResultMsg fileNum(String signId,String dispatchId);

    DispatchDocDto initDispatchBySignId(String signId);

    /**
     * 生成发文模板
     * @param signId
     * @return
     */
    ResultMsg createDisPatchTemplate (String signId);

    /**
     * 修改批复金额
     * @param  disId        发文ID
     * @return apprValue    批复金额
     */
    void updateDisApprValue(String disId,BigDecimal apprValue);


    /**
     * 根据ID查询
     * @param dictId
     * @return
     */
    DispatchDocDto findById(String dictId);

    /**
     * 根据主项目ID查询合并发文次项目信息
     * @param mainSignId
     * @return
     */
    List<DispatchDocDto> findMergeDisInfo(String mainSignId);
}
