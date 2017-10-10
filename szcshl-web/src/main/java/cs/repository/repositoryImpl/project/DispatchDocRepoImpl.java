package cs.repository.repositoryImpl.project;

import java.util.List;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.Validate;
import cs.domain.project.WorkProgram_;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cs.domain.project.DispatchDoc;
import cs.domain.project.DispatchDoc_;
import cs.domain.project.Sign_;
import cs.repository.AbstractRepository;

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
}
