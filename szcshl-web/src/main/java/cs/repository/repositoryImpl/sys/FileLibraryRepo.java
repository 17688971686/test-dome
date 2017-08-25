package cs.repository.repositoryImpl.sys;

import cs.domain.sys.FileLibrary;
import cs.repository.IRepository;

/**
 * Created by MCL
 * 2017/8/21
 */
public interface FileLibraryRepo extends IRepository<FileLibrary,String> {

    FileLibrary findByFileNameAndParentId(String parentFileId ,String fileName ,String fileNatrue ,String fileType);
}
