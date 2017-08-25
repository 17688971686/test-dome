package cs.repository.repositoryImpl.sys;

import cs.domain.sys.FileLibrary;
import cs.domain.sys.FileLibrary_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:
 * Author: mcl
 * Date: 2017/8/21 16:21
 */
@Repository
public class FileLibraryRepoImpl extends AbstractRepository<FileLibrary,String> implements FileLibraryRepo{
    @Override
    public FileLibrary findByFileNameAndParentId(String parentFileId, String fileName ,String fileNatrue ,String fileType) {

        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(FileLibrary_.fileType.getName(),fileType));
        if(parentFileId == null){
            criteria.add(Restrictions.isNull(FileLibrary_.parentFileId.getName()));
        }else{
            criteria.add(Restrictions.eq(FileLibrary_.parentFileId.getName(),parentFileId));
        }
        criteria.add(Restrictions.eq(FileLibrary_.fileNature.getName(),fileNatrue));
        criteria.add(Restrictions.eq(FileLibrary_.fileName.getName(),fileName));
        List<FileLibrary> fileLibraryList = criteria.list();
        if(fileLibraryList !=null  && fileLibraryList.size()>0){
            return fileLibraryList.get(0);
        }else{

            return null;
        }
    }
}