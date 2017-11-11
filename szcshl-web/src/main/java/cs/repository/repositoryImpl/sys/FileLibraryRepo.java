package cs.repository.repositoryImpl.sys;

import cs.domain.sys.FileLibrary;
import cs.repository.IRepository;

/**
 * Created by MCL
 * 2017/8/21
 */
public interface FileLibraryRepo extends IRepository<FileLibrary,String> {

    /**
     * 通过父Id、文件名、文件类型、文件性质 判断该文件是否已经存在
     * @param parentFileId
     * @param fileName
     * @param fileNature
     * @param fileType
     * @return
     */
    Boolean findByFileNameAndParentId(String parentFileId ,String fileName ,String fileNature ,String fileType);
}
