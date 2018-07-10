package cs.repository.repositoryImpl.project;

import cs.common.HqlBuilder;
import cs.common.constants.Constant;
import cs.common.utils.Validate;
import cs.domain.project.DispatchDoc;
import cs.domain.project.DispatchDoc_;
import cs.domain.project.Sign_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static cs.common.constants.Constant.DEVICE_BILL_HOMELAND;
import static cs.common.constants.Constant.DEVICE_BILL_IMPORT;

@Repository
public class DispatchDocRepoImpl extends AbstractRepository<DispatchDoc, String> implements DispatchDocRepo {
    @Autowired
    private SignRepo signRepo;

    @SuppressWarnings("unchecked")
    @Override
    @Deprecated
    public List<DispatchDoc> findDispatchBySignId(String signId) {
        Criteria criteria = getExecutableCriteria();
        List<DispatchDoc> list = criteria.createAlias(DispatchDoc_.sign.getName(), DispatchDoc_.sign.getName())
                .add(Restrictions.eq(DispatchDoc_.sign.getName() + "." + Sign_.signid.getName(), signId)).list();
        return list;
    }

    /**
     * 根据合并发文，修改发文的发文方式
     *
     * @param reviewType
     * @param isMain
     * @param signIds
     */
    @Override
    public void updateRWType(String reviewType, String isMain, String signIds) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update cs_dispatch_doc set " + DispatchDoc_.dispatchWay.getName() + " =:reviewType ");
        sqlBuilder.setParam("reviewType", reviewType);
        if (Validate.isString(isMain)) {
            sqlBuilder.append(" , " + DispatchDoc_.isMainProject.getName() + " =:isMainProject ");
            sqlBuilder.setParam("isMainProject", isMain);
        } else {
            sqlBuilder.append(" , " + DispatchDoc_.isMainProject.getName() + " = null ");
        }
        sqlBuilder.bulidPropotyString("where", "signid", signIds);

        executeSql(sqlBuilder);
    }

    /**
     * 更改发文是否已关联字段
     * @param signId            项目ID
     */
    @Override
    public void updateIsRelatedState(String signId){
        DispatchDoc dispatchDoc = findById("signid",signId);
        if(dispatchDoc != null){
            if(signRepo.checkIsLink(signId) && !Constant.EnumState.YES.getValue().equals(dispatchDoc.getIsRelated())){
                dispatchDoc.setIsRelated(Constant.EnumState.YES.getValue());
                save(dispatchDoc);
            }else if( !signRepo.checkIsLink(signId) && Constant.EnumState.YES.getValue().equals(dispatchDoc.getIsRelated())){
                dispatchDoc.setIsRelated(Constant.EnumState.NO.getValue());
                save(dispatchDoc);
            }
        }
    }

    /**
     * 获取最大发文编号
     * @param yearName
     * @param seqType 1是表示其它发文，2是设备清单发文（国产设备的发文编号为：深投审设[xxxx],其它阶段为：深投审[xxxx],）
     * @return
     */
    @Override
    public int getMaxSeq(String yearName, String seqType) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max("+ DispatchDoc_.fileSeq.getName()+") from cs_dispatch_doc where to_char("+DispatchDoc_.dispatchDate.getName()+", 'yyyy') = :yearName ");
        sqlBuilder.setParam("yearName",yearName);
        if(Constant.EnumState.PROCESS.getValue().equals(seqType)){
            sqlBuilder.append(" and "+DispatchDoc_.dispatchStage.getName() +" != :dispatchStage1 and  "+DispatchDoc_.dispatchStage.getName() +" != :dispatchStage2 ");
            sqlBuilder.setParam("dispatchStage1",DEVICE_BILL_HOMELAND).setParam("dispatchStage2",DEVICE_BILL_IMPORT);
        }else{
            sqlBuilder.append(" and ("+DispatchDoc_.dispatchStage.getName() +" != :dispatchStage1 or "+DispatchDoc_.dispatchStage.getName() +" != :dispatchStage2 )");
            sqlBuilder.setParam("dispatchStage1",DEVICE_BILL_HOMELAND).setParam("dispatchStage2",DEVICE_BILL_IMPORT);
        }
        return returnIntBySql(sqlBuilder);
    }

    @Override
    public void updateDisApprValue(String disId, BigDecimal apprValue) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update cs_dispatch_doc set " + DispatchDoc_.approveValue.getName() + " =:approveValue " );
        sqlBuilder.setParam("approveValue", apprValue);
        sqlBuilder.append("where id =:disId");
        sqlBuilder.setParam("disId",disId);
        executeSql(sqlBuilder);
    }

    @Override
    public void updateMergeDisFileNum(String signId,String fileNum, int maxSeq) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update cs_dispatch_doc set " + DispatchDoc_.fileNum.getName() + " =:fileNum ").setParam("fileNum", fileNum);
        sqlBuilder.append(" ," + DispatchDoc_.fileSeq.getName() + " =:fileSeq").setParam("fileSeq", maxSeq);
        sqlBuilder.append(" where signId in (select mergeId from cs_sign_merge where signId = :signId ");
        sqlBuilder.setParam("signId", signId);
        sqlBuilder.append(" and mergeType =:mergeType )").setParam("mergeType", Constant.MergeType.DISPATCH.getValue());
        executeSql(sqlBuilder);
    }
}
