package cs.repository.repositoryImpl.project;

import cs.common.ResultMsg;
import cs.domain.project.Sign;
import cs.domain.project.SignDispaWork;
import cs.model.project.SignDto;
import cs.repository.IRepository;

import java.util.Date;
import java.util.List;

public interface SignRepo extends IRepository<Sign, String> {

    /**
     * 修改项目状态
     * @param signId
     * @param stateProperty 状态属性
     * @param stateValue 值
     * @return
     */
    boolean updateSignState(String signId,String stateProperty,String stateValue);

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
     * 根据合并评审次项目ID，查找合并评审主项目
     * @param signId
     * @return
     */
    List<Sign> findMainReviewSign(String signId);
    /**
     * 根据合并评审主项目ID，判断合并评审次项目是否完成工作方案环节提交
     * @param signid
     * @return
     */
    boolean isMergeSignEndWP(String signid);

    /**
     * 获取未发送给委里的项目信息
     * @return
     */
    List<Sign> findUnSendFGWList();

    /**
     * 统计项目平均天数，未办结的按当前日期算，已办结的按办结日期算
     * @param signIds
     * @return
     */
    int sumExistDays(String signIds);

    /**
     * 通过收文id查询 评审天数、剩余工作日、收文日期、送来日期等 ---用于评审工作日维护
     * @param signId
     * @return
     */
    SignDto findReviewDayBySignId(String signId);

    /**
     * 保存评审工作日维护的信息
     * @param signDto
     * @return
     */
    ResultMsg saveReview(SignDto signDto);

    /**
     * 更新拟补充资料函状态
     * @param businessId
     * @param value
     * @param disapDate
     */
    void updateSuppLetterState(String businessId, String value, Date disapDate);
}
