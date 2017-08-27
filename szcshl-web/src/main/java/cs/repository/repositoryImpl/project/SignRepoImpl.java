package cs.repository.repositoryImpl.project;

import cs.common.HqlBuilder;
import cs.common.utils.Validate;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.repository.AbstractRepository;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SignRepoImpl extends AbstractRepository<Sign, String> implements SignRepo {

    /**
     * 更改流程状态
     *
     * @param state
     * @return
     */
    @Override
    public boolean updateSignState(String signId, String state) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update " + Sign.class.getSimpleName() + " set " + Sign_.signState.getName() + " =:state ");
        hqlBuilder.setParam("state", state);
        hqlBuilder.append(" where " + Sign_.signid.getName() + " =:signid ");
        hqlBuilder.setParam("signid", signId);

        return executeHql(hqlBuilder) >= 0 ? true : false;
    }

    /**
     * 更改项目流程状态
     * @param signId
     * @param processState
     * @return
     */
    @Override
    public boolean updateSignProcessState(String signId, Integer processState) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update " + Sign.class.getSimpleName() + " set " + Sign_.processState.getName() + " =:processState ");
        hqlBuilder.setParam("processState", processState);
        hqlBuilder.append(" where " + Sign_.signid.getName() + " =:signid ");
        hqlBuilder.setParam("signid", signId);
        return executeHql(hqlBuilder) >= 0 ? true : false;
    }

    /**
     * 根据委里收文编号，获取项目信息
     * @param filecode
     * @return
     */
    @Override
    public Sign findByFilecode(String filecode) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from  " + Sign.class.getSimpleName());
        hqlBuilder.append(" where " + Sign_.filecode.getName() + " = :filecode ");
        hqlBuilder.setParam("filecode", filecode);

        List<Sign> signList = findByHql(hqlBuilder);
        if(Validate.isList(signList)){
            return signList.get(0);
        }else{
            return null;
        }
    }

}
