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
	@SuppressWarnings("unchecked")
	public List<SysFile> findBySignId(String signid) {
		Criteria criteria = getExecutableCriteria();
		List<SysFile> sysFile = criteria.createAlias(SysFile_.businessId.getName(), SysFile_.businessId.getName())
		.add(Restrictions.eq(SysFile_.businessId.getName()+"."+SysFile_.businessId.getName(), signid)).list();
		return sysFile;
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
