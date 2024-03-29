package cs.repository.repositoryImpl.project;

import cs.common.ResultMsg;
import cs.domain.project.Sign;
import cs.domain.project.SignDispaWork;
import cs.domain.sys.User;
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

    Sign findByFilecode(String filecode,String signS);

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
     * 通过收文id查询 评审天数、剩余工作日、收文日期、送来日期、评审总天数等 ---用于评审工作日维护
     * @param signId
     * @return
     */
    SignDto findReviewDayBySignId(String signId);
    /**
     * 通过fileCode查询 sign对象
     * @param signDto
     * @return
     */
    SignDto findSignByFileCode(SignDto signDto);
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

    /**
     * 重新初始化协办部门和负责人信息
     * @param sign
     * @param branchId  排除的分支
     */
    void initAOrgAndUser(Sign sign, String branchId);

    /**
     * 验证是否是调概项目
     * @param businessKey
     * @return
     */
    boolean checkAssistSign(String businessKey);

    /**
     * 根据签收日期和类型，获取最大的收文编号
     * @param yearName
     * @param signType
     * @return
     */
    int getMaxSignSeq(String yearName,String signType);

    /**
     * 通过signId查询平均评审天数和工作日
     * @param signIds
     * @return
     */
    ResultMsg findAVGDayId(String signIds);

    /**
     * 保存项目维护中的添加评审部门
     * @param signId
     * @param orgIds
     * @return
     */
    ResultMsg addAOrg(String signId , String orgIds );

    /**
     * 移除在维护项目中添加的评审部门
     * @param signId
     * @param orgIds
     * @return
     */
    ResultMsg deleteAOrg(String signId , String orgIds );

    /**
     * 保存项目维护中的添加负责人
     * @param signId
     * @param userId
     * @return
     */
    ResultMsg addSecondUser(String signId , String userId);

    /**
     * 删除项目维护中添加的负责人
     * @param signId
     * @param userId
     * @return
     */
    ResultMsg deleteSecondUser(String signId , String userId);

    /**
     * 保存是否能多选专家
     * @param signId
     * @param isMoreExpert
     * @return
     */
    ResultMsg saveMoreExpert(String signId , String isMoreExpert);

}
