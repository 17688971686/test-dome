package cs.repository.repositoryImpl.book;

import cs.domain.book.BorrowBookInfo;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: 图书信息 数据操作实现类
 * author: zsl
 * Date: 2017-9-8 10:24:51
 */
@Repository
public class BorrowBookRepoImpl extends AbstractRepository<BorrowBookInfo, String> implements BorrowBookRepo {
}