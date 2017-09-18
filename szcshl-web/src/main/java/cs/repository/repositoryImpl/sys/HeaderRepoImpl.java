package cs.repository.repositoryImpl.sys;

import cs.domain.sys.Header;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description:
 * Author: mcl
 * Date: 2017/9/12 14:26
 */

@Repository
public class HeaderRepoImpl extends AbstractRepository<Header, String > implements  HeaderRepo {
}