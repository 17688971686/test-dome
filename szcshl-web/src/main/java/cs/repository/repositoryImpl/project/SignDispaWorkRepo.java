package cs.repository.repositoryImpl.project;

import cs.common.ResultMsg;
import cs.domain.project.SignDispaWork;
import cs.repository.IRepository;

import java.util.List;

/**
 * Description: 项目统计视图 数据操作实现接口
 * author: ldm
 * Date: 2017-7-10 9:35:22
 */
public interface SignDispaWorkRepo extends IRepository<SignDispaWork, String> {

    /**
     * 通过专家id获取评审过的项目信息
     * @param expertId
     * @return
     */
    List<SignDispaWork> reviewProject(String expertId);

    /**
     * 通过时间段 获取项目信息（按评审阶段分组），用于项目查询统计分析
     * @param startTime
     * @param endTime
     * @return
     */
   ResultMsg findByTime(String startTime , String endTime);

    /**
     * 通过评审阶段，项目类别，统计项目信息
     * @param startTime
     * @param endTime
     * @return
     */
    ResultMsg findByTypeAndReview(String startTime , String endTime);

    /**
     * 通过收文id获取项目信息
     * @param signId
     * @return
     */
    SignDispaWork findSDPBySignId(String signId);

    /**
     * 通过条件查询统计
     * @param signDispaWork
     * @param page
     * @return
     */
    List<SignDispaWork> queryStatistics(SignDispaWork signDispaWork , int page);


}
