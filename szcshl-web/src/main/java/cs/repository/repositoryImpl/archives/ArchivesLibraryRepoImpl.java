package cs.repository.repositoryImpl.archives;

import cs.domain.archives.ArchivesLibrary;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: 档案借阅管理 数据操作实现类
 * author: sjy
 * Date: 2017-9-12 17:34:30
 */
@Repository
public class ArchivesLibraryRepoImpl extends AbstractRepository<ArchivesLibrary, String> implements ArchivesLibraryRepo {
}