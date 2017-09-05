package cs.repository.repositoryImpl.book;

import cs.domain.book.BookBuy;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: 图书模型数据操作实现类
 * author: zsl
 * Date: 2017-9-5 16:16:22
 */
@Repository
public class BookBuyRepoImpl extends AbstractRepository<BookBuy, String> implements BookBuyRepo {
}