package cs.repository.repositoryImpl.sys;

import java.io.Serializable;
import java.util.List;

import cs.common.utils.Validate;
import cs.domain.sys.Company_;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.sys.Company;
import cs.repository.AbstractRepository;

@Repository
public class CompanyRepoImpl extends AbstractRepository<Company, String> implements CompanyRepo {

    @Override
    public Company findCompany(String coName){
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(Company_.coName.getName(),coName));
        List<Company> companyList=criteria.list();
        if(Validate.isList(companyList)){
            return companyList.get(0);
        }else{
            return null;
        }

    }

}
