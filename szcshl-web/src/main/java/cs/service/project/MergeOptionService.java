package cs.service.project;

public interface MergeOptionService {
    /**
     * 判断是否有合并
     * @param businessId
     * @param signId
     * @return
     */
    boolean isMerge(String businessId,String signId,String businessType);

    /**
     * 判断是否有关联
     * @param mainBusinessId
     * @return
     */
    boolean isHaveLink(String mainBusinessId,String businessType);
}
