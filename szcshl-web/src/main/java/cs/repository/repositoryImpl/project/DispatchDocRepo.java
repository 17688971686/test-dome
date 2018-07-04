package cs.repository.repositoryImpl.project;

import java.math.BigDecimal;
import java.util.List;

import cs.domain.project.DispatchDoc;
import cs.model.project.DispatchDocDto;
import cs.repository.IRepository;

public interface DispatchDocRepo extends IRepository<DispatchDoc,String>{
	@Deprecated
	List<DispatchDoc> findDispatchBySignId(String signId);

    void updateRWType(String reviewType, String isMain, String signIds);

    void updateIsRelatedState(String signId);

    /**
     * 获取最大发文编号
     * @param yearName
     * @param seqType
     * @return
     */
    int getMaxSeq(String yearName, String seqType);


    /**
     * 批复金额
     * @param disId
     * @param apprValue
     */
    void updateDisApprValue(String disId, BigDecimal apprValue);
}
