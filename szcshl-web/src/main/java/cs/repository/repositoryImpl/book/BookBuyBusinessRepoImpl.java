package cs.repository.repositoryImpl.book;

import cs.domain.book.BookBuyBusiness;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: 图书采购申请业务信息 数据操作实现类
 * author: zsl
 * Date: 2017-9-8 10:26:20
 */
@Repository
public class BookBuyBusinessRepoImpl extends AbstractRepository<BookBuyBusiness, String> implements BookBuyBusinessRepo {
}