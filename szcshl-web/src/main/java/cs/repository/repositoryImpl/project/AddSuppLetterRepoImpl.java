package cs.repository.repositoryImpl.project;

import cs.common.constants.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.domain.project.AddSuppLetter;
import cs.domain.project.AddSuppLetter_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Description: 项目资料补充函 数据操作实现类
 * author: ldm
 * Date: 2017-8-1 18:05:57
 */


@Repository
public class AddSuppLetterRepoImpl extends AbstractRepository<AddSuppLetter, String> implements AddSuppLetterRepo {

    @Autowired
    private AddSuppLetterRepo addSuppLetterRepo;
    /**
     * 根据业务ID判断是否有补充资料函
     * @param businessId
     * @return
     */
    @Override
    public boolean isHaveSuppLetter(String businessId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(AddSuppLetter_.businessId.getName(),businessId));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return (totalResult > 0)?true:false;
    }

    /**
     * 根据业务类型，查询最大序号
     * @param fileType
     * @return
     */
    @Override
    public int findybMaxSeq(String fileType){
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max(" + AddSuppLetter_.monthlySeq.getName() + ") from cs_add_suppLetter" );
        sqlBuilder.append(" where " + AddSuppLetter_.fileType.getName() + " =:fileType ").setParam("fileType", fileType);
        return addSuppLetterRepo.returnIntBySql(sqlBuilder);
    }

    /**
     * 归档最大序号
     * @param yearName
     * @return
     */
    @Override
    public int findCurMaxSeq(String yearName) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max(" + AddSuppLetter_.fileSeq.getName() + ") from cs_add_suppLetter where to_char(" + AddSuppLetter_.suppLetterTime.getName()+" , 'yyyy') = :yearName ");
        sqlBuilder.setParam("yearName",yearName);
        return addSuppLetterRepo.returnIntBySql(sqlBuilder);
    }

    /**
     * 检查是否还有正在审批的拟补充资料函
     * @param businessId
     * @param fileType
     * @return
     */
    @Override
    public ResultMsg checkIsApprove(String businessId, String fileType) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select count(ID) from cs_add_suppLetter where BUSINESSID =:businessId ");
        sqlBuilder.setParam("businessId",businessId);
        sqlBuilder.append(" and fileType = :fileType ").setParam("fileType",fileType);
        sqlBuilder.append(" and (processInstanceId is null or APPOVESTATUS != :status) ").setParam("status", Constant.EnumState.YES.getValue());
        int countValue = addSuppLetterRepo.returnIntBySql(sqlBuilder);
        if(countValue > 0){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"该项目还有拟补充资料函未审批完成！");
        }else{
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"该项目没有拟补充资料函待审！");
        }
    }

}