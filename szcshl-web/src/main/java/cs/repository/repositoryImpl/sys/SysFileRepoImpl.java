package cs.repository.repositoryImpl.sys;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.sys.SysFile;
import cs.domain.sys.SysFile_;
import cs.repository.AbstractRepository;

@Repository
public class SysFileRepoImpl extends AbstractRepository<SysFile, String> implements SysFileRepo{

	@Override
	public List<SysFile> findByMainId(String mainId) {
		Criteria criteria = getExecutableCriteria();
		criteria.add(Restrictions.eq(SysFile_.mainId.getName(),mainId));
		return criteria.list();
	}

	@Override
	public List<SysFile> queryFileList(String mainId,String sysBusiType){
		Criteria criteria =getExecutableCriteria();
 		criteria.add(Restrictions.eq(SysFile_.mainId.getName(),mainId));
		criteria.add(Restrictions.eq(SysFile_.sysBusiType.getName(),sysBusiType));
		List<SysFile> sysFiles = criteria.list();
		return sysFiles;
	}

}
