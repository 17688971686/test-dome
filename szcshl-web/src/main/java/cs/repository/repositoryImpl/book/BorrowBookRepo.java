package cs.repository.repositoryImpl.book;

import cs.domain.book.BorrowBookInfo;
import cs.model.book.BookBorrowInfoDto;
import cs.repository.IRepository;

import java.util.List;

/**
 * Description: 图书借阅 数据操作实现接口
 * author: zsl
 * Date: 2017-9-8 10:24:51
 */
public interface BorrowBookRepo extends IRepository<BorrowBookInfo, String> {

    /**
     * 借书列表
     * @return
     */
    List<BookBorrowInfoDto> getBookBorrowList(BookBorrowInfoDto bookBorrowInfoDto);

    /**
     * 获取个人借书总数
     * @return
     */
    List<BookBorrowInfoDto>   getBookBorrowSum(BookBorrowInfoDto bookBorrowInfoDto);
}
