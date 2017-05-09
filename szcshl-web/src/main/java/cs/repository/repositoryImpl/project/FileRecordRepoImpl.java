package cs.repository.repositoryImpl.project;


import org.springframework.stereotype.Repository;

import cs.domain.project.FileRecord;
import cs.repository.AbstractRepository;

@Repository
public class FileRecordRepoImpl  extends AbstractRepository<FileRecord, String> implements FileRecordRepo {

}
