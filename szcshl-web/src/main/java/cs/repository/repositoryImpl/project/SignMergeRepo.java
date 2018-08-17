package cs.repository.repositoryImpl.project;

import cs.domain.project.SignMerge;
import cs.repository.IRepository;

import java.util.List;

public interface SignMergeRepo extends IRepository<SignMerge, String> {

    /**
     * 判断是否有合并
     * @param signId
     * @param mergeType
     * @return
     */
    boolean isHaveMerge(String signId,String mergeType);

    /**
     * 判断是否是关联项目
     * @param signId
     * @param mergeType
     * @return
     */
    boolean checkIsMerege(String signId, String mergeType);

    /**
     * 根据类型，查询合并信息
     * @param signId
     * @param mergeType
     * @return
     */
    List<SignMerge> findByType(String signId, String mergeType);
}
