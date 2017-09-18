package cs.repository.repositoryImpl.sys;

import cs.domain.sys.Header;
import cs.domain.sys.Header_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:
 * Author: mcl
 * Date: 2017/9/12 14:26
 */

@Repository
public class HeaderRepoImpl extends AbstractRepository<Header, String > implements  HeaderRepo {
    @Override
    public boolean isHeaderExist(String headerType, String headerKdy) {
        Criteria criteria = this.getSession().createCriteria(Header.class);
        criteria.add(Restrictions.eq(Header_.headerType.getName() , headerType));
        criteria.add(Restrictions.eq(Header_.headerKey.getName() , headerKdy));
        List<Header> headerList = criteria.list();
        return !headerList.isEmpty();
    }
}