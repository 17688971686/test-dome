package cs.repository.repositoryImpl.project;

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



    void updateDispatchDoc(DispatchDocDto dispatchDocDto,String isMain);
}
