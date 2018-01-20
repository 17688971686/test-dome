package cs.repository.repositoryImpl.project;

import cs.common.utils.Validate;
import cs.domain.project.SignDispaWork_;
import cs.domain.project.SignWork;
import cs.domain.project.SignWork_;
import cs.model.project.SignWorkDto;
import cs.repository.AbstractRepository;
import cs.repository.odata.ODataObj;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SignWorkRepoImpl extends AbstractRepository<SignWork, String> implements SignWorkRepo {

    /**
     * 查询在办项目的工作方案专家抽取信息
     * @param odataObj
     * @return
     */
    @Override
    public List<SignWork> fingSignWorkList(ODataObj odataObj) {
        Criteria criteria = getExecutableCriteria();
        criteria = odataObj.buildFilterToCriteria(criteria);
        return criteria.list();
    }
}
