package cs.repository.repositoryImpl.sys;

import cs.domain.sys.Header;
import cs.repository.IRepository;

/**
 * Created by MCL
 * 2017/9/12
 */
public interface HeaderRepo extends IRepository<Header, String>{

    boolean isHeaderExist(String headerType , String headerKdy);

}
