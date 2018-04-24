package cs.repository.repositoryImpl.project;


import cs.domain.project.FileRecord;
import cs.repository.IRepository;

public interface FileRecordRepo extends IRepository<FileRecord, String> {

    /**
     * 根据业务ID判断是否完成归档
     * 是否有专家评审费
     * @param businessId
     * @return
     */
    boolean isFileRecord(String businessId);

    /**
     * 获取最大归档编号
     * @param yearName
     * @param seqType
     * @return
     */
    int getMaxSeq(String yearName, String seqType);
}
