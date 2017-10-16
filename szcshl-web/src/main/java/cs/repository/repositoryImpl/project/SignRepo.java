package cs.repository.repositoryImpl.project;

import cs.domain.project.Sign;
import cs.repository.IRepository;

import java.util.List;

public interface SignRepo extends IRepository<Sign, String> {

    boolean updateSignState(String signId,String state);

    boolean updateSignProcessState(String signId,Integer processState);

    Sign findByFilecode(String filecode);

    /**
     * 判断是否是合并发文项目
     * @param signId
     * @return
     */
    boolean checkIsLink(String signId);

    /**
     * 根据合并评审主项目ID，查找合并评审项目
     * @param signId
     * @return
     */
    List<Sign> findReviewSign(String signId);

    /**
     * 根据合并评审主项目ID，判断合并评审次项目是否完成工作方案环节提交
     * @param signid
     * @return
     */
    boolean isMergeSignEndWP(String signid);
}
